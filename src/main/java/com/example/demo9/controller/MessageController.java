package com.example.demo9.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {

	@RequestMapping(value = "/message/{msgFlag}", method = RequestMethod.GET)
	public String getMessage(Model model, @PathVariable String msgFlag,
			HttpSession session,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="idx", defaultValue = "0", required = false) int idx,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(name="search", defaultValue = "", required = false) String search,
			@RequestParam(name="searchString", defaultValue = "", required = false) String searchString,
			@RequestParam(name="part", defaultValue = "전체", required = false) String part,
			@RequestParam(name="mSw", defaultValue = "1", required = false) String mSw,
			@RequestParam(name="tempFlag", defaultValue = "", required = false) String tempFlag
		) {
		
		if(msgFlag.equals("fileUploadOk")) {
			model.addAttribute("message", "파일 업로드 성공");
			model.addAttribute("url", "/study/file/fileUpload");
		}
		else if(msgFlag.equals("fileUploadNo")) {
			model.addAttribute("message", "파일 업로드 실패~~");
			model.addAttribute("url", "/study/file/fileUpload");
		}

		return "/include/message";
	}
	
}
