package com.tp.tradexcelsior.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "checklists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Checklist {
    @Id
    private String id;
    private String description;
    private String link;
    private String buttonName;
}