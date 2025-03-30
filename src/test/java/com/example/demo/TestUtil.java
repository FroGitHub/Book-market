package com.example.demo;

import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.dto.category.CategoryCreateDto;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import java.math.BigDecimal;
import java.util.Set;

public class TestUtil {
    private static final CreateBookRequestDto CREATE_BOOK_REQUEST_DTO
            = new CreateBookRequestDto();
    private static final CategoryCreateDto CATEGORY_CREATE_DTO
            = new CategoryCreateDto();
    private static final Category CATEGORY = new Category();
    private static final Book BOOK = new Book();

    public static CategoryCreateDto getCategoryCreateDto() {
        CATEGORY_CREATE_DTO.setName("Science");
        CATEGORY_CREATE_DTO.setDescription("Something like science or frogs");
        return CATEGORY_CREATE_DTO;
    }

    public static CreateBookRequestDto getRequestToUpdateBook() {
        CREATE_BOOK_REQUEST_DTO.setTitle("Updated Title");
        CREATE_BOOK_REQUEST_DTO.setAuthor("Updated Author");
        CREATE_BOOK_REQUEST_DTO.setIsbn("978-3-16-148410-0");
        CREATE_BOOK_REQUEST_DTO.setPrice(BigDecimal.valueOf(25.99));
        CREATE_BOOK_REQUEST_DTO.setDescription("Updated Description");
        CREATE_BOOK_REQUEST_DTO.setCoverImage("http://example.com/new-image.jpg");
        CREATE_BOOK_REQUEST_DTO.setCategoryIds(Set.of(1L));
        return CREATE_BOOK_REQUEST_DTO;
    }

    public static Category getCategory() {
        CATEGORY.setId(1L);
        CATEGORY.setName("Technology");
        CATEGORY.setDescription(
                "Books of this category like interesting, I thing like...");
        return CATEGORY;
    }

    public static Book getBook() {
        BOOK.setId(1L);
        BOOK.setTitle("Java");
        BOOK.setAuthor("author");
        BOOK.setIsbn("978-92-95055-02-5");
        BOOK.setPrice(BigDecimal.valueOf(10.00));
        BOOK.setDescription("desc");
        BOOK.setCoverImage("path");
        BOOK.setCategories(Set.of(getCategory()));
        return BOOK;
    }
}
