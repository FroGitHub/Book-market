package com.example.demo.service;

import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.category.CategoryCreateDto;
import com.example.demo.dto.category.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto saveCategory(CategoryCreateDto categoryCreateDto);

    CategoryDto update(Long id, CategoryCreateDto categoryCreateDto);

    CategoryDto getById(Long id);

    void deleteCategory(Long id);

    Page<BookDtoWithoutCategoryIds> findBooksByCategory(Pageable pageable, Long id);
}
