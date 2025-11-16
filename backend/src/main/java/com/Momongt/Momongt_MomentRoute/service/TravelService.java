package com.Momongt.Momongt_MomentRoute.service;

import com.Momongt.Momongt_MomentRoute.dto.TravelDto;
import com.Momongt.Momongt_MomentRoute.entity.Plan;
import com.Momongt.Momongt_MomentRoute.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;

    /**
     * 여행 저장
     */
    public TravelDto.RecommendedCourseResponse saveTrip(Long userId, TravelDto request) {

        Plan plan = Plan.builder()
                .userId(userId)
                .title(request.getStartPoint() + " → " + request.getEndPoint())
                .notes(request.getAiText()) // AI 추천 문장 저장
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        Plan savedPlan = travelRepository.save(plan);

        // 응답 형식은 기존 RecommendedCourseResponse 사용
        return new TravelDto.RecommendedCourseResponse(
                List.of(
                        new TravelDto.RecommendedCourse(
                                plan.getTitle(),
                                0,
                                null,
                                buildRoute(request),
                                List.of(),
                                List.of()
                        )
                )
        );
    }

    /**
     * 여행 목록 조회
     */
    public TravelDto.RecommendedCourseResponse listTrips(Long userId) {

        List<Plan> plans = travelRepository.findByUserId(userId);

        List<TravelDto.RecommendedCourse> list = plans.stream()
                .map(plan -> new TravelDto.RecommendedCourse(
                        plan.getTitle(),
                        0,
                        null,
                        List.of(),
                        List.of(),
                        List.of()
                ))
                .toList();

        return new TravelDto.RecommendedCourseResponse(list);
    }

    /**
     * 여행 상세 조회
     */
    public TravelDto.RecommendedCourse detail(Long userId, Long tripId) {

        Plan plan = travelRepository.findByIdAndUserId(tripId, userId)
                .orElseThrow(() -> new RuntimeException("여행을 찾을 수 없습니다."));

        List<String> routeList = List.of(plan.getNotes().split("→"))
                .stream().map(String::trim).toList();

        return new TravelDto.RecommendedCourse(
                plan.getTitle(),
                0,
                null,
                routeList,
                List.of(),
                List.of()
        );
    }

    /** start → (waypoints...) → end 전체 경로 생성 */
    private List<String> buildRoute(TravelDto req) {
        List<String> full = new java.util.ArrayList<>();
        full.add(req.getStartPoint());

        if (req.getWaypoints() != null)
            full.addAll(req.getWaypoints());

        full.add(req.getEndPoint());
        return full;
    }
}
