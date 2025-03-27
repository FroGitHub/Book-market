package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.category.CategoryCreateDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.CategoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@WebMvcTest(CategoryController.class)
@WithMockUser(username = "test", roles = {"USER", "ADMIN"})
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllCategories_ReturnsPageOfCategories() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Technology");
        Page<CategoryDto> expectedCategories = new PageImpl<>(List.of(categoryDto));

        Mockito.when(categoryService.findAll(any(Pageable.class)))
                .thenReturn(expectedCategories);

        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode contentNode = objectMapper.readTree(result
                        .getResponse()
                        .getContentAsString())
                .get("content");
        List<CategoryDto> categories = objectMapper
                .readValue(contentNode.toString(), new TypeReference<>() {});
        Page<CategoryDto> actual = new PageImpl<>(categories);

        EqualsBuilder.reflectionEquals(expectedCategories, actual);

    }

    @Test
    void createCategory_ReturnsCreatedCategory() throws Exception {
        CategoryCreateDto request = new CategoryCreateDto();
        request.setName("Science");
        request.setDescription("Something like since or frogs");
        CategoryDto expectedCategory = new CategoryDto();
        expectedCategory.setId(2L);
        expectedCategory.setName("Science");
        Mockito.when(categoryService.saveCategory(any(CategoryCreateDto.class)))
                .thenReturn(expectedCategory);

        String json = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(json)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        EqualsBuilder.reflectionEquals(expectedCategory, actual);
    }

    @Test
    void getBooksByCategory_ReturnsPageOfBooks() throws Exception {
        BookDtoWithoutCategoryIds book = new BookDtoWithoutCategoryIds();
        book.setId(1L);
        book.setTitle("Java");
        Page<BookDtoWithoutCategoryIds> expectedBooks = new PageImpl<>(List.of(book));

        Mockito.when(categoryService.findBooksByCategory(any(Pageable.class), any(Long.class)))
                .thenReturn(expectedBooks);

        MvcResult result = mockMvc.perform(get("/categories/1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode contentNode = objectMapper.readTree(
                result.getResponse().getContentAsString()).get("content");

        List<BookDtoWithoutCategoryIds> books = objectMapper.readValue(
                contentNode.toString(), new TypeReference<>() {});
        Page<BookDtoWithoutCategoryIds> actual = new PageImpl<>(books);

        EqualsBuilder.reflectionEquals(expectedBooks, actual);
    }
}
