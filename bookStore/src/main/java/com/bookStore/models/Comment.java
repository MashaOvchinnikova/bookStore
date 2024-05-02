package com.bookStore.models;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(columnDefinition="TEXT")
    public String comment_text;

    public String username;
    public int book_id;

    public Date date;

    public Comment( String comment_text, String username, int book_id, Date date) {
        this.comment_text = comment_text;
        this.username = username;
        this.book_id = book_id;
        this.date = date;
    }

    public Comment() {
    }
}
