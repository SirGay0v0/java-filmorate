package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {

    Review create(Review review);

    Review update(Review review);

    void delete(Integer id);

    Review getReviewById(Integer id);

    List<Review> getAll(Integer filmId, Integer count);

    void addLikeReview(Integer id, Integer userId);

    void deleteLikeReview(Integer id, Integer userId);

    void addDisLikeReview(Integer id, Integer userId);

    void deleteDisLikeReview(Integer id, Integer userId);

}
