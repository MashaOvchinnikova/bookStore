package com.bookStore.controllers;

import com.bookStore.models.Book;
import com.bookStore.models.BookComment;
import com.bookStore.models.Comment;
import com.bookStore.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;
    private final UserService userService;
    private final RatingService ratingService;

    private final PageNumbersHandler pageNumbersHandler = new PageNumbersHandler();
    @Autowired
    public BookController(BookService bookService, CommentService commentService, UserService userService, RatingService ratingService)
    {
        this.bookService = bookService;
        this.commentService = commentService;
        this.userService = userService;
        this.ratingService = ratingService;
    }

    //Эндпоинты для всех пользователей
    @GetMapping("/")
    public String home()
    {
        return "home";
    }

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
    public String userMain(Model model)
    {
        List<Comment> latestComments = commentService.getLatestComments();
        model.addAttribute("latestComments", latestComments);
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
    public String deleteBookByIdAdmin(@PathVariable("id") Long book_id){
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
    @GetMapping("/admin/books/edit/{id}")
    public String editBook(Model model, @PathVariable("id") Long book_id){
        String name = bookService.get_book_by_id(book_id).getName();
        model.addAttribute("name", name);
        model.addAttribute("book_id", book_id);
        return "bookEditing";
    }

    @PostMapping("/admin/save_changes/{id}")
    public String savingChanges(@ModelAttribute Book book, @PathVariable("id") Long id){
        bookService.updateBook(book);
        return "redirect:/admin/books";
    }
    @GetMapping("/user/book/{book_id}")
    public String view_book(Model model,
                            @PathVariable Long book_id,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "2") int size){
        Book book = bookService.get_book_by_id(book_id);
        Page<Comment> commentPage = commentService.get_book_comments(book_id, page, size);
        List<Comment> comments = commentPage.getContent();
        BookComment bookNcomments = new BookComment(book, comments);
        int totalPages = commentPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = pageNumbersHandler.getPageNumbers(totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }
        Float rating = ratingService.get_rating(book_id);
        if (rating == -1){model.addAttribute("rating", "Рейтинг не сформирован");}
        else{model.addAttribute("rating", rating);}
        Integer user_rated = ratingService.UserRated(book_id, userService.get_current_user_id());
        model.addAttribute("userRated", user_rated);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("book", bookNcomments);
        model.addAttribute("commentPage", commentPage);
        return "bookView";
    }

    /*Нужно еще добавить контроллер для редактирования книг*/
}
