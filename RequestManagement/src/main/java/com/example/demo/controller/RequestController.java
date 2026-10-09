package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.RequestListDto;
import com.example.demo.form.RequestForm;
import com.example.demo.mapper.RequestMapper;
import com.example.demo.service.RequestService;

import lombok.RequiredArgsConstructor;

@Controller //ブラウザのリクエストを受け付ける(Spring MVC)
@RequiredArgsConstructor //自動でコンストラクタを作る(Lombok)
public class RequestController {

	/*DIコンテナ(myBatis)*/
	//☆マッパーを作るたび、ここでfinal宣言する。@Required...にコンストラクタを作らせるため。
	private final RequestMapper requestMapper;
	private final RequestService requestService;
	
	
	
/*【一覧】
 * ①依頼登録
 * ②依頼一覧表示
 * ③依頼内容修正・削除*
 * /	
	

//①
/**
 * 【依頼登録】
 */
	
	/*登録画面の表示*/
	@GetMapping("/register")
	public String showRequestRegister(Model model) {

	    List<String> kind = requestService.selectKind();
	    model.addAttribute("kind", kind);
	    List<String> kindDetail = requestService.selectKindDetail();
	    model.addAttribute("kindDetail", kindDetail);
	    List<String> chara = requestService.selectChara();
	    model.addAttribute("chara", chara);
	    List<String> clientName = requestService.selectCliantName();
	    model.addAttribute("clientName", clientName);

	    return "requestRegister";
	}
	
	
	/*登録内容修正（受け取って、登録画面に戻す）*/
	@PostMapping("/requestRegister")
	public String rollBack(RequestForm form, Model model) {
		
		//進捗度が未入力なら0として扱う
		if (form.getProgress() == null) {
		    form.setProgress(0);
		}
		
		//データを格納
	    model.addAttribute("requestForm", form);
	

	    return "requestRegister";
	}
	
	
	/*登録内容受け取り→確認画面へ渡す*/
	@PostMapping("/register/check")
	public String confirm(RequestForm form,
			 @RequestParam(required = false) String clientName,
			 Model model) {
	
		/*格納前にデータを編集*/
		//①「状態-status」を自動判定して、フィールドにセットする
		if (form.getId() == null) {
		    if (form.getCompletedDay() != null || form.getTotalWorkTime() != null) {
		        form.setStatus("完了");
		    } else if (form.getStartDay() != null) {
		        form.setStatus("制作中");
		    } else {
		        form.setStatus("開始前");
		    }
		} 
		if (form.getCompletedDay() != null || form.getTotalWorkTime() != null) {
		        form.setStatus("完了");
		    } else if (form.getStartDay() != null) {
		        form.setStatus("制作中");
		    } else {
		        form.setStatus("開始前");
		 }
		 //②「時間」「分」をtotalWorkTimeへ変換
		 //加工のために取り出す
		 Integer hourTime = form.getHourTime();
		 Integer minuteTime = form.getMinuteTime();
		 //nullを0として扱う
		 hourTime = hourTime == null ? 0 : hourTime;
		 minuteTime = minuteTime == null ? 0 : minuteTime;
		 //分に変換
		 Integer total = hourTime * 60 + minuteTime;
		 //RequestFormに設定
		 form.setTotalWorkTime(total);
		 
		//③過去依頼とダブってないかチェックする
		if (form.getId() == null) {
			
		     boolean pastRequest = requestMapper.checkPastRequest(form);//作った変数に、メソッドの結果をセットする。

			 if (pastRequest) {//trueの場合
			     model.addAttribute("duplicateWarning", true);
			 }   			
		}
		
		//④進捗度が未入力なら0として扱う
		if (form.getProgress() == null) {
			form.setProgress(0);
		}
		 
		 
		//データを格納
	    model.addAttribute("requestForm", form);

	    return "registerCheck";
	}
	
	/*確認画面→DBへ依頼内容登録*/
	@PostMapping("/register/save")
	public String save(RequestForm form, Model model,
			RedirectAttributes redirectAttributes) {
		 
		
		if(form.getId() == null) {
		requestMapper.insert(form);//DBへ内容登録
		}else {
		requestService.update(form);//DBを修正
		}
	    //ホーム画面にメッセージを渡す
		if(form.getId() != null) {
		redirectAttributes.addFlashAttribute("updateMessage","★内容を修正しました");
		}else {
	    redirectAttributes.addFlashAttribute("message","★依頼を登録しました");
		}
		
	return "redirect:/home";
//	return "redirect:/register/complete";
	}
	
	
	

	

	
//②
/**
 * 【依頼一覧表示】
 */
	@GetMapping("/requestList")
	public String showRequestList(
			@RequestParam(required = false) String period,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String clientName,
	        @RequestParam(required = false) String kind,
	        @RequestParam(required = false) Integer minWorkTime,
			Model model) {
		
	  List<RequestListDto> requestList = requestService.selectAllRequests(
			  				period, status, clientName, kind, minWorkTime);
	  
	 
	  
	  model.addAttribute("requestList", requestList);
	  // 検索フォームの選択肢を取得
	    model.addAttribute("clientNames", requestService.selectCliantName());
	    model.addAttribute("kinds", requestService.selectKind());
	  
	  // 検索後も選択内容を画面に残す
	    model.addAttribute("selectedClientName", clientName);
	    model.addAttribute("selectedKind", kind);
	    model.addAttribute("selectedStatus", status);
	    model.addAttribute("minWorkTime", minWorkTime);
	    model.addAttribute("period", period);
	    
	   //依頼者リスト
	    List<String> clientNames = requestService.getClientNames();
	    model.addAttribute("clientNames", clientNames);
	    
		return "requestList";
	}

	
//③
/**
 * 【依頼内容修正】
 */
	@GetMapping("/requestUpdate")
	public String showRequestUpdate(
	        @RequestParam Integer id,
	        Model model) {

	    RequestForm requestForm = requestService.selectById(id);
	    
	    //分→時間・分に修正。時間が設定されている場合のみ。
	    if (requestForm.getTotalWorkTime() != null) {
	    Integer hourTime = requestForm.getTotalWorkTime() / 60;
		Integer minuteTime = requestForm.getTotalWorkTime() % 60;
		//☆計算結果を格納
		requestForm.setHourTime(hourTime);
        requestForm.setMinuteTime(minuteTime);
	    }
		
	    //予測を格納
	    List<String> kind = requestService.selectKind();
	    model.addAttribute("kind", kind);
	    List<String> kindDetail = requestService.selectKindDetail();
	    model.addAttribute("kindDetail", kindDetail);
	    List<String> chara = requestService.selectChara();
	    model.addAttribute("chara", chara);
	    List<String> clientName = requestService.selectCliantName();
	    model.addAttribute("clientName", clientName);
	    
	  //IDに対応した列情報を格納。 
	    model.addAttribute("requestForm", requestForm);

	    return "requestRegister";
	}	
	
/*依頼内容削除*/	
	@PostMapping("/register/delete")
	public String requestDelete(
	        RequestForm form,
	        @RequestParam(defaultValue = "false") boolean deleteConfirm,
	        Model model,
	        RedirectAttributes redirectAttributes) {

	    if (!deleteConfirm) {
	        // 1回目
	        model.addAttribute("deleteConfirm", true);
	        model.addAttribute("message", "本当にこの依頼を削除しますか？もう一度押すと削除されます。");

	        return "requestRegister";
	    }

	    // 2回目
	    requestMapper.deleteById(form.getId());
	    
	    redirectAttributes.addFlashAttribute("deleteMessage", "★依頼を削除しました");

	    return "redirect:/home";
	}

	

	
}
