package com.example.security1.controller;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.model.user.User;
import com.example.security1.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principal){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("authentication.getPrincipal().getAttributes : {}", principalDetails.getUser());
        log.info("using @AuthenticationPrincipal & principalDetails.getAttributes: {}" , principal.getUser());
        return "세션정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String oauthLoginTest(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth){

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("authentication.getPrincipal().getAttributes : {}", oAuth2User.getAttributes());
        log.info("using @AuthenticationPrincipal & oAuthUser.getAttributes : {}" , oAuth.getAttributes());
        return "OAuth 세션정보 확인하기";
    }

    @GetMapping({".", "/"})
    public String index() {
        // mustache 기본폴더 : src/main/resources
        // view resolver 설정 : templates(prefix), mustache(suffix) -> 기본설정이므로 생략가능
        return "index";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/user")
    public @ResponseBody
    String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("principalDetails.getUser() : {} " , principalDetails.getUser() );
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody
    String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody
    String manager() {
        return "manager";
    }

    //security config 파일 작성후에 작동함

    @PostMapping("/join")
    public String join(User user) {
        userService.join(user);
        return "redirect:/loginForm";
    }
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "개인정보";
    }


}
