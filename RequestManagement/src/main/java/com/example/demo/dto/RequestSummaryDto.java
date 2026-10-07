package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//ChatGPTに聞いて書いた

@Data
//下二つはDataに足りないコンストラクタを補う。
@NoArgsConstructor
@AllArgsConstructor
public class RequestSummaryDto {
	private Integer completedCount;
	private Integer inProgressCount;
	private Integer totalPrice;
	private Integer totalTip;
	private Integer totalWorkTime;
	
	
public String getWorkTimeDisplay() {
		
		//未入力時のエラー回避
		if(getTotalWorkTime() == null) {
			return null;
			}
		
		//処理部分
	    int hour = getTotalWorkTime() / 60;
	    int minute = getTotalWorkTime() % 60;

	    return hour + "時間" + minute + "分";
	}
}
