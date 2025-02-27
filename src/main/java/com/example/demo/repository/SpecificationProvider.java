package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {

    String getKey();

    Specification<T> getSpecification(String param);
}
