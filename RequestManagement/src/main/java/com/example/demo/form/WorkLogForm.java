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
public class WorkLogForm {
  private Integer requestId;
  private LocalDate workDate;//LocalDateはDate型に対応
  private String workContents;
  private Integer progressGain;
  private String memo;
  private Integer completed;
  private Integer currentProgress;//現在の進捗度を表示する
  
  //追加
  private Integer id;//作業単体の認識用
}
