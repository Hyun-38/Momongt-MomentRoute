package com.Momongt.Momongt_MomentRoute.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelDto {

    // ===== 공통 요청 =====
    private String startPoint;
    private String endPoint;
    private List<String> waypoints;
    private String aiText;
    private List<String> preferredCategories;

    // ================================
    // ===== 여행 저장 요청 DTO =====
    // ================================
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveTripRequest {
        private String tripName;       // 제목
        private Map<String, Object> routeData; // JSON 형태로 전달되는 경로 정보
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveTripResponse {
        private Long tripId;
        private String title;
        private String createdAt;
    }

    // ================================
    // ===== 여행 목록 DTO =====
    // ================================
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripSummary {
        private Integer tripId;
        private String title;
        private String date;
        private String createdAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pagination {
        private int page;
        private int totalPages;
        private int totalElements;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripListResponse {
        private List<TripSummary> trips;
        private Pagination pagination;
    }

    // ================================
    // ===== 여행 상세 응답 DTO =====
    // ================================
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripPlace {
        private Integer placeId;
        private String name;
        private String type;
        private Double lat;
        private Double lng;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripDetailResponse {
        private Integer tripId;
        private String title;
        private String date;
        private String createdAt;
        private List<TripPlace> places;
    }

    // ================================
    // ===== AI 추천 코스 응답 DTO =====
    // ================================
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendedCourseResponse {
        private List<RecommendedCourse> recommendedCourses;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendedCourse {
        private String title;
        private Integer totalTimeMinutes;
        private Map<String, Object> routeData;
        private List<String> route;
        private List<Event> events;
        private List<RegionalSpot> regionalSpots;
    }

    // ===== 부가 정보 =====
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Event {
        private String name;
        private String region;
        private String date;
        private String imageUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalSpot {
        private String regionName;
        private List<Place> restaurants;
        private List<Place> touristSpots;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Place {
        private String name;
        private String category;
        private Double rating;
        private String description;
        private String imageUrl;
        private Double lat;
        private Double lng;
    }
}
