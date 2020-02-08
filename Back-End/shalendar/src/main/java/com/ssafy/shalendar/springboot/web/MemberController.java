package com.ssafy.shalendar.springboot.web;

import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.member.Role;
import com.ssafy.shalendar.springboot.help.BoolResult;
import com.ssafy.shalendar.springboot.help.StringResult;

import com.ssafy.shalendar.springboot.service.ChannelService;
import com.ssafy.shalendar.springboot.service.JwtService;
import com.ssafy.shalendar.springboot.service.KakaoAPI;
import com.ssafy.shalendar.springboot.service.MemberService;
import com.ssafy.shalendar.springboot.web.dto.channel.ChannelResponseDto;

import com.ssafy.shalendar.springboot.web.dto.concern.ConcernDeleteRequestDto;
import com.ssafy.shalendar.springboot.web.dto.concern.ConcernSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberLoginRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberResponseDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberUpdateRequestDto;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin({"*"})
public class MemberController {
    //어느 컨트롤러에서나 @LoginUser 를 선언하면 세션 정보를 가져올 수 있다

    private final MemberService mService;
    private final ChannelService cService;

    private final JwtService jwtService;
    private final KakaoAPI kakao;

    @GetMapping("/getImage/{email}")

    public ResponseEntity<Object> getImage(@PathVariable String email) {
        try {
//            InputStream in = getClass().getResourceAsStream("/static/img/member/" + email + ".jpg");
//            if (in == null) throw new FileNotFoundException();
            URL imgURL = getClass().getResource("/static/img/member/" + email + ".jpg");
            if (imgURL == null) throw new FileNotFoundException();

            BufferedImage img = ImageIO.read(imgURL);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", bos);
            System.out.println("찾음");
            return new ResponseEntity<Object>(bos.toByteArray(), HttpStatus.OK);
        } catch (FileNotFoundException e) {
            System.out.println("파일낫파운드");
            return null;
        } catch (IOException e) {
            System.out.println("아이오엑셉션");
            return null;
        }
    }


