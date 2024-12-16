package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Dto.MessageDto;
import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.Service.MessageService;
import com.KMA.BookingCare.ServiceImpl.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Service.UserService;

import java.util.List;

@Controller
public class MessageApi {

    private final Logger log = LoggerFactory.getLogger(MessageApi.class);

    @Autowired
    UserService userServiceImpl;

    @Autowired
    private MessageService messageServiceImpl;

    @PostMapping("/savePeerId")
    public ResponseEntity<?> addUser(@RequestBody String peerId) {
        log.info("Request to save peerId", peerId);
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        MyUser userDetails = UserMapper.convertToMyUser(user);
        userServiceImpl.updatePeerId(peerId, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/api/message/user/{id}")
    public List<MessageDto> findAllByUserId(@PathVariable long id) {
        log.info("Request to get message by userId", id);
        List<MessageDto> lstMessageDto = messageServiceImpl.findAllMessageBySelectUser(id);
        return lstMessageDto;
    }
}
