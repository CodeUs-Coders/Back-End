package com.ssafy.shalendar.springboot.web.dto.feed;

import com.ssafy.shalendar.springboot.domain.feed.Feed;
import com.ssafy.shalendar.springboot.domain.posts.Posts;
import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedListResponseDto {
    private Long id;
    private String content;

    public FeedListResponseDto(Feed Entity) {
        this.id = Entity.getFeedNo();
        this.content = Entity.getContent();
    }
}
