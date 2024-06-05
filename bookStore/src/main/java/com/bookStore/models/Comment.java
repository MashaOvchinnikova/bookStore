package com.bookStore.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    public Date date = new Date();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="book_id",  referencedColumnName="id", nullable = false)
    @JsonIgnore
    public Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", referencedColumnName="id", nullable = false)
    @JsonIgnore
    public User user;

    public Comment( String comment_text, User user, Book book, Date date) {
        this.comment_text = comment_text;
        this.book = book;
        this.user = user;
        this.date = date;
    }

    public Comment() {
    }
}
