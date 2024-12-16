package com.KMA.BookingCare.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpingConfig {
	@Bean
	public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
	                "cloud_name", "bookingcare", // insert here you cloud name
	                "api_key", "745464575487477", // insert here your api code
	                "api_secret", "o1vAbNzhXT9YpfnW7E0js0rlPm4", // insert here your api secret
		 			"secure", true
				 ));
	}
}
