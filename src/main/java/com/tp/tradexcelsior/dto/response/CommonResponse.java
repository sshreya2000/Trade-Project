package com.tp.tradexcelsior.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponse {
  private int status;
  private String message;
}
