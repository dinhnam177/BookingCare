package com.KMA.BookingCare.Controller.admin;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.KMA.BookingCare.Dto.*;
import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.Service.*;
import com.KMA.BookingCare.ServiceImpl.UserDetailsImpl;
import com.KMA.BookingCare.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private HospitalService hospitalServiceImpl;

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private SpecializedService specializedServiceImpl;
    @Autowired
    private WorkTimeService workTimeServiceImpl;

    @Autowired
    private HandbookService handbookService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    @GetMapping(value = {"/admin/home", "/doctor/home"})
    public String homePage(HttpSession session, Model model) {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        MyUser userDetails = UserMapper.convertToMyUser(user);
        session.setAttribute("userDetails", userDetails);
        List<HandbookDto> handbooks = handbookService.getFeaturedHandbook();
        List<User> users = userServiceImpl.getFeaturedDoctor();
        model.addAttribute("featureHandbooks", handbooks);
        model.addAttribute("featureUsers", users);
        return "admin/views/home";
    }


}
