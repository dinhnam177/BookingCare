package com.KMA.BookingCare.Dto;

import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Entity.WorkTimeEntity;
import com.KMA.BookingCare.Mapper.WorkTimeMapper;
import com.KMA.BookingCare.common.Constant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private Long id;
    private String name;
    private String sex;
    @NotEmpty(message = "Thiếu số điện thoại")
    @Size(max = 12)
    private String phone;
    private String img;
    private String description;
    private Long hospitalId;
    private String location;
    private String yearOfBirth;
    private Long specializedId;
    private String reason;
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotEmpty(message = "Thiếu username")
    private String username;
    @NotEmpty(message = "Thiếu password")
    private String password;
    private Set<Role> roles;
    private String role;
    private List<WorkTimeDto> lstWorkTime;
    private String specializedName;
    private String shortDescription;
    private String hospitalName;
    private String hospitalLocation;

    private Long examinationPrice;

	public User() {}
    public User(UserEntity entity) {
        this.setId(entity.getId());
        this.setImg(Strings.isBlank(entity.getImg()) ? Constant.default_avatar : entity.getImg());
        this.setName(entity.getFullName());
        this.setDescription(entity.getDescription());
        this.setShortDescription(entity.getShortDescription());
        this.setSex(entity.getSex());
        this.setPhone(entity.getPhoneNumber());
        this.setLocation(entity.getLocation());
        this.setExaminationPrice(entity.getExaminationPrice());
        if (entity.getHospital() != null) {
            this.setHospitalId(entity.getHospital().getId());
        }
        this.setEmail(entity.getEmail());
        this.setUsername(entity.getUsername());
        this.setYearOfBirth(entity.getYearOfBirth());
        if (entity.getSpecialized() != null) {
            this.setSpecializedId(entity.getSpecialized().getId());
            this.setSpecializedName(entity.getSpecialized().getName());
        }
        if (entity.getHospital() != null) {
            this.setHospitalName(entity.getHospital().getName());
            this.setHospitalLocation(entity.getHospital().getLocation());
        }
        if (entity.getWorkTimeEntity() != null) {
            Set<WorkTimeEntity> wkEntityLst = entity.getWorkTimeEntity();
            Set<WorkTimeDto> wkDtoLst = new HashSet<WorkTimeDto>();
            for (WorkTimeEntity wkEntity : wkEntityLst) {
                WorkTimeDto wkDto = WorkTimeMapper.convertToDto(wkEntity);
                wkDtoLst.add(wkDto);
            }
            List<WorkTimeDto> targetList = new ArrayList<>(wkDtoLst);
            this.setLstWorkTime(targetList);
        }
    }

    public User(Long id, String name, String img, String shortDescription) {
        this.id = id;
        this.name = name;
        this.img = Strings.isBlank(img) ? Constant.default_avatar : img;
        this.shortDescription = shortDescription;
    }

    public User(Long id, String name, String img, String specializedName, String shortDescription) {
        this.id = id;
        this.name = name;
        this.img = Strings.isBlank(img) ? Constant.default_avatar : img;
        this.specializedName = specializedName;
        this.shortDescription = shortDescription;
    }

}
