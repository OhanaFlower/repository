package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.MonthlySummaryDto;
import com.example.demo.dto.RequestListDto;
import com.example.demo.dto.RequestSummaryDto;
import com.example.demo.service.RequestService;

import lombok.RequiredArgsConstructor;

@Controller //ブラウザのリクエストを受け付ける(Spring MVC)
@RequiredArgsConstructor //自動でコンストラクタを作る(Lombok)
public class HomeController {
	//DIコンテナ
	private final RequestService requestService;

	
/*【一覧】
 * ①ホーム画面の表示*/
	
	
//①
	/**ホーム画面の表示*/
	@GetMapping("/home")
	public String showHome(
			//requiredは「絶対必要かどうか」。falseなら無くてもエラー吐かない。trueなら必須。
	        @RequestParam(required = false, defaultValue = "6months") String period,
	        Model model) {
		
		 //表示したいDtoを宣言
		 RequestSummaryDto summary = requestService.selectSummary();
		 List<RequestListDto> requestList =
		            requestService.selectAllRequestsByDeadline();
		 //宣言２：月の依頼数・金額の総計
		 List<MonthlySummaryDto> monthlySummary =
			        requestService.getMonthlySummary(period);
		 
		 //表示したいDtoをmodelに格納
		 model.addAttribute("summary", summary);
		 model.addAttribute("requestList", requestList);
		 //（宣言２の分）
		 model.addAttribute("monthlySummary", monthlySummary);
		 
		return "home";
	}
}
