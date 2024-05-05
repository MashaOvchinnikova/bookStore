package com.bookStore.controllers;

import com.bookStore.models.Book;
import com.bookStore.models.User;
import com.bookStore.services.BookService;
import com.bookStore.services.PageNumbersHandler;
import com.bookStore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

@Controller
public class UserController {
    private final UserService userService;
    private final BookService bookService;
    private final PageNumbersHandler pageNumbersHandler = new PageNumbersHandler();
    @Autowired
    public UserController(UserService userService, BookService bookService)
    {
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String getAllUsersAdmin(Model model, @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "5") int size){
        Page<User> pageUser = userService.getUsers(page,size);
        List<User> users = pageUser.getContent();
        int totalPages = pageUser.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = pageNumbersHandler.getPageNumbers(totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("users", users);
        model.addAttribute("userPage", pageUser);
        return "usersListAdmin";
    }

    @GetMapping("/user/favorite_books")
    public String getFavoriteBooksAuthorized(Model model)
    {
        Set<Book> books = userService.getFavoriteBooks();
        model.addAttribute("books", books);
        return "favoriteBooks";
    }

    @RequestMapping("/user/add_to_favorite/{id}")
    public String addBookToFavorite(@PathVariable("id") Long book_id){
        Book book = bookService.get_book_by_id(book_id);
        userService.addBookToFavorite(book);
        return "redirect:/user/favorite_books";
    }

    @RequestMapping("/user/favorite_books/delete/{id}")
    public String deleteBookByIdAdmin(@PathVariable("id") Long book_id){
        userService.deleteBookFromFavorite(book_id);
        return "redirect:/user/favorite_books";
    }

}
