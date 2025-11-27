package com.telusko.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telusko.model.Player;
import com.telusko.service.IUserService;
import com.telusko.service.JWTService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class FirstController {
	
	@Autowired
	private IUserService service;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	BCryptPasswordEncoder bcrypt=new BCryptPasswordEncoder(12);
	
	@GetMapping("/info")
	public ResponseEntity<List<Player>> getUserInfo()
	{
		List<Player> userList = service.displayUser();
		return new ResponseEntity<List<Player>>(userList,HttpStatus.OK);
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<Player> registration(@RequestBody Player player)
	{
		String encodedPassword = bcrypt.encode(player.getPassword());
		player.setPassword(encodedPassword);
		Player userdb=service.registerUser(player);
		return new ResponseEntity<Player>(userdb,HttpStatus.OK);
	}
	
	@GetMapping("/fetchViaEmail")
	public ResponseEntity<Player> fetchUserByEmail(@RequestParam String email)
	{
		Player userdb=service.searchByEmail(email);
		return new ResponseEntity<Player>(userdb,HttpStatus.OK);
	}
	
	
	@GetMapping("/msg")
	public ResponseEntity<String> getinfo()
	{
		String msg="I am Soumik Dhara";
		return new ResponseEntity<String>(msg,HttpStatus.OK);
	}
	
	
	
	@GetMapping("/csrf")
	public  CsrfToken csrfToken(HttpServletRequest request)
	{
		return (CsrfToken)request.getAttribute("_csrf");
	}
	
	
	
	
	//to varify user name and password in spring we have "userpasswordauthenticationToken". 
	//it well verify & return token.
	@PostMapping("/login")
	public String login(@RequestBody Player player) {
		
		Authentication authentication=authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(player.getEmail(),player.getPassword()));
		
				if(authentication.isAuthenticated())
					return jwtService.generateToken(player.getEmail());
				else
				return "login failed";
		
		
	}
	
}
