package com.KMA.BookingCare.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto {
	private Long id;

	private Long idUser;

	private Long idHandbook;

	private String content;

	private String userName;

	private String fullName;

	private String img;

}
