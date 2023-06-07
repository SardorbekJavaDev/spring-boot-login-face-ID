package com.bezkoder.spring.thymeleaf.image.upload.web;

import com.bezkoder.spring.thymeleaf.image.upload.dto.UserVerificationDto;
import com.bezkoder.spring.thymeleaf.image.upload.model.User;
import com.bezkoder.spring.thymeleaf.image.upload.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CheckController {

    private UserService userService;

    public CheckController(UserService userService) {
        super();
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserVerificationDto userRegistrationDto() {
        return new UserVerificationDto();
    }


    @GetMapping("/check")
    public String showVerification() {
        return "check";
    }


    @PostMapping("/check")
    public String registerCheckUserAccount(@ModelAttribute("user") UserVerificationDto verificationDto) {

        User byEmail = userService.getByEmail(verificationDto.getEmail());
        if (byEmail.getCode().equals(verificationDto.getCode())) return "redirect:/login";
        return "redirect:/check?error";
    }
}
