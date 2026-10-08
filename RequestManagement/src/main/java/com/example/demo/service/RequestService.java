package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.RequestListDto;
import com.example.demo.dto.RequestSummaryDto;
import com.example.demo.form.RequestForm;
import com.example.demo.mapper.RequestMapper;

@Service
public class RequestService {
	
  private final RequestMapper requestMapper;
  
  //コンストラクタ
  public RequestService(RequestMapper requestMapper) {
	  this.requestMapper = requestMapper;
  }
  
  //①総計表示用
  public RequestSummaryDto selectSummary() {
	  return requestMapper.selectSummary();
  }
  
  //②REQUESTSテーブル表示用
  public List<RequestListDto> selectAllRequests(String period, String status) {
	    return requestMapper.selectAllRequests(period, status);
	}
  
  //②'同上。納期順で並べる。
  public List<RequestListDto> selectAllRequestsByDeadline() {
	    return requestMapper.selectAllRequestsByDeadline();
	}
  
  //③依頼登録時、入力項目を表示する用
  public List<String> selectKind() {
	    return requestMapper.selectKind();
	}
  public List<String> selectKindDetail() {
	    return requestMapper.selectKindDetail();
	}
  public List<String> selectChara() {
	    return requestMapper.selectChara();
	}
  public List<String> selectCliantName() {
	    return requestMapper.selectClientName();
	}
  
  //④IDを取得して一件表示
  public RequestForm selectById(Integer id) {
	    return requestMapper.selectById(id);
	}
  
  //⑤内容修正用
  public void update(RequestForm form) {
	    requestMapper.update(form);
	}
}
