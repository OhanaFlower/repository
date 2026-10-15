
package com.example.demo.dto;

import lombok.Data;

/**
 * 依頼1件ごとの金額を受け取るクラス
 * */

@Data
public class RequestPriceDto {

    private Integer id;
    private Integer price;
}
