package com.jh.SomsomMarket.domain.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue(value="PERSONAL")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalItem extends Item {

    @Transient
    private String nickName; // 화면에 닉네임 출력하기 위한 필드

    @Column(name="start_date")
    private Date startDate; // 글 올린 날짜 ㅎ

}
