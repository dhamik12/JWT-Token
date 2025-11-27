package com.telusko.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telusko.model.Player;
import com.telusko.repo.IUserRepo;

@Service
public class UserService implements IUserService 
{
	
	@Autowired
	private IUserRepo repo;
	

	@Override
	public Player registerUser(Player user) {
		// TODO Auto-generated method stub
		return repo.save(user);
		
	}

	@Override
	public List<Player> displayUser() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	
	@Override
	public Player searchByEmail(String email) {
		
		return repo.findByEmail(email);
	}

}
