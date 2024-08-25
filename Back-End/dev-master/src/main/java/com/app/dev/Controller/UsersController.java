package com.app.dev.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dev.CommonFunctions.Response;
import com.app.dev.JsonWebTokenServices.JwtService;
import com.app.dev.Model.LoginDto;
import com.app.dev.Model.RegisterDto;
import com.app.dev.Model.Users;
import com.app.dev.Model.VerifyUser;
import com.app.dev.Repository.UsersRepository;
import com.app.dev.Service.UsersAuthenticationService;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	JwtService jwtService;
	@Autowired
	UsersAuthenticationService authenticationService;
	@Autowired
	UsersRepository repository;

	@PostMapping("/register")
	public Response<Users> register(@RequestBody RegisterDto input) {
		Response<Users> response = new Response<Users>();
		
		if (input.getEmail() == null) {
			response.setCode(600);
			response.setStatus("Failed");
			response.setMessage("user not found");
			return response;
		}
		if (input.getPassword().length() < 8) {
			response.setCode(601);
			response.setStatus("Failed");
			response.setMessage("Password length must be 8 or grater");
			return response;
		}

		return authenticationService.signUp(input);
	}

	@PostMapping("/login")
	public Response<Users> authenticate(@RequestBody LoginDto input) {

		Response<Users> response = new Response<Users>();
		try {
			authenticationService.authenticate(input);
			Users user = repository.findByUserEmail(input.getuserEmail());

			if (user == null) {
				response.setCode(600);
				response.setStatus("Failed");
				response.setMessage("user not found");
				return response;
			}
			
			String jwtToken = jwtService.generateToken(user);

			response.setCode(200);
			response.setData(authenticationService.authenticate(input).getData());
			response.setMessage(jwtToken);
			response.setStatus("success");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	@PostMapping("/verify")
	public Response<Users> verify(@RequestBody VerifyUser input) {
		Response<Users> response = new Response<Users>();
		try {
			authenticationService.verifyUser(input);
			response.setCode(200);
			response.setData(authenticationService.verifyUser(input).getData());
			response.setStatus("success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/resend/{email}")
	public Response<Users> reSendVerificationCode(@PathVariable String email) {
		Response<Users> response = new Response<Users>();
		try {
			authenticationService.reSendVerificationCode(email);
			response.setCode(200);
			response.setData(authenticationService.reSendVerificationCode(email).getData());
			response.setStatus("success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
