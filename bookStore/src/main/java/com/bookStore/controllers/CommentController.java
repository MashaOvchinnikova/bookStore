package com.bookStore.controllers;

import com.bookStore.models.Comment;
import com.bookStore.services.BookService;
import com.bookStore.services.CommentService;
import com.bookStore.services.UserService;
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
    private UserService userService;
    private BookService bookService;

    public CommentController(CommentService commentService, UserService userService, BookService bookService) {
        this.commentService = commentService;
        this.userService = userService;
        this.bookService = bookService;
    }


//    @GetMapping("/user/comments")
//    public ModelAndView getFavoriteBooksAuthorized()
//    {
//        /*
//        List<Comment> comments= commentService.get_comments_by_user();
//        return new ModelAndView("commentList", "comments", comments);
//    */
//    }
//
//    @GetMapping("/user/book/{id}")
//    public ModelAndView add_comment(){
//
//    }

//    @GetMapping("/user/comments")
//    public ModelAndView get_user_test(){
//        String username=userService.get_current_user();
//        return new ModelAndView("user_test", "user", username);
//    }

    @PostMapping("/user/book/comment_add/{id}")
    public String add_comment(@PathVariable Integer id, String comment_text){
        String username = userService.get_current_user();
        commentService.saveComment(comment_text, username, id);
        return "redirect:/user/books";
    }

    @GetMapping("/user/book/{book_id}/comment")
    public ModelAndView add_comm( @PathVariable Integer book_id){
        return new ModelAndView("commentAdd","book_id", book_id);
    }

    @GetMapping("/user/comments")
    public ModelAndView get_user_comments(){
        String username = userService.get_current_user();
        List<Comment> comments = commentService.get_user_comments(username);
        return new ModelAndView("commentList", "comments",comments);
    }

    @RequestMapping("/user/comments/{id}")
    public String delete_comment(@PathVariable int id){
        commentService.delete_comment_by_id(id);
        return "redirect:/user/comments";
    }
}
