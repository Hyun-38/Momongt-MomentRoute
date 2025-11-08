package com.Momongt.Momongt_MomentRoute.service;

import com.Momongt.Momongt_MomentRoute.dto.MainPageDto;
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

    public MainPageDto getMainPageInfo() {
        List<Event> events = eventRepository.findAll();

        List<MainPageDto.EventInfo> eventInfos = events.stream()
                .map(e -> new MainPageDto.EventInfo(
                        e.getTitle(),
                        e.getDescription(),
                        e.getImageUrl(),
                        e.getDate()
                ))
                .collect(Collectors.toList());

        return new MainPageDto(
                eventInfos,
                "경기도 여행, AI가 완벽하게 계획해드립니다"
        );
    }
}
