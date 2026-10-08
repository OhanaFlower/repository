package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.RequestListDto;
import com.example.demo.dto.RequestSummaryDto;
import com.example.demo.form.RequestForm;

@Mapper//JavaとSQLをつなぐ(myBatis)
public interface RequestMapper {
    //①RequestFormを受け取って、テーブルにINSERTする
	void insert(RequestForm form);
	
	//②テーブルの総計系のデータを、Mapperから受け取る。
	RequestSummaryDto selectSummary();
	
	//③「状態-status」の更新
	void updateStatus(Integer requestId, String status);
	
	//④テーブルの「一覧」を取得
	List<RequestListDto> selectAllRequests(String period, String status);
	
	//④'同上。納期順に並べる。
	List<RequestListDto> selectAllRequestsByDeadline();
	
	//⑤テーブルの「過去の入力」を取得
	//依頼の種類
	List<String> selectKind();
	//種類の詳細
	List<String> selectKindDetail();
	//キャラ名
	List<String> selectChara();
	//依頼者
	List<String> selectClientName();
	
    //⑥確認用：登録依頼が過去とダブってないかチェック
    boolean checkPastRequest(RequestForm form);
    
	//⑦更新用：IDを指定して依頼を一件取得
	RequestForm selectById(Integer id);
	
	//⑧RequestFormを受け取って、テーブルにUPDATEする
    void update(RequestForm form);
    
    //⑨作業ログ登録時、進捗度をREQUESTSテーブルに加算
    void addProgress(Integer requestId, Integer progressGain);
    
    //⑩削除用：指定IDの依頼を削除
    void deleteById(Integer id);
    
    

}
