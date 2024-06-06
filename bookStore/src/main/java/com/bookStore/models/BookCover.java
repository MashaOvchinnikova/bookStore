package com.bookStore.models;

import java.util.List;

public class BookCover extends Book{
    public Book book;
    public String link;
    public Integer ratingCount;

    public BookCover(Book book, String link, Integer ratingCount){
        this.book = book;
        this.link = link;
        this.ratingCount = ratingCount;
    }
}
