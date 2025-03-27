package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookSearchParametersDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
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

@WebMvcTest(BookController.class)
@WithMockUser(username = "test", roles = {"USER", "ADMIN"})
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Test get book by id")
    void testGetBookById_getBookById_ReturnsBook() throws Exception {
        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setTitle("Title");
        expectedBookDto.setAuthor("Author");
        expectedBookDto.setIsbn("978-3-16-148410-0");
        expectedBookDto.setPrice(BigDecimal.valueOf(19.99));
        expectedBookDto.setDescription("Updated Description");
        expectedBookDto.setCoverImage("http://example.com/image.jpg");
        expectedBookDto.setCategoryIds(Set.of(1L, 2L));

        Mockito.when(bookService.getBookById(1L)).thenReturn(expectedBookDto);

        MvcResult result = mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        BookDto actual = objectMapper.treeToValue(jsonNode, BookDto.class);

        EqualsBuilder.reflectionEquals(expectedBookDto, actual);

    }

    @Test
    @DisplayName("Test update book")
    void updateBook_updateBookRequest_ReturnsUpdatedBook() throws Exception {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Updated Title");
        request.setAuthor("Some Author"); // Обов’язкове поле
        request.setIsbn("978-3-16-148410-0");
        request.setPrice(BigDecimal.valueOf(19.99));
        request.setDescription("Some description");
        request.setCoverImage("http://example.com/image.jpg");
        request.setCategoryIds(Set.of(1L, 2L));

        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setId(1L);
        expectedBookDto.setTitle("Updated Title");

        Mockito.when(bookService.updateBook(1L, request)).thenReturn(expectedBookDto);

        String json = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(json)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        BookDto actual = objectMapper.treeToValue(jsonNode, BookDto.class);

        Assert.assertEquals(expectedBookDto.getTitle(), actual.getTitle());
        Assert.assertEquals(expectedBookDto.getId(), actual.getId());

    }

    @Test
    @DisplayName("Test search books")
    void searchBooks_findBooksBySearch_ReturnsPageOfBooks() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Java Basics");

        Page<BookDto> expectedBooks = new PageImpl<>(List.of(bookDto));

        Mockito.when(bookService.search(any(BookSearchParametersDto.class), any(Pageable.class)))
                .thenReturn(expectedBooks);

        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("title", "Java")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode contentNode = objectMapper.readTree(
                result.getResponse().getContentAsString())
                .get("content");

        List<BookDto> books = objectMapper.readValue(
                contentNode.toString(), new TypeReference<>() {});

        Page<BookDto> actual = new PageImpl<>(books);

        EqualsBuilder.reflectionEquals(expectedBooks, actual);
    }
}
