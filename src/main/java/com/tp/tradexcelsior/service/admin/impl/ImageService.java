package com.tp.tradexcelsior.service.admin.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Filters;
import com.tp.tradexcelsior.dto.response.ImageDto;
import com.tp.tradexcelsior.repo.ImageRepo;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

  @Autowired
  private ImageRepo imageRepository;

  @Autowired
  private GridFSBucket gridFSBucket;

//  // Upload the image and return the filename
//  public String uploadImage(MultipartFile file) throws IOException {
//    return imageRepository.saveImage(file);
//  }

  private static final Map<String, String> EXTENSION_TO_MIME_TYPE = new HashMap<>();

  static {
    EXTENSION_TO_MIME_TYPE.put("jpg", "image/jpeg");
    EXTENSION_TO_MIME_TYPE.put("jpeg", "image/jpeg");
    EXTENSION_TO_MIME_TYPE.put("png", "image/png");
    EXTENSION_TO_MIME_TYPE.put("gif", "image/gif");
    EXTENSION_TO_MIME_TYPE.put("bmp", "image/bmp");
    EXTENSION_TO_MIME_TYPE.put("tiff", "image/tiff");
    EXTENSION_TO_MIME_TYPE.put("webp", "image/webp");
  }

  // Method to upload image with content type based on file extension
  public String uploadImage(MultipartFile file) throws IOException {
    String fileName = file.getOriginalFilename();
    String fileExtension = getFileExtension(fileName);
    String contentType = EXTENSION_TO_MIME_TYPE.getOrDefault(fileExtension, "application/octet-stream"); // Default to binary if unknown

    // Prepare upload options with content type
    GridFSUploadOptions options = new GridFSUploadOptions()
        .metadata(new Document("contentType", contentType));

    try (InputStream inputStream = file.getInputStream()) {
      gridFSBucket.uploadFromStream(fileName, inputStream, options);
    }

    return fileName;
  }

  // Helper method to get file extension
  private String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
      return fileName.substring(dotIndex + 1).toLowerCase();
    }
    return ""; // No extension found
  }

  // Retrieve the image by filename and return it as a byte array
  public byte[] getImageByName(String imageName) throws IOException {
    Optional<GridFSFile> imageFileOpt = imageRepository.getImageByName(imageName);

    return imageFileOpt
        .map(gridFSFile -> {
          try (InputStream inputStream = imageRepository.openDownloadStream(gridFSFile.getObjectId())) {
            return inputStream.readAllBytes();
          } catch (IOException e) {
            throw new RuntimeException("Error reading image from InputStream", e);
          }
        })
        .orElseThrow(() -> new RuntimeException("Image not found with name: " + imageName));
  }


  // Method to retrieve content type dynamically from the image metadata
  public String getImageContentType(String imageName) {
    // Fetch the image metadata to get the content type
    Optional<GridFSFile> imageFile = Optional.ofNullable(gridFSBucket.find(
        Filters.eq("filename", imageName)).first());

    if (imageFile.isEmpty()) {
      throw new RuntimeException("Image not found with name: " + imageName);
    }

    // Retrieve the content type from the metadata (if available)
    GridFSFile gridFSFile = imageFile.get();
    Document metadata = gridFSFile.getMetadata();
    String contentType = (metadata != null && metadata.containsKey("contentType"))
        ? metadata.getString("contentType")
        : "application/octet-stream";  // Default content type if not found

    return contentType;
  }

  // Delete an image by its filename
  public void deleteImageByName(String imageName) {
    // Check if the image exists
    Optional<GridFSFile> imageFileOpt = imageRepository.getImageByName(imageName);

    if (imageFileOpt.isEmpty()) {
      throw new RuntimeException("Image with the name '" + imageName + "' does not exist.");
    }

    // Image exists, delete it
    GridFSFile gridFSFile = imageFileOpt.get();
    imageRepository.deleteImageByObjectId(gridFSFile.getObjectId());
  }

  public List<ImageDto> getAllImages() {
    return gridFSBucket.find(Filters.empty())
        .map(gridFSFile -> {
          // Get metadata safely, ensuring it's not null
          Document metadata = gridFSFile.getMetadata();
          // Check if metadata is null or doesn't contain "contentType" key
          String contentType = (metadata != null && metadata.containsKey("contentType"))
              ? metadata.getString("contentType")
              : "application/octet-stream";  // Default content type

          // Return new ImageDto with filename and content type
          return new ImageDto(gridFSFile.getFilename(), contentType);
        })
        .into(new java.util.ArrayList<>());
  }


}