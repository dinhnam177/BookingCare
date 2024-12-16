package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Api.form.*;
import com.KMA.BookingCare.Dto.User;
import com.KMA.BookingCare.Dto.UserInput;
import com.KMA.BookingCare.Dto.WorkTimeDto;
import com.KMA.BookingCare.Entity.MedicalExaminationScheduleEntity;
import com.KMA.BookingCare.Service.UserService;
import com.KMA.BookingCare.ServiceImpl.MedicalExaminationScheduleServiceImpl;
import com.KMA.BookingCare.ServiceImpl.UserDetailsImpl;
import com.KMA.BookingCare.ServiceImpl.UserDetailsServiceImpl;
import com.KMA.BookingCare.common.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class UserApi {

    private final Logger log = LoggerFactory.getLogger(UserApi.class);

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private MedicalExaminationScheduleServiceImpl medicalService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @PostMapping("/user")
    public ResponseEntity<User> getByUsername(@RequestBody UserInput userInput) {
        User user = userServiceImpl.findByUsername(userInput.getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/admin/api/managerUser/add")
    public ResponseEntity<?> addOrUser(@ModelAttribute UserForm form) {
        try {
            userServiceImpl.saveDoctor(form);
            System.out.println("oke");
        } catch (IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("looxi");
        }
        return ResponseEntity.ok("true");

    }

    @PostMapping(value = "/api/user/uDelete")
    public ResponseEntity<?> deleteUserOke(@RequestBody DeleteForm form) {
        log.info("request to uDelete user by ids: {}", form.getIds());
        boolean isExits = userServiceImpl.isExistItemRelationWithSpecialIsUsing(form.getIds());
        if (isExits) {
            return ResponseEntity.badRequest().body("Không thể xoá người dùng do vẫn còn bài viết hoặc ca khám còn hoạt động");
        }
        try {
            userServiceImpl.updateUserByStatus(form.getIds(), Constant.del_flg_on);
            return ResponseEntity.ok("true");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = {"/api/user/restore"})
    public ResponseEntity<?> restoreUser(@RequestBody DeleteForm form) {
        boolean isValid = userServiceImpl.isValidSpecialtyAndHospital(form);
        if(!isValid) {
            return ResponseEntity.badRequest().body("Vẫn còn \"Chuyên khoa\" hoặc \"Cơ sở y tế\" đang trong trạng thái không sẵn sàng, vui lòng kiểm tra lại dữ liệu");
        }
        try {
            userServiceImpl.updateUserByStatus(form.getIds(), Constant.del_flg_off);
            return ResponseEntity.ok("true");
        } catch (Exception e ) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Có lỗi xảy ra xin vui lòng thử lại");
        }
    }

    @PostMapping(value = {"/api/user/delete"})
    public ResponseEntity<?> deleteUser(@RequestBody DeleteForm form) {
        try {
            userServiceImpl.deleteUser(form.getIds());
            return ResponseEntity.ok("true");
        } catch (Exception e ) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/api/updateClient")
    public ResponseEntity<?> addUser(@ModelAttribute UpdateCientForm form) {
        userServiceImpl.updateClient(form);
        return ResponseEntity.ok("true");

    }

    /*
     * hien thi len trang chu
     * */
    @GetMapping(value = "/api/user/get-random-docter")
    public List<User> getRandomDocter() {
        log.info("Request to get random docter {}");
        List<User> lstBacsi = userServiceImpl.findRandomDoctor();
        return lstBacsi;
    }

    @GetMapping(value = "/api/user/specialzed/{id}")
    public ResponseEntity<Page<User>> getALlBySpecialzedId(@PathVariable long id,
                                                           @PageableDefault(page = 0, size = 6) Pageable pageable) {
        log.info("Request to getAllBySpecialzedId {}");
        Page<User> page = userServiceImpl.findAllDoctorOfSpecializedApi(id, 1, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping(value = "/api/user/hospital/{id}")
    public ResponseEntity<Page<User>> getAllByHospitalId(@PathVariable long id, @PageableDefault(page = 0, size = 6) Pageable pageable) {
        log.info("Request to getAllByHospitalId {} ");
        Page<User> page = userServiceImpl.findAllDoctorOfHospitalApi(id, 1, pageable);
        return ResponseEntity.ok(page);
    }

    //get thong tin bac si
    @GetMapping(value = "/api/user/doctor/{id}")
    public ResponseEntity<User> infoDoctor(@PathVariable("id") Long id, @RequestParam(value = "date", required = false) String date) throws ParseException {
        log.info("Request to infoDocter");
        User user = userServiceImpl.findOneDoctorAndWorktime(id, date);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/api/user/doctor-by-medical/{id}")
    public ResponseEntity<?> infoDoctorByMedicalId(@PathVariable("id") Long id) throws ParseException {
        log.info("Request to infoDocter");
        UserDetailsImpl userDetails = userDetailsService.getUserDetailsImplFromContext();
        Optional<MedicalExaminationScheduleEntity> entity = medicalService.findOneByIdAndUserId(id, userDetails.getId());
        if (entity.isPresent()) {
            User user = userServiceImpl.findOneDoctorAndWorktime(entity.get().getDoctor().getId(), entity.get().getDate());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "api/user/search-docter")
    public List<User> searchDocter(@RequestBody searchDoctorForm form, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        log.info("Request to search docter {}", form);
        List<User> lstUser = userServiceImpl.searchDoctorAndPageable(form, "USER", pageable, Constant.del_flg_off);
        return lstUser;
    }

    @GetMapping(value = "/api/current-login")
    public ResponseEntity<User> getCurrentLogin(HttpSession session) {
        log.info("Request to get current login {}");
        Object result = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) result;
        User userDto = userServiceImpl.findOneById(userDetails.getId());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping(value = "/api/user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserForm form) throws IOException {
        log.info("Request to update user");
        userServiceImpl.saveDoctor(form);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(value = "/api/user/doctor")
    public ResponseEntity<Page<User>> findAllDoctor(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        log.debug("Request to get all doctor");
        Page<User> page = userServiceImpl.findAllDoctor(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping(value = "/api/user/get-doctor-online")
    public ResponseEntity<?> getDoctorOnline() {
        log.debug("Request to get doctor online");
        return ResponseEntity.ok(userServiceImpl.getDoctorOnline());
    }

    @GetMapping(value = "/api/user/get-featured-doctor")
    public ResponseEntity<?> getFeaturedDoctor() {
        return ResponseEntity.ok(userServiceImpl.getFeaturedDoctor());
    }

    @GetMapping(value = "/api/user/get-all-by-specialty-workTimeId")
    public ResponseEntity<List<User>> getALlBySpecialzedId(@RequestParam("specialtyId") Long specialtyId,
                                                           @RequestParam("workTimeId") Long workTimeId,
                                                           @RequestParam("date") String date) {
        log.info("Request to getAllBySpecialzedId {}");
        List<User> users = userServiceImpl.findAllDoctorBySpecialIdAndWorkTimeIdAndDate(specialtyId, workTimeId, date);
        return ResponseEntity.ok(users);
    }

    //search-doctor
    @GetMapping("/api/user/search-doctor")
    public ResponseEntity<?> searchDoctor(@RequestParam("hospitalId") Long hospitalId,
                                          @RequestParam("specialtyId") Long specialtyId,
                                          @RequestParam(value = "doctorName", defaultValue = "") String doctorName,
                                          @PageableDefault(page = 0, size = 8) Pageable pageable) {
        log.info("Request to search Doctor by hospitalid {}, specialtyId {}, doctorName {}", hospitalId, specialtyId, doctorName);
        return ResponseEntity.ok(userServiceImpl.searchDoctorForClient(hospitalId, specialtyId, doctorName, pageable));
    }

    @PostMapping("/api/user/create-url-reset-pass")
    public ResponseEntity<?> createUrl(@RequestBody ResetPasswordForm form) throws JsonProcessingException {
        log.info("Request to createUrl to resetPassword with username: {}", form.getUserName());
        boolean isExistUser = userServiceImpl.isExistUserNameAndEmail(form.getUserName(), form.getEmail());
        if (!isExistUser) return ResponseEntity.badRequest().body("UserName hoặc Email không đúng, xin vòng lòng nhập lại");
        userServiceImpl.sendMessageResetPassword(form.getUserName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/user/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordForm form) {
        boolean isValidUserName = userServiceImpl.verifyUserName(form.getUserName(), form.getKey());
        if (!isValidUserName)
            return ResponseEntity.badRequest().body("Đường dẫn không đúng, vui lòng kiểm tra lại đường dẫn trong email");
        userServiceImpl.changPassword(form.getUserName(), form.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ResetPasswordForm form) {
        userServiceImpl.changPassword(form.getUserName(), form.getPassword());
        return ResponseEntity.ok(true);
    }

    @GetMapping(value = "/api/user/docter")
    public ResponseEntity<Page<User>> findAllDoctorAPI(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        log.debug("Request to get all doctor");
        Page<User> page = userServiceImpl.findAllDoctor(pageable);
        return ResponseEntity.ok(page);
    }

    //get thong tin bac si
    @GetMapping(value="/api/user/docter/{id}")
    public ResponseEntity<User>  infoDoctor(Model model, @PathVariable("id") Long id) throws ParseException {
        log.info("Request to infoDocter");
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        String date = sp.format(new Date());
        User user = userServiceImpl.findOneDoctorAndWorktime(id, date);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value="/api/user/doctor/mobile/{id}")
    public ResponseEntity<User>  infoDoctorMobile(Model model, @PathVariable("id") Long id) throws ParseException {
        log.info("Request to infoDoctor");
        User user = userServiceImpl.findOneById(id);
        return ResponseEntity.ok(user);
    }
}
