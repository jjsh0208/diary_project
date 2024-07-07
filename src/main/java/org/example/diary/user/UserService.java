package org.example.diary.user;

import jakarta.servlet.http.HttpSession;
import org.example.diary.Security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private HttpSession httpSession;

    public void save(UserRegisterDTO userDto) {
        String rawPassword = userDto.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .phone(userDto.getPhone())
                .birthDate(userDto.getBirthDate())
                .sex(userDto.getSex())
                .mbti(userDto.getMbti())
                .password(encPassword)
                .build();
        userRepository.save(user);
    }

    public User getUser(String email){
        User user = this.userRepository.findByEmail(email);
        if (user != null){
            return user;
        }
        return null;
    }

    public Optional<User> profile(){
        Long userId = SecurityUtil.getCurrentUserId();   //
        return userRepository.findById(userId);
    }

    public User UserSessionCheck(HttpSession session){
        // 세션에 저장된 사용자 정보가 없을 경우에만 가져옴
        if (session.getAttribute("currentUser") == null) {
            User user = getUser(SecurityUtil.getCurrentUsername());
            session.setAttribute("currentUser", user);
        }

        // 세션에서 사용자 정보를 가져와 모델에 추가
        return (User) session.getAttribute("currentUser");
    }

    public void update(UserUpdateDTO userUpdateDTO){
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElse(null);
        user.setName(userUpdateDTO.getName());
        user.setPhone(userUpdateDTO.getPhone());
        user.setSex(userUpdateDTO.getSex());
        user.setMbti(userUpdateDTO.getMbti());

        userRepository.save(user);
    }

    public void updatePw(UpdatePwDTO updatePwDTO){
        String rawPassword = updatePwDTO.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        String userEmail = String.valueOf(httpSession.getAttribute("email"));
        User user = userRepository.findByEmail(userEmail);
        System.out.println(user.getEmail());

        user.setPassword(encPassword);
        userRepository.save(user);
    }

    public int sendCode(String email){
        System.out.println(email);
        User user = userRepository.findByEmail(email);
        if(user == null){
            return 0;  //이메일이 존재하지 않음
        }else{
            //email api 호출 부분
            return 1;   //정상동작
        }
    }
}