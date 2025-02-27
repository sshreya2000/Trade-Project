package com.tp.tradexcelsior.service.user.impl;

import com.tp.tradexcelsior.dto.request.ResetPasswordDto;
import com.tp.tradexcelsior.dto.request.SetPasswordDto;
import com.tp.tradexcelsior.dto.response.CommonResponse;
import com.tp.tradexcelsior.entity.User;
import com.tp.tradexcelsior.exception.UserNotFoundException;
import com.tp.tradexcelsior.repo.UserRepo;
import com.tp.tradexcelsior.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepo userRepo;

  // Password setting logic
  public CommonResponse setPassword(SetPasswordDto setPasswordDto, String userId) {
    // Password mismatch validation
    if (!setPasswordDto.getPassword().equals(setPasswordDto.getConfirmPassword())) {
      throw new IllegalArgumentException("Password and confirm password do not match!");
    }

    // Fetch the user by userId
    User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User does not exist with id: " + userId));

    // Set the new password for the user
    user.setPassword(setPasswordDto.getPassword());
    userRepo.save(user); // Save the updated user to DB

    // Return success response
    return new CommonResponse(200, "Password updated successfully!");
  }

  public CommonResponse resetPassword(ResetPasswordDto resetPasswordDto, String userId) {
    // Validate if the old password matches the current one
    User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User does not exist with id: " + userId));

    // Compare the old password with the stored password
    if (!user.getPassword().equals(resetPasswordDto.getOldPassword())) {
      throw new IllegalArgumentException("Old password does not match!");
    }

    // Password mismatch validation (new password and confirm password)
    if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())) {
      throw new IllegalArgumentException("New password and confirm password do not match!");
    }

    // Set the new password for the user
    user.setPassword(resetPasswordDto.getNewPassword());
    userRepo.save(user); // Save the updated user to DB

    // Return success response
    return new CommonResponse(200, "Password reset successfully!");
  }

}
