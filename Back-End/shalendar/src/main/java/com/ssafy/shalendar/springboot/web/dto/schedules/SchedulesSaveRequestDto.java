package com.ssafy.shalendar.springboot.web.dto.schedules;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.member.Member;
import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class SchedulesSaveRequestDto {
    private String title;
    private String contents;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String place;
    private String attendants;
    private String id;
    private Member member;
    private Channel channel;

    @Builder
    public SchedulesSaveRequestDto(String title, String contents, LocalDateTime startAt, LocalDateTime endAt, String place, String attendants, String id){
        this.title=title;
        this.contents=contents;
        this.startAt=startAt;
        this.endAt=endAt;
        this.place=place;
        this.attendants=attendants;
        this.id=id;
    }
    public Schedules toEntity(){
        return Schedules.builder().title(title).contents(contents).sdate(startAt).edate(endAt).place(place).attendants(attendants).memberNo(member).chNo(channel).build();
    }

    @Override
    public String toString() {
        return "SchedulesSaveRequestDto{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", place='" + place + '\'' +
                ", attendants='" + attendants + '\'' +
                ", id='" + id + '\'' +
                ", member=" + member +
                ", channel=" + channel +
                '}';
    }
}
