package com.bookStore.models;

import java.util.List;

public class BookComment extends Book{
    public Book book;
    public List<Comment> comments;
    public BookComment(Book book, List<Comment> comments){
        this.book = book;
        this.comments = comments;
    }
}
