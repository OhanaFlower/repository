package com.example.demo.form;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*Formクラス
 *画面からデータを受け取ったり、コントローラーに引き渡したりする。*/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestForm {
	//変数名は、入力時のテキストボックスの名前と一緒
	private Integer id;
	private String orderKind;
	private String orderKindDetail;
	private Integer price;
	private String clientName;	
	private LocalDate receivedDay;
	private LocalDate startDay;
	private LocalDate deadLine; 
	private Integer progress;
	private LocalDate completedDay;
	private Integer tip;
	private Integer totalWorkTime;
	private Integer hourTime;
	private Integer minuteTime;
	private String status;
	//追加
	private String chara;
	private String memo;
	
	
/*データ表示調整用メソッド*/
	//分→時間・分表記
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

