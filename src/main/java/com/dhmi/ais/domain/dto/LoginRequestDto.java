package com.dhmi.ais.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "Kullanıcı adı gerekli.")
    private String email;
    @NotBlank(message = "Parola gerekli.")
    private String password;
}
