package com.ttnm.chillchatting.services.user;

import com.ttnm.chillchatting.dtos.user.UserDto;
import com.ttnm.chillchatting.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User getUserByUsername(String userName);

    UserDetails loadUserByID(String userId);

    User createNewAdmin(UserDto dto);
}
