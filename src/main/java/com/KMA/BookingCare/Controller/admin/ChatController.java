package com.KMA.BookingCare.Controller.admin;import java.util.List;

import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.ServiceImpl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.KMA.BookingCare.Dto.InteractiveDto;
import com.KMA.BookingCare.Dto.MessageDto;
import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Dto.User;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Service.InteractiveService;
import com.KMA.BookingCare.Service.MessageService;
import com.KMA.BookingCare.Service.UserService;

import javax.servlet.http.HttpSession;


@Controller
public class ChatController {
	
	@Autowired
	private InteractiveService interactiveServiceImpl;
	
	@Autowired
	private MessageService messageServiceImpl;
	
	@Autowired
	private UserService userServiceImpl;
	
	@GetMapping(value = {"/admin/managerChat","/doctor/managerChat"})
	public  String chatPage(Model model, HttpSession httpSession) {
		MyUser userDetails= (MyUser) httpSession.getAttribute("userDetails");
		List<InteractiveDto> lstInteractive= interactiveServiceImpl.findAll(userDetails);
		model.addAttribute("lstInteractive", lstInteractive);
		return "admin/views/managerChat2";
	}

	@RequestMapping(path = "/api/selectUser", method = RequestMethod.POST)
	public ResponseEntity<?> searchMessageUser(Model model, @RequestBody Long id) {
		List<MessageDto> lstMessageDto= messageServiceImpl.findAllMessageBySelectUser(id);
		User userDto= userServiceImpl.findOneById(id);
		Object[] mang = new Object[2];
		mang[0]=userDto;
		mang[1]=lstMessageDto;
		System.out.println("oke");
		return new ResponseEntity<Object>(mang, HttpStatus.OK);
	}
	  
	  @RequestMapping(path = "/mesageUser", method = RequestMethod.POST)
	    public ResponseEntity<?> mesageUser(Model model, @RequestBody Long id) {
		  UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				  .getPrincipal();
		  MyUser userDetails = UserMapper.convertToMyUser(user);
			List<MessageDto> lstMessageDto= messageServiceImpl.findAllMessageBySelectUser(id);
	    	Object[] mang = new Object[2];
	    	mang[0]=userDetails;
	    	mang[1]=lstMessageDto;
	    	System.out.println("oke");
	        return new ResponseEntity<Object>(mang, HttpStatus.OK);
	    }
	  @RequestMapping(path = "/showLstUser", method = RequestMethod.POST)
	    public ResponseEntity<?> showLstUser(Model model, @RequestBody Long id) {
	    	User dto = userServiceImpl.findOneById(id);
	        return new ResponseEntity<User>(dto, HttpStatus.OK);
	    }
}
