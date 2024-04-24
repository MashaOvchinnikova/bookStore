package com.bookStore.services;

import com.bookStore.models.Book;
import com.bookStore.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    public Book saveBook(Book book) {
        bookRepository.save(book);
        return book;
    }

    /*Нужно будет написать методы для добавления/удаления книги в избранное,
     * удаления из базы книги в целом, редактирования полей книги*/
}