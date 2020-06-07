package com.rahel.lxblog.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.service.BlogUserService;
import com.rahel.lxblog.config.jwt.JwtProvider;

@RestController
public class AuthController {
	
	@Autowired
	private BlogUserService blogUserService;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private HttpServletRequest request;
	
	@PostMapping("/register")
    public String registerUser(@RequestBody RegistrationRequest registrationRequest){
		BlogUser blogUser = new BlogUser(registrationRequest);
		System.out.println(blogUser);
		blogUserService.save(blogUser);
		return "Successfully registered";
	}
	
	@PostMapping("/auth")
	public @ResponseBody AuthResponse auth(@RequestBody AuthRequest authRequest) {
		BlogUser user =blogUserService.findByEmailAndPassword(authRequest.getEmail(),authRequest.getPassword());
		if (user==null) {
			//return new AuthResponse(""); what shall I do here?????????????? 
			return null;
		}
	    String token = jwtProvider.generateToken(user.getEmail());
	    return new AuthResponse(token);
	}

	  @GetMapping("/mypage")
	  public String getMyPage(){
		  String token = request.getHeader("Authorization");
		  String myemail = jwtProvider.getEmailFromToken(token);
	      return "hi from my page "+ myemail  ;
	  }
}
