package com.tp.tradexcelsior.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDto {
  @NotBlank(message = "Old password cannot be empty")
  @Size(min = 8, message = "Old password must be at least 8 characters long")
  private String oldPassword;

  @NotBlank(message = "New password cannot be empty")
  @Size(min = 8, message = "New password must be at least 8 characters long")
  private String newPassword;

  @NotBlank(message = "Confirm password cannot be empty")
  @Size(min = 8, message = "Confirm password must be at least 8 characters long")
  private String confirmPassword;
}
