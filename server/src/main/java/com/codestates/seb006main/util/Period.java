package com.codestates.seb006main.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.time.LocalDate;

/**
 * 여행 기간 Embedded Type
 */
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Period {
    LocalDate startDate;
    LocalDate endDate;

    public Period(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
