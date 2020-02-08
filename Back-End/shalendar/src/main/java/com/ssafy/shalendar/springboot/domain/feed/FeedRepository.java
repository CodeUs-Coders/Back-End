package com.ssafy.shalendar.springboot.domain.feed;

import com.ssafy.shalendar.springboot.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    //SpringDataJpa 에서 제공하지 않는 메소드는 아래처럼 쿼리로 작성 해도 됨
    @Query("SELECT f FROM Feed f ORDER BY  f.feedNo DESC")
    List<Feed> findAllDesc();
}
