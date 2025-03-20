package com.jsp.springboot.film.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

import com.jsp.springboot.artist.utility.ResponseStrcture;
import com.jsp.springboot.film.dto.ArtistDTO;
import com.jsp.springboot.film.entity.Artist;

public interface ArtistService {

	public ResponseEntity<ResponseStrcture<ArtistDTO>> addArtist(ArtistDTO artistDTO);

	public ResponseEntity<ResponseStrcture<List<ArtistDTO>>> findAllArtists();

	public ResponseEntity<ResponseStrcture<ArtistDTO>> findByArtistId(int artistId);

	public ResponseEntity<ResponseStrcture<ArtistDTO>> updateByArtistId(int artistId, ArtistDTO updateArtistDTO);

	public ResponseEntity<ResponseStrcture<ArtistDTO>> deleteByArtistId(int artistId);

	public ResponseEntity<ResponseStrcture<List<ArtistDTO>>> findByArtistName(String artistName);

	public ResponseEntity<ResponseStrcture<List<ArtistDTO>>> findByIndustry(String industry);






}
