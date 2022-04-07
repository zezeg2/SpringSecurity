package com.example.security1.service.user;

import com.example.security1.model.user.User;
import com.example.security1.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public void join(User user){
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        /*
         회원가입은 잘 됨,
         비밀번호 1234 => 시큐리티로 로그인할 수 없음
         이유는 패스워드가 암호화 안되었기 때문
         */
    }


}
