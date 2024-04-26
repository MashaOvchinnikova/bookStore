package com.bookStore.services;

import com.bookStore.models.Book;
import com.bookStore.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

/*    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }*/
    public Page<Book> getBooks(int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<Book> pageBooks = bookRepository.findAll(paging);
        return pageBooks;
    }

    public List<Integer> getPageNumbers(int totalPages){
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
        return pageNumbers;
    }


    public Book saveBook(Book book) {
        bookRepository.save(book);
        return book;
    }

    public Book get_book_by_id(Integer book_id){
        Optional<Book> book = bookRepository.findById(book_id);
        if (book.isPresent()) {
            return
                book.get();}
        else
            return new Book();
    }
    /*Нужно будет написать методы для добавления/удаления книги в избранное,
     * удаления из базы книги в целом, редактирования полей книги*/
}