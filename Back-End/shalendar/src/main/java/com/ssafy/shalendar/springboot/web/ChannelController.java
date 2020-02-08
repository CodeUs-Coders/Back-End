package com.ssafy.shalendar.springboot.web;

import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.channel.Role;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.help.BoolResult;
import com.ssafy.shalendar.springboot.help.StringResult;

import com.ssafy.shalendar.springboot.service.ChannelService;
import com.ssafy.shalendar.springboot.service.JwtService;
import com.ssafy.shalendar.springboot.service.MemberService;

import com.ssafy.shalendar.springboot.web.dto.channel.ChannelLoginRequestDto;
import com.ssafy.shalendar.springboot.web.dto.channel.ChannelResponseDto;
import com.ssafy.shalendar.springboot.web.dto.channel.ChannelSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.channel.ChannelUpdateRequestDto;
import com.ssafy.shalendar.springboot.web.dto.concern.ConcernDeleteRequestDto;
import com.ssafy.shalendar.springboot.web.dto.concern.ConcernSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/channel")
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin({"*"})
public class ChannelController {

    private final MemberService mService;
    private final ChannelService cService;

    private final JwtService jwtService;

    @PostMapping("/uploadImage/{email}")
    public ResponseEntity<Object> uploadImage(@RequestBody MultipartFile file, @PathVariable String email){
        StringResult nr = null;
        if (file != null) {
            List<HashMap> fileArrayList = new ArrayList<>();
            HashMap fileHashMap;

            String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\channel"; //파일 저장 경로, 설정파일로 따로 관리한다.

            File dir = new File(filePath); //파일 저장 경로 확인, 없으면 만든다.
            if (!dir.exists()) {
                dir.mkdirs();
            }

            fileHashMap = new HashMap();
            String originalFilename = file.getOriginalFilename(); //파일명

            String fileName = email + ".jpg";
            String fileFullPath = filePath + "/" + fileName; //파일 전체 경로

            try {
                //파일 저장
                file.transferTo(new File(fileFullPath)); //파일저장

                fileHashMap.put("fileName", fileName);
                fileHashMap.put("fileFullPath", fileFullPath);

                fileArrayList.add(fileHashMap);
                nr = new StringResult("uploadImage", originalFilename, "SUCCESS");

            } catch (Exception e) {
                System.out.println("postTempFile_ERROR======>" + fileFullPath);
                e.printStackTrace();
                nr = new StringResult("uploadImage", "-1", "FAIL");
            }
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } else {
            nr = new StringResult("uploadImage", "no image", "SUCCESS");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> addChannel(@RequestBody ChannelSaveRequestDto channel) {
        System.out.println(channel);
        try {
            ChannelResponseDto searchedChannel = cService.searchChannel(channel.getId());
            MemberResponseDto searchedMember = mService.searchMember(channel.getId());
            StringResult nr = null;
            if (searchedChannel == null && searchedMember == null) {
                channel.setRole(Role.USER);
                boolean result = cService.signup(channel);
                if (result) {
                    nr = new StringResult("addChannel", channel.getId(), "SUCCESS");
                } else {
                    nr = new StringResult("addChannel", "-1", "FAIL");
                }
            } else {
                nr = new StringResult("addChannel", "Duplicate ID", "FAIL");
            }
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } catch(RuntimeException e) {
            log.error("addChannel", e);
            throw e;
        }
    }

    @GetMapping("/findChannelById/{id}")
    public ResponseEntity<Object> findChannelById(@PathVariable String id) {
        log.trace("findChannelById");
        try {
            ChannelResponseDto channel = cService.searchChannel(id);
            System.out.println(channel.getId() + " " + channel.getNickname());
            return new ResponseEntity<Object>(channel, HttpStatus.OK);
        } catch(RuntimeException e) {
            log.error("findChannelById", e);
            throw e;
        }
    }

    @GetMapping("findAllChannels")
    public ResponseEntity<Object> findAllChannels() {
        log.trace("findAllChannels");
        try {
            List<Channel> channels = cService.searchAll();
            for (int i = 0; i < channels.size(); i++){
                System.out.println(channels.get(i).getId() + " " + channels.get(i).getNickname());
            }
            return new ResponseEntity<Object>(channels, HttpStatus.OK);
        } catch(RuntimeException e) {
            log.error("findAllChannels", e);
            throw e;	// spring, tomcat이 받음
        }
    }

    @PutMapping("/updateChannels")
    @ApiOperation(value="채널정보를 수정한다.", response= BoolResult.class)
    public ResponseEntity<Object> updateChannel(@RequestBody ChannelUpdateRequestDto channel){
        try {
            boolean result = cService.update(channel.getId(), channel);
            BoolResult br = null;
            if (result) {
                br = new BoolResult(true, "updateChannel", "SUCCESS");
            } else {
                br = new BoolResult(false, "updateChannel", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch(RuntimeException e) {
            log.error("updateChannel", e);
            throw e;
        }
    }

    @DeleteMapping("/deleteChannel/{id}")
    @ApiOperation(value="채널정보를 삭제한다.", response=BoolResult.class)
    public ResponseEntity<Object> deleteChannel(@PathVariable String id){
        log.trace("deleteChannel");
        try {
            boolean result = cService.delete(id);
            BoolResult br = null;
            if (result) {
                br = new BoolResult(true, "deleteChannel", "SUCCESS");
            } else {
                br = new BoolResult(false, "deleteChannel", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch(RuntimeException e) {
            log.error("deleteChannel", e);
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody ChannelLoginRequestDto channel) {    //HTTP요청의 내용을 객체에 매핑하기 위해 @RequestBody 를 설정.
        Map<String, Object> resultmap = new HashMap<>();
        HttpStatus status = null;
        try {
            Member loginChannel = mService.login(channel.getId()).toEntity();
            if (loginChannel != null) {
                String token = jwtService.create(loginChannel);
                // 토큰 정보는 request의 헤더로 보내고 나머지는 Map에 담아주자.
//                res.setHeader("jwt-auth-token", token);
                resultmap.put("jwt-auth-token", token);
                resultmap.put("status", true);
                resultmap.put("data", loginChannel);
            } else {
//                res.setHeader("jwt-auth-token", null);
                resultmap.put("jwt-auth-token", null);
                resultmap.put("status", false);
                resultmap.put("data", "-1");
            }
            status = HttpStatus.ACCEPTED;
        } catch (RuntimeException e){
            log.error("로그인 실패");
            resultmap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultmap, status);
    }

    @GetMapping("/getMyInterests/{id}")
    public ResponseEntity<Object> getMyInterest(@PathVariable String id){
        try {
            List<String> concerns = cService.getInterests(id);
            return new ResponseEntity<>(concerns, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("getMyConcerns", e);
            throw e;
        }
    }

    @PostMapping("/saveInterest")
    public ResponseEntity<Object> saveInterest(@RequestBody ConcernSaveRequestDto requestDto){
        try {
            BoolResult br = null;
            boolean result = cService.saveInterest(requestDto);
            if (result){
                br = new BoolResult(true, "saveInterest", "SUCCESS");
            } else {
                br = new BoolResult(false, "saveInterest", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("saveInterest", e);
            throw e;
        }
    }

    @DeleteMapping("/deleteInterest/")
    public ResponseEntity<Object> deleteInterest(@RequestBody ConcernDeleteRequestDto requestDto){
        try {
            BoolResult br = null;
            boolean result = cService.deleteInterest(requestDto);
            if (result){
                br = new BoolResult(true, "deleteInterest", "SUCCESS");
            } else {
                br = new BoolResult(false, "saveInterest", "FAIL");
            }
            return new ResponseEntity<>(br, HttpStatus.OK);
        } catch (RuntimeException e){
            log.error("deleteInterest", e);
            throw e;
        }
    }
}
