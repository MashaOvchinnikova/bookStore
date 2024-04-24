package com.bookStore.services;

import com.bookStore.models.Book;
import com.bookStore.models.User;
import com.bookStore.models.Role;
import com.bookStore.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class UserService implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;

    //ВНИМАНИЕ КОСТЫЛЬ!!!
    /*ОБЯЗАТЕЛЬНО один раз раскомментить эти строки, ОДИН раз запустить, и потом закомментить
    * это нужно, чтобы можно было зайти в учетку с ролью админа
    * этот код при каждом запуске создает одну и ту же запись, и postgres выдает ошибку
    * при повторном входе из-за нескольких одинаковых записей в таблице*/

   /* @PostConstruct
    private void postConstruct() {
        User admin = new User("admin", "admin");
        admin.setRoles(Collections.singleton(Role.ADMIN));
        admin.setActive(true);
        userRepository.save(admin);
    }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = userRepository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(myUser.getUsername(),
                myUser.getPassword(), mapRolesToAuthorities(myUser.getRoles()));

    }

    private List<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles)
    {
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toList());
    }

    public void addUser(User user) throws Exception
    {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null)
        {
            throw new Exception("user exist");
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setActive(true);
        userRepository.save(user);
    }

}
