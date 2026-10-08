package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.WorkLogDto;
import com.example.demo.dto.WorkLogListDto;
import com.example.demo.form.RequestForm;
import com.example.demo.form.WorkLogForm;
import com.example.demo.mapper.RequestMapper;
import com.example.demo.mapper.WorkLogMapper;
import com.example.demo.service.RequestService;
import com.example.demo.service.WorkLogService;

import lombok.RequiredArgsConstructor;

@Controller //ブラウザのリクエストを受け付ける(Spring MVC)
@RequiredArgsConstructor //自動でコンストラクタを作る(Lombok)
public class WorkLogController {

	//DIコンテナ
	private final RequestMapper RequestMapper;
	private final WorkLogMapper WorkLogMapper;
	private final RequestService requestService;
	private final WorkLogService WorkLogService;//③用
	
	
/*【一覧】
 *①作業登録
 *②個別の作業一覧を表示
 *③全ての作業一覧を表示
　④作業内容修正画面を表示
　⑤修正内容を保存
  ⑥作業ログの削除（1件）
*/
	
	
	
//①	
	/**
	 * 【作業登録】
	 **/
		@GetMapping("/workLogRegister")
		public String showWorkLogRegister(@RequestParam Integer id, Model model) {
			
			WorkLogForm form = new WorkLogForm();
			form.setRequestId(id);
			
			//現在の進捗率を取得
			RequestForm requestForm = requestService.selectById(id);
			form.setCurrentProgress(requestForm.getProgress());//currentProgressにセッターを通じて、
															   //今のフォームの進捗度をセットする
			
			//formが持ってるrequest_idに、
	        //遷移元から持ってきたidをセット
			model.addAttribute("workLogForm", form);
			
			return "workLogRegister";
		}
		
		//内容をDBに保存
		@PostMapping("/workLog/save")
		public String save(WorkLogForm form, Model model,RedirectAttributes redirectAttributes) {
			
			//IDか作業日がない場合差し戻し
			if (form.getRequestId() == null || form.getWorkDate() == null) {
			    model.addAttribute("workLogForm", form);//内容の保持
			    model.addAttribute("message", "「ID」か「作業日」が入力されていません");
			    return "workLogRegister";
			    }
			
			//進捗増加量が未入力なら0として扱う
			if (form.getProgressGain() == null) {
			    form.setProgressGain(0);
			}
			
			
			//作業ログをDBへ登録
			WorkLogMapper.insert(form);//DBへ内容登録
			
			//進捗度を加算
			RequestMapper.addProgress(
				    form.getRequestId(),
				    form.getProgressGain()
				);
			
			//「作業完了」にチェックが入っていた場合
//			※if(form.getCompleted() == 1) { だとnullでエラーなので...
			if (Integer.valueOf(1).equals(form.getCompleted())) {
				RequestMapper.updateStatus(form.getRequestId(),("完了"));
			}
			//ホーム画面にメッセージを渡す
			redirectAttributes.addFlashAttribute("workMessage","★作業を登録しました");
			
		return "redirect:/home";//ホームに戻す処理
		}
	
//②	
	/**
	* 個別の作業一覧を表示
	**/
		@GetMapping("/workLogEachList")
		public String showWorkLogList(
		        @RequestParam Integer id,
		        Model model) {

		    List<WorkLogDto> workLogList =
		            WorkLogMapper.selectByRequestId(id);

		    model.addAttribute("workLogList", workLogList);
		    model.addAttribute("requestId", id);
		    
		    
		    return "workLogEachList";
		}

//③
	/**
	 * 全ての作業一覧を表示
	 **/
	@GetMapping("/workLogAllList")
	public String showWorkLogAllList(
			 @RequestParam(required = false) String period,
			 @RequestParam(required = false) LocalDate date,
			 Model model) {

		  List<WorkLogListDto> workLogAllList =
		          WorkLogService.selectAllWorkLogs(period,date);

		  model.addAttribute("workLogAllList", workLogAllList);

		  return "workLogAllList";
		}

//④
	/**
	 *作業内容修正画面を表示
	 **/
	@GetMapping("/workLogEdit")
	public String showWorkLogEdit(
	        @RequestParam Integer id,
	        Model model) {

	    WorkLogForm workLogForm =
	            WorkLogService.selectById(id);

	    model.addAttribute("workLogForm", workLogForm);

	    return "workLogEdit";
	}

//⑤
	/**
	 *修正内容を保存 
	 **/
	@PostMapping("/workLogEdit/save")
	public String updateWorkLog(
	        WorkLogForm form,
	        RedirectAttributes redirectAttributes) {

	    WorkLogService.updateWorkLog(form);
	    
	    redirectAttributes.addFlashAttribute(
	            "workEditMessage",
	            "★作業内容を修正しました");
	    
	    //idを返すことで、EachListの表示を可能にする
	    return "redirect:/workLogEachList?id=" + form.getRequestId();
	}
	
//⑥
	/**
	 *作業ログの削除（一件） 
	 **/
@PostMapping("/workLogEdit/delete")
public String workLogDelete(
        WorkLogForm form,
        @RequestParam(defaultValue = "false") boolean deleteConfirm,
        Model model,
        RedirectAttributes redirectAttributes) {

    if (!deleteConfirm) {
        // 1回目：確認メッセージを表示
        model.addAttribute("deleteConfirm", true);
        model.addAttribute(
                "message",
                "本当にこの作業ログを削除しますか？もう一度押すと削除されます。");

        // 修正画面に必要な情報を再設定
        WorkLogForm workLogForm =
                WorkLogService.selectById(form.getId());

        model.addAttribute("workLogForm", workLogForm);

        return "workLogEdit";
    }

    // 2回目：削除を実行
    WorkLogService.deleteWorkLog(form.getId());

    redirectAttributes.addFlashAttribute(
            "workDeleteMessage",
            "★作業ログを削除しました");

    return "redirect:/workLogEachList?id=" + form.getRequestId();
}

}
