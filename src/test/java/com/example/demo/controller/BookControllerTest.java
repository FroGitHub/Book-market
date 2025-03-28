package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.ObjectUtil;
import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
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
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test get book by id")
    void testGetBookById() throws Exception {
        long bookId = 1;

        MvcResult result = mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        BookDto actual = objectMapper.treeToValue(jsonNode, BookDto.class);
        assertNotNull(actual);
        assertEquals("Java", actual.getTitle());
    }

    @Test
    @DisplayName("Test update book")
    void updateBook() throws Exception {
        String json = objectMapper.writeValueAsString(ObjectUtil.getRequestToUpdateBook());
        long bookId = 1;

        MvcResult result = mockMvc.perform(put("/books/" + bookId)
                        .content(json)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        BookDto actual = objectMapper.treeToValue(jsonNode, BookDto.class);
        assertEquals(ObjectUtil.getRequestToUpdateBook().getTitle(), actual.getTitle());
    }

    @Test
    @DisplayName("Test search books")
    void searchBooks() throws Exception {
        String title = "Java";

        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("title", title)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode contentNode = objectMapper.readTree(result.getResponse().getContentAsString()).get("content");
        List<BookDto> books = objectMapper.readValue(contentNode.toString(), new TypeReference<>() {});

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals(title, books.get(0).getTitle());
    }

    @Test
    @DisplayName("Test valid books")
    void validBookTest() throws Exception {
        CreateBookRequestDto bookDtoWithNotValidIsbn = ObjectUtil.getRequestToUpdateBook();
        bookDtoWithNotValidIsbn.setIsbn("WrongIsbn");
        String json = objectMapper.writeValueAsString(bookDtoWithNotValidIsbn);

        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(json)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actual = result.getResponse().getContentAsString();
        assertTrue(actual.contains("invalid ISBN"));
    }
}
