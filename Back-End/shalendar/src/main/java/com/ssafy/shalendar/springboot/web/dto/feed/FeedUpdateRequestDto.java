package com.ssafy.shalendar.springboot.web.dto.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedUpdateRequestDto {
    private String content;

    @Builder
    public FeedUpdateRequestDto(String content) {
        this.content = content;
    }
}
