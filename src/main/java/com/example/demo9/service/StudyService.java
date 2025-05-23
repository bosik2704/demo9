package com.example.demo9.service;

import com.example.demo9.dto.User3Dto;
import com.example.demo9.entity.User3;
import com.example.demo9.repository.StudyRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudyService {

  private final StudyRepository studyRepository;

  public int setFileUpload(MultipartFile sFile) {
    int res = 0;

    String oFileName = sFile.getOriginalFilename();
    String sFileName = oFileName.substring(0,oFileName.lastIndexOf(".")) + "_" + UUID.randomUUID().toString().substring(0,4) + oFileName.substring(oFileName.lastIndexOf("."));
    log.info("==================>> 원본파이명 : " + oFileName);
    log.info("==================>> 저장파이명 : " + sFileName);

    try {
      writeFile(sFile, sFileName, "upload");
      res = 1;
    } catch (Exception e) { e.printStackTrace(); }

    return res;
  }

  private void writeFile(MultipartFile file, String sFileName, String urlPath) throws IOException {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String realPath = request.getSession().getServletContext().getRealPath("/"+urlPath+"/");

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

        writeFile(file, sFileName, "upload");

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

  public String fileCsvToMysql(MultipartFile fName) {
    String res = "0";

    String oFileName = fName.getOriginalFilename();
    String sFileName = oFileName.substring(0,oFileName.lastIndexOf(".")) + "_" + UUID.randomUUID().toString().substring(0,4) + oFileName.substring(oFileName.lastIndexOf("."));
    log.info("==================>> 원본파이명 : " + oFileName);
    log.info("==================>> 저장파이명 : " + sFileName);

    try {
      writeFile(fName, sFileName, "excel");
      res = "1";
    } catch (Exception e) { e.printStackTrace(); }

    // 업로드된 파일을 Line단위로 읽어와서 ','를 기준으로 분리한후 DB에 저장한다.
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String realPath = request.getSession().getServletContext().getRealPath("/excel/"+sFileName);

    try {
      BufferedReader br = new BufferedReader(new FileReader(realPath));
      String line, str = "";
      int cnt = 0;
      br.readLine();
      while ((line = br.readLine()) != null) {
        cnt++;
        //str += cnt + " : " + line + "\n";
        str += cnt + " : " + line + "<br>";

        int k = 1;
        String[] users = line.split(",");
        User3 user3 = new User3();
        //user3.setId(Long.parseLong(users[k])); k++;
        user3.setName(users[k]); k++;
        user3.setAge(Integer.parseInt(users[k])); k++;
        user3.setGender(users[k]); k++;
        user3.setAddress(users[k]); k++;

        // DB에 저장처리
        studyRepository.save(user3);
      }
      System.out.println("=======>>str : " + str);
    } catch (Exception e) { e.printStackTrace(); }

    return res;
  }

  public String fileExcelUpload(MultipartFile file, Model model) {
    log.info("파일명 : {}",  file.getOriginalFilename());
    log.info("파일크기 : {}",  file.getSize());

    try {
      if (file.isEmpty()) {
        model.addAttribute("message", "파일을 선택해 주세요");
        return "study/exce/excelTransfer";
      }


      // 확장자 체크
      String fileName = file.getOriginalFilename();
      if(!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
        model.addAttribute("message", "Excel 파일을 선택해 주세요");
        return "study/excel/excelTransfer";
      }

      List<User3Dto> user3DtoList = new ArrayList<>();

      // Excel파일 처리
      Workbook workbook = WorkbookFactory.create(file.getInputStream());
      Sheet sheet = workbook.getSheetAt(0); // 첫번째 시트 읽기

      // 헤더 제목(행) 읽기
      Row headerRow = sheet.getRow(0);
      for (Cell cell : headerRow) {
        log.info("제목 {}\\t", cell.getStringCellValue());
      }

      // 데이터 행 읽기
      for (int i=1; i<=10; i++) {
        Row row = sheet.getRow(i);

        user3DtoList.add(User3Dto.builder()
                .id((long) row.getCell(0).getNumericCellValue())
                .name(row.getCell(1).getStringCellValue())
                .age((int) row.getCell(2).getNumericCellValue())
                .gender(row.getCell(3).getStringCellValue())
                .address(row.getCell(4).getStringCellValue())
                .build());
      }
      log.info("리스트 : {}", user3DtoList.toString());

      workbook.close();
      model.addAttribute("message", "파일이 성공적으로 업로드 되었습니다.");
    } catch (Exception e) {
      model.addAttribute("message", "파일 업로드 실패~~~");
    }

    return "study/excel/excelTransfer";
  }

  public Resource getExcelDownload(Model model) {
    /*
      HSSF (Horrible SpreadSheet Format)	Excel 97-2003 (.xls) 파일 형식을 처리하기 위한 컴포넌트
      XSSF (XML SpreadSheet Format)	Excel 2007 이상 버전의 (.xlsx) 파일 형식을 처리하기 위한 컴포넌트
      SXSSF (Streaming XML SpreadSheet Format)	대용량 Excel 파일을 메모리 효율적으로 처리하기 위한 스트리밍 방식의 컴포넌트
      CSV (Comma-Separated Values)	단순한 텍스트 기반의 데이터 형식으로, 쉼표(,)로 구분된 값들을 저장하는 형식. Apache POI는 OpenCSV 라이브러리와 함께 사용하여 CSV 파일도 처리 가능
     */
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("사용자정보");

    // 헤더 행 생성
    Row headerRow = sheet.createRow(0);
    String[] headers = {"순번", "이름", "나이", "성별", "연락처"};
    for(int i=0; i<headers.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(headers[i]);
    }

    // 데이터 행 생성
    Object[][] data = {
            {1, "홍길동", 22, "남", "010-1234-1234"},
            {2, "김말숙", 32, "여", "010-1224-1277"},
            {3, "이기자", 24, "남", "010-3334-1266"},
            {4, "소나무", 42, "남", "010-4434-1243"},
            {5, "김연아", 52, "여", "010-1255-2134"},
    };

    // 생성된 Sheet 내에 헤더(제목행)와 데이터행을 구성(생성)처리
    for(int i=0; i<data.length; i++) {
      Row row = sheet.createRow(i+1);
      for(int j=0; j<data[i].length; j++) {
        Cell cell = row.createCell(j);  // 열 자료 생성

        if(data[i][j] instanceof String) cell.setCellValue((String) data[i][j]);

        if(data[i][j] instanceof Integer) cell.setCellValue((Integer) data[i][j]);
      }
    }

    // 열 너비 자동 조정
    for(int i=0; i<headers.length; i++) {
      sheet.autoSizeColumn(i);
    }

    // 시트 예쁘게 편집

    // 파일 생성
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      workbook.write(outputStream);
      workbook.close();
    } catch (IOException e) { throw new RuntimeException(e); }
    ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray()); // 엑셀형식의 파일을 생성처리
    model.addAttribute("downloadMessage", "엑셀파일로 다운로드 되었습니다.");
    return resource;
  }
}
