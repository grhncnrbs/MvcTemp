package com.dhmi.ais.service;

import com.dhmi.ais.domain.dto.RegisterRequestDto;
import com.dhmi.ais.domain.dto.ResponseDto;
import com.dhmi.ais.domain.dto.UserResponseDto;
import com.dhmi.ais.domain.entity.Role;
import com.dhmi.ais.domain.entity.User;
import com.dhmi.ais.exception.CustomException;
import com.dhmi.ais.repository.RoleRepository;
import com.dhmi.ais.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;

    public ResponseEntity<?> create(RegisterRequestDto registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail().trim())) {
            return ResponseDto.errorResponse("Bu eposta zaten kullanılıyor.");
        }
        User user = new User(registerRequest.getFirstName(),registerRequest.getLastName(), registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()));
        Set<String> strRoles = registerRequest.getRole();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            String roleName = role.trim().toUpperCase();
            Role verifiedRole = roleRepository.findByName(roleName).orElseThrow(() -> new CustomException(roleName + " rol bulunamadı."));
            roles.add(verifiedRole);
        });
        user.setRoles(roles);
        userRepository.save(user);
        LOGGER.info("Yeni kullanıcı oluşturuldu. ", readAuthenticatedUserName(), user.getEmail());
        return ResponseDto.successResponse("Kullanıcı başarıyla oluşturuldu.", null);
    }

    public String readAuthenticatedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String user = "";
        if (principal instanceof UserDetails) {
            user = ((UserDetails) principal).getUsername();
        }
        return user;
    }

    public ResponseEntity<?> readAllUsers() {
        List<UserResponseDto> users = userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
        return ResponseDto.successResponse("Users fetched successfully!", users);
    }

    public ResponseEntity<?> deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("Kullanıcı bulunamadı id: " + id));
        userRepository.delete(user);
        LOGGER.info("Silinen kullanıcı", readAuthenticatedUserName(), user.getEmail());
        return ResponseDto.successResponse("Kullanıcı başarıyla silindi.");
    }
}
