package com.rahel.lxblog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.model.EmailForm;
import com.rahel.lxblog.model.ResetPasswordForm;
import com.rahel.lxblog.entity.ActivationCode;
import com.rahel.lxblog.service.BlogUserService;
//import com.rahel.lxblog.service.UserCodeService;
import com.rahel.lxblog.config.jwt.JwtProvider;
import com.rahel.lxblog.dao.ActivationCodeDao;

@RestController
public class AuthController {
	
	@Autowired
	private BlogUserService blogUserService;
	
	@Autowired
	private ActivationCodeDao ucDao;
//	private UserCodeService userCodeService;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private HttpServletRequest request;
	
	@PostMapping("/register")
    public String registerUser(@RequestBody RegistrationRequest registrationRequest){
	//	BlogUser blogUser = new BlogUser(registrationRequest);
	//	System.out.println("Sending email to " + blogUser.getEmail());
		if(blogUserService.save(registrationRequest)) {
		return "confirmation e-mail is sent";
		}
		else return "user with such e-mail is alredy exist";
	}
	
	@PostMapping("/auth")
	public @ResponseBody AuthResponse auth(@RequestBody AuthRequest authRequest) {
		BlogUser user =blogUserService.findByEmailAndPassword(authRequest.getEmail(),authRequest.getPassword());
		if (user==null) {
			//return new AuthResponse(""); what shall I do here?????????????? 
			System.out.println("user is not found");
			return null;
		}
		if (user.getIs_active()==0) {
			//return new AuthResponse(""); what shall I do here?????????????? 
			System.out.println("user is not activated");
			return null;
		}
	    String token = jwtProvider.generateToken(user.getEmail());
	    return new AuthResponse(token);
	}

	@GetMapping("/auth/confirm/{code}")
	public String activate(@PathVariable String code) {
		return blogUserService.activateUser(code);
	//	if(isActivated) {return "user is active now";}
	//	else return "not activated";
	}
	
	@PostMapping("/auth/forgot_password")
	public String dropPassword(@RequestBody EmailForm emailForm) {
		System.out.println(emailForm);
		System.out.println(emailForm.getEmail());
		return blogUserService.dropPassword(emailForm.getEmail());
	//	if(isActivated) {return "user is active now";}
	//	else return "not activated";
	}
	
	@PostMapping("/auth/reset")
	public String resetPassword(@RequestBody ResetPasswordForm prForm) {
		return blogUserService.resetPassword(prForm);
	}
	
	  @GetMapping("/mypage")
	  public ActivationCode getMyPage(@RequestBody ActivationCode userCode){
		  List<ActivationCode> list =(List<ActivationCode>) ucDao.findAll();
		  for( ActivationCode ac : list) {
			  System.out.println(ac);
		  };
		  System.out.println(list.size());
	//	  System.out.println(ucDao.findById(userCode.getUser_Id()).get());

	//	  System.out.println(ucDao.findByActivation_code(userCode.getActivation_code()));
		  
	      return null;//ucDao.findById(userCode.getUser_Id()).get() ;
	  }
	  
}
