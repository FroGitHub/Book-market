package com.example.demo.repository.book;

import com.example.demo.dto.BookSearchParametersDto;
import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationBuilder;
import com.example.demo.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR_KEY = "author";
    private static final String ISBN_KEY = "isbn";
    private static final String TITLE_KEY = "title";

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> spec = Specification.where(null);
        if (searchParametersDto.author() != null) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParametersDto.author()));
        }
        if (searchParametersDto.title() != null) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParametersDto.title()));
        }
        if (searchParametersDto.isbn() != null) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(ISBN_KEY)
                    .getSpecification(searchParametersDto.isbn()));
        }
        return spec;
    }
}
