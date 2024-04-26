package com.bookStore.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageNumbersHandler {
    public List<Integer> getPageNumbers(int totalPages){
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
        return pageNumbers;
    }
}
