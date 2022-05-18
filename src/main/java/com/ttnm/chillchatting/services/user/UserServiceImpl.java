package com.ttnm.chillchatting.services.user;

import com.ttnm.chillchatting.dtos.user.UserDto;
import com.ttnm.chillchatting.entities.User;
import com.ttnm.chillchatting.exceptions.InvalidException;
import com.ttnm.chillchatting.exceptions.NotFoundException;
import com.ttnm.chillchatting.repositories.UserRepository;
import com.ttnm.chillchatting.utils.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;


@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByUsername(String userName){
        return userRepository.findByUserName(userName).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameCore(userName).orElse(null);
        if (ObjectUtils.isEmpty(user)) {
            System.out.println("User not found!");
            throw new NotFoundException(String.format("User có username: %s not found", userName));
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

    @Override
    public User createNewAdmin(UserDto dto){
        User user = getUserByUsername(dto.getUserName());
        if(!ObjectUtils.isEmpty(user))
            throw new InvalidException("Username existed");
        if(ObjectUtils.isEmpty(dto.getUserName()))
           throw new InvalidException("Username cant not be empty");
        if(ObjectUtils.isEmpty(dto.getPassword()))
            dto.setPassword("123456");
        if(dto.getPassword().length()<6 || dto.getPassword().length()>20)
            throw new InvalidException("Password must be 6 to 20 character");
        if(ObjectUtils.isEmpty(dto.getName()))
            throw new InvalidException("Name cant not be empty");

        User newAdmin = new User();
        newAdmin.setUserName(dto.getUserName());
        newAdmin.setPassword(passwordEncoder.encode(dto.getPassword()));
        newAdmin.setName(dto.getName());
        return userRepository.save(newAdmin);
    }

    @Override
    public List<User> getAllUser(){
        return userRepository.findAll();
    }
}