    @PostMapping("/uploadImage/{email}")
    public ResponseEntity<Object> uploadImage(@RequestBody MultipartFile file, @PathVariable String email) {
        StringResult nr = null;
        if (file != null) {
            System.out.println("email: " + email);
            List<HashMap> fileArrayList = new ArrayList<HashMap>();
            HashMap fileHashMap;

            String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\member"; //파일 저장 경로, 설정파일로 따로 관리한다.

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

            } catch (Exception e) {
                System.out.println("postTempFile_ERROR======>" + fileFullPath);
                e.printStackTrace();
            }
            nr = new StringResult("uploadImage", originalFilename, "SUCCESS");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } else {
            nr = new StringResult("uploadImage", "no image", "SUCCESS");
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        }
    }

    @GetMapping("/isExist/{id}")
    public ResponseEntity<Object> isExist(@PathVariable String id) {
        System.out.println(id);
        BoolResult br = null;
        try {
            boolean result = mService.isExist(id);

            if (!result) {
                br = new BoolResult(true, "isExist", "SUCCESS");
            } else {
                br = new BoolResult(false, "isExist", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("isExist", e);
            throw e;
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> addMember(@RequestBody MemberSaveRequestDto member) {
        System.out.println(member);
        try {
            MemberResponseDto searchedMember = mService.searchMember(member.getId());
            ChannelResponseDto searchedChannel = cService.searchChannel(member.getId());
            StringResult nr = null;
            if (searchedMember == null && searchedChannel == null) {
                member.setRole(Role.USER);
                boolean result = mService.signup(member);
                if (result) {
                    nr = new StringResult("addMember", member.getId(), "SUCCESS");
                } else {
                    nr = new StringResult("addMember", "-1", "FAIL");
                }
            } else {
                nr = new StringResult("addMember", "Duplicate ID", "FAIL");
            }
            return new ResponseEntity<Object>(nr, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("addMember", e);
            throw e;
        }
    }

    @GetMapping("/findMemberById/{id}")
    public ResponseEntity<Object> findMemberById(@PathVariable String id) {
        log.trace("findMemberById");
        try {
            MemberResponseDto member = mService.searchMember(id);
            return new ResponseEntity<Object>(member, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("findMemberById", e);
            throw e;
        }
    }

    @GetMapping("findAllMembers")
    public ResponseEntity<Object> findAllMembers() {
        log.trace("findAllMembers");
        try {

            List<MemberResponseDto> members = mService.searchAll();
            return new ResponseEntity<Object>(members, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("findAllMembers", e);
            throw e;    // spring, tomcat이 받음
        }
    }

    @PutMapping("/updateMember")
    @ApiOperation(value = "회원정보를 수정한다.", response = BoolResult.class)
    public ResponseEntity<Object> updateMember(@RequestBody MemberUpdateRequestDto member) {
        log.trace("updateBoard");
        try {
            boolean result = mService.update(member.getId(), member);
            BoolResult br = null;
            if (result) {
                br = new BoolResult(true, "updateMember", "SUCCESS");
            } else {
                br = new BoolResult(false, "updateMember", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("updateMember", e);
            throw e;
        }
    }

    @DeleteMapping("/deleteMember/{id}")
    @ApiOperation(value = "회원정보를 삭제한다.", response = BoolResult.class)
    public ResponseEntity<Object> deleteMember(@PathVariable String id) {
        log.trace("deleteMember");
        try {
            boolean result = mService.delete(id);
            BoolResult br = null;
            if (result) {
                br = new BoolResult(true, "deleteMember", "SUCCESS");
            } else {
                br = new BoolResult(false, "deleteMember", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("deleteMember", e);
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody MemberLoginRequestDto member) {    //HTTP요청의 내용을 객체에 매핑하기 위해 @RequestBody 를 설정.
        Map<String, Object> resultmap = new HashMap<>();
        HttpStatus status = null;
        try {
            MemberResponseDto mrd = mService.login(member.getId());
            if (mrd == null) {
                resultmap.put("jwt-auth-token", null);
                resultmap.put("status", false);
                resultmap.put("data", "-1");
            } else {
                Member loginMember = mrd.toEntity();
                String token = jwtService.create(loginMember);
                // 토큰 정보는 request의 헤더로 보내고 나머지는 Map에 담아주자.
//                res.setHeader("jwt-auth-token", token);
                resultmap.put("jwt-auth-token", token);
                resultmap.put("status", true);
                resultmap.put("data", loginMember);
            }
            status = HttpStatus.ACCEPTED;
        } catch (RuntimeException e) {
            log.error("로그인 실패");
            resultmap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultmap, status);
    }

    @GetMapping("/getMyInterests/{id}")
    public ResponseEntity<Object> getMyInterest(@PathVariable String id) {
        try {
            List<String> concerns = mService.getInterests(id);
            return new ResponseEntity<>(concerns, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("getMyConcerns", e);
            throw e;
        }
    }

    @PostMapping("/saveInterest")
    public ResponseEntity<Object> saveInterest(@RequestBody ConcernSaveRequestDto requestDto) {
        try {
            BoolResult br = null;
            boolean result = mService.saveInterest(requestDto);
            if (result) {
                br = new BoolResult(true, "saveInterest", "SUCCESS");
            } else {
                br = new BoolResult(false, "saveInterest", "FAIL");
            }
            return new ResponseEntity<Object>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("saveInterest", e);
            throw e;
        }
    }

    @DeleteMapping("/deleteInterest/")
    public ResponseEntity<Object> deleteInterest(@RequestBody ConcernDeleteRequestDto requestDto) {
        try {
            BoolResult br = null;
            boolean result = mService.deleteInterest(requestDto);
            if (result) {
                br = new BoolResult(true, "deleteInterest", "SUCCESS");
            } else {
                br = new BoolResult(false, "saveInterest", "FAIL");
            }
            return new ResponseEntity<>(br, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("deleteInterest", e);
            throw e;
        }
    }


    @GetMapping("/callbackKakao")
    public ResponseEntity<HashMap<String, Object>> callbackKaKao(@RequestParam("code") String code, HttpSession session
    ) throws IOException {
        String access_Token = kakao.getAccessToken(code);
        System.out.println("controller access_token : " + access_Token);
        HashMap<String, Object> userInfo = kakao.getUserInfo(access_Token);
        System.out.println("login Controller : " + userInfo);
        session.setAttribute("userInfo", userInfo);
        return new ResponseEntity<HashMap<String, Object>>(userInfo, HttpStatus.OK);
    }
}
