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


public long getHourlyWage() {

    // 作業時間が未入力、または0以下の場合
    if (totalWorkTime == null || totalWorkTime <= 0) {
        return 0;
    }

    // 合計金額と合計チップを足す
    int totalAmount = totalPrice + totalTip;

    // 時給を計算して四捨五入
    //※60.0で小数点まで書くことで、小数部分が失われないようにする。
    return Math.round(totalAmount * 60.0 / totalWorkTime);
}
}
