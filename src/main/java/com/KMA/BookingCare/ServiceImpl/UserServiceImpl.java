package com.KMA.BookingCare.ServiceImpl;

import com.KMA.BookingCare.Api.form.DeleteForm;
import com.KMA.BookingCare.Api.form.UpdateCientForm;
import com.KMA.BookingCare.Api.form.UserForm;
import com.KMA.BookingCare.Api.form.searchDoctorForm;
import com.KMA.BookingCare.Dto.*;
import com.KMA.BookingCare.Entity.*;
import com.KMA.BookingCare.Mapper.UserMapper;
import com.KMA.BookingCare.Mapper.WorkTimeMapper;
import com.KMA.BookingCare.Repository.*;
import com.KMA.BookingCare.Service.UserService;
import com.KMA.BookingCare.Service.WorkTimeService;
import com.KMA.BookingCare.common.AESUtils;
import com.KMA.BookingCare.common.Constant;
import com.KMA.BookingCare.common.GetUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private WorkTimeService workTimeServiceImpl;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private SpecializedRepository specializedRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private MedicalExaminationScheduleRepository medicalRepository;

    @Autowired
    private WorkTimeRepository wkRepository;

    @Autowired
    private HandbookRepository handbookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private InteractiveRepository interactiveRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${booking_care.ui.domain}")
    private String domain;

    @Autowired
    private HolidayRepository holidayRepository;

    @Override
    public void add(User user, String nameRole) {
        UserEntity userEntity = new UserEntity();
        if (user.getId() != null && !user.getId().equals("") && user.getId() != 0) {
            userEntity.setId(user.getId());
        }
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUsername());
        userEntity.setLocation(user.getLocation());
        userEntity.setSex(user.getSex());
        userEntity.setPhoneNumber(user.getPhone());
        userEntity.setFullName(user.getName());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setStatus(Constant.del_flg_off);

        switch (nameRole) {
            case "admin": {
                Set<RoleEntity> role = new HashSet<>(roleRepository.findByName("ROLE_ADMIN"));
                userEntity.setRoles(role);
                break;
            }
            case "user": {
                Set<RoleEntity> role = new HashSet<>(roleRepository.findByName("ROLE_USER"));
                userEntity.setRoles(role);
                break;
            }
            case "doctor": {
                Set<RoleEntity> role = new HashSet<>(roleRepository.findByName("ROLE_DOCTOR"));
                userEntity.setRoles(role);
                break;
            }
        }
        userRepository.save(userEntity);
    }

    @Override
    public void update(User user) {
        UserEntity userEntity = new UserEntity();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String password = userDetails.getPassword();
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub

    }


    @Override
    public List<User> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User findByUsername(String username) {
        User user = new User();
        UserEntity userEntity = userRepository.findByUsernameAndStatus(username, Constant.del_flg_off);
        user.setId(userEntity.getId());
        user.setName(userEntity.getFullName());
        user.setUsername(userEntity.getUsername());
        user.setPassword((userEntity.getPassword()));
        user.setEmail(userEntity.getEmail());
        user.setImg(userEntity.getImg());
        Set<RoleEntity> set = userEntity.getRoles();
        Set<Role> roles = new HashSet<Role>();
        for (RoleEntity roleEn : userEntity.getRoles()) {
            Role role = new Role();
            role.setId(roleEn.getId());
            role.setName(roleEn.getName());
            roles.add(role);
        }
        user.setRoles(roles);
        return user;

    }

    @Override
    public void saveDoctor(UserForm form) throws IllegalStateException, IOException {
        UserEntity entity = new UserEntity();
        if (form.getId() != null && !form.getId().equals("")) {
            entity = userRepository.findOneById(form.getId());
            if (form.getImg().getOriginalFilename() != null && !form.getImg().getOriginalFilename().equals("")) {
                try {
                    Map result = cloudinary.uploader().upload(form.getImg().getBytes(),
                            ObjectUtils.asMap("resource_type", "auto"));
                    String urlImg = (String) result.get("secure_url");
                    entity.setImg(urlImg);
                } catch (Exception e) {
                    System.out.println("upload img fail");
                }
            }
        } else {
            entity.setPassword(passwordEncoder.encode(form.getPassword()));
            entity.setUsername(form.getUsername());
            entity.setStatus(1);
            Set<RoleEntity> role = new HashSet<>(roleRepository.findByName(form.getRoleName()));
            entity.setRoles(role);
            try {
                Map result = cloudinary.uploader().upload(form.getImg().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                String urlImg = (String) result.get("secure_url");
                entity.setImg(urlImg);
            } catch (Exception e) {
                System.out.println("upload img fail");
            }
        }
        entity.setExaminationPrice(form.getExaminationPrice());
        entity.setShortDescription(form.getShortDescription());
        entity.setDescription(form.getDescription());
        entity.setEmail(form.getEmail());
        entity.setFullName(form.getFullname());
        entity.setLocation(form.getLocation());
        entity.setPhoneNumber(form.getPhoneNumber());
        entity.setYearOfBirth(form.getYearOfBirth());
        entity.setSex(form.getSex());
        HospitalEntity hospital = hospitalRepository.findOneById(form.getHospitalId());
        entity.setHospital(hospital);
        SpecializedEntity specialized = specializedRepository.findOneById(form.getSpecializedId());
        entity.setSpecialized(specialized);
        if (form.getWorkTimeId() != null) {
            Set<WorkTimeEntity> lstWorkTimeEntities = new HashSet<WorkTimeEntity>(workTimeServiceImpl.findByListId(form.getWorkTimeId()));
            if (lstWorkTimeEntities != null) {
                entity.setWorkTimeEntity(lstWorkTimeEntities);
            }
        }
        entity = userRepository.save(entity);
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> lstEntity = userRepository.findAll();
        List<User> lstDto = new ArrayList<User>();
        for (UserEntity entity : lstEntity) {
            User dto = new User();
            dto.setId(entity.getId());
            dto.setDescription(entity.getDescription());
            dto.setImg(entity.getImg());
            dto.setName(entity.getFullName());
            dto.setLocation(entity.getLocation());
            dto.setPhone(entity.getPhoneNumber());
            dto.setSex(entity.getSex());
            if (entity.getSpecialized() != null) {
                dto.setSpecializedId(entity.getSpecialized().getId());
            } else {
//				dto.setSpecializedId("null");
            }
            Set<RoleEntity> lstRole = entity.getRoles();
            StringBuilder roleName = new StringBuilder();
            for (RoleEntity roleEntity : lstRole) {
                roleName.append(roleEntity.getName());
                roleName.append(", ");
            }
            String strRoleName = roleName.toString();
            strRoleName = (String) strRoleName.subSequence(0, (strRoleName.length() - 2));
            dto.setRole(strRoleName);
            dto.setUsername(entity.getUsername());
            lstDto.add(dto);
        }
        return lstDto;
    }

    @Override
    public List<User> findAllByStatus(Integer status, Pageable pageable) {
        List<UserEntity> lstEntity = userRepository.findAllByStatus(status, pageable);
        List<User> lstDto = new ArrayList<User>();
        for (UserEntity entity : lstEntity) {
            User dto = new User();
            dto.setId(entity.getId());
            dto.setDescription(entity.getDescription());
            dto.setImg(entity.getImg());
            dto.setName(entity.getFullName());
            dto.setLocation(entity.getLocation());
            dto.setPhone(entity.getPhoneNumber());
            dto.setSex(entity.getSex());
            if (entity.getSpecialized() != null) {
                dto.setSpecializedId(entity.getSpecialized().getId());
            } else {

            }
            Set<RoleEntity> lstRole = entity.getRoles();
            StringBuilder roleName = new StringBuilder();
            for (RoleEntity roleEntity : lstRole) {
                roleName.append(roleEntity.getName());
                roleName.append(", ");
            }
            String strRoleName = roleName.toString();
            strRoleName = (String) strRoleName.subSequence(0, (strRoleName.length() - 2));
            dto.setRole(strRoleName);
            dto.setUsername(entity.getUsername());
            lstDto.add(dto);
        }
        return lstDto;
    }

    @Override
    public void updateUserByStatus(List<String> ids, Integer status) {
        userRepository.updateStatus(ids, status);
    }

    @Override
    public User findOneById(Long id) {
        UserEntity entity = userRepository.findOneById(id);
        return UserMapper.convertToDto(entity);
    }

    @Override
    public List<User> findAllBySpecializedIdAndStatus(Long id, Integer status) {
        List<UserEntity> lstEntity = userRepository.findAllBySpecializedIdAndStatus(id, status);
        List<User> lstDto = new ArrayList<>();
        for (UserEntity entity : lstEntity) {
            User dto = UserMapper.convertToDto(entity);
            lstDto.add(dto);
        }
        return lstDto;
    }


    @Override
    public List<User> findAllByMedical(String date, Long specialized, Integer status) {
        List<UserEntity> lstEntity = userRepository.findAllByMedical(date, specialized, status, 1); //lấy tất cả bác sĩ có lịch khám theo date, chuyên khoa specialized
        for (UserEntity entity : lstEntity) {
            List<Long> lstWktId = medicalRepository.findAllWorkTimeIdByDateAndDoctorIdAndStatus(entity.getId(), date); //lấy danh sách lịch khám từng bác sĩ
            Set<WorkTimeEntity> lstWk = new HashSet<>(entity.getWorkTimeEntity());
            for (WorkTimeEntity wk : entity.getWorkTimeEntity()) {
                if (lstWktId.contains(wk.getId())) {
                    lstWk.remove(wk);
                }
            }
            entity.setWorkTimeEntity(lstWk);
        }
        List<User> lstDto = new ArrayList<>();
        for (UserEntity entity : lstEntity) {
            User dto = UserMapper.convertToDto(entity);
            lstDto.add(dto);
        }
        return lstDto;
    }

    @Override
    public List<User> findAllBySpecialized(String date, Long specialized, Integer status) {
        List<UserEntity> lstEntity = userRepository.findAllBySpecialized(date, specialized, status, 1);
        List<User> lstDto = new ArrayList<>();
        for (UserEntity entity : lstEntity) {
            User dto = UserMapper.convertToDto(entity);
            lstDto.add(dto);
        }
        return lstDto;
    }

    @Override
    public List<User> findAllDoctorOfSpecialized(Long specialized, Integer status) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(new Date());
        List<User> lstDto1 = findAllBySpecialized(date, specialized, status); //lấy danh sách những bác sĩ không có lịch khám trong ngày
        List<User> lstDto2 = findAllByMedical(date, specialized, status);
        lstDto1.addAll(lstDto2);
        System.out.println("test");
        return lstDto1;
    }

    @Override
    public Page<User> findAllDoctorOfSpecializedApi(Long specialized, Integer status, Pageable pageable) {
        Page<User> userPage = userRepository.findAllBySpecializedApi(specialized, status, pageable);
        getWorkTimesOfDoctor(userPage);
        return userPage;
    }

    @Override
    public List<User> findAllDoctorOfHospital(Long hospitalId, Integer status) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(new Date());
        List<UserEntity> lstEntity1 = userRepository.findAllByHospital(date, hospitalId, status, 1); //lấy danh sách những bác sĩ không có lịch khám trong ngày
        List<UserEntity> lstEntity2 = userRepository.findAllByMedicalAndHospital(date, hospitalId, status, 1);
        for (UserEntity entity : lstEntity2) {
            List<Long> lstWktId = medicalRepository.findAllWorkTimeIdByDateAndDoctorIdAndStatus(entity.getId(), date); //lấy danh sách lịch khám từng bác sĩ
            Set<WorkTimeEntity> lstWk = new HashSet<WorkTimeEntity>(entity.getWorkTimeEntity());
            for (WorkTimeEntity wk : entity.getWorkTimeEntity()) {
                if (lstWktId.contains(wk.getId())) {
                    lstWk.remove(wk);
                }
            }
            entity.setWorkTimeEntity(lstWk);
        }
        lstEntity1.addAll(lstEntity2);
        List<User> lstDto = new ArrayList<User>();
        for (UserEntity entity : lstEntity1) {
            User dto = UserMapper.convertToDto(entity);
            lstDto.add(dto);
        }
        return lstDto;
    }

    @Override
    public Page<User> findAllDoctorOfHospitalApi(Long hospitalId, Integer status, Pageable pageable) {
        Page<User> userPage = userRepository.findAllDoctorByHospitalAndStatus(hospitalId, status, pageable);
        getWorkTimesOfDoctor(userPage);
        return userPage;
    }

    private void getWorkTimesOfDoctor(Page<User> userPage) {
        Calendar calendar = Calendar.getInstance();
        for (User doctor : userPage.getContent()) {
            List<Long> wkInHolidays = holidayRepository.getWorkTimeIdByDateAndDoctorId(doctor.getId(), new Date());
            List<Long> workTimeScheduled = medicalRepository.findAllWorkTimeIdByDateAndDoctorIdAndStatus(doctor.getId(), String.valueOf(new Date()));
            List<WorkTimeDto> lstWk = new ArrayList<>(doctor.getLstWorkTime());
            for (WorkTimeDto wk : doctor.getLstWorkTime()) {
                if (workTimeScheduled.contains(wk.getId())
                        ||
                        !GetUtils.isValidWorkTime(wk.getTime(), calendar)
                        || wkInHolidays.contains(wk.getId())
                ) {
                    lstWk.remove(wk);
                }
            }
            doctor.setLstWorkTime(lstWk);
        }
    }

    @Override
    public void updateClient(UpdateCientForm form) {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        MyUser userDetails = UserMapper.convertToMyUser(user);
        UserEntity entity = userRepository.findOneById(userDetails.getId());
        entity.setFullName(form.getFullName());
        entity.setEmail(form.getEmail());
        entity.setPhoneNumber(form.getPhone());
        if (form.getPasswod() != null && !form.getPasswod().equals("")) {
            entity.setPassword(passwordEncoder.encode(form.getPasswod()));
        }
        if (form.getImg() != null && !form.getImg().isEmpty()) {
            try {
                Map result = cloudinary.uploader().upload(form.getImg().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                String urlImg = (String) result.get("secure_url");
                entity.setImg(urlImg);
            } catch (Exception e) {
                System.out.println("upload img fail");
            }
        }
        userRepository.save(entity);
    }

    @Override
    public List<User> findAllDoctor() {
        List<UserEntity> lstEntity = userRepository.findAllDoctor();
        List<User> lstDto = new ArrayList<User>();
        for (UserEntity entity : lstEntity) {
            User dto = UserMapper.convertToDto(entity);
            lstDto.add(dto);
        }
        return lstDto;
    }

    @Override
    public List<User> searchDoctor(searchDoctorForm form) {
        String name = form.getName();
        Long specializedId = form.getSpecializedId();
        Long hospitalId = form.getHospitalId();
        StringBuilder sql = new StringBuilder("1");
        if (name != null || hospitalId != null || specializedId != null) {
//			sql.append(" WHERE 1");
            if (name != null) {
                sql.append(" AND ");
                sql.append(" full_name like CONCAT('%',");
                sql.append(name);
                sql.append(",'%')");
            }
            if (specializedId != null) {
                sql.append(" AND ");
                sql.append("specialized_id=");
                sql.append(specializedId);
            }
            if (hospitalId != null) {
                sql.append(" AND ");
                sql.append(" hospital_id =");
                sql.append(hospitalId);
            }
        } else {
            sql.append(" 1");
        }
        String sqlString = sql.toString();
        List<UserEntity> lstEntities = userRepository.searchAllDoctor(sqlString);
        List<User> lstDto = new ArrayList<User>();
        for (UserEntity entity : lstEntities) {
            User dto = UserMapper.convertToDto(entity);
            lstDto.add(dto);
        }
        return lstDto;
    }

    @Override
    public User findOneDoctorAndWorktime(Long id, String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (Strings.isBlank(date)) {
            date = formatter.format(new Date());
        }
        Date currentDate = new Date();
        boolean isSameDate = date.equals(formatter.format(currentDate));
        UserEntity entity = userRepository.findOneById(id);
        List<WorkTimeEntity> lstWkEntity = wkRepository.findByDateAndDoctorId(date, id);
        List<Long> wkInHolidays = holidayRepository.getWorkTimeIdByDateAndDoctorId(id, formatter.parse(date));
        Set<WorkTimeEntity> workTimeEntities = new HashSet<>(lstWkEntity);
        Calendar calendar = Calendar.getInstance();
        List<WorkTimeEntity> lstWkValid = workTimeEntities
                .stream()
                .filter((e) ->
                        !wkInHolidays.contains(e.getId())
                        &&
                        (!isSameDate || GetUtils.isValidWorkTime(e.getTime(), calendar)))
                .collect(Collectors.toList());
        List<WorkTimeDto> dtos = lstWkValid.stream().map(WorkTimeMapper::convertToDto).collect(Collectors.toList());
        User dto = UserMapper.convertToDto(entity);
        dto.setLstWorkTime(dtos);
        return dto;
    }

    @Override
    public String findPeerIdById(Long id) {
        // TODO Auto-generated method stub
        return userRepository.findPeerIdById(id);
    }

    @Override
    public Integer updatePeerId(String peerId, Long id) {
        // TODO Auto-generated method stub
        return userRepository.updatePeerId(peerId, id);
    }

    @Override
    public List<User> findRandomDoctor() {
        List<UserEntity> lstEntity = userRepository.findRandomDoctor();
        List<User> lstDto = new ArrayList<User>();
        for (UserEntity entity : lstEntity) {
            User dto = UserMapper.convertToDto(entity);
            lstDto.add(dto);
        }
        return lstDto;
    }

    @Override
    public List<User> getDoctorOnline() {
        Pageable pageable = PageRequest.of(0, 4);
        List<User> userPage = userRepository.getDoctorOnline(pageable);
        return userPage;
    }

    @Override
    public List<User> getFeaturedDoctor() {
        List<UserFeaturedDto> userFeaturedDtoList = userRepository.getFeaturedUser();
        List<Long> ids = userFeaturedDtoList.stream().map(UserFeaturedDto::getId).collect(Collectors.toList());
        return userRepository.findAllDoctorByIds(ids);
    }

    @Override
    public List<User> findAllDoctorBySpecialIdAndWorkTimeIdAndDate(Long specialtyId, Long workTimeId, String date) {
        List<User> users = userRepository.findAllBySpecialtyIdAndWorkTimeId(specialtyId, workTimeId);
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        List<MedicalExaminationScheduleEntity> medicalExaminationSchedule = medicalRepository.findAllByDoctorIdsAndDate(userIds, date);
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        Date dateSearch = new Date();
        try {
             dateSearch = sp.parse(date);
        } catch (Exception e) {

        }
        List<HolidayEntity> holidays = holidayRepository.getAllByDateAndDoctorIdAndWkId(userIds, dateSearch);
        List<Long> userIdsRemove = new ArrayList<>();
        for (User user : users) {
            for (MedicalExaminationScheduleEntity medical : medicalExaminationSchedule) {
                if (!Objects.equals(user.getId(), medical.getDoctor().getId())) continue;
                if (Objects.equals(medical.getWorkTimeID(), workTimeId)) {
                    userIdsRemove.add(medical.getDoctor().getId());
                    continue;
                }
                List<WorkTimeDto> workTimeDto = user.getLstWorkTime()
                        .stream()
                        .filter(e -> !Objects.equals(e.getId(), medical.getWorkTimeID()))
                        .collect(Collectors.toList());
                user.setLstWorkTime(workTimeDto);
            }

            for (HolidayEntity holiday : holidays) {
                if (!Objects.equals(user.getId(), holiday.getUser().getId())) continue;
                List<Long> ids = holiday.getWorkTimes().stream().map(WorkTimeEntity::getId).collect(Collectors.toList());
                if (ids.contains(workTimeId)) {
                    userIdsRemove.add(holiday.getUser().getId());
                    continue;
                }
                List<WorkTimeDto> workTimeDto = user.getLstWorkTime()
                        .stream()
                        .filter(e -> !ids.contains(e.getId()))
                        .collect(Collectors.toList());
                user.setLstWorkTime(workTimeDto);
            }
        }
        if (CollectionUtils.isEmpty(userIdsRemove)) return users;
        return users.stream().filter(e -> !userIdsRemove.contains(e.getId())).collect(Collectors.toList());
    }

    @Override
    public List<SearchFullTextDto> searchAllByFullText(String query) {
        List<UserEntity> userEntities = userRepository.searchAllByFullText(query);
        return userEntities.stream()
                .map(e -> SearchFullTextDto.builder()
                        .id(e.getId())
                        .title(e.getFullName())
                        .description(e.getShortDescription())
                        .img(e.getImg())
                        .tableName("USER")
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Page<User> searchDoctorForClient(Long hospitalId, Long specialtyId, String doctorName, Pageable pageable) {
        return userRepository.searchDoctorAndPageable(doctorName, specialtyId, hospitalId, pageable);
    }

    @Override
    public boolean isExistItemRelationWithSpecialIsUsing(List<String> ids) {
        List<Long> userIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        long totalHandbook = handbookRepository.existsByUser(userIds);
        long totalMedical = medicalRepository.existsByUser(userIds);
        return !Objects.equals(0L, totalHandbook) || !Objects.equals(0L, totalMedical);
    }

    @Override
    public void deleteUser(List<String> ids) {
        List<Long> userIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        holidayRepository.deleteAllByUserIds(userIds);
        List<UserEntity> userEntities = userRepository.findAllById(userIds);
        for (UserEntity user : userEntities) {
            user.setWorkTimeEntity(Collections.emptySet());
            user.setRoles(Collections.emptySet());
        }

        List<Long> handBookIds = handbookRepository.findAllByUser(userIds).stream().map(HandbookDto::getId).collect(Collectors.toList());
        commentRepository.deleteAllByHandbookIds(handBookIds);
        commentRepository.deleteAllByUser(userIds);
        handbookRepository.deleteAllByUser(userIds);
        messageRepository.deleteAllByReceiverIdOrSenderId(userIds);
        interactiveRepository.deleteAllByUserIdOrYouId(userIds);
        medicalRepository.deleteAllByDoctorIds(userIds);
        userRepository.deleteAllById(userIds);
    }

    @Override
    public boolean isExistUserNameAndEmail(String userName, String email) {
        UserEntity user = userRepository.findByUsernameAndEmail(userName, email);
        return user != null;
    }

    @Override
    public String createUrlResetPassword(String userName) {
        String userNameEncrypt = AESUtils.encrypt(userName, Constant.secret);
        String query = null;
        try {
            query = "username=" + userName + "&key=" + URLEncoder.encode(userNameEncrypt, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            query = "username=" + userName + "&key=" + userNameEncrypt;
        }
        String url = domain + "/reset-mat-khau?" + query;
        return url;
    }

    @Override
    public void sendMessageResetPassword(String userName) throws JsonProcessingException {
        String url = this.createUrlResetPassword(userName);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        UserEntity user = userRepository.findByUsernameAndStatus(userName, Constant.del_flg_off);
        NotificationResetPasswordDTO notification = new NotificationResetPasswordDTO();
        notification.setFullName(user.getFullName());
        notification.setEmail(user.getEmail());
        notification.setUrl(url);
        String json = ow.writeValueAsString(notification);
        kafkaTemplate.send(Constant.NOTIFICATION_RESET_PASS_TOPIC, json);
    }

    @Override
    public void changPassword(String userName, String password) {
       String newPassword = passwordEncoder.encode(password);
       userRepository.updatePasswordByUsername(newPassword, userName);
    }

    @Override
    public boolean verifyUserName(String userName, String key) {
        String decodeKey = "";
        try {
            decodeKey = URLDecoder.decode(key, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            decodeKey = key;
        }
        String decrypt = AESUtils.decrypt(decodeKey, Constant.secret);
        return userName.equals(decrypt);
    }

    @Override
    public boolean isValidSpecialtyAndHospital(DeleteForm form) {
        List<Long> ids = form.getIds().stream().map(Long::parseLong).collect(Collectors.toList());
        Long total = userRepository.getTotalUserBySpecialStatusOrHospitalStatus(ids, Constant.del_flg_on);
        return total == 0L;
    }

    @Override
    public boolean isValidWorkTime(Long workTimeId) {
        WorkTimeEntity wk = wkRepository.findById(workTimeId).get();

        return false;
    }

    @Override
    public List<User> searchDoctorAndPageable(searchDoctorForm form, String roleUser, Pageable page, Integer status) {

        if (form.getName() == null || form.getName().equals("")) {
            form.setName("");
        }
        List<Integer> roleIds;
        if ("ADMIN".equals(roleUser)) roleIds = List.of(1, 2);
        else roleIds = List.of(2);
        List<UserEntity> lstEntities = userRepository.searchHandbookAndPageable(form.getName(), form.getSpecializedId(), form.getHospitalId(), page, roleIds, status);
        Set<UserEntity> filterDuplicate = new HashSet<>(lstEntities);
        List<User> lstDto = new ArrayList<>();
        for (UserEntity entity : filterDuplicate) {
            User dto = UserMapper.convertToDto(entity);
            lstDto.add(dto);
        }
        return lstDto;
    }

    @Override
    public Integer searchTotalPageDoctorAndPageable(searchDoctorForm form, String roleUser, Pageable page, Integer status) {
        List<Integer> roleIds;
        if ("ADMIN".equals(roleUser)) roleIds = List.of(1, 2);
        else roleIds = List.of(2);
        Integer totalElement = userRepository.searchToTalHandbookAndPageable(form.getName(), form.getSpecializedId(), form.getHospitalId(), roleIds, status);
        int remainder = totalElement % page.getPageSize();
        return remainder > 0 ? (totalElement / page.getPageSize()) + 1 : totalElement / page.getPageSize();
    }

    @Override
    public Page<User> findAllDoctor(Pageable pageable) {
        return userRepository.findAllDoctor(pageable);
    }

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsernameAndStatus(username, Constant.del_flg_off);
        return UserDetailsImpl.build(user);
    }


}
