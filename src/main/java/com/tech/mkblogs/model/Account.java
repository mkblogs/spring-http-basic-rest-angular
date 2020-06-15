package com.tech.mkblogs.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Account extends BaseModel{
	
	private String accountName;	
	private String accountType;
	private BigDecimal amount;	
	
	Account(Integer id,String accountName,String accountType,BigDecimal amount){
		super(id);
		this.accountName = accountName;
		this.accountType = accountType;
		this.amount = amount;
	}
}
