package com.bookStore.services;

import com.bookStore.models.Rating;
import com.bookStore.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RatingService {
    private final RatingRepository ratingRepository;
    @Autowired
    public RatingService(RatingRepository ratingRepository){
        this.ratingRepository = ratingRepository;
    }

    public Rating save(Integer rate, Long user_id, Long book_id){
        Rating rating = new Rating(rate, user_id, book_id);
        ratingRepository.save(rating);
        return rating;
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
