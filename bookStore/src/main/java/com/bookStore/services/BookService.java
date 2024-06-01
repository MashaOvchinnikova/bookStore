package com.bookStore.services;

import com.bookStore.models.Book;
import com.bookStore.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> getBooks(int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<Book> pageBooks = bookRepository.findAll(paging);
        return pageBooks;
    }

    public Book saveBook(Book book) {
        bookRepository.save(book);
        return book;
    }
    public Book updateBook(Book book){
        bookRepository.save(book);
        return book;
    }
    public void deleteBookByID(Long book_id)
    {
        bookRepository.deleteById(book_id);
    }

    public Book get_book_by_id(Long book_id){
        Optional<Book> book = bookRepository.findById(book_id);
        if (book.isPresent()) {
            return
                book.get();}
        else
            return new Book();
    }
}