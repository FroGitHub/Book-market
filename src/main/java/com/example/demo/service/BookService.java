package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookSearchParametersDto;
import com.example.demo.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto createBookRequestDto);

    void deleteBook(Long id);

    List<BookDto> search(BookSearchParametersDto searchParametersDto);
}
