package com.bookStore.services;

import com.bookStore.models.Book;
import com.bookStore.models.User;
import com.bookStore.models.Role;
import com.bookStore.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;


@Component
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void postConstruct() {
        User adminFromDb = userRepository.findByUsername("admin");
        if (adminFromDb == null){
            User admin = new User("admin", "admin");
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setRoles(Collections.singleton(Role.ADMIN));
            admin.setActive(true);
            userRepository.save(admin);
        }
    }

    public Page<User> getUsers(int page, int size){
        Pageable paging = PageRequest.of(page - 1, size);
        Page<User> pageUsers = userRepository.findAll(paging);
        return pageUsers;
    }

    public void addUser(User user) throws Exception
    {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null)
        {
            throw new Exception("user exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        user.setActive(true);
        userRepository.save(user);
    }

    public Set<Book> getFavoriteBooks(){
        String username = get_current_user();
        User user = userRepository.findByUsername(username);
        return user.getBooks();
    }

    public void addBookToFavorite(Book book){
        String username = get_current_user();
        User user = userRepository.findByUsername(username);
        user.addBook(book);
        userRepository.save(user);
    }

    public void deleteBookFromFavorite(Long book_id){
        String username = get_current_user();
        User user = userRepository.findByUsername(username);
        user.removeBook(book_id);
        userRepository.save(user);
    }

    public String get_current_user(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getUser(String username){
        User usr = userRepository.findByUsername(username);
        return usr;
    }
    public Long get_current_user_id(){
        String username = get_current_user();
        User user = userRepository.findByUsername(username);
        return user.getId();
    }
}
