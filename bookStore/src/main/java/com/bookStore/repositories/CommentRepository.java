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
public interface CommentRepository extends JpaRepository<Comment, Long> {
/*    @Query("SELECT e FROM Comment e WHERE e.username LIKE %:username%")
    List<Comment> findAllByUsernameContaining(@Param("username") String username);*/
    @Query("SELECT e FROM Comment e WHERE e.username LIKE %:username%")
    Page<Comment> findAllByUsernameContaining(@Param("username") String username, Pageable pageable);

    /*@Query("SELECT e FROM Comment e WHERE e.book_id = %:book_id%")
    List<Comment> findAllByBookContaining(@Param("book_id") Long book_id);*/
    @Query("SELECT e FROM Comment e WHERE e.book_id = %:book_id%")
    Page<Comment> findAllByBookContaining(@Param("book_id") Long book_id, Pageable pageable);

    @Query("SELECT COUNT(e) FROM Comment e WHERE e.book_id=%:book_id% and e.username=%:username%" )
    Integer countComment(@Param("book_id") Long book_id, @Param("username") String username);

    List<Comment> findFirst5ByOrderByDateDesc();
}
