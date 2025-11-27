package com.telusko.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.telusko.model.Player;
import com.telusko.repo.IUserRepo;

@Configuration
public class MyUserDetailsService implements UserDetailsService

{
	@Autowired
	private IUserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Player playerDb = repo.findByEmail(username);
		
		if(playerDb==null) {
			
			throw new UsernameNotFoundException("User not found 404");
		}
		
		return new UserPrincipal(playerDb);
	}

}
