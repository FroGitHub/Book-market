package com.example.demo.service.category;

import static org.mockito.Mockito.when;

import com.example.demo.dto.category.CategoryCreateDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import com.example.demo.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;
    private CategoryCreateDto categoryCreateDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Fiction");
        category.setDescription("Fiction books");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Fiction");
        categoryDto.setDescription("Fiction books");

        categoryCreateDto = new CategoryCreateDto();
        categoryCreateDto.setName("Fiction");
        categoryCreateDto.setDescription("Fiction books");
    }

    @Test
    void findAllTest_findAll_ReturnsPageOfCategories() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categories = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(pageable)).thenReturn(categories);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> result = categoryService.findAll(pageable);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.getTotalElements());
        Assert.assertEquals("Fiction", result.getContent().get(0).getName());
    }

    @Test
    @DisplayName("Test saving category")
    void saveCategoryTest_saveCategory_ReturnsSavedCategory() {
        when(categoryMapper.toModel(categoryCreateDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.saveCategory(categoryCreateDto);

        Assert.assertNotNull(result);
        Assert.assertEquals("Fiction", result.getName());
    }

    @Test
    @DisplayName("Test update category")
    void updateCategoryTest_updateCategory_ReturnsUpdatedCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryDto result = categoryService.update(1L, categoryCreateDto);

        Assert.assertNotNull(result);
        Assert.assertEquals("Fiction", result.getName());
    }
}
