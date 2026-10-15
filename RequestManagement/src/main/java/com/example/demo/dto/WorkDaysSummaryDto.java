package com.example.demo.dto;

import lombok.Data;

@Data
public class WorkDaysSummaryDto {
    private Double averageWorkDays;
    private Integer minWorkDays;
    private Integer maxWorkDays;
}
