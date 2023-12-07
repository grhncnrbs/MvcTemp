package com.dhmi.ais.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterRequestDto {

    @NotBlank(message = "Ad gerekli.")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Soyad gerekli.")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "Eposta gerekli.")
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank(message = "Parola gerekli")
    @Size(min = 6, max = 120)
    private String password;

    @NotEmpty(message = "Kullanıcı rolü gerekli.")
    private Set<String> role;
}
