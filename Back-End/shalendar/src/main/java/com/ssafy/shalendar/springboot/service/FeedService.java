package com.ssafy.shalendar.springboot.service;

import com.ssafy.shalendar.springboot.domain.feed.Feed;
import com.ssafy.shalendar.springboot.domain.feed.FeedRepository;
import com.ssafy.shalendar.springboot.domain.follow.Follow;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.posts.Posts;
import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import com.ssafy.shalendar.springboot.domain.schedules.SchedulesRepository;
import com.ssafy.shalendar.springboot.web.dto.feed.FeedListResponseDto;
import com.ssafy.shalendar.springboot.web.dto.feed.FeedSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.feed.FeedUpdateRequestDto;
import com.ssafy.shalendar.springboot.web.dto.feed.PostsListResponseDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FeedService {

    private static Logger logger = LoggerFactory.getLogger(FollowService.class);
    private final FeedRepository feedRepository;
    private final SchedulesRepository schedulesRepository;

    // 피드를 생성하면 피드 엔티티 만들고 피드를 schedule에 넣어야 한다.
    @Transactional
    public boolean save(FeedSaveRequestDto requestDto) {
        String content = requestDto.getContent();
        Long schNo = requestDto.getSchNo();
        try {
            Optional<Schedules> Oschedules = schedulesRepository.findById(schNo);
            System.out.println(Oschedules);
            Schedules schedules = Oschedules.get();
            Feed newFeed = Feed.builder().content(content).build();
            if (schedules == null || newFeed == null) {
                return false;
            }
//            schedules.update(newFeed);
            feedRepository.save(newFeed);
            return true;
        } catch (RuntimeException e) {
            logger.error("FEED 공유 실패", e);
            return false;
        }
    }

    @Transactional
    public boolean update(Long id, FeedUpdateRequestDto requestDto) {
        String content = requestDto.getContent();
        try {
            Optional<Feed> OFeed = feedRepository.findById(id);
            Feed feed = OFeed.get();
            if (feed == null) {
                return false;
            }
            feed.update(requestDto.getContent());
            return true;
        } catch (RuntimeException e) {
            logger.error("FEED 수정 실패", e);
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<FeedListResponseDto> findAllDesc() {
        return feedRepository.findAllDesc().stream()
                .map(FeedListResponseDto::new)  //.map(posts -> new PostsListResponseDto(posts)) 와 같은 람다식
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean delete(Long id) {
        try {
            Optional<Feed> OFeed = feedRepository.findById(id);
            Feed feed = OFeed.get();
            if (feed == null) {
                return false;
            }
            System.out.println(feed.toString());
            feedRepository.delete(feed);
            return true;
        } catch (RuntimeException e) {
            logger.error("FEED 삭제 실패", e);
            return false;
        }
    }
}
