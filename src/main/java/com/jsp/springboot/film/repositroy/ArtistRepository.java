package com.jsp.springboot.film.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jsp.springboot.film.entity.Artist;
import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    // Custom query method to find artists by name
    List<Artist> findByArtistName(String artistName);

    // Custom query method to find artists by industry
    List<Artist> findByIndustry(String industry);
}
