package com.KMA.BookingCare.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.KMA.BookingCare.Mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.KMA.BookingCare.Dto.MessageDto;
import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Entity.MessageEntity;
import com.KMA.BookingCare.Mapper.MessageMapper;
import com.KMA.BookingCare.Repository.MessageRepository;
import com.KMA.BookingCare.Service.MessageService;
@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Override
	public MessageDto save(MessageDto dto) {
		// TODO Auto-generated method stub
		MessageEntity entity = new MessageEntity();
		entity.setContent(dto.getContent());
		entity.setCreatedDate(new Date());
		entity.setSenderId(dto.getSenderId());
		entity.setReceiverId(dto.getReceiverId());
		entity =messageRepository.save(entity);
		SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
		MessageDto resultf = MessageMapper.convertToDto(entity,sp);
		return resultf;
		
	}

	@Override
	public List<MessageDto> findBySenderOrReceiver(Long sender, Long receiver) {
		List<MessageEntity> lstEntity=messageRepository.findBySenderIdOrReceiverIdOrderByCreatedDateAsc( sender,  receiver);
		List<MessageDto> lstDto = new ArrayList<MessageDto>();
		SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
		for(MessageEntity entity: lstEntity) {
			MessageDto dto = MessageMapper.convertToDto(entity,sp);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public List<MessageDto> findAllMessageBySelectUser(Long idSelect) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		MyUser userDetails = UserMapper.convertToMyUser(user);
		Long idServer;
		if(userDetails.getRoles().contains("ROLE_ADMIN")) {
			 idServer= 0L; 
		}else {
			idServer= userDetails.getId();
		}
		Long idUser= idSelect;
		List<MessageEntity> lstEntity=messageRepository.findAllMessageBySelectUser( idUser,  idServer);
		List<MessageDto> lstDto = new ArrayList<>();
		SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
		for(MessageEntity entity: lstEntity) {
			MessageDto dto = MessageMapper.convertToDto(entity,sp);
			lstDto.add(dto);
		}
		return lstDto;
	}

}
