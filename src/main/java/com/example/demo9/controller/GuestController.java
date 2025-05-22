package com.example.demo9.controller;

import com.example.demo9.dto.GuestDto;
import com.example.demo9.entity.Guest;
import com.example.demo9.service.GuestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/guest")
public class GuestController {

  private final GuestService guestService;

  @GetMapping("/guestList")
  public String guestListGet(Model model,
                             @RequestParam(name = "pag", defaultValue = "0", required = false) int pag,
                             @RequestParam(name = "pageSize", defaultValue = "3", required = false) int pageSize) {
    //List<Guest> guestList = guestService.getGuestList(pag, pageSize);
    Page<Guest> dtos = guestService.getGuestList(pag, pageSize);
    int totPage = dtos.getTotalPages();
    int curScrStartNo = (int) dtos.getTotalElements() - (pag * pageSize);
    int blockSize = 3;
    int curBlock = ((pag + 1) - 1) / blockSize;
    int lastBlock = (totPage - 1) / blockSize;

    model.addAttribute("guestList", dtos);
    model.addAttribute("pag", pag+1);
    model.addAttribute("totPage", totPage);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("curScrStartNo", curScrStartNo);
    model.addAttribute("blockSize", blockSize);
    model.addAttribute("curBlock", curBlock);
    model.addAttribute("lastBlock", lastBlock);

    // 개행처리를 위한 코드
    // String newLine = System.getProperty("line.separator").toString();
    String newLine = System.lineSeparator();
    model.addAttribute("newLine", newLine);

    return "guest/guestList";
  }

  @GetMapping("/guestInput")
  public String guestInputGet() {
    return "guest/guestInput";
  }

  @PostMapping("/guestInput")
  public String guestInputPost(HttpServletRequest request, GuestDto dto) {
    dto.setHostIp(request.getRemoteAddr());
    Guest guest = Guest.createGuest(dto);
    guestService.setGuestInput(guest);

    return "redirect:/guest/guestList";
  }
}
