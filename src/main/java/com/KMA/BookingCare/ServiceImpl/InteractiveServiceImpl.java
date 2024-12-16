package com.KMA.BookingCare.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KMA.BookingCare.Dto.InteractiveDto;
import com.KMA.BookingCare.Dto.MessageDto;
import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Entity.InteractiveEntity;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Mapper.InteractiveMapper;
import com.KMA.BookingCare.Repository.InteractiveRepository;
import com.KMA.BookingCare.Repository.UserRepository;
import com.KMA.BookingCare.Service.InteractiveService;

@Service
public class InteractiveServiceImpl implements InteractiveService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InteractiveRepository interactiveRepository;


    @Override
    public void saveOrUpdate(MessageDto chatMessage) {
        // TODO Auto-generated method stub
        InteractiveEntity entity = new InteractiveEntity();
        InteractiveEntity entityOld;

        entity.setCreatedDate(new Date());
        entity.setUserId(chatMessage.getReceiverId());
        entity.setYouId(chatMessage.getSenderId());

        entity.setStatus(0);
        entityOld = interactiveRepository.findOneByUserIdAndYouId(entity.getUserId(), entity.getYouId());
        if (entityOld != null) {
            entity.setId(entityOld.getId());
        }
        entity.setLastMessage(chatMessage.getContent());
        interactiveRepository.save(entity);

        InteractiveEntity entity2 = new InteractiveEntity();
        InteractiveEntity entityOld2;
        entity2.setUserId(chatMessage.getSenderId());
        entity2.setYouId(chatMessage.getReceiverId());
        entity2.setStatus(0);
        entity2.setCreatedDate(new Date());
        entityOld2 = interactiveRepository.findOneByUserIdAndYouId(entity2.getUserId(), entity2.getYouId());
        if (entityOld2 != null) {
            entity2.setId(entityOld2.getId());
        }
        entity2.setLastMessage(chatMessage.getContent());
        interactiveRepository.save(entity2);

    }


    @Override
    public List<InteractiveDto> findAll(MyUser userDetails) {
        List<InteractiveEntity> lstEntity = new ArrayList<>();
        if (userDetails.getRoles().contains("ROLE_ADMIN")) {
            lstEntity = interactiveRepository.findAllByYouIdOrderByCreatedDateDesc((long) 0);
        } else {
            lstEntity = interactiveRepository.findAllByYouIdOrderByCreatedDateDesc(userDetails.getId());
        }
        List<InteractiveDto> lstDto = new ArrayList<>();
        for (InteractiveEntity entity : lstEntity) {
            UserEntity userEntity = userRepository.findOneById(entity.getUserId());
            InteractiveDto dto = InteractiveMapper.convertToDto(entity, userEntity);
            lstDto.add(dto);
        }
        return lstDto;
    }

}
