package com.bookStore.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue
    private int id;
    private String author;
    private String name;
    private String price;
    private String description;
    //public String rating;

    /*Вот это заготовочка для реализации добавления книг в избранное юзером
    * подумала, что тут связь many-to-many, возможно неправа и можно как-то по-другому сделать*/
    @ManyToMany(mappedBy = "addedBooks")
    private Set<User> additions;

    public Book(String name, String author, String price, String description) {
        this.author = author;
        this.name = name;
        this.price = price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}
