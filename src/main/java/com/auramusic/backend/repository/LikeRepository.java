package com.auramusic.backend.repository;

import com.auramusic.backend.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    boolean existsByUserIdAndSongId(Integer userId, Integer songId);
    void deleteByUserIdAndSongId(Integer userId, Integer songId);
    List<Like> findByUserId(Integer userId);
}