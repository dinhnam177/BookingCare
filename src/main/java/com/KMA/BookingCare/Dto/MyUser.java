package com.KMA.BookingCare.Dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.KMA.BookingCare.Entity.RoleEntity;

public class MyUser extends User {

	public MyUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		// TODO Auto-generated constructor stub
	}
	private Long id;
	
	private String roleCode;
	
	private String fullName;
	
	private String img;
	
	private List<String> roles;
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String code) {
		this.roleCode = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public MyUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id,
			String code, String fullName) {
		super(username, password, authorities);
		this.id = id;
		this.roleCode = code;
		this.fullName = fullName;
	}

}
