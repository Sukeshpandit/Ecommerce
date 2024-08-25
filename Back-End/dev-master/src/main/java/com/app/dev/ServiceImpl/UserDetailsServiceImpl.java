package com.app.dev.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.dev.Model.Users;
import com.app.dev.Repository.UsersRepository;

@Service

public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UsersRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		System.out.println(userEmail);
		Users user = usersRepository.findByUserEmail(userEmail);
		System.out.println(userEmail);
		if (user!=null) {
			return new Users(user.getUserEmail(), user.getUsername(), user.getPassword());
		} else {
			throw new UsernameNotFoundException(userEmail	 + "Not Found");
		}
	}

}
