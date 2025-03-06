package com.tp.tradexcelsior.service.admin.impl;

import com.tp.tradexcelsior.criteria.UserSearchCriteriaBuilder;
import com.tp.tradexcelsior.dto.request.AddUserDto;
import com.tp.tradexcelsior.dto.response.PagedResponse;
import com.tp.tradexcelsior.dto.response.UserResponseDto;
import com.tp.tradexcelsior.dto.response.UsersCountWithStatus;
import com.tp.tradexcelsior.entity.User;
import com.tp.tradexcelsior.exception.UserAlreadyExistsException;
import com.tp.tradexcelsior.exception.ValidationException;
import com.tp.tradexcelsior.repo.UserRepo;
import com.tp.tradexcelsior.service.EmailService;
import com.tp.tradexcelsior.service.admin.IAdminUserService;
import com.tp.tradexcelsior.exception.UserNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class AdminUserService implements IAdminUserService {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private EmailService emailService;

  @Override
  @Transactional
  public UserResponseDto addUser(AddUserDto addUserDto) {
    LocalDate subscriptionStartDate = LocalDate.now();
    LocalDate subscriptionEndDate = subscriptionStartDate
        .plusYears(addUserDto.getSubscriptionDuration())
        .minusDays(1);

    // Check if user with the same email exists
    userRepo.findByEmail(addUserDto.getEmail())
        .ifPresent(existingUser -> {
          throw new UserAlreadyExistsException("Email is already in use.");
        });

    // Check if user with the same mobile number exists
    userRepo.findByMobileNumber(addUserDto.getMobileNumber())
        .ifPresent(existingUser -> {
          throw new UserAlreadyExistsException("Mobile number is already in use.");
        });

    User user = modelMapper.map(addUserDto, User.class);
    user.setSubscriptionStartDate(subscriptionStartDate);
    user.setSubscriptionEndDate(subscriptionEndDate);

    try {
      User savedUser = userRepo.save(user);
      // Send email asynchronously and handle success/failure
      CompletableFuture<Boolean> emailResult = emailService.sendEmail(user.getEmail());

      // You can handle the result here or log if the email failed
      emailResult.thenAccept(success -> {
        if (!success) {
          // Handle email failure (e.g., log, retry, notify admin, etc.)
          log.error("Failed to send email to user: {}", user.getEmail());
        }

        if(success){
          log.info("Sent activation email");
        }
      });
      return modelMapper.map(savedUser, UserResponseDto.class);
    } catch (DataIntegrityViolationException ex) {
      log.error("Error saving user: {}", ex.getMessage());
      throw new RuntimeException("User already exists or invalid data", ex);
    }
  }


  @Override
  public UserResponseDto getUser(String id) {
    User user = userRepo.findById(id).orElseThrow(() -> {
      log.error("User not found with id: {}", id);
      return new UserNotFoundException("No user present with the id : " + id);
    });
    return modelMapper.map(user, UserResponseDto.class);
  }


  @Override
  public PagedResponse<UserResponseDto> getUsersList(int page, int size) {

    // If the page number is negative, throw an exception
    if (page < 0) {
      throw new IllegalArgumentException("Page number cannot be negative");
    }

    // If the page number is negative, throw an exception
    if (size < 1) {
      throw new IllegalArgumentException("Page size can't be less than 1");
    }

    // Calculate the total number of users
    long totalItems = userRepo.count();  // Get total count of users

    // Calculate total pages
    int totalPages = (int) Math.ceil((double) totalItems / size);

    // If the requested page exceeds the available total pages, return the last page
    if (page >= totalPages && totalPages > 0) {
      return new PagedResponse<>(Collections.emptyList(), (int) totalItems, totalPages, page, size);
    }

    // Create a Pageable object for pagination with the corrected page number
    Pageable pageable = PageRequest.of(page, size);

    // Fetch the paginated results from the repository
    Page<User> usersPage = userRepo.findAll(pageable);

    // Convert the list of users to UserResponseDto
    List<UserResponseDto> userResponseDto = usersPage.getContent().stream()
        .map(user -> modelMapper.map(user, UserResponseDto.class))
        .collect(Collectors.toList());

    // Return a PagedResponse object containing the results, pagination information, and the total count
    return new PagedResponse<>(userResponseDto, (int) totalItems, totalPages, page, size);
  }


  @Transactional
  public UserResponseDto updateUser(AddUserDto addUserDto, String id) {
    // Fetch the existing user from MongoDB
    User existingUser = userRepo.findById(id).orElseThrow(() -> {
      log.error("User not found with id: {}", id);
      return new UserNotFoundException("No user with this user id: " + id);
    });

    // Check if another user exists with the same email (but not the current user)
    Optional<User> userWithEmail = userRepo.findByEmail(addUserDto.getEmail());
    if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(id)) {
      throw new UserAlreadyExistsException("A user with this email already exists");
    }

    // Check if another user exists with the same phone number (but not the current user)
    Optional<User> userWithPhoneNumber = userRepo.findByMobileNumber(addUserDto.getMobileNumber());
    if (userWithPhoneNumber.isPresent() && !userWithPhoneNumber.get().getId().equals(id)) {
      throw new UserAlreadyExistsException("A user with this phone number already exists");
    }

    // Create an update object
    Update update = new Update();

    // Add the fields to be updated only if they're not null or empty
    if (addUserDto.getFirstName() != null && !addUserDto.getFirstName().isEmpty()) {
      update.set("firstName", addUserDto.getFirstName());
    }
    if (addUserDto.getLastName() != null && !addUserDto.getLastName().isEmpty()) {
      update.set("lastName", addUserDto.getLastName());
    }
    if (addUserDto.getMobileNumber() != null && !addUserDto.getMobileNumber().isEmpty()) {
      update.set("mobileNumber", addUserDto.getMobileNumber());
    }
    if (addUserDto.getAddress() != null && !addUserDto.getAddress().isEmpty()) {
      update.set("address", addUserDto.getAddress());
    }
    if (addUserDto.getOccupation() != null && !addUserDto.getOccupation().isEmpty()) {
      update.set("occupation", addUserDto.getOccupation());
    }
    if (addUserDto.getPassword() != null && !addUserDto.getPassword().isEmpty()) {
      update.set("password", addUserDto.getPassword());
    }

    // Perform the update
    mongoTemplate.updateFirst(
        Query.query(Criteria.where("_id").is(id)),  // Find by the user ID
        update,  // Update object
        User.class  // Entity class
    );

    // Fetch and return the updated user
    User updatedUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    return modelMapper.map(updatedUser, UserResponseDto.class);
  }


  @Override
  @Transactional
  public boolean deleteUser(String id) {
    User user = userRepo.findById(id).orElseThrow(() -> {
      log.error("User not found with id: {}", id);
      return new UserNotFoundException("User does not exist with id : " + id);
    });
    userRepo.delete(user);  // Delete the user directly
    return !userRepo.existsById(id);  // Returns true if the user is successfully deleted
  }


  @Override
  public PagedResponse<UserResponseDto> searchUsers(String name, String email, String mobileNumber, int page, int size) {

    // Build the query using the helper class
    Query query = new Query();
    query.addCriteria(UserSearchCriteriaBuilder.buildSearchCriteria(name, email, mobileNumber));

    // Calculate the total number of matching records based on search criteria
    long totalItems = mongoTemplate.count(query, User.class); // This counts the documents based on the search query

    // Calculate total pages
    int totalPages = (int) Math.ceil((double) totalItems / size);

    // If totalPages is 0, set the page to 0
    if (totalPages == 0) {
      page = 0;
    }

    // If the requested page exceeds the available total pages, return the last page
    if (page >= totalPages && totalPages > 0) {
      page = totalPages - 1; // Adjust the page to the last available page
    }

    // Create a Pageable object for pagination with the corrected page number
    Pageable pageable = PageRequest.of(page, size);

    // Apply pagination and fetch the results based on the query and pageable
    query.with(pageable);

    // Fetch the paginated results from the repository
    List<User> users = mongoTemplate.find(query, User.class);

    // Convert the list of users to UserResponseDto
    List<UserResponseDto> userResponseDto = users.stream()
        .map(user -> modelMapper.map(user, UserResponseDto.class))
        .collect(Collectors.toList());

    // Return a PagedResponse object containing the results, pagination information, and the total count
    return new PagedResponse<>(userResponseDto, (int) totalItems, totalPages, page, size);
  }


  @Override
  @Transactional
  public UserResponseDto updateSubscription(String userId, int extendSubscriptionByYear) {
    // Edge case: Ensure the subscription duration is valid (greater than zero)
    if (extendSubscriptionByYear <= 0) {
      throw new ValidationException("Subscription duration must be a positive number.");
    }

    try {
      // Step 1: Fetch the user from the database by userId
      User user = userRepo.findById(userId).orElseThrow(() -> {
        throw new UserNotFoundException("User with ID " + userId + " not found");
      });

      // Step 2: Update only the subscription fields
      LocalDate currentDate = LocalDate.now();
      user.setSubscriptionDuration(user.getSubscriptionDuration() + extendSubscriptionByYear); // Add years to existing duration
      user.setSubscriptionStartDate(currentDate);
      user.setSubscriptionEndDate(currentDate.plusYears(user.getSubscriptionDuration()));

      // Step 3: Save the updated user back to the database
      User updatedUser = userRepo.save(user);

      // Return the updated user details in the response format
      return modelMapper.map(updatedUser, UserResponseDto.class);

    } catch (UserNotFoundException ex) {
      // Propagate the user not found exception
      throw ex;
    } catch (Exception ex) {
      // Catch any other exceptions, such as database issues
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ex);
    }
  }

  @Override
  public UsersCountWithStatus getUsersStatusCount() {
    LocalDate currentDate = LocalDate.now();

    long totalUsers = userRepo.count();
    long activeUsers = userRepo.countBySubscriptionEndDateAfter(currentDate);
    long inactiveUsers = totalUsers - activeUsers;

    return new UsersCountWithStatus(totalUsers,  activeUsers, inactiveUsers);
  }

}
