package com.example.demo.repository.book;

import com.example.demo.model.Book;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @Sql(scripts = {"classpath:database/book/add-category-for-book.sql",
            "classpath:database/book/add-book-to-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-book-and-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test find books by category id")
    public void findByCategoriesId_findOneBookByCategoryId_ReturnsOneBook() {

        Pageable pageable = PageRequest.of(0, 5);
        Long categoryId = 1L;
        int expectedSize = 5;
        int expectedNumElements = 1;

        Page<Book> actualBooks = bookRepository.findByCategoriesId(categoryId, pageable);

        Assert.assertNotNull(actualBooks);
        Assert.assertEquals(expectedSize, actualBooks.getSize());
        Assert.assertEquals(expectedNumElements, actualBooks.getTotalElements());
    }

    @Test
    @Sql(scripts = {"classpath:database/book/add-category-for-book.sql",
            "classpath:database/book/add-book-to-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-book-and-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find books with filter title")
    public void findAll_findAllWithFilter_ReturnsOneBook() {
        int expectedNumElements = 1;
        String expectedTitle = "title";
        Specification<Book> spec = (root, query, cb) -> cb.like(root.get("title"),
                "%" + expectedTitle + "%");
        Pageable pageable = PageRequest
                .of(0, 2, Sort.by("title").ascending());

        Page<Book> actualBooks = bookRepository.findAll(spec, pageable);
        String actualTitle = actualBooks.get().findAny().get().getTitle();

        Assert.assertNotNull(actualBooks);
        Assert.assertFalse(actualBooks.isEmpty());
        Assert.assertEquals(expectedNumElements, actualBooks.getTotalElements());
        Assert.assertEquals(expectedTitle, actualTitle);
    }

}
