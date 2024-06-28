package org.example.diary.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(UserRegisterDTO userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPhone(userDto.getPhone());
        user.setBirthDate(userDto.getBirthDate());
        user.setSex(userDto.getSex());
        user.setMbti(userDto.getMbti());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
    }

    public ResponseEntity<String> login(UserLoginDTO userLoginDto) {
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
