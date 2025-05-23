package com.example.demo9.controller;

import com.example.demo9.service.StudyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;

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

  @GetMapping("/file/fileUploadList")
  public String fileUploadListGet(HttpServletRequest request, Model model) {
    String realPath = request.getServletContext().getRealPath("/upload/");
    String[] files = new File(realPath).list();
    model.addAttribute("files", files);
    model.addAttribute("fileCnt", files.length);
    return "study/file/fileUploadList";
  }

  // 개별파일 삭제
  @ResponseBody
  @PostMapping("/file/fileDeleteCheck")
  public String fileDeleteCheckPost(HttpServletRequest request, String file) {
    String realPath = request.getServletContext().getRealPath("/upload/");
    new File(realPath + file).delete();
    return "1";
  }

  // 멀티파일 삭제
  @ResponseBody
  @PostMapping("/file/fileSelectDelete")
  public String fileSelectDeletePost(HttpServletRequest request, String delItems) {
    System.out.println("delItems : " + delItems);
    delItems = delItems.substring(0, delItems.length()-1);

    String[] fileNames = delItems.split("/");

    String realPath = request.getServletContext().getRealPath("/upload/");
    for(String fileName : fileNames) {
      System.out.println("file : " + fileName);
      new File(realPath + fileName).delete();
    }
    return "1";
  }

  @GetMapping("/excel/excelTransfer")
  public String excelTransferGet() {
    return "study/excel/excelTransfer";
  }

  @ResponseBody
  @PostMapping("/excel/excelTransfer")
  public String excelTransferPost(MultipartFile fName) {
    String oFileName = fName.getOriginalFilename();
    //System.out.println("==============>> oFileName : " + oFileName);
    return studyService.fileCsvToMysql(fName);
  }

  @PostMapping("/excel/excelUpload")
  public String excelUploadPost(MultipartFile file, Model model) {
    String oFileName = file.getOriginalFilename();
    System.out.println("==============>> oFileName : " + oFileName);
    String res = studyService.fileExcelUpload(file, model);
    return res;
  }

  @PostMapping("/excel/excelDownload")
  public ResponseEntity<Resource> excelDownloadPost(Model model) {
    Resource res = studyService.getExcelDownload(model);
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=userList.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(res);
  }


}
