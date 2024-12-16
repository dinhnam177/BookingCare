package com.KMA.BookingCare.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtReponse {

    private String token;

    private String type = "Bearer";

    private Long id;

    private String username;

    private String password;

    private List<String> roles;

    private String fullName;


    public JwtReponse() {
    }

    public JwtReponse(String token, String type, Long id, String username, String password, List<String> roles) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public JwtReponse(String token, Long id, String username, List<String> roles, String fullName) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.fullName = fullName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
