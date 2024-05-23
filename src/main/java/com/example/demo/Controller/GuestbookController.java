package com.example.demo.Controller;

import com.example.demo.dto.GuestbookDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.entity.Guestbook;
import com.example.demo.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService service;

    @GetMapping("/")
    public String index(){
        return "redirect:/guestbook/list";
    }

    @GetMapping("list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("list............." + pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register(){
        log.info("register..............");
    }

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){

        log.info("dto.............." + dto);

        long gno = service.register(dto);

        redirectAttributes.addFlashAttribute("msg", gno);

        return  "redirect:/guestbook/list";
    }

    @GetMapping("/read")
    // @ModelAttribute("requestDTO") PageRequestDTO requestDTO
    //바인딩 : 요청받은 파라미터를 자동으로 자바 객체의 필드에 매핑
    //PageRequestDTO requestDTO를 "requestDTO" 모델에 저장
    public void read(long gno,@ModelAttribute("requestDTO") PageRequestDTO requestDTO,Model model){

        System.out.println("GetMapping Read ===============" + gno);
        System.out.println("GetMapping requestDTO ===============" + requestDTO);
        log.info(("gno : "+gno));

        GuestbookDTO dto = service.read(gno);

        model.addAttribute("dto", dto);
    }
}
