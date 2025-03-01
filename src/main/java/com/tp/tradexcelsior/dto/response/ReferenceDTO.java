package com.tp.tradexcelsior.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReferenceDTO {

    private String id;

    @NotBlank(message = "Reference name cannot be empty.")
    private String name;

    @NotBlank(message = "Reference type cannot be empty.")
    private String type;

    @NotBlank(message = "Reference link cannot be empty.")
    private String link;

    @NotBlank(message = "Reference image cannot be empty.")
    private String image;
}
