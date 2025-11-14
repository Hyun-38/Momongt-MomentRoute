package com.Momongt.Momongt_MomentRoute.service;

import com.Momongt.Momongt_MomentRoute.dto.TravelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelService {

    public TravelDto.RecommendedCourseResponse getRecommendedCourses(TravelDto request) {
        // TODO: AI 서비스 연동 및 추천 코스 생성 로직 구현
        // 현재는 빈 응답 반환
        return new TravelDto.RecommendedCourseResponse();
    }

    public TravelDto.SaveTripResponse saveTrip(TravelDto.SaveTripRequest request) {
        // TODO: 여행 저장 로직 구현
        // 현재는 빈 응답 반환
        return new TravelDto.SaveTripResponse();
    }

    public TravelDto.TripListResponse getTripList(Integer page, Integer limit) {
        // TODO: 여행 목록 조회 로직 구현
        // 현재는 빈 응답 반환
        return new TravelDto.TripListResponse();
    }

    public TravelDto.TripDetailResponse getTripDetail(Integer tripId) {
        // TODO: 여행 상세 조회 로직 구현
        // 현재는 빈 응답 반환
        return new TravelDto.TripDetailResponse();
    }

    public TravelDto.UpdateTripResponse updateTrip(Integer tripId, TravelDto.UpdateTripRequest request) {
        // TODO: 여행 계획 수정 로직 구현
        // 현재는 빈 응답 반환
        return new TravelDto.UpdateTripResponse();
    }
}
