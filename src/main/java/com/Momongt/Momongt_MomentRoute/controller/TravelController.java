package com.Momongt.Momongt_MomentRoute.controller;

import com.Momongt.Momongt_MomentRoute.dto.TravelDto;
import com.Momongt.Momongt_MomentRoute.service.TravelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/travel")
@RequiredArgsConstructor
@Tag(name = "Travel", description = "여행 코스 추천 API")
public class TravelController {

    private final TravelService travelService;

    @Operation(
            summary = "AI 추천 코스 조회",
            description = "출발지, 목적지, 경유지를 기반으로 AI가 추천하는 여행 코스를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공적으로 추천 코스 반환",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TravelDto.RecommendedCourseResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"recommendedCourses\": [\n" +
                                            "    {\n" +
                                            "      \"title\": \"수원-성남 핵심 코스 (1안)\",\n" +
                                            "      \"totalTimeMinutes\": 480,\n" +
                                            "      \"routeData\": { \"coordinates\": [] },\n" +
                                            "      \"route\": [\"수원\", \"성남\", \"부천\", \"안양\", \"화성\", \"고양\"],\n" +
                                            "      \"events\": [\n" +
                                            "        {\n" +
                                            "          \"name\": \"수원 화성문화제 2025\",\n" +
                                            "          \"region\": \"수원시 팔달구\",\n" +
                                            "          \"date\": \"2025-10-05 ~ 2025-10-12\",\n" +
                                            "          \"imageUrl\": \"https://example.com/suwon_festival.png\"\n" +
                                            "        }\n" +
                                            "      ],\n" +
                                            "      \"regionalSpots\": [\n" +
                                            "        {\n" +
                                            "          \"regionName\": \"수원\",\n" +
                                            "          \"restaurants\": [\n" +
                                            "            {\n" +
                                            "              \"name\": \"수원 왕갈비\",\n" +
                                            "              \"category\": \"한식\",\n" +
                                            "              \"rating\": 4.5,\n" +
                                            "              \"description\": \"수원 대표 갈비 맛집\",\n" +
                                            "              \"imageUrl\": \"https://example.com/wanggalbi.png\",\n" +
                                            "              \"lat\": 37.28,\n" +
                                            "              \"lng\": 127.01\n" +
                                            "            }\n" +
                                            "          ],\n" +
                                            "          \"touristSpots\": []\n" +
                                            "        }\n" +
                                            "      ]\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (e.g., 필수 Body 값 누락)"),
            @ApiResponse(responseCode = "500", description = "서버 오류 (e.g., AI 모델 응답 실패)")
    })
    @PostMapping("/recommend")
    public ResponseEntity<TravelDto.RecommendedCourseResponse> getRecommendedCourses(
            @RequestBody TravelDto request
    ) {
        TravelDto.RecommendedCourseResponse response = travelService.getRecommendedCourses(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "여행 저장",
            description = "AI가 추천한 여행 코스를 저장합니다. 인증이 필요합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공적으로 여행 저장됨",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TravelDto.SaveTripResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"tripId\": 12345,\n" +
                                            "  \"tripName\": \"일본 여행 1\",\n" +
                                            "  \"createdAt\": \"2025-10-27T10:30:00Z\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TravelDto.ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"errorCode\": \"INVALID_INPUT\",\n" +
                                            "  \"message\": \"여행 이름은 필수이며, 20자를 넘을 수 없습니다.\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 오류 (Unauthorized)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TravelDto.ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"errorCode\": \"UNAUTHORIZED\",\n" +
                                            "  \"message\": \"여행을 저장하려면 로그인이 필요합니다.\"\n" +
                                            "}"
                            )
                    )
            )
    })
    @PostMapping("/save")
    public ResponseEntity<TravelDto.SaveTripResponse> saveTrip(
            @Parameter(description = "JWT 토큰", required = true, hidden = true)
            @RequestHeader("Authorization") String authorization,
            @RequestBody TravelDto.SaveTripRequest request
    ) {
        TravelDto.SaveTripResponse response = travelService.saveTrip(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "여행 목록 조회",
            description = "사용자가 저장한 여행 계획 목록을 페이지네이션과 함께 조회합니다. 인증이 필요합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TravelDto.TripListResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"trips\": [\n" +
                                            "    {\n" +
                                            "      \"tripId\": 123,\n" +
                                            "      \"title\": \"수원 화성 맛집 정복 코스\",\n" +
                                            "      \"date\": \"2025-11-10\",\n" +
                                            "      \"createdAt\": \"2025-10-26T11:00:00Z\"\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"tripId\": 124,\n" +
                                            "      \"title\": \"[수정] 행궁동 카페 투어!\",\n" +
                                            "      \"date\": \"2025-11-12\",\n" +
                                            "      \"createdAt\": \"2025-10-27T14:30:00Z\"\n" +
                                            "    }\n" +
                                            "  ],\n" +
                                            "  \"pagination\": {\n" +
                                            "    \"currentPage\": 1,\n" +
                                            "    \"totalPages\": 4,\n" +
                                            "    \"totalCount\": 40\n" +
                                            "  }\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/trips")
    public ResponseEntity<TravelDto.TripListResponse> getTripList(
            @Parameter(description = "JWT 토큰", required = true, hidden = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "조회할 페이지 번호", example = "1", required = true)
            @RequestParam("page") Integer page,
            @Parameter(description = "페이지당 가져올 항목 수", example = "10", required = true)
            @RequestParam("limit") Integer limit
    ) {
        TravelDto.TripListResponse response = travelService.getTripList(page, limit);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "여행 상세 조회",
            description = "특정 여행 계획의 상세 정보를 조회합니다. 인증이 필요합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TravelDto.TripDetailResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"tripId\": 123,\n" +
                                            "  \"title\": \"수원 화성 맛집 정복 코스\",\n" +
                                            "  \"date\": \"2025-11-10\",\n" +
                                            "  \"createdAt\": \"2025-10-26T11:00:00Z\",\n" +
                                            "  \"places\": [\n" +
                                            "    {\n" +
                                            "      \"placeId\": 101,\n" +
                                            "      \"name\": \"A 식당\",\n" +
                                            "      \"type\": \"맛집\",\n" +
                                            "      \"lat\": 37.288,\n" +
                                            "      \"lng\": 127.009\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"placeId\": 102,\n" +
                                            "      \"name\": \"B 카페\",\n" +
                                            "      \"type\": \"카페\",\n" +
                                            "      \"lat\": 37.288,\n" +
                                            "      \"lng\": 127.011\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증 만료"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/trips/{tripId}")
    public ResponseEntity<TravelDto.TripDetailResponse> getTripDetail(
            @Parameter(description = "JWT 토큰", required = true, hidden = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "조회하려는 여행 계획 ID", example = "123", required = true)
            @PathVariable("tripId") Integer tripId
    ) {
        TravelDto.TripDetailResponse response = travelService.getTripDetail(tripId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "여행 계획 수정",
            description = "저장된 여행 계획을 수정합니다. 인증이 필요합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "수정완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TravelDto.UpdateTripResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"tripId\": 123,\n" +
                                            "  \"title\": \"[수정] 행궁동 카페 투어\",\n" +
                                            "  \"date\": \"2025-11-12\",\n" +
                                            "  \"places\": [\n" +
                                            "    {\n" +
                                            "      \"placeId\": 102,\n" +
                                            "      \"name\": \"B 카페\",\n" +
                                            "      \"type\": \"카페\",\n" +
                                            "      \"lat\": 37.288,\n" +
                                            "      \"lng\": 127.011\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"placeId\": 105,\n" +
                                            "      \"name\": \"C 베이커리\",\n" +
                                            "      \"type\": \"카페\",\n" +
                                            "      \"lat\": 37.289,\n" +
                                            "      \"lng\": 127.012\n" +
                                            "    }\n" +
                                            "  ],\n" +
                                            "  \"updatedAt\": \"2025-10-27T14:30:00Z\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "요청값 누락")
    })
    @PutMapping("/trips/{tripId}")
    public ResponseEntity<TravelDto.UpdateTripResponse> updateTrip(
            @Parameter(description = "JWT 토큰", required = true, hidden = true)
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "수정하려는 계획의 tripId", example = "123", required = true)
            @PathVariable("tripId") Integer tripId,
            @RequestBody TravelDto.UpdateTripRequest request
    ) {
        TravelDto.UpdateTripResponse response = travelService.updateTrip(tripId, request);
        return ResponseEntity.ok(response);
    }
}
