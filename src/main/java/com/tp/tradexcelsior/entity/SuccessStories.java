package com.tp.tradexcelsior.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessStories {

  @Id
  private String id;

  @Indexed(unique = true)
  private String userName;

  private String feedback;
  private String videoUrl;
  private String tagline;

}
