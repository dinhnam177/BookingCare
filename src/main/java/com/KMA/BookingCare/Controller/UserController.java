package com.KMA.BookingCare.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.KMA.BookingCare.Dto.User;
import com.KMA.BookingCare.Service.UserService;


@Controller
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping(value = "/login")
	public String login() {

		return "login";
	}

	@GetMapping(value = "/register")
	public String formRegister() {

		return "register";
	}

	@PostMapping(value = "/register")
	public String register( @ModelAttribute @Valid User user, BindingResult bindingResult) {
		System.out.println(bindingResult.getFieldErrors());
		if(bindingResult.hasErrors()) {
			return"redirect:register";
		}
		userService.add(user, "admin");

		return "redirect:/login";
	}
	@PostMapping(value = "/changePass")
	public String change( @ModelAttribute @Valid User user, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return"redirect:changePass";
		}
		userService.update(user);

		return "redirect:/login";
	}
	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response,HttpSession session) {
		session.removeAttribute("userDetails");
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		
		if (auth!= null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
			
		}
		return "redirect:/login";
	}

	@GetMapping(value = "/welcome")
	public String weString() {
		return "welcome";
	}

	@GetMapping(value = "/admin/hehe")
	public String hehe() {
		return "hehe";
	}

	@GetMapping(value = "/access-deny")
	public String deny() {
		return "access-deny";
	}
	@GetMapping(value = "/changePass")
	public String changePass() {
		return "changePass";
	}
}
