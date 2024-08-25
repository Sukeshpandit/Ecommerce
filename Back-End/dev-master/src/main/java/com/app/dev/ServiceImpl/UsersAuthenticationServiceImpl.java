package com.app.dev.ServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dev.CommonFunctions.Response;
import com.app.dev.EmailService.EmailSender;
import com.app.dev.Model.LoginDto;
import com.app.dev.Model.RegisterDto;
import com.app.dev.Model.Users;
import com.app.dev.Model.VerifyUser;
import com.app.dev.Repository.UsersRepository;
import com.app.dev.Service.UsersAuthenticationService;

@Service
public class UsersAuthenticationServiceImpl implements UsersAuthenticationService {
	@Autowired
	UsersRepository repository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	EmailSender emailSender;

	@Override
	public Response<Users> signUp(RegisterDto input) {
		Response<Users> response = new Response<Users>();
		try {

			Users users = repository.findByUserEmail(input.getEmail());
			if (users != null) {
				response.setCode(600);
				response.setStatus("Failed");
				response.setMessage("User already present please try logging in / Verifying the Account");
				return response;
			}

			Users user = new Users(input.getEmail(), input.getUsername(), input.getPassword());
			user.setEnabled(false);
			user.setVerificationCode(generateVerificationCode());
			user.setVerificationexpire(LocalDateTime.now().plusMinutes(3));

			user = repository.save(user);
			sendVerificationCode(user);

			response.setCode(200);
			response.setStatus("ok");
			response.setMessage("success");
			response.setData(user);

			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setCode(500);
			response.setStatus("Failed");
			response.setMessage("Internal server error");
		}
		return response;
	}

	@Override
	public Response<Users> authenticate(LoginDto input) {
		Response<Users> response = new Response<Users>();
		try {
			Users user = repository.findByUserEmail(input.getuserEmail());

			if (user == null) {
				response.setCode(600);
				response.setStatus("Failed");
				response.setMessage("User not found");
				return response;
			}

			if (!user.isEnabled()) {
				response.setCode(601);
				response.setStatus("Failed");
				response.setMessage("Account not verified , Please verify");
				return response;
			}
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(input.getuserEmail(), input.getPassword()));
			response.setCode(200);
			response.setStatus("ok");
			response.setMessage("success");
			response.setData(user);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response.setCode(500);
			response.setStatus("Failed");
			response.setMessage("Internal server error");
		}

		return response;
	}

	@Override
	public Response<Users> verifyUser(VerifyUser input) {
		Response<Users> response = new Response<>();
		try {
			Users user = repository.findByUserEmail(input.getEmail());

			if (user == null) {
				response.setCode(602); // Assuming 602 for user not found
				response.setStatus("Failed");
				response.setMessage("User not found");
				return response;
			}

			// Check if the verification code is null or doesn't match the input code
			else if (user.getVerificationCode() == null
					|| !user.getVerificationCode().equals(input.getVerificationcode())) {
//	            logger.debug("Verification code invalid or null");"
				response.setCode(601);
				response.setStatus("Failed");
				response.setMessage("Invalid verification code");
				return response;
			}

			// Check if the verification code has expired
			else if (user.getVerificationexpire().isBefore(LocalDateTime.now())) {
//	            logger.debug("Verification code expired");
				response.setCode(600);
				response.setStatus("Failed");
				response.setMessage("Verification code expired");
				return response;
			}

			else {
				user.setEnabled(true);
				user.setVerificationCode(null);
				user.setVerificationexpire(null);
				user = repository.save(user);

				response.setCode(200);
				response.setStatus("ok");
				response.setMessage("success");
				response.setData(user);
				return response;
			}

			// Enable user and clear verification fields

		} catch (Exception e) {
//	        logger.error("Error during user verification", e);
			response.setCode(500);
			response.setStatus("Failed");
			response.setMessage("Internal server error");
		}
		return response;
	}

	@Override
	public Response<Users> reSendVerificationCode(String email) {
		Response<Users> response = new Response<Users>();
		try {
			Users user = repository.findByUserEmail(email);

			if (user != null) {
				if (user.isEnabled()) {
					response.setCode(600);
					response.setStatus("Failed");
					response.setMessage("Account verified");
					return response;
				}
			}
			user.setVerificationCode(generateVerificationCode());

			System.out.println(user.getVerificationCode());
			user.setVerificationexpire(LocalDateTime.now().plusMinutes(3));
			sendVerificationCode(user);

			user = repository.save(user);

			response.setCode(200);
			response.setStatus("ok");
			response.setMessage("success");
			response.setData(user);
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setCode(500);
			response.setStatus("Failed");
			response.setMessage("Internal server error");
		}
		return response;
	}

	public void sendVerificationCode(Users user) {
		String to = user.getUserEmail();
		String subject = "Account verification code";
		String verficationCode = user.getVerificationCode();
		String mimeMessage = "Your verification code is" + " " + ":" + " " + verficationCode;
		try {
			emailSender.sendVerificationEmail(to, mimeMessage, subject);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public String generateVerificationCode() {
		Random random = new Random();
		int code = random.nextInt(900000) + 100000;
		return String.valueOf(code);
	}

}
