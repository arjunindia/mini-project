package io.bootify.mini_project.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import io.bootify.mini_project.user.UserDTO;

@Controller
public class LoginController {
    @GetMapping("/login")
    String login(@ModelAttribute("user") final UserDTO userDTO) {
        return "login";
    }

    @GetMapping("/logout")
    String logout() {
        return "logout";
    }
}