package com.KMA.BookingCare.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class SearchAllDto {

    private Long id;

    private String title;

    private String img;

    private String description;

    private String content;

    private Integer status;

    private String createdBy;

    private String modifiedBy;

    private String fullName;

    private String sex;

    private String phoneNumber;

    private String location;

    private String shortDescription;

    private String username;

    private String email;

    private String yearOfBirth;

    private String name;

    private String code;

    private String _class;

}
