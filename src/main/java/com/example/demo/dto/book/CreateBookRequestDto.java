package com.example.demo.dto.book;

import com.example.demo.validation.Path;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotNull
    @Size(max = 25, message = "Title must be up to 25 characters")
    private String title;
    @NotNull
    @Size(max = 25, message = "Author name must be up to 25 characters")
    private String author;
    @NotNull
    @ISBN
    private String isbn;
    @NotNull
    @Positive
    private BigDecimal price;
    private String description;
    @Path
    private String coverImage;
}

