package com.vadhvan.repositories;

import com.vadhvan.models.RailTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RailTrackRepository extends JpaRepository<RailTrack, String> {
    List<RailTrack> findByStatus(String status);
    Optional<RailTrack> findByTrackNumber(String trackNumber);
    boolean existsByTrackNumber(String trackNumber);
}
