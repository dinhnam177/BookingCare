package com.KMA.BookingCare.Controller.admin;

import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.ServiceImpl.UserDetailsImpl;
import com.KMA.BookingCare.config.SessionUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class TestVideoCall {

    @Autowired
    SessionUserDetail sessionUserDetail;

    @GetMapping("/video")
    public String homePage(Model model) {
        model.addAttribute("id", sessionUserDetail.getId());
        return "video";
    }
}
