package com.ssafy.shalendar.springboot.service;

import com.ssafy.shalendar.springboot.domain.concern.Concern;
import com.ssafy.shalendar.springboot.domain.concern.ConcernRepository;
import com.ssafy.shalendar.springboot.domain.interest.Interest;
import com.ssafy.shalendar.springboot.domain.interest.InterestRepository;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.member.MemberRepositroy;

import com.ssafy.shalendar.springboot.web.dto.concern.ConcernDeleteRequestDto;
import com.ssafy.shalendar.springboot.web.dto.concern.ConcernSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberResponseDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.member.MemberUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private static Logger logger = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepositroy memberRepository;
    private final InterestRepository interestRepository;
    private final ConcernRepository concernRepository;

    public Boolean isExist(String id){
        try {
            return memberRepository.existsById(id);
        } catch (RuntimeException e){
            logger.error("존재여부 파악 실패", e);
            throw e;
        }
    }
    @Transactional
    public Boolean signup(MemberSaveRequestDto requestDto) {
        try {
            memberRepository.save(requestDto.toEntity()).getMem_no();

            Member member = memberRepository.findMemberById(requestDto.getId());

            List<String> interestsName = requestDto.getInterests();

            for (String interestName : interestsName) {
                Interest interest = interestRepository.findByInterestName(interestName);
                Concern newConcern = Concern.builder().member(member).interest(interest).build();
                concernRepository.save(newConcern);
            }
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    public MemberResponseDto searchMember(String id) {
        Member result = null;
        try {
            result = memberRepository.findMemberById(id);
        } catch (RuntimeException e) {
            logger.error("추가 실패", e);
        }
        if (result == null) {
            return null;
        } else {
            return new MemberResponseDto(result);
        }
    }

    public List<MemberResponseDto> searchAll() {
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDto> result = new ArrayList<>();
        for (Member member : members){
            result.add(new MemberResponseDto(member));
        }
        return result;
    }

    @Transactional
    public Boolean update(String id, MemberUpdateRequestDto requestDto) {
        MemberResponseDto member = null;
        try {
            member = new MemberResponseDto(memberRepository.findMemberById(id));
            Member memberEntity = memberRepository.findById(member.getMem_no()).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
            ;
            if (member == null) {
                throw new RuntimeException("해당 아이디가 없습니다.");
            }
            memberEntity.update(requestDto);
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Boolean delete(String id) {
        MemberResponseDto member = null;
        try {
            member = new MemberResponseDto(memberRepository.findMemberById(id));
            if (member == null) {
                throw new RuntimeException("해당 아이디가 없습니다.");
            }
            memberRepository.delete(member.toEntity());
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional(readOnly = true)
    public MemberResponseDto login(String id) {
        Member member = memberRepository.findMemberById(id);
        return new MemberResponseDto(member);
    }

    public List<String> getInterests(String id){
        List<Concern> concerns = concernRepository.findAllByMember_Id(id);
        List<String> result = new ArrayList<>();
        for (Concern concern : concerns){
            result.add(concern.getInterest().getInterestName());
        }
        return result;
    }

    @Transactional
    public boolean saveInterest(ConcernSaveRequestDto requestDto){
        try {
            Member member = memberRepository.findMemberById(requestDto.getId());
            Interest interest = interestRepository.findByInterestName(requestDto.getInterest());

            Concern searchedConcern = concernRepository.findByMemberAndInterest(member, interest);
            if (searchedConcern == null) {
                Concern newConcern = Concern.builder().member(member).interest(interest).build();
                concernRepository.save(newConcern);
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean deleteInterest(ConcernDeleteRequestDto requestDto){
        try {
            Member member = memberRepository.findMemberById(requestDto.getId());
            Interest interest = interestRepository.findByInterestName(requestDto.getInterest());

            int result = concernRepository.deleteByMemberAndInterest(member, interest);
            if (result != 0) {
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }
    }
}
