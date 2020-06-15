package com.tech.mkblogs.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@NamedEntityGraph(name = "basic")
public class User extends BaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String loginName;
	private String password;
	private LocalDateTime lastLogin;
	private Boolean accountExpired; 
	private Boolean accountLocked;
	private Boolean credentialsExpired;
	private Boolean enabled;
	
	private String createdName;
	private String lastModifiedName;
	
	private String firstName;
	private String lastName;
	
	private Boolean encrypted;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true,cascade = CascadeType.ALL)
    private Set<Authorities> authorities = new HashSet<Authorities>(0);
	
	public void addAuthorities(Authorities authority) {
		getAuthorities().add(authority);
		authority.setUser(this);
	}
	
	public void removeAuthorities(Authorities authority) {
		getAuthorities().remove(authority);
		authority.setUser(null);
	}
	
	public void removeAllAuthorities() {
		getAuthorities().forEach(auth -> removeAuthorities(auth));
	}
	
	
	public String getRole() {
		Set<Authorities> list = getAuthorities();
		//System.out.println("Get Role Method >> "+list);
		String roleString = "";
		if(list.size() > 0) {
			for(Authorities authorities :list) {
			  roleString += authorities.getAuthority() + ",";
			}
			roleString = roleString.substring(0, roleString.length()-1);
		}
		return roleString;
	}
	
	/*
	public void setRole(String role) {
		System.out.println("setting Role ::"+role);
		Authorities authority = new Authorities();
		authority.setAuthority(role);
		addAuthorities(authority);
	}
	
	
	public void setUpdateRole(String role) {
		for(Authorities auth : getAuthorities()) {
			System.out.println("removed..");
			removeAuthorities(auth);
		}
		setRole(role);
	}
	*/
	
}
