package com.bookStore.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String author;
    private String name;

    @Enumerated(EnumType.STRING)
    private BookGenre genre;

    @Column(columnDefinition="TEXT")
    private String description;

    /*Вот это заготовочка для реализации добавления книг в избранное юзером
    * подумала, что тут связь many-to-many, возможно не права и можно как-то по-другому сделать*/
    @ManyToMany(mappedBy = "addedBooks")
    private Set<User> additions;

    public Book(String name, String author, String description) {
        this.author = author;
        this.name = name;
        this.description = description;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<User> getAdditions() {
        return additions;
    }

    public void setAdditions(Set<User> additions) {
        this.additions = additions;
    }

    public BookGenre getGenre() {
        return genre;
    }

    public void setGenre(BookGenre genre) {
        this.genre = genre;
    }

    public String getFirstNChars(int n) {
        if (this.description == null) {
            return null;
        }

        return this.description.length() < n ? description : description.substring(0, n);
    }
}
