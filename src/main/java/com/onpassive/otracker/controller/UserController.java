package com.onpassive.otracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onpassive.otracker.dto.AuthRequest;
import com.onpassive.otracker.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class UserController {
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	  @PostMapping("/signin")
	  public String signUp(@RequestBody AuthRequest authRequest) throws Exception {
		  try {
		  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		  }
		  catch (Exception e) {
			throw new Exception("invalid username/password");
		}
		  return jwtUtil.generateToken(authRequest.getEmail());
	  
	  }
	 
	
}
