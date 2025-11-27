package com.telusko.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.telusko.model.Player;

public class UserPrincipal implements UserDetails {
	
	@Autowired
	private Player player;

	public UserPrincipal(Player player) {
		
		this.player=player;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return Collections.singleton(new SimpleGrantedAuthority("PLAYER"));
	}

	@Override
	public String getPassword() {
		return player.getPassword();
	}

	@Override
	public String getUsername() {
		return player.getEmail();
	}

}
