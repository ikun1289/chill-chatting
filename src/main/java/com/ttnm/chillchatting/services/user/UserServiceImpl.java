package com.ttnm.chillchatting.services.user;

import com.ttnm.chillchatting.entities.User;
import com.ttnm.chillchatting.exceptions.NotFoundException;
import com.ttnm.chillchatting.repositories.UserRepository;
import com.ttnm.chillchatting.utils.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameCore(userName).orElse(null);
        if (ObjectUtils.isEmpty(user)) {
            System.out.println("User not found!");
            throw new NotFoundException(String.format("User có username: %s không tồn tại", userName));
        }
        return new CustomUserDetails(user);
    }

    @Override
    public UserDetails loadUserByID(String userId) {
        User user = userRepository.findByIdCore(userId).orElse(null);
        if (ObjectUtils.isEmpty(user)) {
            System.out.println("User not found!");
            throw new NotFoundException(String.format("User có id: %s không tồn tại", userId));
        }
        return new CustomUserDetails(user);
    }
}
