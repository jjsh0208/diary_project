package org.example.diary.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(UserRegisterDTO userDto) {
        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .phone(userDto.getPhone())
                .birthDate(userDto.getBirthDate())
                .sex(userDto.getSex())
                .mbti(userDto.getMbti())
                .password(userDto.getPassword())
                .build();
        userRepository.save(user);
    }

    public ResponseEntity<String> login(UserLoginDTO userLoginDto) {
        String os = System.getProperty("os.name").toLowerCase();
        User user = userRepository.findByEmail(userLoginDto.getEmail());
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else{
            if(user.getPassword().equals(userLoginDto.getPassword())){
                return ResponseEntity.ok("sucess");
            }else{
                return ResponseEntity.badRequest().build();
            }
        }
    }
}