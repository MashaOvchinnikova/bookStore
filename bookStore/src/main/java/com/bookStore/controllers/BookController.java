package com.bookStore.controllers;

import com.bookStore.models.*;
import com.bookStore.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Controller
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;
    private final UserService userService;
    private final RatingService ratingService;
    private final PhotoUploadService photoUploadService;
    private final PageNumbersHandler pageNumbersHandler = new PageNumbersHandler();
    @Autowired
    public BookController(BookService bookService, CommentService commentService,
                          UserService userService, RatingService ratingService, PhotoUploadService photoUploadService)
    {
        this.bookService = bookService;
        this.commentService = commentService;
        this.userService = userService;
        this.ratingService = ratingService;
        this.photoUploadService = photoUploadService;
    }

    //Эндпоинты для всех пользователей
    @GetMapping("/")
    public String home()
    {
        return "home";
    }

    @GetMapping("/books")
    public String getBooks(Model model, @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size) {
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
                                           @RequestParam(defaultValue = "5") int size) {
        Page<Book> pageBooks = bookService.getBooks(page,size);
        List<Book> books = pageBooks.getContent();
        List<BookCover> bookCovers = new ArrayList<>();
        for(Book book: books){
            String image_link = photoUploadService.getImageLink(book.image_name);
            Integer ratingCount = ratingService.getRatingCount(book.getId());
            bookCovers.add(new BookCover(
                    book,image_link, ratingCount));
        }
        int totalPages = pageBooks.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = pageNumbersHandler.getPageNumbers(totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("books", bookCovers);
        model.addAttribute("bookPage", pageBooks);
        return "booksListAuthorizedUser";
    }


    //Эндпоинты для зареганных пользователей с ролью ADMIN
    @GetMapping("/admin/books")
    public String getBooksAdmin(Model model, @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "5") int size) {
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
    public String saveBook(@ModelAttribute Book book,@RequestParam("file") MultipartFile file) {
        String filename = photoUploadService.getFileName(file);
        book.image_name = filename;
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
                            @RequestParam(defaultValue = "3") int size){
        Book book = bookService.get_book_by_id(book_id);
        Page<Comment> commentPage = commentService.get_book_comments(book, page, size);
        Integer user_rated = ratingService.UserRated(book_id, userService.get_current_user_id());
        Integer ratingCount = ratingService.getRatingCount(book_id);
        String filename = book.image_name;
        String link = photoUploadService.getImageLink(filename);
        User user = userService.getUser(userService.get_current_user());
        Integer userCommented = commentService.userCommented(user, book);
        Integer commentsCount = commentService.getBookCommentsCount(book_id);
        int totalPages = commentPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = pageNumbersHandler.getPageNumbers(totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("rating", book.rating);
        model.addAttribute("userRated", user_rated);
        model.addAttribute("ratingCount", ratingCount);
        model.addAttribute("userCommented", userCommented);
        model.addAttribute("commentsCount", commentsCount);
        model.addAttribute("link", link);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("book", book);
        model.addAttribute("commentPage", commentPage);
        return "bookView";
    }
}
