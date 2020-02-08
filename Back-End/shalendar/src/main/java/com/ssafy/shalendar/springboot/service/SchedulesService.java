package com.ssafy.shalendar.springboot.service;

import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.channel.ChannelRepositroy;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.member.MemberRepositroy;
import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import com.ssafy.shalendar.springboot.domain.schedules.SchedulesRepository;
import com.ssafy.shalendar.springboot.domain.subscribe.SubscribeRepository;
import com.ssafy.shalendar.springboot.web.dto.schedules.SchedulesResponseDto;
import com.ssafy.shalendar.springboot.web.dto.schedules.SchedulesSaveRequestDto;
import com.ssafy.shalendar.springboot.web.dto.schedules.SchedulesUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SchedulesService {
    private static Logger logger = LoggerFactory.getLogger(SchedulesService.class);
    private final SchedulesRepository schedulesRepository;
    private final MemberRepositroy memberRepositroy;
    private final ChannelRepositroy channelRepositroy;
    private final SubscribeRepository subscribeRepository;

    @Transactional
    public Boolean save(SchedulesSaveRequestDto requestDto){
        try {
            Member member =memberRepositroy.findMemberById(requestDto.getId());
            Channel channel = channelRepositroy.findChannelById(requestDto.getId());
            requestDto.setChannel(channel);
            requestDto.setMember(member);
            System.out.println("Schedules 서비스에서 일정 등록 실행 : ...."+requestDto.toString());
            schedulesRepository.save(requestDto.toEntity());
            return true;
        }catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Boolean update(SchedulesUpdateRequestDto requestDto) {
        try {
            Schedules sch=schedulesRepository.findById(requestDto.getSchNo()).orElseThrow(() -> new IllegalArgumentException("해당 일정이 없습니다. "));
            System.out.println("업데이트가 왜 안될까요???!!!!"+requestDto.toString());
            System.out.println("업데이트가 왜 안될까요???!!!!"+requestDto.toEntity().toString());
            sch.update(requestDto.toEntity());
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public List<SchedulesResponseDto> getSchedulesYymm(String id, String yymm) {
        List<Schedules> listSchedules;
        List<SchedulesResponseDto> listResponse=new LinkedList<>();
        Member member =memberRepositroy.findMemberById(id);
        Channel channel= channelRepositroy.findChannelById(id);
        if(member!=null){
            listSchedules=schedulesRepository.findByMemNoAndSdateOrEdate(member, yymm);
        }else{
            listSchedules=schedulesRepository.findByChNoAndSdateOrEdate(channel, yymm);
        }
        for(Schedules sc : listSchedules){
            SchedulesResponseDto schedulesResponseDto=new SchedulesResponseDto(sc);
            listResponse.add(schedulesResponseDto);
        }
        return listResponse;
    }

    public List<Schedules> searchAll() {
        List<Schedules> result = schedulesRepository.findAll();
        return result;
    }
    public Long findRecentSchNo(String id){
        Long schNo=0l;
        Member member =memberRepositroy.findMemberById(id);
        Channel channel= channelRepositroy.findChannelById(id);
        if(member!=null){
            schNo=schedulesRepository.findByMemNoOrderByCreatedDateDesc(member);
        }else{
            schNo=schedulesRepository.findByChNoOrderByCreatedDateDesc(channel);
        }
        return schNo;
    }

    @Transactional
    public Boolean delete(Long schNo) {
        Schedules sch=null;
        try {
            sch = schedulesRepository.findById(schNo).orElseThrow(() -> new IllegalArgumentException("해당 일정이 없습니다. "));
            if (sch == null) {
                throw new RuntimeException("해당 일정이 없습니다.");
            }
            schedulesRepository.delete(sch);
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Schedules> findScheduleswithSubscribe(Member member) {
        List<Schedules> result = schedulesRepository.findAll();
        return result;
    }
}
