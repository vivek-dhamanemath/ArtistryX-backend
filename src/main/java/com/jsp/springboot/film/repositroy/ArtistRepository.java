package com.jsp.springboot.film.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jsp.springboot.film.entity.Artist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    // Custom query method to find artists by name
    List<Artist> findByArtistName(String artistName);

    // Custom query method to find artists by industry
    List<Artist> findByIndustry(String industry);

    Optional<Artist> findByArtistId(int artistId);

    // Case-insensitive search for industry
    @Query("SELECT a FROM Artist a WHERE LOWER(a.industry) = LOWER(:industry)")
    List<Artist> findByIndustryIgnoreCase(@Param("industry") String industry);
}
