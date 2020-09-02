package com.shradha.DataImport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user")
@Data
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
	@Column(name="email")
	private String email;
	@Column(name="phone_number")
	private String phoneNumber;
	@Column(name="file_type")
	private String fileType;
	
	@Transient
	private MultipartFile file;

	public User(String firstName, String lastName, String email, String phoneNumber, String fileType) {
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.fileType = fileType;
	}
	
	
}
