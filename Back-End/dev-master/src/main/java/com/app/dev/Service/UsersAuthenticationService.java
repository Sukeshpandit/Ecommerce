package com.app.dev.Service;

import com.app.dev.CommonFunctions.Response;
import com.app.dev.Model.LoginDto;
import com.app.dev.Model.RegisterDto;
import com.app.dev.Model.Users;
import com.app.dev.Model.VerifyUser;

public interface UsersAuthenticationService {
	Response<Users> signUp(RegisterDto input);
	Response<Users> authenticate(LoginDto input);
	Response<Users> verifyUser(VerifyUser input);
	Response<Users> reSendVerificationCode(String email);
}
