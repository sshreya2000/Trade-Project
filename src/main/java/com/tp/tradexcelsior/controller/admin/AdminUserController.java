package com.tp.tradexcelsior.controller.admin;

import com.tp.tradexcelsior.dto.request.AddUserDto;
import com.tp.tradexcelsior.dto.response.CommonResponse;
import com.tp.tradexcelsior.dto.response.PagedResponse;
import com.tp.tradexcelsior.dto.response.UserResponseDto;
import com.tp.tradexcelsior.dto.response.UsersCountWithStatus;
import com.tp.tradexcelsior.exception.ValidationException;
import com.tp.tradexcelsior.service.admin.impl.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {

  @Autowired
  private AdminUserService adminUserService;

  // Create a new user
  @Operation(summary = "Create a new user", description = "Create a new user by providing necessary details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User successfully created",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Validation failed",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @PostMapping
  public ResponseEntity<UserResponseDto> addUser(@RequestBody @Valid AddUserDto addUserDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      // Collecting validation errors
      String errorMessages = bindingResult.getAllErrors().stream()
          .map(error -> error.getDefaultMessage())
          .reduce((msg1, msg2) -> msg1 + ", " + msg2)
          .orElse("Validation failed");

      // Throw a custom exception with error details
      throw new ValidationException(errorMessages);
    }

    UserResponseDto userResponseDto = adminUserService.addUser(addUserDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
  }

  // Get a user by ID
  @Operation(summary = "Get a user by ID", description = "Fetch the details of a user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User details fetched successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getUser(@PathVariable String id) {
    UserResponseDto userResponseDto = adminUserService.getUser(id);
    return ResponseEntity.ok(userResponseDto);  // Return status 200
  }

  // Get a paginated list of users
  @Operation(summary = "Get a paginated list of users", description = "Retrieve a paginated list of users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of users fetched successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedResponse.class)))
  })
  @GetMapping
  public ResponseEntity<PagedResponse<UserResponseDto>> getUsersList(
      @RequestParam(defaultValue = "0") int page,  // Default to the first page
      @RequestParam(defaultValue = "10") int size   // Default to a page size of 10
  ) {
    PagedResponse<UserResponseDto> pagedResponse = adminUserService.getUsersList(page, size);
    return ResponseEntity.ok(pagedResponse);  // Return status 200 with paginated user list
  }

  // Update a user's information
  @Operation(summary = "Update a user's information", description = "Update the details of an existing user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User successfully updated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid data provided",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDto> updateUser(@RequestBody @Valid AddUserDto addUserDto,
      BindingResult bindingResult, @PathVariable String id) {
    if (bindingResult.hasErrors()) {
      // Collecting validation errors
      String errorMessages = bindingResult.getAllErrors().stream()
          .map(error -> error.getDefaultMessage())
          .reduce((msg1, msg2) -> msg1 + ", " + msg2)
          .orElse("Validation failed");

      // Throw a custom exception with error details
      throw new ValidationException(errorMessages);
    }

    UserResponseDto updatedUser = adminUserService.updateUser(addUserDto, id);
    return ResponseEntity.ok(updatedUser);  // Return status 200 with updated user
  }

  // Delete a user by ID
  @Operation(summary = "Delete a user by ID", description = "Delete a user from the system by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User successfully deleted"),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    adminUserService.deleteUser(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "Update user subscription", description = "Extend or modify a user's subscription")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User subscription successfully updated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid subscription duration",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @PutMapping("/updateSubscription/{userId}")
  public ResponseEntity<UserResponseDto> updateSubscription(
      @PathVariable String userId,
      @RequestParam int extendSubscriptionByYear) {

    // Call the service method
    UserResponseDto updatedUser = adminUserService.updateSubscription(userId,
        extendSubscriptionByYear);
    return ResponseEntity.ok(updatedUser);  // Return status 200 with updated user
  }

  // Search users by parameters
  @Operation(summary = "Search users by parameters", description = "Search users based on optional parameters like name, email, or mobile number")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results fetched successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedResponse.class)))
  })
  @GetMapping("/search")
  public ResponseEntity<PagedResponse<UserResponseDto>> searchUsers(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String mobileNumber,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    // Validate the page number (must not be negative)
    if (page < 0) {
      return ResponseEntity.badRequest()
          .body(new PagedResponse<>(Collections.emptyList(), 0, 0, 0, size));
    }

    // Validate the size (optional, could add more checks)
    if (size <= 0) {
      return ResponseEntity.badRequest()
          .body(new PagedResponse<>(Collections.emptyList(), 0, 0, 0, 10));
    }

    // Fetch paginated results from the service
    PagedResponse<UserResponseDto> searchResults = adminUserService.searchUsers(name, email,
        mobileNumber, page, size);

    // Return a ResponseEntity with status 200 and the paginated results
    return ResponseEntity.ok(searchResults);
  }

  @Operation(
      summary = "Get users subscription status count",
      description = "Retrieve the count of users based on their subscription status (active or inactive)"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Count of users based on their subscription status successfully fetched",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsersCountWithStatus.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @GetMapping("/status")
  public ResponseEntity<UsersCountWithStatus> getUsersStatusCount() {
    UsersCountWithStatus usersCountWithStatus = adminUserService.getUsersStatusCount();
    return ResponseEntity.ok(usersCountWithStatus);  // Return status 200 with users count by status
  }

}
