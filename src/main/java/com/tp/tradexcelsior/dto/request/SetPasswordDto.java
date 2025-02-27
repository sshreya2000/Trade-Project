package com.tp.tradexcelsior.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SetPasswordDto {

  @NotBlank(message = "Password cannot be empty")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  private String password;

  @NotBlank(message = "Confirm password cannot be empty")
  @Size(min = 8, message = "Confirm password must be at least 8 characters long")
  private String confirmPassword;
}
