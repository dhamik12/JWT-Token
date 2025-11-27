package com.telusko.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telusko.model.Player;

public interface IUserRepo extends JpaRepository<Player,Integer>
{

	public Player findByEmail(String email);
}
