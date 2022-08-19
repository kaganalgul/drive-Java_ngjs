package com.beam.drive.controller;

import com.beam.drive.dto.AuthenticationRequest;
import com.beam.drive.dto.AuthenticationResponse;
import com.beam.drive.model.User;
import com.beam.drive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.beam.drive.service.UserService.SESSION_ACCOUNT;

@Controller
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    // Kullanıcının girişini kontrol eder ve duruma göre bir response döndürür.
    @ResponseBody
    @PostMapping("login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request, HttpSession session) {
        AuthenticationResponse response = userService.login(request);

        if (response.getCode() == 0) {
            session.setAttribute(SESSION_ACCOUNT, response.getUser());
        }

        return response;
    }
    //Kullanıcı kayıt
    @ResponseBody
    @PostMapping("register")
    public void register(@RequestBody User user) {
        String name = user.getName();
        userService.register(user);
    }

    // Kullanıcı logout
    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SESSION_ACCOUNT);
        return "redirect:/login";
    }
}
