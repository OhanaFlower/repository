package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.WorkLogDto;
import com.example.demo.dto.WorkLogListDto;
import com.example.demo.form.WorkLogForm;

@Mapper
public interface WorkLogMapper {
    //①INSERT:WorkLogFormを受け取って、テーブルにINSERTする
	void insert(WorkLogForm form);
	
	//②SELECT:IDを指定してDBの中身を取得（個別の依頼で表示する用）
	List<WorkLogDto> selectByRequestId(Integer requestId);
	
	//③SELECT:作業ログ全件表示
	public List<WorkLogListDto> selectAllWorkLogs();
	
	//④SELECT/修正用：一件取得してidを引き渡す//
	WorkLogForm selectById(Integer id);
	
	//⑤更新前の進捗度を取得。
	void update(WorkLogForm form);

	//⑥ログの削除(一件)
	void deleteById(Integer id);
}
