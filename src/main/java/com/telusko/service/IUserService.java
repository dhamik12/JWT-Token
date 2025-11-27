package com.telusko.service;

import java.util.List;

import com.telusko.model.Player;

public interface IUserService {
	
	public Player registerUser(Player user);
	public List<Player> displayUser();
	public Player searchByEmail(String email);


}