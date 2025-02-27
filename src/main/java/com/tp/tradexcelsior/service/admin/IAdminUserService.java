package com.tp.tradexcelsior.service.admin;


import com.tp.tradexcelsior.dto.request.AddUserDto;
import com.tp.tradexcelsior.dto.response.PagedResponse;
import com.tp.tradexcelsior.dto.response.UserResponseDto;
import com.tp.tradexcelsior.dto.response.UsersCountWithStatus;

public interface IAdminUserService {
  UserResponseDto addUser(AddUserDto addUserDto);
  UserResponseDto getUser(String id);
  PagedResponse<UserResponseDto> getUsersList(int page, int size);
  UserResponseDto updateUser(AddUserDto addUserDto, String id);
  boolean deleteUser(String id);
  PagedResponse<UserResponseDto> searchUsers(String name, String email, String mobileNumber, int page, int size);
  UserResponseDto updateSubscription(String userId, int extendSubscriptionByYear);

  UsersCountWithStatus getUsersStatusCount();
}
