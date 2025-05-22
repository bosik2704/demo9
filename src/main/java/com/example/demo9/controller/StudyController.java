package com.example.demo9.controller;

import com.example.demo9.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyController {

  private final StudyService studyService;

  @GetMapping("/file/fileUpload")
  public String fileUploadGet() {
    return "study/file/fileUpload";
  }

  @PostMapping("/file/fileUpload")
  public String fileUploadPost(MultipartFile sFile, Model model) {
    String oFileName = sFile.getOriginalFilename();
    System.out.println("===============>> oFileName : " + oFileName);
    if(oFileName.equals("")) {
      return "redirect:/message/fileUploadNo";
    }

    // 업로드된 파일을 서비스객체에서 처리
    int res = studyService.setFileUpload(sFile);

    if(res != 0) return "redirect:/message/fileUploadOk";
    else return "redirect:/message/fileUploadNo";
  }

  @PostMapping("/file/multiUpload")
  public String multiUploadPost(MultipartHttpServletRequest workFile, Model model) {
    int res = studyService.setMultiFileUpload(workFile);

    if(res != 0) return "redirect:/message/fileUploadOk";
    else return "redirect:/message/fileUploadNo";
  }

}
