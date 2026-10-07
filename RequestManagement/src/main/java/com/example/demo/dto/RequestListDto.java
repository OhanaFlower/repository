package com.example.demo.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 依頼一覧を表示するクラス
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestListDto {
	private Integer id;
    private String Kind;
    private String KindDetail;
    private Integer price;
    private Integer tip;
    private String clientName;
    private LocalDate receivedDay;
    private LocalDate deadLine;
    private Integer totalWorkTime;
    private LocalDate completedDay;//追加
    private Integer progress;
    private String status;
    //追加
    private String chara;
    private String memo;
    private Long remainingDays;
    
    
    /**納期算出用メソッド*/
	public Long getRemainingDays() {
		 if (deadLine == null) {
		 return null;//納期を設定していない場合はnullを返す
		 }
		 LocalDate today = LocalDate.now();
		 return ChronoUnit.DAYS.between(today, deadLine);
		 }
	
	/*データ表示調整用メソッド*/
	//分→時間・分表記
	public String getWorkTimeDisplay() {
		
		//未入力時のエラー回避
		if(getTotalWorkTime() == null || getTotalWorkTime() == 0) {
			return null;
		}else {
		
		//処理部分
	    int hour = getTotalWorkTime() / 60;
	    int minute = getTotalWorkTime() % 60;

	    return hour + "時間" + minute + "分";
		}
	}
}
