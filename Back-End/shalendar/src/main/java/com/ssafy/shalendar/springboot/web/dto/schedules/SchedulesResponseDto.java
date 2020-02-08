package com.ssafy.shalendar.springboot.web.dto.schedules;

import com.ssafy.shalendar.springboot.domain.filter.Filter;
import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SchedulesResponseDto {
    private String title;
    private String contents;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String place;
    private String attendants;
    private boolean isOneDay;
    private Long schNo;

    public SchedulesResponseDto(Schedules entity){
        this.title=entity.getTitle();
        this.contents=entity.getContents();
        this.place=entity.getPlace();
        this.attendants=entity.getAttendants();
        this.startAt=entity.getSdate();
        this.endAt=entity.getEdate();
        this.schNo=entity.getSchNo();
        this.isOneDay=entity.getSdate().equals(entity.getEdate());
    }

    @Override
    public String toString() {
        return "SchedulesResponseDto{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", place='" + place + '\'' +
                ", attendants='" + attendants + '\'' +
                ", isOneDay=" + isOneDay +
                ", schNo=" + schNo +
                '}';
    }
}
