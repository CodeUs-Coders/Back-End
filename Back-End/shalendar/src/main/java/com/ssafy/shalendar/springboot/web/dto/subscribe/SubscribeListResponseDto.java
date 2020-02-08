package com.ssafy.shalendar.springboot.web.dto.subscribe;

import lombok.Getter;

@Getter
public class SubscribeListResponseDto {
    private String id;
    private String nickName;
    private String img;

    public SubscribeListResponseDto(String id, String nickName, String img) {
        this.id = id;
        this.nickName = nickName;
        this.img = img;
    }
}
