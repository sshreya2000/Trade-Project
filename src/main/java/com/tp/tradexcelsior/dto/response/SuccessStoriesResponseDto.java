package com.tp.tradexcelsior.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SuccessStoriesResponseDto {

  private String id;
  private String userName;
  private String feedback;
  private String videoUrl;
  private String tagline;
}
