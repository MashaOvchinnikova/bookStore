package com.bookStore.controllers;

import com.bookStore.models.Book;
import com.bookStore.models.Comment;
import com.bookStore.models.User;
import com.bookStore.services.BookService;
import com.bookStore.services.CommentService;
import com.bookStore.services.PageNumbersHandler;
import com.bookStore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final BookService bookService;
    private final PageNumbersHandler pageNumbersHandler = new PageNumbersHandler();

    @Autowired
    public CommentController(CommentService commentService, UserService userService, BookService bookService) {
        this.commentService = commentService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @PostMapping("/user/book/comment_add/{id}")
    public String add_comment(@PathVariable Long id, String comment_text){
        String username = userService.get_current_user();
        User user = userService.getUser(username);
        Book book = bookService.get_book_by_id(id);
        commentService.saveComment(comment_text, user, book);
        return "redirect:/user/comments";
    }

    @GetMapping("/user/book/comment/{book_id}")
    public ModelAndView add_comm(@PathVariable Long book_id){
        return new ModelAndView("commentAdd","book_id", book_id);
    }

    @GetMapping("/user/comments")
    public String get_user_comments(Model model, @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size){
        User user = userService.getUser(userService.get_current_user());
        Page<Comment> commentPage = commentService.get_user_comments(user, page, size);
        List<Comment> comments = commentPage.getContent();
        int totalPages = commentPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = pageNumbersHandler.getPageNumbers(totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("comments", comments);
        model.addAttribute("commentPage", commentPage);
        return "commentList";
    }

    @RequestMapping("/user/comments/{id}")
    public String delete_comment(@PathVariable Long id){
        commentService.delete_comment_by_id(id);
        return "redirect:/user/comments";
    }
}
