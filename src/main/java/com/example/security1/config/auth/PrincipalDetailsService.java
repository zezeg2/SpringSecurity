package com.example.security1.config.auth;

import com.example.security1.model.user.User;
import com.example.security1.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// security 설정에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 함수가 실행
// session(내부 Authentication(내부 UserDetails(PrincipalDetails(userEntity)))))
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if (userEntity!= null) return new PrincipalDetails(userEntity); //  PrincipalDetails 타입으로 리턴
        return null;
    }
}
