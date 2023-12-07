package com.dhmi.ais.security.service;

import com.dhmi.ais.domain.dto.LoginRequestDto;
import com.dhmi.ais.domain.dto.LoginResponseDto;
import com.dhmi.ais.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<LoginResponseDto> authenticateLoginRequest(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(LoginResponseDto.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .roles(roles)
                .accessToken(jwtToken)
                .build());
    }
}
