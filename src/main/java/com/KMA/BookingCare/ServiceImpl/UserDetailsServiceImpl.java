package com.KMA.BookingCare.ServiceImpl;

import com.KMA.BookingCare.Dto.MyUser;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.Repository.UserRepository;
import com.KMA.BookingCare.common.Constant;
import com.KMA.BookingCare.config.SessionUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsernameAndStatus(username, Constant.del_flg_off);
        return UserDetailsImpl.build(user);
    }

    public UserDetailsImpl getUserDetailsImplFromContext() {
        Object result = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        UserDetailsImpl user = (UserDetailsImpl) result;
        return user;
    }
}
