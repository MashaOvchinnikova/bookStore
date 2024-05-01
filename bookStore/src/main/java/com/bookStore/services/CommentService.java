package com.bookStore.services;

import com.bookStore.models.Book;
import com.bookStore.models.Comment;
import com.bookStore.repositories.BookRepository;
import com.bookStore.repositories.CommentRepository;
import com.bookStore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CommentService {
    private final CommentRepository commentRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository){

        this.commentRepository = commentRepository;
    }


    //delete comment

    public Comment saveComment(String comment_text, String username, Integer book_id) {
        Comment comment = new Comment(comment_text, username, book_id, new Date());
        commentRepository.save(comment);
        return comment;
    }

    public List<Comment> get_user_comments(String username){
        List<Comment> comments = commentRepository.findAllByUsernameContaining(username);
        return comments;
    }

    public List<Comment> get_book_comments(Integer book_id){
        List<Comment> comments = commentRepository.findAllByBookContaining(book_id);
        return comments;
    }

    public void delete_comment_by_id(int id){
        commentRepository.deleteById(id);
    }
}
