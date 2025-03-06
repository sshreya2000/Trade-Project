package com.tp.tradexcelsior.dto.response;

import com.tp.tradexcelsior.entity.Book;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    @Id
    private String id;

    @NotBlank(message = "Book name cannot be empty")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Tagline cannot be empty")
    private String tagLine;

    @NotBlank(message = "Link to buy book cannot be empty")
    private String linkToBuyBook;

    @NotBlank(message = "Button name cannot be empty")
    private String buttonName;

    @NotBlank(message = "Image URL cannot be empty")
    private String imageUrl;

    public BookDTO(Book book) {
        this.id=book.getId();
        this.name = book.getName();
        this.description = book.getDescription();
        this.tagLine = book.getTagLine();
        this.linkToBuyBook = book.getLinkToBuyBook();
        this.buttonName = book.getButtonName();
        this.imageUrl = book.getImageUrl();
    }
}