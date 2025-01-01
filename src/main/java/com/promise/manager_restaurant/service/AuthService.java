package com.promise.manager_restaurant.service;

import com.nimbusds.jose.JOSEException;
import com.promise.manager_restaurant.dto.request.auth.*;
import com.promise.manager_restaurant.dto.response.auth.AuthenticationResponse;
import com.promise.manager_restaurant.dto.response.auth.IntrospectResponse;
import com.promise.manager_restaurant.dto.response.auth.RegisterResponse;
import com.promise.manager_restaurant.dto.response.auth.VerifyEmailResponse;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface AuthService {
    public RegisterResponse register(RegisterRequest registerRequest) throws MessagingException;

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException;

    public AuthenticationResponse refreshToken(RefreshRequest refreshRequest) throws ParseException, JOSEException;

    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;

    public VerifyEmailResponse verifyEmail(VerifyEmailRequest verifyEmailRequest);
}
