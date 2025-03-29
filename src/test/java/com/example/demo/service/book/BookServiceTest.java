package com.example.demo.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.example.demo.TestUtil;
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
    @DisplayName("Get book by id")
    void getBookById_ShouldReturnBookDto() {
        // Given
        Book book = TestUtil.getBook();
        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setTitle(book.getTitle());

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);

        // When
        BookDto actualBookDto = bookService.getBookById(book.getId());

        // Then
        assertEquals(expectedBookDto, actualBookDto);
        verify(bookRepository).findById(book.getId());
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Update book")
    void updateBook_ShouldReturnUpdatedBookDto() {
        // Given
        Book book = TestUtil.getBook();
        CreateBookRequestDto requestDto = TestUtil.getRequestToUpdateBook();
        Book updatedBook = TestUtil.getBook();
        updatedBook.setTitle(requestDto.getTitle());
        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setTitle(requestDto.getTitle());

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expectedBookDto);

        // When
        BookDto actualBookDto = bookService.updateBook(book.getId(), requestDto);

        // Then
        assertEquals(expectedBookDto, actualBookDto);
        verify(bookRepository).findById(book.getId());
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(updatedBook);
    }

    @Test
    @DisplayName("Search books by title")
    void searchBooks_ShouldReturnFilteredBooks() {
        // Big given
        Book book = TestUtil.getBook();
        BookDto bookDto = new BookDto();
        bookDto.setTitle(book.getTitle());
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        Page<BookDto> expectedPage = new PageImpl<>(List.of(bookDto));

        Specification<Book> spec = mock(Specification.class);
        when(bookSpecificationBuilder.build(any(BookSearchParametersDto.class))).thenReturn(spec);
        when(bookRepository.findAll(spec, Pageable.unpaged())).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookSearchParametersDto searchDto
                = new BookSearchParametersDto("Java", null, null);

        // When
        Page<BookDto> actualPage = bookService.search(searchDto, Pageable.unpaged());

        // Then
        assertEquals(expectedPage.getContent(), actualPage.getContent());
        verify(bookSpecificationBuilder).build(any(BookSearchParametersDto.class));
        verify(bookRepository).findAll(spec, Pageable.unpaged());
        verify(bookMapper).toDto(book);
    }
}
