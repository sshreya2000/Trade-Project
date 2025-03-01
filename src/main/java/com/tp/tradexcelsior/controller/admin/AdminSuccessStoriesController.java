package com.tp.tradexcelsior.controller.admin;

import com.tp.tradexcelsior.dto.request.SuccessStoriesRequestDto;
import com.tp.tradexcelsior.dto.response.CommonResponse;
import com.tp.tradexcelsior.dto.response.PagedResponse;
import com.tp.tradexcelsior.dto.response.SuccessStoriesResponseDto;
import com.tp.tradexcelsior.service.admin.impl.AdminSuccessStoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/admin/success-stories")
public class AdminSuccessStoriesController {

  @Autowired
  private AdminSuccessStoriesService adminSuccessStoriesService;

  @Operation(summary = "Add a new Success Story", description = "This endpoint allows you to add a new success story.")
  @ApiResponse(responseCode = "201", description = "Success story added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessStoriesRequestDto.class)))
  @PostMapping
  public ResponseEntity<SuccessStoriesResponseDto> addSuccessStories(@RequestBody @Valid SuccessStoriesRequestDto successStoriesRequestDto) {
    SuccessStoriesResponseDto savedSuccessStoriesResponseDto = adminSuccessStoriesService.addSuccessStories(
        successStoriesRequestDto);
    return new ResponseEntity<>(savedSuccessStoriesResponseDto, HttpStatus.CREATED);
  }

  @Operation(summary = "Get a Success Story by userName", description = "Fetch a success story by the given userName.")
  @ApiResponse(responseCode = "200", description = "Success story retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessStoriesRequestDto.class)))
  @GetMapping("/{userName}")
  public ResponseEntity<SuccessStoriesResponseDto> getSuccessStoryByUserName(@PathVariable String userName) {
    SuccessStoriesResponseDto successStoriesResponseDto = adminSuccessStoriesService.getSuccessStoryByUserName(userName);
    return new ResponseEntity<>(successStoriesResponseDto, HttpStatus.OK);
  }

  @Operation(summary = "Get all Success Stories with pagination", description = "Retrieve all success stories with pagination support.")
  @ApiResponse(responseCode = "200", description = "Success stories fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedResponse.class)))
  @GetMapping
  public ResponseEntity<PagedResponse<SuccessStoriesResponseDto>> getAllSuccessStories(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {

    PagedResponse<SuccessStoriesResponseDto> pagedResponse = adminSuccessStoriesService.getAllSuccessStories(page, size);
    return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
  }

  @Operation(summary = "Update a Success Story", description = "Update an existing success story with new data.")
  @ApiResponse(responseCode = "200", description = "Success story updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessStoriesRequestDto.class)))
  @PutMapping("/{userName}")
  public ResponseEntity<SuccessStoriesResponseDto> updateSuccessStory(
      @PathVariable String userName,
      @RequestBody @Valid SuccessStoriesRequestDto successStoriesRequestDto) {

    SuccessStoriesResponseDto updatedSuccessStoryDto = adminSuccessStoriesService.updateSuccessStory(userName,
        successStoriesRequestDto);
    return new ResponseEntity<>(updatedSuccessStoryDto, HttpStatus.OK);
  }

  @Operation(summary = "Delete a Success Story", description = "Delete a success story by the given userName.")
  @ApiResponse(responseCode = "204", description = "Success story deleted successfully")
  @ApiResponse(responseCode = "500", description = "Internal server error occurred", content = @Content(mediaType = "application/json"))
  @DeleteMapping("/{userName}")
  public ResponseEntity<CommonResponse> deleteSuccessStory(@PathVariable String userName) {
    CommonResponse response = adminSuccessStoriesService.deleteSuccessStory(userName);
    if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
      return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);  // No content response after deletion
    } else {
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  // Error response
    }
  }

}