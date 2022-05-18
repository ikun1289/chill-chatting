package com.ttnm.chillchatting.services.user;

import com.ttnm.chillchatting.dtos.user.UserDto;
import com.ttnm.chillchatting.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User getUserByUsername(String userName);

    UserDetails loadUserByID(String userId);

    User createNewAdmin(UserDto dto);

    List<User> getAllUser();
}
