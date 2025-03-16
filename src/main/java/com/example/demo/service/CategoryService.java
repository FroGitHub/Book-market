package com.example.demo.service;

import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.category.CategoryCreateDto;
import com.example.demo.dto.category.CategoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto saveCategory(CategoryCreateDto categoryCreateDto);

    CategoryDto update(Long id, CategoryCreateDto categoryCreateDto);

    CategoryDto getById(Long id);

    void deleteCategory(Long id);

    List<BookDtoWithoutCategoryIds> findBooksByCategory(Pageable pageable, Long id);
}
