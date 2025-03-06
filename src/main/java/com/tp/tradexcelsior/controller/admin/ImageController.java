package com.tp.tradexcelsior.controller.admin;

import com.tp.tradexcelsior.dto.response.ImageDto;
import com.tp.tradexcelsior.service.admin.impl.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {

  @Autowired
  private ImageService imageService;

  // Endpoint to upload the image and return its name
  @Operation(
      summary = "Upload an image",
      description = "Upload an image file to the server and return the image filename."
  )
  @ApiResponse(
      responseCode = "200",
      description = "Image uploaded successfully",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
  )
  @ApiResponse(
      responseCode = "500",
      description = "Error uploading image",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
  )
  @PostMapping("/upload")
  public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
    try {
      String fileName = imageService.uploadImage(file);
      return ResponseEntity.ok("Image uploaded successfully with name: " + fileName);
    } catch (IOException e) {
      return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
    }
  }

  // Endpoint to retrieve the image by filename
  @Operation(
      summary = "Get an image by filename",
      description = "Retrieve the image by its filename."
  )
  @ApiResponse(
      responseCode = "200",
      description = "Image retrieved successfully",
      content = @Content(mediaType = "image/jpeg")
  )
  @ApiResponse(
      responseCode = "404",
      description = "Image not found",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
  )
  @GetMapping("/{imageName}")
  public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
    byte[] imageBytes = imageService.getImageByName(imageName);
    String contentType = imageService.getImageContentType(imageName);
    return ResponseEntity.ok()
        .header("Content-Type", contentType) // Assuming JPEG; you can set dynamically
        .header("Content-Length", String.valueOf(imageBytes.length))
        .body(imageBytes); // Return the image as byte array in the response body
  }

  @Operation(
      summary = "Delete an image by filename",
      description = "Delete the image by its filename from the server."
  )
  @ApiResponse(
      responseCode = "204",
      description = "Image deleted successfully"
  )
  @ApiResponse(
      responseCode = "404",
      description = "Image not found",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
  )
  @DeleteMapping("/{imageName}")
  public ResponseEntity<String> deleteImage(@PathVariable String imageName) {
    try {
      imageService.deleteImageByName(imageName); // No content response will be triggered here
      return ResponseEntity.noContent().build(); // Return 204 No Content response
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).body("Image not found with name: " + imageName);
    }
  }

  // Endpoint to retrieve all images
  @Operation(summary = "Get all images", description = "Fetch a list of all images stored in the system with their filenames and content types.")
  @ApiResponse(responseCode = "200", description = "List of images retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageDto.class)))
  @GetMapping
  public ResponseEntity<List<ImageDto>> getAllImages() {
    List<ImageDto> imageDtos = imageService.getAllImages();
    return ResponseEntity.ok(imageDtos);
  }
}
