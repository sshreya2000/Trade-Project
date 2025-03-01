package com.tp.tradexcelsior.repo;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class SuccessStoriesImageRepo {

  @Autowired
  private GridFSBucket gridFSBucket;

  // Save image and return ObjectId as String
  public String saveImage(InputStream inputStream, String fileName, String contentType) throws IOException {
    ObjectId fileId = gridFSBucket.uploadFromStream(fileName, inputStream);
    return fileId.toString();
  }

  // Retrieve image by ObjectId and return InputStream
  public Optional<GridFSFile> getImage(String imageId) {
    ObjectId objectId = new ObjectId(imageId);
    GridFSFile gridFSFile = gridFSBucket.find(Filters.eq("_id", objectId)).first();
    return Optional.ofNullable(gridFSFile);
  }

  // Open InputStream to read the image content
  public InputStream openDownloadStream(ObjectId objectId) {
    return gridFSBucket.openDownloadStream(objectId);
  }
}