package com.tp.tradexcelsior.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

@Data
public class WatchlistTrendUpdateDto {

  @NotBlank
  private String company;

  private String trend;

}
