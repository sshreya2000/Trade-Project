package com.tp.tradexcelsior.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistDTO {
    private String id;

    @NotBlank(message = "Description should not be blank")
    private String description;
    @NotBlank(message = "Link should not be blank")
    private String link;
    @NotBlank(message = "ButtonName should not be blank")
    private String buttonName;
}
