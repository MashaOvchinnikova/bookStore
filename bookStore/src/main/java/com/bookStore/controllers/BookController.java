package com.bookStore.controllers;

import com.bookStore.models.Book;
import com.bookStore.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class BookController {

    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //Эндпоинты для всех пользователей
    @GetMapping("/")
    public String home()
    {
        return "home";
    }

    @GetMapping("/all_books")
    public ModelAndView getAllBooks()
    {
        List<Book> books = bookService.getAllBooks();
       /* ModelAndView model = new ModelAndView();
        model.setViewName("booksList");
        model.addObject("books", books);*/
        return new ModelAndView("booksList", "books", books);
    }

    //Эндпоинты для зареганных пользователей с ролью USER
    @GetMapping("/user/main")
    public String userMain()
    {
        return "homeAuthorizedUser";
    }


    @GetMapping("/user/all_books")
    public ModelAndView getAllBooksAuthorized()
    {
        List<Book> books = bookService.getAllBooks();
        return new ModelAndView("booksListAuthorizedUser", "books", books);
    }

    /*Логику получения из БД списка добавленных книг в избранное юзером
    нужно будет прописать в классе BookService*/
    @GetMapping("/user/favorite_books")
    public String getFavoriteBooksAuthorized()
    {
      /*  List<Book> books = bookService.getFavoriteBooks();
        return new ModelAndView("favoriteBooks", "books", books);*/
        return "favoriteBooks";
    }

    //Эндпоинты для зареганных пользователей с ролью ADMIN
    @GetMapping("/admin/all_books")
    public ModelAndView getAllBooksAdmin()
    {
        List<Book> books = bookService.getAllBooks();
        return new ModelAndView("booksListAdmin", "books", books);
    }

    @GetMapping("/admin/add_book")
    public String addBook()
    {
        return "bookAddition";
    }

    @PostMapping("/admin/save_book")
    public String saveBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/admin/all_books";
    }

    @GetMapping("/book/{book_id}")
    public ModelAndView view_book(@PathVariable Integer book_id){
        Book book = bookService.get_book_by_id(book_id);
        return  new ModelAndView("bookView", "book", book);
    }

    /*Нужно еще добавить контроллеры для удаления и редактирования книг*/
}
