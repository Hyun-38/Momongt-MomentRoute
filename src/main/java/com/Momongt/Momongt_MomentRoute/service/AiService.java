package com.Momongt.Momongt_MomentRoute.service;

import com.Momongt.Momongt_MomentRoute.dto.TravelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AiService {

    public TravelDto.RecommendedCourseResponse recommendCourse(TravelDto request) {

        // 🔥 AI 문장에서 경유지 자동 추출 (옵션)
        if (request.getAiText() != null && !request.getAiText().isBlank()) {
            List<String> extracted = extractWaypoints(request.getAiText());
            request.setWaypoints(extracted);    // ⭐ 자동 세팅
        }

        // MOCK 데이터 (기존 로직 유지)
        List<TravelDto.RecommendedCourse> list = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            list.add(new TravelDto.RecommendedCourse(
                    request.getStartPoint() + " → 추천코스 " + i,
                    480,
                    Map.of("polyline", Arrays.asList(1, 2, 3)),
                    buildRoute(request), // 자동경유지 포함하여 최종 경로 생성
                    List.of(
                            new TravelDto.Event("수원 화성 축제", "수원", "2025-10-01", "image-url")
                    ),
                    List.of()
            ));
        }

        return new TravelDto.RecommendedCourseResponse(list);
    }

    /**
     * 출발지 → 경유지들 → 종료지 조합해서 최종 경로 생성
     */
    private List<String> buildRoute(TravelDto req) {
        List<String> full = new ArrayList<>();
        full.add(req.getStartPoint());

        if (req.getWaypoints() != null)
            full.addAll(req.getWaypoints());

        full.add(req.getEndPoint());
        return full;
    }

    /**
     * AI 문장에서 경유지 자동 추출
     * 예: "서울 → 대전 → 대구 → 부산"
     * -> [대전, 대구]
     */
    private List<String> extractWaypoints(String aiText) {

        // 한글 지명만 추출하는 정규식
        Pattern pattern = Pattern.compile("([가-힣]+)");
        Matcher matcher = pattern.matcher(aiText);

        List<String> places = new ArrayList<>();

        while (matcher.find()) {
            places.add(matcher.group(1));
        }

        // 출발지 + 도착지만 있고 경유지가 없으면 빈 리스트
        if (places.size() <= 2) {
            return new ArrayList<>();
        }

        // 중간: 경유지들
        return places.subList(1, places.size() - 1);
    }
}
