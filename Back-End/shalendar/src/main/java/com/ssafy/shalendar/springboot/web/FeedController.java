package com.ssafy.shalendar.springboot.web;

import com.ssafy.shalendar.springboot.help.BoolResult;
import com.ssafy.shalendar.springboot.service.FeedService;
import com.ssafy.shalendar.springboot.web.dto.feed.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/feed")
@RequiredArgsConstructor
@RestController
@Slf4j
@CrossOrigin({"*"})
public class FeedController {
    private final FeedService feedService;

    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody FeedSaveRequestDto requestDto) {
        BoolResult br = null;
        try {
            boolean result = feedService.save(requestDto);
            if (result) {
                br = new BoolResult(true, "feed", "SUCCESS");
            } else {
                br = new BoolResult(false, "feed", "FAIL");
            }
        } catch (RuntimeException e) {
            log.error("feed", e);
            throw e;
        }
        return new ResponseEntity<Object>(br, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody FeedUpdateRequestDto requestDto) {
        BoolResult br = null;
        try {
            boolean result = feedService.update(id, requestDto);
            if (result) {
                br = new BoolResult(true, "feed", "SUCCESS");
            } else {
                br = new BoolResult(false, "feed", "FAIL");
            }
        } catch (RuntimeException e) {
            log.error("feed", e);
            throw e;
        }
        return new ResponseEntity<Object>(br, HttpStatus.OK);
    }

    @GetMapping("/feedList")
    public ResponseEntity<Object> findById() {
        try {
            List<FeedListResponseDto> feedList = feedService.findAllDesc();
            if (feedList == null) {
                return new ResponseEntity<Object>("There isn't antthinFeed you provide", HttpStatus.OK);
            }
            for (int i = 0; i < feedList.size(); i++) {
                System.out.println(feedList.get(i));
            }
            return new ResponseEntity<Object>(feedList, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("feed", e);
            throw e;    // spring, tomcat이 받음
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        BoolResult br = null;
        try {
            boolean result = feedService.delete(id);
            if (result) {
                br = new BoolResult(true, "feed", "SUCCESS");
            } else {
                br = new BoolResult(false, "feed", "FAIL");
            }
        } catch (RuntimeException e) {
            log.error("feed", e);
            throw e;
        }
        return new ResponseEntity<Object>(br, HttpStatus.OK);
    }

}
