package com.bookStore.models;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private boolean isActive;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    /*Вот это заготовочка для реализации добавления книг в избранное юзером
     * подумала, что тут связь many-to-many, возможно не права и можно как-то по-другому сделать*/
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name="book_addition",
            joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="book_id", referencedColumnName="id")
    )
    private Set<Book> addedBooks;

    public User() {
    }

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Book> getBooks() {
        return addedBooks;
    }

    public void setBooks(Set<Book> addedBooks) {
        this.addedBooks = addedBooks;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addBook(Book book){
        this.addedBooks.add(book);
        book.getAdditions().add(this);
    }

    public void removeBook(Long bookId){
        Book book = this.addedBooks.stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
        if (book != null) {
            this.addedBooks.remove(book);
            book.getAdditions().remove(this);
        }
    }
}
