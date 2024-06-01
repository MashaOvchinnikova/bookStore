package com.bookStore.controllers;

import com.bookStore.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RatingController {
    private final RatingService ratingService;
    private final UserService userService;

    @Autowired
    public RatingController(RatingService ratingService, UserService userService) {
        this.ratingService=ratingService;
        this.userService=userService;
    }

    @PostMapping("/user/book/rate/{book_id}")
    public String ratingBook(Integer rate, @PathVariable("book_id") Long book_id){
        Long user_id = userService.get_current_user_id();
        ratingService.save(rate, user_id, book_id);
        return "redirect:/user/book/{book_id}";
    }

}
