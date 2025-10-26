package com.Momongt.Momongt_MomentRoute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MainPageResponseDto {

    private List<EventInfo> eventList;
    private String welcomeMessage;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventInfo {
        private String title;
        private String description;
        private String imageUrl;
        private String date;
    }
}
