package org.example.diary.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(UserRegisterDTO userDto) {
        System.out.println(userDto.getName());
        System.out.println(userDto.getSex());
        System.out.println(userDto.getMbti());
        System.out.println(userDto.getPassword());
        System.out.println(userDto.getPhone());

        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .phone(userDto.getPhone())
                .birthDate(null)
                .sex(userDto.getSex())
                .mbti(userDto.getMbti())
                .password(userDto.getPassword())
                .build();
        userRepository.save(user);
    }

    public void login(UserLoginDTO userLoginDto) {
        System.out.println("test");
        User user = userRepository.findByEmail(userLoginDto.getEmail());
        if (user == null) {
            System.out.println("아이디없음");
        } else{
            if(user.getPassword().equals(userLoginDto.getPassword())){
                System.out.println("성공");
            }else{
                System.out.println("실패");
            }
        }
    }
}