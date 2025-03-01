package com.tp.tradexcelsior.service.admin;

import com.tp.tradexcelsior.dto.request.SuccessStoriesRequestDto;
import com.tp.tradexcelsior.dto.response.CommonResponse;
import com.tp.tradexcelsior.dto.response.PagedResponse;
import com.tp.tradexcelsior.dto.response.SuccessStoriesResponseDto;

public interface IAdminSuccessStoriesService {
  SuccessStoriesResponseDto addSuccessStories(SuccessStoriesRequestDto successStoriesRequestDto);
  SuccessStoriesResponseDto getSuccessStoryByUserName(String userName);
  PagedResponse<SuccessStoriesResponseDto> getAllSuccessStories(int page, int size);
  SuccessStoriesResponseDto updateSuccessStory(String userName, SuccessStoriesRequestDto successStoriesRequestDto);
  CommonResponse deleteSuccessStory(String userName);
}
