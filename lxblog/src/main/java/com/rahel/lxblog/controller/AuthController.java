package com.rahel.lxblog.controller;

//import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rahel.lxblog.entity.BlogUser;
//import com.rahel.lxblog.jwt.JwtAuthenticationException;
import com.rahel.lxblog.jwt.JwtProvider;
import com.rahel.lxblog.model.EmailForm;
import com.rahel.lxblog.model.ResetPasswordForm;
//import com.rahel.lxblog.entity.ActivationCode;
//import com.rahel.lxblog.service.ActivationCodeService;
import com.rahel.lxblog.service.BlogUserService;
import com.sun.mail.iap.Response;
//import com.rahel.lxblog.dao.ActivationCodeDao;

@RestController
public class AuthController {
	
	@Autowired
	private BlogUserService blogUserService;
	
//	@Autowired
//	private ActivationCodeService acService;   //where is my service?
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private HttpServletRequest request;
	
	@PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest){
	//	BlogUser blogUser = new BlogUser(registrationRequest);
	//	System.out.println("Sending email to " + blogUser.getEmail());
		String exceptionMsg = blogUserService.save(registrationRequest);
		if(exceptionMsg!=null) {
			throw new BadCredentialsException(exceptionMsg);
		}
		return ResponseEntity.ok().body("confirmation e-mail is sent") ;
	}
	
	@PostMapping("/auth")
	public @ResponseBody AuthResponse auth(@RequestBody AuthRequest authRequest) {
	//	try {
		BlogUser user =blogUserService.findByEmailAndPassword(authRequest.getEmail(),authRequest.getPassword());
		if (user==null) {
			throw new UsernameNotFoundException("user is not found");
		}	
		if (user.getIs_active()==0) {
			//return new AuthResponse(""); what shall I do here?????????????? 
			throw new BadCredentialsException("user is not activated");
		}
	    String token = jwtProvider.generateToken(user.getEmail(), user.getId());
	    return new AuthResponse(token);
//	}
//		catch (AuthenticationException e) {
			//throw new BadCredentialsException("Invalid username or password");
	//		System.out.println("Invalid username or password");
		//	return null;
	//	}
	}

	@GetMapping("/auth/confirm/{code}")
	public ResponseEntity<String> activate(@PathVariable String code) {
		if(blogUserService.activateUser(code)) {
			return ResponseEntity.ok().body("Your account is active now");
		}
		else return null;
	}
	
	@PostMapping("/auth/forgot_password")
	public ResponseEntity<String> dropPassword(@RequestBody EmailForm emailForm) {
	//	System.out.println(emailForm);
	//	System.out.println(emailForm.getEmail());
	String message =blogUserService.dropPassword(emailForm.getEmail());
	return ResponseEntity.ok().body(message) ;
	//	if(isActivated) {return "user is active now";}
	//	else return "not activated";
	}
	
	@PostMapping("/auth/reset")
	public String resetPassword(@RequestBody ResetPasswordForm prForm) {
		return blogUserService.resetPassword(prForm);
	}
	
/*	  @GetMapping("/mypage")
	  public ActivationCode getMyPage(@RequestBody ActivationCode userCode){
		  List<ActivationCode> list =(List<ActivationCode>) acService.findAll();
		  for( ActivationCode ac : list) {
			  System.out.println(ac);
		  };
		  System.out.println(list.size());
	//	  System.out.println(ucDao.findById(userCode.getUser_Id()).get());

	//	  System.out.println(ucDao.findByActivation_code(userCode.getActivation_code()));
		  
	      return null;//ucDao.findById(userCode.getUser_Id()).get() ;
	  }*/
	  
}
