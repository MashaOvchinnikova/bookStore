package com.bookStore.repositories;

import com.bookStore.models.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT e FROM Comment e WHERE e.username LIKE %:username%")
    List<Comment> findAllByUsernameContaining(@Param("username") String username);

    @Query("SELECT e FROM Comment e WHERE e.book_id = %:book_id%")
    List<Comment> findAllByBookContaining(@Param("book_id") int book_id);
}
