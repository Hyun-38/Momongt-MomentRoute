package com.Momongt.Momongt_MomentRoute.service;

import com.Momongt.Momongt_MomentRoute.dto.MainPageResponseDto;
import com.Momongt.Momongt_MomentRoute.entity.Event;
import com.Momongt.Momongt_MomentRoute.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final EventRepository eventRepository;

    public MainPageResponseDto getMainPageInfo() {
        List<Event> events = eventRepository.findAll();

        List<MainPageResponseDto.EventInfo> eventInfos = events.stream()
                .map(e -> new MainPageResponseDto.EventInfo(
                        e.getTitle(),
                        e.getDescription(),
                        e.getImageUrl(),
                        e.getDate()
                ))
                .collect(Collectors.toList());

        return new MainPageResponseDto(
                eventInfos,
                "경기도 여행, AI가 완벽하게 계획해드립니다"
        );
    }
}
