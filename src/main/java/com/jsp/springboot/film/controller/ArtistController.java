package com.jsp.springboot.film.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jsp.springboot.artist.utility.ResponseStrcture;
import com.jsp.springboot.film.dto.ArtistDTO;
import com.jsp.springboot.film.service.ArtistService;

@RestController
@RequestMapping("/api/artists")
@CrossOrigin(origins = "http://localhost:3000")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    // Add a new artist
    @PostMapping
    public ResponseEntity<ResponseStrcture<ArtistDTO>> addArtist(@RequestBody ArtistDTO artistDTO) {
        System.out.println("✅ Received Artist: " + artistDTO);
        return artistService.addArtist(artistDTO);
    }

    // Get all artists
    @GetMapping
    public ResponseEntity<ResponseStrcture<List<ArtistDTO>>> findAllArtists() {
        return artistService.findAllArtists();
    }

    // Update artist by ID
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStrcture<ArtistDTO>> updateArtistById(
            @PathVariable("id") int artistId,
            @RequestBody ArtistDTO updateArtistDTO) {
        return artistService.updateByArtistId(artistId, updateArtistDTO);
    }

    // Delete artist by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStrcture<ArtistDTO>> deleteByArtistId(@PathVariable("id") int artistId) {
        return artistService.deleteByArtistId(artistId);
    }

    // Find artist by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStrcture<ArtistDTO>> findByArtistId(@PathVariable("id") int artistId) {
        System.out.println("🔍 Searching artist with ID: " + artistId);
        return artistService.findByArtistId(artistId);
    }

    // Find artists by name
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseStrcture<List<ArtistDTO>>> findByArtistName(@PathVariable("name") String artistName) {
        System.out.println("🔍 Searching artist with name: " + artistName);
        return artistService.findByArtistName(artistName);
    }

    // Find artists by industry
    @GetMapping("/industry/{industry}")
    public ResponseEntity<ResponseStrcture<List<ArtistDTO>>> findByIndustry(@PathVariable("industry") String industry) {
        System.out.println("🔍 Filtering artists by industry: " + industry);
        return artistService.findByIndustry(industry);
    }
}