package com.example.demo.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookSearchParametersDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.book.BookSpecificationBuilder;
import com.example.demo.service.impl.BookServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Test get book by id")
    void getBookById_ShouldReturnBookDto() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Java");

        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setTitle("Java");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);

        BookDto actualBookDto = bookService.getBookById(1L);

        assertEquals(expectedBookDto, actualBookDto);
        verify(bookRepository).findById(1L);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Test update book")
    public void updateBookTest_UpdateBook_ReturnsUpdatedBookDto() {

        Book bookFromRepository = new Book();
        bookFromRepository.setId(1L);
        bookFromRepository.setTitle("Java");

        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("Updated Java");

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Updated Java");

        BookDto expected = new BookDto();
        expected.setTitle("Updated Java");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookFromRepository));
        when(bookRepository.save(bookFromRepository)).thenReturn(updatedBook);

        BookDto actual = bookService.updateBook(1L, createBookRequestDto);

        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("Test searching books by title")
    void searchBooks_ShouldReturnFilteredBooks() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Java");
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        Specification<Book> mockSpec = mock(Specification.class);
        when(bookSpecificationBuilder.build(any(BookSearchParametersDto.class)))
                .thenReturn(mockSpec);
        when(bookRepository.findAll(mockSpec, Pageable.unpaged()))
                .thenReturn(bookPage);

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Java");
        Page<BookDto> expectedPage = new PageImpl<>(List.of(bookDto));

        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookSearchParametersDto searchDto = new BookSearchParametersDto("Java", null, null);
        Page<BookDto> actualPage = bookService.search(searchDto, Pageable.unpaged());

        assertEquals(expectedPage.getContent(), actualPage.getContent());
        verify(bookSpecificationBuilder).build(any(BookSearchParametersDto.class));
        verify(bookRepository).findAll(mockSpec, Pageable.unpaged());
        verify(bookMapper).toDto(book);
    }
}
