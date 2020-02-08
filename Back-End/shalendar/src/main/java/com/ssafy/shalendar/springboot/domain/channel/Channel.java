package com.ssafy.shalendar.springboot.domain.channel;

import com.ssafy.shalendar.springboot.domain.BaseTimeEntity;
import com.ssafy.shalendar.springboot.web.dto.channel.ChannelUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Channel extends BaseTimeEntity {
    @Column(nullable = false, columnDefinition = "varchar(45)")
    private String id;
    @Id  //해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //PK의 생성 규칙
    private Long ch_no;
    @Column(nullable = false, columnDefinition = "varchar(200)")
    private String pw;
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String name;
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String nickname;
    private String img;
    @Column(columnDefinition = "varchar(200)")
    private String link;
    @Column(columnDefinition = "varchar(200)")
    private String msg;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Builder
    public Channel(Long ch_no, String id, String pw, String name, String nickname,
                   String img, String link, String msg, Role role) {
        this.ch_no = ch_no;
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.nickname = nickname;
        this.img = img;
        this.link = link;
        this.msg = msg;
        this.role = role;
    }
    public void update(ChannelUpdateRequestDto channel) {
        this.pw = channel.getPw();
        this.name = channel.getName();
        this.nickname = channel.getNickname();
        this.img = channel.getImg();
        this.link = channel.getLink();
        this.msg = channel.getMsg();
    }
    public Channel updateGoogle(String name, String picture) {
        this.name = name;
        this.img = picture;
        return this;
    }
    public String getRoleKey() {
        return this.role.getKey();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ", ch_no=" + ch_no +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", img='" + img + '\'' +
                ", link='" + link + '\'' +
                ", msg='" + msg + '\'' +
                ", role=" + role +
                '}';
    }
}