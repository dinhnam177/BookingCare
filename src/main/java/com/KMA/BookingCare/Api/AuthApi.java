package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Dto.*;
import com.KMA.BookingCare.Entity.RoleEntity;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.Repository.RoleRepository;
import com.KMA.BookingCare.Repository.UserRepository;
import com.KMA.BookingCare.Service.UserService;
import com.KMA.BookingCare.ServiceImpl.UserDetailsImpl;
import com.KMA.BookingCare.common.JwtUtils;
import com.KMA.BookingCare.config.SessionUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class AuthApi {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final UserService userServiceImpl;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final SessionUserDetail sessionUserDetail;

    @PostMapping(value = "/signin")
    public ResponseEntity<?> singin(@RequestBody LoginDto loginDto, HttpSession session){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        MyUser myUser = UserMapper.convertToMyUser(userDetails);
        session.setAttribute("userDetails", myUser);
        sessionUserDetail.setImg(myUser.getImg());
        sessionUserDetail.setFullName(myUser.getFullName());
        sessionUserDetail.setId(myUser.getId());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        sessionUserDetail.setRoles(roles);
        return ResponseEntity.ok(new JwtReponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles, myUser.getFullName()));
    }


    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }
        userServiceImpl.add(user, "user");
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
