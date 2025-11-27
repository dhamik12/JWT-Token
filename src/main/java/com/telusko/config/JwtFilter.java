package com.telusko.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.telusko.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//Validating Token
@Component
public class JwtFilter extends OncePerRequestFilter
{
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private ApplicationContext context;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//Header=token + Other information.
		String authHeader = request.getHeader("Authorization");
		//We will extract taken from header.
		String token=null;
		//then we will extract userName from token.
		String userName=null;
		
		if(authHeader!=null && authHeader.startsWith("Bearer ")) {
			//Don't take first 7 letters from Token.
			token=authHeader.substring(7);
			System.out.println("Token: "+token);
			userName=jwtService.extractUserName(token);
		}
		
		if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userDetails=context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);
			
			if(jwtService.validateToken(token,userDetails)) {
				
				UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken (userDetails,null,userDetails.getAuthorities());
			
				authToken.setDetails(new WebAuthenticationDetailsSource()
						.buildDetails(request));
			
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		
		filterChain.doFilter(request, response);
		
		
	}
	
	

}
