package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.RequestListDto;
import com.example.demo.dto.RequestSummaryDto;
import com.example.demo.form.RequestForm;
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
	public String showHome(Model model,RequestForm form) {
		
		 //表示したいDtoを宣言
		 RequestSummaryDto summary = requestService.selectSummary();
		 List<RequestListDto> requestList =
		            requestService.selectAllRequests(null, null);
		 
		 
		 //表示したいDtoをmodelに格納
		 model.addAttribute("summary", summary);
		 model.addAttribute("requestList", requestList);
		 
		return "home";
	}
}
