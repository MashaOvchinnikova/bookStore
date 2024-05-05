package com.bookStore.services;

import com.bookStore.models.Comment;
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

    public Comment saveComment(String comment_text, String username, Long book_id, String bookName) {
        Comment comment = new Comment(comment_text, username, book_id, bookName, new Date());
        commentRepository.save(comment);
        return comment;
    }

    public Page<Comment> get_user_comments(String username, int page, int size){
        Pageable paging = PageRequest.of(page - 1, size);
        Page<Comment> pageComments = commentRepository.findAllByUsernameContaining(username, paging);
        return pageComments;
    }

    public Page<Comment> get_book_comments(Long book_id, int page, int size){
        Pageable paging = PageRequest.of(page - 1, size);
        Page<Comment> pageComments = commentRepository.findAllByBookContaining(book_id, paging);
        return pageComments;
    }

    public void delete_comment_by_id(Long id){
        commentRepository.deleteById(id);
    }

    public List<Comment> getLatestComments(){
        return commentRepository.findFirst5ByOrderByDateDesc();
    }
}
