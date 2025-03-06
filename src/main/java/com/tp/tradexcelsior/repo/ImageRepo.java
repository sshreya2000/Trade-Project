package com.tp.tradexcelsior.repo;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class ImageRepo {

  @Autowired
  private GridFSBucket gridFSBucket;

  @Autowired
  private GridFsTemplate gridFsTemplate;


  // Save image with filename, check for duplicates, and save the content type
  public String saveImage(MultipartFile file) throws IOException {
    String fileName = file.getOriginalFilename();

    // Check if an image with the same name already exists in GridFS
    Optional<GridFSFile> existingFile = Optional.ofNullable(gridFSBucket.find(
        Filters.eq("filename", fileName)).first());

    if (existingFile.isPresent()) {
      throw new RuntimeException("Image with the same name already exists: " + fileName);
    }

    // Store the new image in GridFS
    InputStream inputStream = file.getInputStream();
    String contentType = file.getContentType(); // Get the content type dynamically
    System.out.println(contentType);
    gridFsTemplate.store(inputStream, fileName, contentType); // Store the image along with content type

    return fileName; // Return the filename or ObjectId as needed
  }

  // Retrieve image by filename
  public Optional<GridFSFile> getImageByName(String imageName) {
    return Optional.ofNullable(gridFSBucket.find(Filters.eq("filename", imageName)).first());
  }

  // Open InputStream for image file
  public InputStream openDownloadStream(ObjectId objectId) {
    return gridFSBucket.openDownloadStream(objectId);
  }

  // Delete image by ObjectId
  public void deleteImageByObjectId(ObjectId objectId) {
    gridFSBucket.delete(objectId);
  }
}