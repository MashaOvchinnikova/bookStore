package com.bookStore.models;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(columnDefinition="TEXT")
    public String comment_text;

    public String username;
    public Long book_id;
    public String bookName;

    public Date date;

    public Comment( String comment_text, String username, Long book_id, String bookName, Date date) {
        this.comment_text = comment_text;
        this.username = username;
        this.book_id = book_id;
        this.bookName = bookName;
        this.date = date;
    }

    public Comment() {
    }
}
