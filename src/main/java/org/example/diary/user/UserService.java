package org.example.diary.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.diary.Security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void save(UserRegisterDTO userDto) {
        String rawPassword = userDto.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        Date birthDate = null;
        try {
            birthDate = dateFormat.parse(userDto.getBirthDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .phone(userDto.getPhone())
                .birthDate(birthDate)
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

    public User UserSessionCheck(HttpSession session){
        // 세션에 저장된 사용자 정보가 없을 경우에만 가져옴
        if (session.getAttribute("currentUser") == null) {
            User user = getUser(SecurityUtil.getCurrentUsername());
            session.setAttribute("currentUser", user);
        }

        // 세션에서 사용자 정보를 가져와 모델에 추가
        return (User) session.getAttribute("currentUser");
    }


}