package com.bezkoder.spring.thymeleaf.image.upload.service;

import com.bezkoder.spring.thymeleaf.image.upload.dto.UserRegistrationDto;
import com.bezkoder.spring.thymeleaf.image.upload.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	User save(UserRegistrationDto registrationDto);

	List<User> getAll();

	User getByEmail(String email);
}
