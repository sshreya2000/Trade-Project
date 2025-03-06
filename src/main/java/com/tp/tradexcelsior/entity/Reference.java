package com.tp.tradexcelsior.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "references")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reference {

    @Id
    private String id;
    private String name;
    private String type;
    private String link;
    private String image;
}