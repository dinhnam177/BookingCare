package com.KMA.BookingCare.Controller.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.KMA.BookingCare.Api.form.notificationForm;
import com.KMA.BookingCare.Dto.CommentDto;
import com.KMA.BookingCare.Dto.MessageDto;
import com.KMA.BookingCare.Repository.CommentRepository;
import com.KMA.BookingCare.Service.CommentService;
import com.KMA.BookingCare.Service.InteractiveService;
import com.KMA.BookingCare.Service.MessageService;
import com.KMA.BookingCare.Service.UserService;

@Controller
public class WebSocketController {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private MessageService messageServiceImpl;

	@Autowired
	private InteractiveService interactiveserviceImpl;;

	@Autowired
	private UserService userServiceImpl;
	
	@Autowired
	private CommentService commentServiceImpl;


	@MessageMapping("/sendToUSer")
	public void sendMessage(@Payload MessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		interactiveserviceImpl.saveOrUpdate(chatMessage);
		chatMessage = messageServiceImpl.save(chatMessage);
		simpMessagingTemplate.convertAndSend("/topic/" + chatMessage.getReceiverId(), chatMessage);
	}

//    UserSendToServer
	@MessageMapping("/server")
	public void userSendMessage(@Payload MessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		chatMessage = messageServiceImpl.save(chatMessage);
		interactiveserviceImpl.saveOrUpdate(chatMessage);
		System.out.println("tét");
		if(chatMessage.getReceiverId()==0) {
			simpMessagingTemplate.convertAndSend("/topic/server", chatMessage);
		}else {
			simpMessagingTemplate.convertAndSend("/topic/" + chatMessage.getReceiverId(), chatMessage);
		}
	}

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/publicChatRoom")
	public MessageDto sendMessage(@Payload MessageDto chatMessage) {
		return chatMessage;
	}

	@PostMapping(path = "/call")
	public ResponseEntity<?> callUser(Model model, @RequestBody Long id) {
		String peerId = userServiceImpl.findPeerIdById(id);
		return new ResponseEntity<String>(peerId, HttpStatus.OK);
	}

	@MessageMapping("/close")
	public void close(@Payload MessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		if(chatMessage.getReceiverId()==0) {
			simpMessagingTemplate.convertAndSend("/topic/server", chatMessage);
		}else {
			simpMessagingTemplate.convertAndSend("/topic/" + chatMessage.getReceiverId(), chatMessage);
		}
	}
	
	@MessageMapping("/notification")
	public void notification(@Payload notificationForm notificationForm, SimpMessageHeaderAccessor headerAccessor) {
		StringBuilder content= new StringBuilder("Lịch khám online vào ngày: ");
		content.append(notificationForm.getDate());
		content.append(" , khoảng thời gian: ");
		content.append(notificationForm.getWkTime());
		
		MessageDto messageDoctor= new MessageDto();
		messageDoctor.setContent(content.toString());
		messageDoctor.setSenderId(notificationForm.getUserId());
		messageDoctor.setReceiverId(notificationForm.getDoctorId());
		
		MessageDto messageUser= new MessageDto();
		messageUser.setContent(content.toString());
		messageUser.setSenderId(notificationForm.getDoctorId());
		messageUser.setReceiverId(notificationForm.getUserId());
		
		interactiveserviceImpl.saveOrUpdate(messageDoctor);
		messageDoctor = messageServiceImpl.save(messageDoctor);

		simpMessagingTemplate.convertAndSend("/topic/" + messageDoctor.getReceiverId(), messageDoctor);
		simpMessagingTemplate.convertAndSend("/topic/" + messageUser.getReceiverId(), messageUser);
	}
	
//	comment sendComment
	
	@MessageMapping("/sendComment")
	  public void comment(@Payload CommentDto commentDto, SimpMessageHeaderAccessor headerAccessor) {
		commentDto = commentServiceImpl.save(commentDto);
	    	simpMessagingTemplate.convertAndSend("/topic/comment" , commentDto);
	}

	@MessageMapping("/sendToHieu")
	public void sendMessageToHieu(@Payload MessageDto chatMessage) {
		interactiveserviceImpl.saveOrUpdate(chatMessage);
		chatMessage = messageServiceImpl.save(chatMessage);
		simpMessagingTemplate.convertAndSend("/topic/hieu" , chatMessage);
	}


}
