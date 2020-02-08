package com.ssafy.shalendar.springboot.service;

import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.channel.ChannelRepositroy;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.member.MemberRepositroy;
import com.ssafy.shalendar.springboot.domain.subscribe.Subscribe;
import com.ssafy.shalendar.springboot.domain.subscribe.SubscribeRepository;
import com.ssafy.shalendar.springboot.web.dto.subscribe.SubscribeListResponseDto;
import com.ssafy.shalendar.springboot.web.dto.subscribe.SubscribeRequestDto;
import com.ssafy.shalendar.springboot.web.dto.subscribe.SubscribeResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubscribeService {

    private static Logger logger = LoggerFactory.getLogger(SubscribeService.class);

    private final MemberRepositroy memberRepositroy;
    private final ChannelRepositroy channelRepositroy;
    private final SubscribeRepository subscribeRepository;

    @Transactional
    public boolean subscribe(SubscribeRequestDto requestDto) {
        String fromId = requestDto.getFromId();
        String toChannelId = requestDto.getToCheannel();
        try {
            Member fromMember = memberRepositroy.findMemberById(fromId);
            Channel toChannel = channelRepositroy.findChannelById(toChannelId);
            if (fromMember == null || toChannel == null){
                return false;
            }
            Subscribe newSubscribe = Subscribe.builder().fromMember(fromMember).toChannel(toChannel).build();
            subscribeRepository.save(newSubscribe);
            return true;
        } catch (RuntimeException e){
            logger.error("구독 실패", e);
            return false;
        }
    }

    @Transactional
    public boolean unSubscribe(SubscribeRequestDto requestDto) {
        String fromId = requestDto.getFromId();
        String toChannelId = requestDto.getToCheannel();
        try {
            Member fromMember = memberRepositroy.findMemberById(fromId);
            Channel toChannel = channelRepositroy.findChannelById(toChannelId);
            if (fromMember == null || toChannel == null){
                return false;
            }
            subscribeRepository.deleteByFromMemberAndToChannel(fromMember, toChannel);
            return true;
        } catch (RuntimeException e) {
            logger.error("구독 취소 실패", e);
            return false;
        }
    }

    @Transactional
    public List<SubscribeListResponseDto> findAllSubscribe(String fromId) {
        Member fromMember = memberRepositroy.findMemberById(fromId);
        if (fromMember == null){
            return null;
        }
        List<Subscribe> subscriberList = subscribeRepository.findAllByFromMember(fromMember);

        List<SubscribeListResponseDto> channels = new ArrayList<>();
        for (Subscribe subscribe : subscriberList) {
            String id = subscribe.getToChannel().getId();
            Channel channel = channelRepositroy.findChannelById(id);
            SubscribeListResponseDto dto = new SubscribeListResponseDto(channel.getId(), channel.getNickname(), channel.getImg());
            channels.add(dto);
        }
        return channels;
    }

    @Transactional
    public List<SubscribeListResponseDto> findAllSubscriber(String toChannelId) {
        Channel toChannel = channelRepositroy.findChannelById(toChannelId);
        if (toChannel == null){
            return null;
        }
        List<Subscribe> subscriberList = subscribeRepository.findAllByToChannel(toChannel);

        List<SubscribeListResponseDto> members = new ArrayList<>();
        for (Subscribe subscribe : subscriberList) {
            String id = subscribe.getFromMember().getId();
            Member member = memberRepositroy.findMemberById(id);
            SubscribeListResponseDto dto = new SubscribeListResponseDto(member.getId(), member.getNickname(), member.getImg());
            members.add(dto);
        }
        return members;
    }

    public Integer getCountOfMySubscribe(String fromId){
        Member fromMember = memberRepositroy.findMemberById(fromId);
        if (fromMember == null){
            return -1;
        }
        return subscribeRepository.countAllByFromMember(fromMember);
    }

    public Integer getCountOfMySubscriber(String toChannelId){
        Channel toChannel = channelRepositroy.findChannelById(toChannelId);
        if (toChannel == null){
            return -1;
        }
        return subscribeRepository.countAllByToChannel(toChannel);
    }

    public Boolean isExist(String fromId, String toChannelId){
        Member fromMember = memberRepositroy.findMemberById(fromId);
        Channel toChannel = channelRepositroy.findChannelById(toChannelId);

        boolean result = subscribeRepository.existsByFromMemberAndToChannel(fromMember, toChannel);
        return result;
    }
}
