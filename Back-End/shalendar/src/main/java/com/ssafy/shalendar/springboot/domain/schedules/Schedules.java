package com.ssafy.shalendar.springboot.domain.schedules;

import com.ssafy.shalendar.springboot.domain.BaseTimeEntity;
import com.ssafy.shalendar.springboot.domain.channel.Channel;
import com.ssafy.shalendar.springboot.domain.feed.Feed;
import com.ssafy.shalendar.springboot.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Schedules extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schNo;

    @Column(length = 30, nullable = false)
    private String title;
    @Column(length = 200)
    private String contents;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime sdate;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime edate;
    @Column(length = 50)
    private String place;
    @Column(length = 100)
    private String attendants;

    @ManyToOne
    @JoinColumn(name = "memNo")
    private Member memNo;

    @ManyToOne
    @JoinColumn(name = "chNo")
    private Channel chNo;

//    @OneToOne
//    @JoinColumn(name = "feedNo")
//    private Feed feed;

    @Builder
    public Schedules(String title, String contents, LocalDateTime sdate, LocalDateTime edate, String place, String attendants, Member memberNo, Channel chNo, Feed feed) {
        this.title = title;
        this.contents = contents;
        this.sdate = sdate;
        this.edate = edate;
        this.place = place;
        this.attendants = attendants;
        this.memNo = memberNo;
        this.chNo = chNo;
    }

    public void update(Schedules schedules){
        this.title=schedules.getTitle();
        this.contents=schedules.getContents();
        this.sdate=schedules.getSdate();
        this.edate=schedules.getEdate();
        this.place=schedules.getPlace();
        this.attendants=schedules.getAttendants();
    }

    // feed 만 업데이트
//    public void update(Feed feed) {
//        this.feed = feed;
//    }
    @Override
    public String toString() {
        return "Schedules{" +
                "schNo=" + schNo +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", sdate=" + sdate +
                ", edate=" + edate +
                ", place='" + place + '\'' +
                ", attendants='" + attendants + '\'' +
                ", memNo=" + memNo +
                ", chNo=" + chNo +
                '}';
    }
}
