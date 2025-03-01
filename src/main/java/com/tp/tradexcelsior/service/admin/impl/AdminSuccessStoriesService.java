package com.tp.tradexcelsior.service.admin.impl;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.tp.tradexcelsior.dto.request.SuccessStoriesRequestDto;
import com.tp.tradexcelsior.dto.response.CommonResponse;
import com.tp.tradexcelsior.dto.response.PagedResponse;
import com.tp.tradexcelsior.dto.response.SuccessStoriesResponseDto;
import com.tp.tradexcelsior.entity.SuccessStories;
import com.tp.tradexcelsior.exception.SuccessStoryAlreadyExistsException;
import com.tp.tradexcelsior.exception.SuccessStoryNotFoundException;
import com.tp.tradexcelsior.repo.SuccessStoriesImageRepo;
import com.tp.tradexcelsior.repo.SuccessStoriesRepo;
import com.tp.tradexcelsior.service.admin.IAdminSuccessStoriesService;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class AdminSuccessStoriesService implements IAdminSuccessStoriesService {

  @Autowired
  private SuccessStoriesRepo successStoriesRepo;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private SuccessStoriesImageRepo imageRepo;

  @Override
  @Transactional
  public SuccessStoriesResponseDto addSuccessStories(SuccessStoriesRequestDto successStoriesRequestDto) {
    // Check if a success story with the same userName already exists
    successStoriesRepo.findByUserName(successStoriesRequestDto.getUserName())
        .ifPresent(existingStory -> {
          throw new SuccessStoryAlreadyExistsException("A success story with this user name already exists.");
        });

    // Map the DTO to the SuccessStories entity
    SuccessStories successStories = modelMapper.map(successStoriesRequestDto, SuccessStories.class);

    try {
      // Save the SuccessStories entity
      SuccessStories savedSuccessStories = successStoriesRepo.save(successStories);

      // Log the successful save and return the response DTO
      log.info("New success story added successfully for user: {}", savedSuccessStories.getUserName());
      return modelMapper.map(savedSuccessStories, SuccessStoriesResponseDto.class);
    } catch (DataIntegrityViolationException ex) {
      // Handle cases like unique constraint violations, invalid data, etc.
      throw new RuntimeException("Failed to save success story. Invalid data or duplicate entry.", ex);
    } catch (Exception ex) {
      // General error handling for any unexpected exceptions
      throw new RuntimeException("An unexpected error occurred while adding success story", ex);
    }
  }

  @Override
  public SuccessStoriesResponseDto getSuccessStoryByUserName(String userName) {
    SuccessStories successStories = successStoriesRepo.findByUserName(userName)
        .orElseThrow(() -> new SuccessStoryNotFoundException("Success story with user name " + userName + " not found."));

    return modelMapper.map(successStories, SuccessStoriesResponseDto.class);
  }

  @Override
  public PagedResponse<SuccessStoriesResponseDto> getAllSuccessStories(int page, int size) {
    // Count the total number of success stories
    long totalItems = successStoriesRepo.count();
    int totalPages = (int) Math.ceil((double) totalItems / size);

    // Handle the case where the requested page exceeds available pages
    if (page >= totalPages && totalPages > 0) {
      log.warn("Requested page {} exceeds available pages. Returning empty response.", page);
      return new PagedResponse<>(List.of(), (int) totalItems, totalPages, page, size);
    }

    // Default size handling
    if (size < 1) {
      size = 10;
    } else if (size > 100) {
      size = 100;
    }

    // Create a Pageable object with the given page and size
    Pageable pageable = PageRequest.of(page, size);

    // Fetch the paginated data
    Page<SuccessStories> successStoriesPage = successStoriesRepo.findAll(pageable);

    // Map the entities to DTOs
    List<SuccessStoriesResponseDto> successStoriesResponseDtoList = successStoriesPage.getContent().stream()
        .map(successStory -> modelMapper.map(successStory, SuccessStoriesResponseDto.class))
        .toList();

    // Log the info
    log.info("Fetched {} success stories, page {} of {}.", successStoriesResponseDtoList.size(), page, totalPages);

    // Return the paginated response
    return new PagedResponse<>(successStoriesResponseDtoList, (int) totalItems, totalPages, page, size);
  }

  @Override
  @Transactional
  public SuccessStoriesResponseDto updateSuccessStory(String userName, SuccessStoriesRequestDto successStoriesRequestDto) {
    // Fetch the existing success story
    SuccessStories existingSuccessStory = successStoriesRepo.findByUserName(userName)
        .orElseThrow(() -> new SuccessStoryNotFoundException("Success story with user name " + userName + " not found."));

    // Check if the userName is being modified (it should not be)
    if (!existingSuccessStory.getUserName().equals(successStoriesRequestDto.getUserName())) {
      String errorMessage = "userName: User name cannot be changed; ";
      errorMessage += "userName: User name must be between 1 and 100 characters.";

      // Throw a custom validation exception or handle the error as per your requirements
      throw new IllegalArgumentException(errorMessage);
    }

    // Update the rest of the fields
    existingSuccessStory.setFeedback(successStoriesRequestDto.getFeedback());
    existingSuccessStory.setVideoUrl(successStoriesRequestDto.getVideoUrl());
    existingSuccessStory.setTagline(successStoriesRequestDto.getTagline());

    // Save the updated success story
    SuccessStories updatedSuccessStory = successStoriesRepo.save(existingSuccessStory);

    // Log the success and return the updated SuccessStoriesDto
    log.info("Success story updated successfully for user: {}", updatedSuccessStory.getUserName());
    return modelMapper.map(updatedSuccessStory, SuccessStoriesResponseDto.class);
  }


  @Override
  @Transactional
  public CommonResponse deleteSuccessStory(String userName) {
    // Fetch the SuccessStory entity by userName
    SuccessStories successStories = successStoriesRepo.findByUserName(userName)
        .orElseThrow(() -> new SuccessStoryNotFoundException("Success story with user name " + userName + " not found."));

    try {
      // Delete the success story from the repository
      successStoriesRepo.delete(successStories);
      log.info("Success story deleted successfully for user: {}", userName);

      // Return a "No Content" response with HTTP status 204
      return new CommonResponse(HttpStatus.NO_CONTENT.value(), "Success story deleted successfully."); // Status 204 and no message
    } catch (Exception ex) {
      // Log error if deletion fails for any reason
      log.error("Error occurred while deleting success story for user: {}", userName, ex);

      // Return a failure response with HTTP status 500 INTERNAL SERVER ERROR
      return new CommonResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete success story. Please try again later.");
    }
  }

  // Upload the image and return ObjectId as String
  public String uploadImage(MultipartFile file) throws IOException {
    try (InputStream inputStream = file.getInputStream()) {
      return imageRepo.saveImage(inputStream, file.getOriginalFilename(), file.getContentType());
    }
  }

  // Retrieve the image by ObjectId and return the byte array content
  public byte[] getImage(String imageId) throws IOException {
    return imageRepo.getImage(imageId)
        .map(gridFSFile -> {
          try (InputStream inputStream = imageRepo.openDownloadStream(gridFSFile.getObjectId())) {
            return inputStream.readAllBytes();
          } catch (IOException e) {
            throw new RuntimeException("Error reading image from InputStream", e);
          }
        })
        .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));
  }
}