package com.KMA.BookingCare.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

@SessionScope
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class SessionUserDetail {
    private Long id;

    private String roleCode;

    private String fullName;

    private String img;

    private List<String> roles;
}
