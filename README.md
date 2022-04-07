## Spring Security

### **SecurityConfig.java 권한 설정 방법**

```java
// protected void configure(HttpSecurity http) 함수 내부에 권한 설정법
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
```

### **컨트롤러의 함수에 직접 권한 설정 하는 방법**

```java
// 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화 SecurityConfig.java에 설정
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)

// 컨트롤러에 어노테이션 거는 법
@PostAuthorize("hasRole('ROLE_MANAGER')")
@PreAuthorize("hasRole('ROLE_MANAGER')")
@Secured("ROLE_MANAGER")
```

- 서버 내부에 security 가 관리하는 별도의 session이 존재
- security session에 들어갈 수 있는 객체의 타입은 → Authentication  객체 뿐!
- session에 존재하는 Authentication은 필요할때 컨트롤러, 서비스에서 DI가능
- Authentication 객체에 들어갈 수 있는 두가지 타입
    - UserDetails → 일반적인 로그인
    - OAuth2User → OAuth 로그인(구글, 페이스북 ..)

      →세션 객체가 필요한 컨트롤러/서비스에서 따로 구현을 해야하는것일까?  → NO, 두가지를 모두 구현한 PrincipalDetails 클래스를 이용한다

        ```java
        @Controller
        public class IndexController {
        
            @Autowired
            private UserService userService;
        
            @GetMapping("/test/login")
            public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){
        
                PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
                System.out.println(principalDetails.getUser());
                System.out.println("userDetails : " + userDetails.getUser());
                return "세션정보 확인하기";
            }
        
            @GetMapping("/test/oauth/login")
            public @ResponseBody String oauthLoginTest(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth){
        
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                System.out.println(oAuth2User.getAttributes());
                System.out.println("oAuth : " + oAuth.getAttributes());
                return "OAuth 세션정보 확인하기"; 
        }
        ```


    **PrincipalDetails**
    
    - 회원가입, 로그인시 User 객체를 사용해야하는데 UserDetails, OAuth2Details 타입은 User 오브젝트를 포함하고 있지 않다
    - PrincipalDetails에서  User 객체를 포함시키고 UserDetails, OAuth2Details를 구현하여. 복잡도를 낮추고, Authentication에서 유저 객체를 가질수 있게 한다.
    - **PrincipalDetails 타입을 리턴하기 위해서 service를 구현한다**