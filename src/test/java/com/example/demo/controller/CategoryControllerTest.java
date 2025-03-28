package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.category.CategoryCreateDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = {"USER", "ADMIN"})
@Sql(scripts = {
        "classpath:database/book/add-category-for-book.sql",
        "classpath:database/book/add-book-to-table.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/book/delete-book-and-category.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test get all categories")
    void getAllCategoriesTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode contentNode = objectMapper.readTree(result.getResponse().getContentAsString()).get("content");
        List<CategoryDto> categories = objectMapper.readValue(contentNode.toString(), new TypeReference<>() {});

        assertNotNull(categories);
        assertEquals(categories.size(), 1);
        assertEquals("Technology", categories.get(0).getName());
    }

    @Test
    @DisplayName("Test create category")
    @Sql(scripts = "classpath:database/category/delete-category-with-id-2.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategoryTest() throws Exception {
        CategoryCreateDto categoryDto = new CategoryCreateDto();
        categoryDto.setName("Science");
        categoryDto.setDescription("desc");
        String json = objectMapper.writeValueAsString(categoryDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(json)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actual.getId());
        assertEquals(categoryDto.getName(), actual.getName());
    }

    @Test
    @DisplayName("Test get books by category")
    void getBooksByCategoryTest() throws Exception {
        long categoryId = 1;

        MvcResult result = mockMvc.perform(get("/categories/" + categoryId + "/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode contentNode = objectMapper.readTree(result.getResponse().getContentAsString()).get("content");
        List<BookDtoWithoutCategoryIds> books = objectMapper.readValue(contentNode.toString(), new TypeReference<>() {});

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Java", books.get(0).getTitle());
    }

    @Test
    @DisplayName("Test not valid category to create")
    void validCategoryTest() throws Exception {
        CategoryCreateDto categoryWithNullField = new CategoryCreateDto();
        categoryWithNullField.setName(null);
        String json = objectMapper.writeValueAsString(categoryWithNullField);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(json)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actual = result.getResponse().getContentAsString();
        assertTrue(actual.contains("must not be blank"));
    }
}
