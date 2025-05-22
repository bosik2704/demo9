package com.example.demo9.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudyService {


  public int setFileUpload(MultipartFile sFile) {
    int res = 0;

    String oFileName = sFile.getOriginalFilename();
    String sFileName = oFileName.substring(0,oFileName.lastIndexOf(".")) + "_" + UUID.randomUUID().toString().substring(0,4) + oFileName.substring(oFileName.lastIndexOf("."));
    log.info("==================>> 원본파이명 : " + oFileName);
    log.info("==================>> 저장파이명 : " + sFileName);

    try {
      writeFile(sFile, sFileName);
      res = 1;
    } catch (Exception e) { e.printStackTrace(); }

    return res;
  }

  private void writeFile(MultipartFile file, String sFileName) throws IOException {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String realPath = request.getSession().getServletContext().getRealPath("/upload/");

    FileOutputStream fos = new FileOutputStream(realPath + sFileName);

    if(file.getBytes().length != -1) {
      fos.write(file.getBytes());
    }
    fos.flush();
    fos.close();
  }

  public int setMultiFileUpload(MultipartHttpServletRequest workFile) {
    int res = 0;

    try {
      List<MultipartFile> fileList = workFile.getFiles("mFile");
      String oFileNames = "";
      String sFileNames = "";
      int fileSizes = 0;

      for(MultipartFile file : fileList) {
        String oFileName = file.getOriginalFilename();
        String sFileName = oFileName.substring(0,oFileName.lastIndexOf(".")) + "_" + UUID.randomUUID().toString().substring(0,4) + oFileName.substring(oFileName.lastIndexOf("."));

        writeFile(file, sFileName);

        oFileNames += oFileName + "/";
        sFileNames += sFileName + "/";
        fileSizes += file.getSize();
      }
      oFileNames = oFileNames.substring(0,oFileNames.length()-1);
      sFileNames = sFileNames.substring(0,sFileNames.length()-1);

      System.out.println("================>> 원본파일 : " + oFileNames);
      System.out.println("================>> 저장파일 : " + sFileNames);

      res = 1;
    } catch (Exception e) { e.printStackTrace(); }

    return res;
  }
}
