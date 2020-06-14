package com.rahel.lxblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rahel.lxblog.dto.AuthRequest;
import com.rahel.lxblog.dto.EmailForm;
import com.rahel.lxblog.dto.RegistrationRequest;
import com.rahel.lxblog.dto.ResetPasswordForm;
import com.rahel.lxblog.entity.BlogUser;
import com.rahel.lxblog.jwt.JwtProvider;
import com.rahel.lxblog.service.BlogUserService;

@RestController
public class AuthController {

	@Autowired
	private BlogUserService blogUserService;

	@Autowired
	private JwtProvider jwtProvider;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {

		String exceptionMsg = blogUserService.save(registrationRequest);
		if (exceptionMsg != null) {
			return ResponseEntity.badRequest().body(exceptionMsg);
		}
		return ResponseEntity.ok().body("confirmation e-mail is sent");
	}

	@PostMapping("/auth")
	public ResponseEntity<String> auth(@RequestBody AuthRequest authRequest) {

		BlogUser user = blogUserService.findByEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
		if (user == null) {
			return ResponseEntity.badRequest().body("username or password is incorrect");
		}
		if (user.getIs_active() == 0) {
			return ResponseEntity.badRequest().body("user is not activated");
		}
		String token = jwtProvider.generateToken(user.getEmail(), user.getId());
		return ResponseEntity.ok().body("Bearer " + token);
	}

	@GetMapping("/auth/confirm/{code}")
	public ResponseEntity<String> activate(@PathVariable String code) {
		String response = blogUserService.activateUser(code);
		if (response == null) {
			return ResponseEntity.ok().body("Your account is active now");
		} else {
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/auth/forgot_password")
	public ResponseEntity<String> dropPassword(@RequestBody EmailForm emailForm) {
		String message = blogUserService.dropPassword(emailForm.getEmail());
		return ResponseEntity.ok().body(message);
	}

	@PostMapping("/auth/reset")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordForm prForm) {
		String response = blogUserService.resetPassword(prForm);
		if (response == null) {
			return ResponseEntity.ok().body("E-mail with activation code is sent");
		} else {
			return ResponseEntity.badRequest().body(response);
		}
	}

}
