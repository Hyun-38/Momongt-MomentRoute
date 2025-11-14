package com.Momongt.Momongt_MomentRoute.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI 추천 코스 요청 DTO")
public class TravelDto {

    @Schema(description = "출발지", example = "수원", required = true)
    private String startPoint;

    @Schema(description = "목적지", example = "고양", required = true)
    private String endPoint;

    @Schema(description = "경유지 목록 (선택)", example = "[\"성남\", \"부천\", \"안양\", \"화성\"]")
    private List<String> waypoints;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "AI 추천 코스 응답 DTO")
    public static class RecommendedCourseResponse {
        @Schema(description = "AI 추천 코스 (3개)", required = true)
        private List<RecommendedCourse> recommendedCourses;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "추천 코스")
    public static class RecommendedCourse {
        @Schema(description = "추천 코스 제목", example = "수원-성남 코스(1안)", required = true)
        private String title;

        @Schema(description = "총 소요 시간 (분)", example = "480", required = true)
        private Integer totalTimeMinutes;

        @Schema(description = "지도 표시용 경로 (좌표 데이터)", required = true)
        private Map<String, Object> routeData;

        @Schema(description = "AI가 구성한 경로 도시", example = "[\"수원\", \"성남\", \"부천\", \"안양\", \"화성\", \"고양\"]", required = true)
        private List<String> route;

        @Schema(description = "경로 기반 축제/이벤트")
        private List<Event> events;

        @Schema(description = "지역별 장소", required = true)
        private List<RegionalSpot> regionalSpots;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "이벤트 정보")
    public static class Event {
        @Schema(description = "이벤트명", example = "수원 화성문화제", required = true)
        private String name;

        @Schema(description = "지역", example = "수원시 팔달구", required = true)
        private String region;

        @Schema(description = "날짜", example = "2025-10-05 ~ 2025-10-12", required = true)
        private String date;

        @Schema(description = "이미지 URL", example = "https://...", required = true)
        private String imageUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "지역별 장소")
    public static class RegionalSpot {
        @Schema(description = "지역명", example = "수원", required = true)
        private String regionName;

        @Schema(description = "맛집 목록")
        private List<Place> restaurants;

        @Schema(description = "관광지 목록")
        private List<Place> touristSpots;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "장소 정보")
    public static class Place {
        @Schema(description = "장소명", example = "수원 왕갈비", required = true)
        private String name;

        @Schema(description = "카테고리", example = "한식", required = true)
        private String category;

        @Schema(description = "평점", example = "4.5")
        private Double rating;

        @Schema(description = "설명", example = "수원 대표 갈비 맛집")
        private String description;

        @Schema(description = "이미지 URL", example = "https://...", required = true)
        private String imageUrl;

        @Schema(description = "위도", example = "37.28", required = true)
        private Double lat;

        @Schema(description = "경도", example = "127.01", required = true)
        private Double lng;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "여행 저장 요청 DTO")
    public static class SaveTripRequest {
        @Schema(description = "사용자가 입력한 여행 이름", example = "일본 여행 1", required = true, maxLength = 20)
        private String tripName;

        @Schema(description = "AI가 추천해준 경로", example = "{ \"route\": {}, \"recommendations\": {} }", required = true)
        private Map<String, Object> routeData;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "여행 저장 응답 DTO")
    public static class SaveTripResponse {
        @Schema(description = "여행 ID", example = "12345", required = true)
        private Long tripId;

        @Schema(description = "여행 이름", example = "일본 여행 1", required = true)
        private String tripName;

        @Schema(description = "생성 일시", example = "2025-10-27T10:30:00Z", required = true)
        private String createdAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "에러 응답 DTO")
    public static class ErrorResponse {
        @Schema(description = "에러 코드", example = "INVALID_INPUT", required = true)
        private String errorCode;

        @Schema(description = "에러 메시지", example = "여행 이름은 필수이며, 20자를 넘을 수 없습니다.", required = true)
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "여행 목록 조회 응답 DTO")
    public static class TripListResponse {
        @Schema(description = "여행 계획 요약 목록", required = true)
        private List<TripSummary> trips;

        @Schema(description = "페이지 정보", required = true)
        private Pagination pagination;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "여행 계획 요약")
    public static class TripSummary {
        @Schema(description = "여행 계획 ID", example = "123", required = true)
        private Integer tripId;

        @Schema(description = "여행 계획 제목", example = "수원 화성 맛집 정복 코스", required = true)
        private String title;

        @Schema(description = "여행 날짜", example = "2025-11-10", required = true)
        private String date;

        @Schema(description = "생성 일시", example = "2025-10-26T11:00:00Z", required = true)
        private String createdAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "페이지 정보")
    public static class Pagination {
        @Schema(description = "현재 페이지 번호", example = "1", required = true)
        private Integer currentPage;

        @Schema(description = "전체 페이지 수", example = "4", required = true)
        private Integer totalPages;

        @Schema(description = "전체 항목 수", example = "40", required = true)
        private Integer totalCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "여행 상세 조회 응답 DTO")
    public static class TripDetailResponse {
        @Schema(description = "여행 계획 ID", example = "123", required = true)
        private Integer tripId;

        @Schema(description = "여행 계획 제목", example = "수원 화성 맛집 정복 코스", required = true)
        private String title;

        @Schema(description = "여행 날짜", example = "2025-11-10", required = true)
        private String date;

        @Schema(description = "생성 일시", example = "2025-10-26T11:00:00Z", required = true)
        private String createdAt;

        @Schema(description = "코스에 포함된 장소 목록", required = true)
        private List<TripPlace> places;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "여행 코스 장소")
    public static class TripPlace {
        @Schema(description = "장소 ID", example = "101", required = true)
        private Integer placeId;

        @Schema(description = "장소명", example = "A 식당", required = true)
        private String name;

        @Schema(description = "장소 타입", example = "맛집", required = true)
        private String type;

        @Schema(description = "위도", example = "37.288", required = true)
        private Double lat;

        @Schema(description = "경도", example = "127.009", required = true)
        private Double lng;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "여행 계획 수정 요청 DTO")
    public static class UpdateTripRequest {
        @Schema(description = "여행 계획 제목", example = "[수정] 행궁동 카페 투어")
        private String title;

        @Schema(description = "여행 날짜", example = "2025-11-12")
        private String date;

        @Schema(description = "장소 목록")
        private List<TripPlace> places;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "여행 계획 수정 응답 DTO")
    public static class UpdateTripResponse {
        @Schema(description = "수정된 여행 계획의 tripId", example = "123", required = true)
        private Integer tripId;

        @Schema(description = "수정된 여행 계획 제목", example = "[수정] 행궁동 카페 투어", required = true)
        private String title;

        @Schema(description = "수정된 여행 날짜", example = "2025-11-12", required = true)
        private String date;

        @Schema(description = "수정된 장소 목록", required = true)
        private List<TripPlace> places;

        @Schema(description = "수정 완료 시간", example = "2025-10-27T14:30:00Z", required = true)
        private String updatedAt;
    }
}
