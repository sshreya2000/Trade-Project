package com.tp.tradexcelsior.controller.user;

import com.tp.tradexcelsior.dto.request.ResetPasswordDto;
import com.tp.tradexcelsior.dto.request.SetPasswordDto;
import com.tp.tradexcelsior.dto.response.CommonResponse;
import com.tp.tradexcelsior.service.user.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Operation(summary = "Set the password for a user", description = "Allows a user to set a new password for their account.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Password successfully set",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid password or other validation errors",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @PutMapping("/set-password/{userId}")
  public ResponseEntity<CommonResponse> setPassword(
      @RequestBody @Valid SetPasswordDto setPasswordDto, @PathVariable String userId) {
    CommonResponse commonResponse = userService.setPassword(setPasswordDto, userId);
    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
  }

  @Operation(summary = "Reset the password for a user", description = "Allows a user to reset their password by verifying the old password and setting a new one.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Password successfully reset",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid password or other validation errors",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
      @ApiResponse(responseCode = "403", description = "Old password is incorrect",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @PutMapping("/reset-password/{userId}")
  public ResponseEntity<CommonResponse> resetPassword(
      @RequestBody @Valid ResetPasswordDto resetPasswordDto, @PathVariable String userId) {

    // Service layer handling password reset logic
    CommonResponse commonResponse = userService.resetPassword(resetPasswordDto, userId);

    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
  }

}