package com.bookStore.services;

import com.bookStore.models.Book;
import com.bookStore.models.Comment;
import com.bookStore.models.User;
import com.bookStore.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Comment saveComment(String comment_text, User user, Book book) {
        Comment comment = new Comment(comment_text, user, book, new Date());
        commentRepository.save(comment);
        return comment;
    }

    public Page<Comment> get_user_comments(User user, int page, int size){
        Pageable paging = PageRequest.of(page - 1, size);
        Long user_id = user.getId();
        Page<Comment> pageComments = commentRepository.findAllByUsernameContaining(user_id, paging);
        return pageComments;
    }

    public Page<Comment> get_book_comments(Book book, int page, int size){
        Pageable paging = PageRequest.of(page - 1, size);
        Long book_id = book.getId();
        Page<Comment> pageComments = commentRepository.findAllByBookContaining(book_id, paging);
        return pageComments;
    }

    public Integer getBookCommentsCount(Long book_id){
        return commentRepository.countAllBookComment(book_id);
    }

    public void delete_comment_by_id(Long id){
        commentRepository.deleteById(id);
    }

    public List<Comment> getLatestComments(){
        return commentRepository.findFirst5ByOrderByDateDesc();
    }

    public Integer userCommented(User user, Book book){
        Long user_id = user.getId();
        Long book_id = book.getId();
        return commentRepository.countComment(book_id, user_id);
    }
}
