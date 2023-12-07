package com.dhmi.ais.service;

import com.dhmi.ais.domain.dto.RegisterRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> create(RegisterRequestDto registerRequestDto);
    ResponseEntity<?> deleteUser(Long id);
    ResponseEntity<?> readAllUsers();
    String readAuthenticatedUserName();
}
