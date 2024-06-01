package com.bookStore.models;

import jakarta.persistence.*;

@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Integer rate;
    public Long user_id;
    public Long book_id;

    public Rating(Integer rate, Long user_id, Long book_id){
        this.rate = rate;
        this.user_id = user_id;
        this.book_id = book_id;
    }
}
