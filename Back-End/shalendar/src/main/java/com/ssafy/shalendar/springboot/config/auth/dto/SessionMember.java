package com.ssafy.shalendar.springboot.config.auth.dto;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.web.dto.member.MemberResponseDto;
import lombok.Getter;
import java.io.Serializable;

// 세션에 저장된 사람들
@Getter
public class SessionMember implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionMember(Member member) {
        this.name = member.getName();
        this.email = member.getId();
    }

    public SessionMember(MemberResponseDto memberRepo){
        this.email = memberRepo.getId();
    }
}