package com.bezkoder.spring.thymeleaf.image.upload.service;

import com.bezkoder.spring.thymeleaf.image.upload.dto.UserRegistrationDto;
import com.bezkoder.spring.thymeleaf.image.upload.model.Role;
import com.bezkoder.spring.thymeleaf.image.upload.model.User;
import com.bezkoder.spring.thymeleaf.image.upload.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        String random = String.valueOf((int) (Math.random() * 100000));
        System.out.println(random);
        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()),
                random,
                Arrays.asList(new Role("ROLE_USER")));
        Thread thread = new Thread() {
            @Override
            public void run() {
                sendVerificationEmail(registrationDto, random);
            }
        };

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {

        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    private void sendVerificationEmail(UserRegistrationDto registrationDto, String random) {
        StringBuilder builder = new StringBuilder();
        builder.append("To verify your registration click to next link.");
        builder.append(random);
    }
}
