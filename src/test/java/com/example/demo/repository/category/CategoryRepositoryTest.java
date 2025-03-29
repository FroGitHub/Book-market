package com.example.demo.repository.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.model.Category;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Sql(scripts = {"classpath:database/category/add-category-to-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/category/remove-category-from-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Test find category with pageable")
    void findAll_TakePageable_ReturnPage() {
        Pageable pageable = PageRequest.of(0, 5);
        int expectedSize = 5;
        int expectedNumElements = 2;

        Page<Category> actualCategories = categoryRepository.findAll(pageable);

        assertNotNull(actualCategories);
        assertEquals(expectedSize, actualCategories.getSize());
        assertEquals(expectedNumElements, actualCategories.getTotalElements());

        Map<Long, Category> categoryMap = actualCategories.getContent()
                .stream()
                .collect(Collectors.toMap(Category::getId, Function.identity()));

        assertCategory(categoryMap.get(1L), 1L, "cat1", "desc1");
        assertCategory(categoryMap.get(2L), 2L, "cat2", "desc2");
    }

    private void assertCategory(Category category, Long expectedId, String expectedName, String expectedDescription) {
        assertNotNull(category);
        assertEquals(expectedId, category.getId());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
    }
}
