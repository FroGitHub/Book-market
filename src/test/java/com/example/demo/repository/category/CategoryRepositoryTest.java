package com.example.demo.repository.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.ObjectUtil;
import com.example.demo.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

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
         EqualsBuilder.reflectionEquals(ObjectUtil.getCategory(),
                 actualCategories.getContent().get(0));
     }

}
