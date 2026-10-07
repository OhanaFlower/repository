package com.example.demo.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkLogDto {

    private Integer id;
    private Integer requestId;
    private LocalDate workDate;
    private String workContents;
    private Integer progressGain;
    private String memo;
}
