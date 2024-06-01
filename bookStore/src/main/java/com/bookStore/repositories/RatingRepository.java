package com.bookStore.repositories;

import com.bookStore.models.Comment;
import com.bookStore.models.Rating;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT SUM(rate)FROM Rating e WHERE e.book_id=%:book_id%")
    Integer getPoints(@Param("book_id") Long book_id);
    @Query("SELECT COUNT(rate) FROM Rating e WHERE e.book_id=%:book_id%")
    Integer getCount(@Param("book_id") Long book_id);

    @Query("SELECT rate FROM Rating e WHERE e.book_id=%:book_id% and e.user_id=%:user_id%" )
    Integer userRated(@Param("book_id") Long book_id, @Param("user_id") Long user_id);
}
