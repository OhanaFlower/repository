
package com.example.demo.dto;

import lombok.Data;

/**
 * 依頼種類ごとの件数を受け取るクラス
 * */

@Data
public class RequestKindCountDto {

    private String kind;
    private Long count;
}
