package com.example.demo.service.impl;

import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.category.CategoryCreateDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.category.CategoryRepository;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto saveCategory(CategoryCreateDto categoryCreateDto) {
        return categoryMapper.toDto(
                categoryRepository.save(
                        categoryMapper.toModel(categoryCreateDto)
                )
        );
    }

    @Override
    public CategoryDto update(Long id, CategoryCreateDto categoryCreateDto) {
        Category existedCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category is not found with id: " + id));

        categoryMapper.updateCategory(categoryCreateDto, existedCategory);

        return categoryMapper.toDto(categoryRepository.save(existedCategory));
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category is not found with id: " + id)
                ));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> findBooksByCategory(Pageable pageable, Long id) {
        return bookRepository.findByCategoriesId(id, pageable)
                .map(bookMapper::toDtoWithoutCategoryIds);
    }

}
