package com.ssafy.shalendar.springboot.web.dto.subscribe;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SubscribeRequestDto {
    private String fromId;
    private String toCheannel;

    @Builder
    public SubscribeRequestDto(String fromId, String toCheannel) {
        this.fromId = fromId;
        this.toCheannel = toCheannel;
    }
}
