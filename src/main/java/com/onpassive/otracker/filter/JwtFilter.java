package com.onpassive.otracker.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.onpassive.otracker.serviceimpl.CustomUserDetailsService;
import com.onpassive.otracker.util.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer")) {
			token=authorizationHeader.substring(7);
			userName=jwtUtil.extractUserName(token);
		}
		if(userName!=null  && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails=customUserDetailsService.loadUserByUsername(userName);	

		if(jwtUtil.validateToken(token, userDetails)) {
			
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);	
		}
		}
		filterChain.doFilter(request, response);
	}

}
