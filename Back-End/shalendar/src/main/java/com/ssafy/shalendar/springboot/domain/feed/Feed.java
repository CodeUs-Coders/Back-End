package com.ssafy.shalendar.springboot.domain.feed;

import com.ssafy.shalendar.springboot.domain.BaseTimeEntity;
import com.ssafy.shalendar.springboot.domain.schedules.Schedules;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Feed extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedNo;

    private String content;

//    @OneToOne(mappedBy = "feed")
//    private Schedules schedules;

    @Builder
    public Feed(String content) {
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "feedNo=" + feedNo +
                ", content='" + content +
                '}';
    }
}
