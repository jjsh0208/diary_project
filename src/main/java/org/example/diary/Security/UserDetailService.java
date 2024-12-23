package org.example.diary.Security;

import lombok.RequiredArgsConstructor;
import org.example.diary.user.User;
import org.example.diary.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username);
        if (user != null){
            return new UserDetail(user);
        }else{
            throw new UsernameNotFoundException("User not found");
        }
    }
}
