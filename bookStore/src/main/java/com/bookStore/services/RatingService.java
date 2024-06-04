package com.bookStore.services;

import com.bookStore.models.Book;
import com.bookStore.models.Rating;
import com.bookStore.repositories.BookRepository;
import com.bookStore.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RatingService {
    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;
    @Autowired
    public RatingService(RatingRepository ratingRepository, BookRepository bookRepository){
        this.ratingRepository = ratingRepository;
        this.bookRepository = bookRepository;
    }

    public Rating save(Integer rate, Long user_id, Long book_id){
        Rating rating = new Rating(rate, user_id, book_id);
        ratingRepository.save(rating);
        saveRatingInstance(book_id);
        return rating;
    }
    private void saveRatingInstance(Long book_id){
        Book book = bookRepository.findById(book_id).get();
        Float rating = get_rating(book_id);
        book.rating = rating;
        bookRepository.save(book);
        Book nb = bookRepository.findById(book_id).get();
    }
    public Float get_rating(Long book_id){
        Integer rating = ratingRepository.getPoints(book_id);
        Integer count = ratingRepository.getCount(book_id);
        Float result = (float) 0;
        if (count == 0) {
            result = (float) -1;
        }
        else{
            result = (float)rating/count;
        }
        return result;
    }

    public Integer UserRated(Long book_id, Long user_id){
        Integer rate = ratingRepository.userRated(book_id, user_id);
        if (rate == null){
            rate = -1;
        }
        return rate;
    }
}
