package com.tech.mkblogs.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_action_log")
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UserActionLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	String loginName;
	String ipAddress;
	String visitedPage;
	@Column(columnDefinition = "TEXT")
	String input;
	@Column(columnDefinition = "TEXT")
	String output;
	LocalDateTime startTime;
	LocalDateTime endTime;
	Long timeTaken;
}
