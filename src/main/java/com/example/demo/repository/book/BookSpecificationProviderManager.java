package com.example.demo.repository.book;

import com.example.demo.exception.DataProcessingException;
import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationProvider;
import com.example.demo.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new DataProcessingException(
                        "Can`t find correct specification provider for key: " + key));
    }
}
