package com.Momongt.Momongt_MomentRoute.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "메인 페이지 응답")
public class MainPageDto {

    @Schema(description = "이벤트 목록", example = "[ { \"title\": \"가을 이벤트\", \"description\": \"특별 쿠폰...\", \"imageUrl\": \"https://...\", \"date\": \"2025-10-27\" } ]")
    private List<EventInfo> eventList;

    @Schema(description = "환영 메시지", example = "환영합니다!")
    private String welcomeMessage;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "이벤트 정보")
    public static class EventInfo {
        @Schema(description = "이벤트 제목", example = "가을 이벤트")
        private String title;

        @Schema(description = "이벤트 설명", example = "특별 쿠폰...")
        private String description;

        @Schema(description = "이벤트 이미지 URL", example = "https://...")
        private String imageUrl;

        @Schema(description = "이벤트 날짜", example = "2025-10-27")
        private String date;
    }
}
