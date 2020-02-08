package com.ssafy.shalendar.springboot.web.dto.feed;

import com.ssafy.shalendar.springboot.domain.feed.Feed;
import com.ssafy.shalendar.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedSaveRequestDto {
    private String content;
    private Long schNo;

    @Builder
    public FeedSaveRequestDto(String content, Long schNo) {
        this.content = content;
        this.schNo = schNo;
    }
}
