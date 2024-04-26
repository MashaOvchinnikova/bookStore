package com.bookStore.controllers;

import com.bookStore.models.Book;
import com.bookStore.services.PageNumbersHandler;
import com.bookStore.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class BookController {

    private final BookService bookService;

    private final PageNumbersHandler pageNumbersHandler = new PageNumbersHandler();
    @Autowired
    public BookController(BookService bookService)
    {
        this.bookService = bookService;
    }

    //Эндпоинты для всех пользователей
    @GetMapping("/")
    public String home()
    {
        return "home";
    }

/*    @GetMapping("/all_books")
    public ModelAndView getBooks()
    {
        List<Book> books = bookService.getAllBooks();
       *//* ModelAndView model = new ModelAndView();
        model.setViewName("booksList");
        model.addObject("books", books);*//*
        return new ModelAndView("booksList", "books", books);
    }*/

    @GetMapping("/books")
    public String getBooks(Model model, @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "2") int size) {
        Page<Book> bookPage = bookService.getBooks(page,size);
        List<Book> books = bookPage.getContent();
        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = pageNumbersHandler.getPageNumbers(totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("books", books);
        model.addAttribute("bookPage", bookPage);
        return "booksList";
    }

    //Эндпоинты для зареганных пользователей с ролью USER
    @GetMapping("/user/main")
    public String userMain()
    {
        return "homeAuthorizedUser";
    }


    @GetMapping("/user/books")
    public String getBooksAuthorized(Model model, @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "2") int size) {
        Page<Book> pageBooks = bookService.getBooks(page,size);
        List<Book> books = pageBooks.getContent();
        int totalPages = pageBooks.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = pageNumbersHandler.getPageNumbers(totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("books", books);
        model.addAttribute("bookPage", pageBooks);
        return "booksListAuthorizedUser";
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
    @GetMapping("/admin/books")
    public String getBooksAdmin(Model model, @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "2") int size) {
        Page<Book> pageBook = bookService.getBooks(page,size);
        List<Book> books = pageBook.getContent();
        int totalPages = pageBook.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = pageNumbersHandler.getPageNumbers(totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("books", books);
        model.addAttribute("bookPage", pageBook);
        return "booksListAdmin";
    }

    @RequestMapping("/admin/books/{id}")
    public String deleteBookByIdAdmin(@PathVariable("id") int book_id){
        bookService.deleteBookByID(book_id);
        return "redirect:/admin/books";
    }

    @GetMapping("/admin/add_book")
    public String addBook(Model model)
    {
        return "bookAddition";
    }

    @PostMapping("/admin/save_book")
    public String saveBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/book/{book_id}")
    public ModelAndView view_book(@PathVariable Integer book_id){
        Book book = bookService.get_book_by_id(book_id);
        return  new ModelAndView("bookView", "book", book);
    }

    /*Нужно еще добавить контроллеры для удаления и редактирования книг*/
}
