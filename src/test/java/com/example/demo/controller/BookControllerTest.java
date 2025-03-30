package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.TestUtil;
import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = {"USER", "ADMIN"})
@Sql(scripts = {
        "classpath:database/book/add-category-for-book.sql",
        "classpath:database/book/add-book-to-table.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/book/delete-book-and-category.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test get book by id")
    void testGetBookById() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertBook(actual,
                1L,
                "Java", "author",
                "978-92-95055-02-5",
                BigDecimal.valueOf(10.00),
                "desc", "path");
    }

    @Test
    @DisplayName("Test update book")
    void updateBook() throws Exception {
        String json = objectMapper.writeValueAsString(TestUtil.getRequestToUpdateBook());

        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(json)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertBook(actual,
                1L,
                "Updated Title",
                "Updated Author",
                "978-3-16-148410-0",
                BigDecimal.valueOf(25.99),
                "Updated Description",
                "http://example.com/new-image.jpg");
    }

    @Test
    @DisplayName("Test search books")
    void searchBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("title", "Java")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode contentNode = objectMapper.readTree(
                result.getResponse().getContentAsString()).get("content");
        List<BookDto> books = objectMapper.readValue(
                contentNode.toString(), new TypeReference<>() {});

        assertFalse(books.isEmpty());
        assertBook(books.get(0), 1L,
                "Java", "author",
                "978-92-95055-02-5",
                BigDecimal.valueOf(10.00),
                "desc", "path");
    }

    @Test
    @DisplayName("Test invalid book ISBN")
    void validBookTest() throws Exception {
        CreateBookRequestDto invalidBook = TestUtil.getRequestToUpdateBook();
        invalidBook.setIsbn("WrongIsbn");
        String json = objectMapper.writeValueAsString(invalidBook);

        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(json)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("invalid ISBN"));
    }

    private void assertBook(BookDto actualBook,
                            Long id,
                            String title,
                            String author,
                            String isbn,
                            BigDecimal price,
                            String description,
                            String coverImage) {
        assertNotNull(actualBook);
        assertEquals(id, actualBook.getId());
        assertEquals(title, actualBook.getTitle());
        assertEquals(author, actualBook.getAuthor());
        assertEquals(isbn, actualBook.getIsbn());
        assertTrue(price.compareTo(actualBook.getPrice()) == 0);
        assertEquals(description, actualBook.getDescription());
        assertEquals(coverImage, actualBook.getCoverImage());
    }
}
