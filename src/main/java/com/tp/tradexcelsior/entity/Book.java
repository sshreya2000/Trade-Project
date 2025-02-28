package com.tp.tradexcelsior.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	@Id
	private String id;
	private String tagLine;
	@Indexed(unique = true)
	private String name;
	private String description;
	private String linkToBuyBook;
	private String buttonName;
	private String imageUrl;
	private boolean isDeleted = false;

}