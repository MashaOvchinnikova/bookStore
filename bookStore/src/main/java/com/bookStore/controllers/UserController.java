package com.bookStore.controllers;

import com.bookStore.models.User;
import com.bookStore.services.BookService;
import com.bookStore.services.PageNumbersHandler;
import com.bookStore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final PageNumbersHandler pageNumbersHandler = new PageNumbersHandler();
    @Autowired
    public UserController(UserService userService)
    {
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

}
