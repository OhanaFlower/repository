package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.WorkLogListDto;
import com.example.demo.form.WorkLogForm;
import com.example.demo.mapper.RequestMapper;
import com.example.demo.mapper.WorkLogMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class WorkLogService{

	private final WorkLogMapper WorkLogMapper;//③用	
	private final RequestMapper RequestMapper;//⑤用

	
//③作業ログ全件表示（全体）
	public List<WorkLogListDto> selectAllWorkLogs() {
	    return WorkLogMapper.selectAllWorkLogs();
	}
	
//④作業ログを一件取得（修正用）
	public WorkLogForm selectById(Integer id) {
	    return WorkLogMapper.selectById(id);
	}
	    
//⑤更新前の進捗率を取得して、差の計算もする
	public void updateWorkLog(WorkLogForm form) {
		
		// ① 更新前のWorkLogを取得
		WorkLogForm before =
		        WorkLogMapper.selectById(form.getId());

	    // ② 更新前の進捗増加率
	    Integer beforeProgress = before.getProgressGain();

	    // ③ 更新後の進捗増加率
	    Integer afterProgress = form.getProgressGain();

	    // ④ 差分を計算
	    int difference = afterProgress - beforeProgress;

	    // ⑤ WorkLogを更新
	    WorkLogMapper.update(form);

	    // ⑥ 差分だけ依頼全体の進捗に加算
	    RequestMapper.addProgress(
	        form.getRequestId(),
	        difference
	    );
   	}

//⑥ログの削除（1件）
// 作業ログ削除
	  public void deleteWorkLog(Integer id) {

	      // ① 削除対象の作業ログを取得
	      WorkLogForm form = WorkLogMapper.selectById(id);

	      // ② 進捗増加量を取得（未入力なら0）
	      Integer progressGain = form.getProgressGain();

	      if (progressGain == null) {
	          progressGain = 0;
	      }

	      // ③ 依頼全体の進捗から、増加量を引く
	      RequestMapper.addProgress(
	          form.getRequestId(),
	          -progressGain
	      );

	      // ④ 作業ログを削除
	      WorkLogMapper.deleteById(id);
	  }
	
	
}
