package com.ttnm.chillchatting.services.user;

import com.ttnm.chillchatting.dtos.user.NewPassDto;
import com.ttnm.chillchatting.dtos.user.UserDto;
import com.ttnm.chillchatting.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends UserDetailsService {

    User getUserByUsername(String userName);

    UserDetails loadUserByID(String userId);

    User getUserById(String id);

    User createNewAdmin(UserDto dto);

    List<User> getAllUser();

    User changePass(NewPassDto newPassDto, HttpServletRequest request);
}
