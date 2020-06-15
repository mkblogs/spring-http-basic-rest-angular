package com.tech.mkblogs.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserAuthDTO {

	String userName;
	String basicToken;
	Boolean isAuthenticated;
	Boolean isUser;
	Boolean isAdmin;
	
}
