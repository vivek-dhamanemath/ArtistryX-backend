package com.jsp.springboot.film.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.jsp.springboot.artist.exception.ArtistNotFoundByIdException;
import com.jsp.springboot.artist.exception.ArtistNotFoundByIndustryException;
import com.jsp.springboot.artist.exception.ArtistNotFoundByNameException;
import com.jsp.springboot.artist.utility.ResponseStrcture;
import com.jsp.springboot.film.dto.ArtistDTO;
import com.jsp.springboot.film.entity.Artist;
import com.jsp.springboot.film.repositroy.ArtistRepository;
import com.jsp.springboot.film.service.ArtistService;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ModelMapper modelMapper; // Inject ModelMapper

    @Override
    public ResponseEntity<ResponseStrcture<ArtistDTO>> addArtist(ArtistDTO artistDTO) {
        Artist artist = new Artist(
                artistDTO.getArtistName(),
                artistDTO.getGender(),
                artistDTO.getAge(),
                artistDTO.getNationality(),
                artistDTO.getIndustry()
        );

        Artist savedArtist = artistRepository.save(artist);
        System.out.println("Saved artist: " + savedArtist);

        ArtistDTO responseDTO = new ArtistDTO(
                savedArtist.getArtistName(),
                savedArtist.getGender(),
                savedArtist.getAge(),
                savedArtist.getNationality(),
                savedArtist.getIndustry()
        );

        ResponseStrcture<ArtistDTO> response = new ResponseStrcture<>();
        response.setStauscode(201);
        response.setMessage("Artist object created successfully!!");
        response.setArtist(responseDTO);

        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<ResponseStrcture<List<ArtistDTO>>> findAllArtists() {
        List<Artist> artists = artistRepository.findAll();

        if (artists.isEmpty()) {
            throw new ArtistNotFoundByIdException("No artists found in the database.");
        } else {
            // Convert List<Artist> to List<ArtistDTO> using ModelMapper
            List<ArtistDTO> artistDTOs = artists.stream()
                    .map(artist -> modelMapper.map(artist, ArtistDTO.class))
                    .collect(Collectors.toList());

            // Prepare response
            ResponseStrcture<List<ArtistDTO>> responseStrcture = new ResponseStrcture<>();
            responseStrcture.setStauscode(HttpStatus.OK.value());
            responseStrcture.setMessage("Artist objects found successfully!!");
            responseStrcture.setArtist(artistDTOs);

            return new ResponseEntity<>(responseStrcture, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseStrcture<ArtistDTO>> findByArtistId(int artistId) {
        Optional<Artist> optional = artistRepository.findById(artistId);

        if (optional.isPresent()) {
            Artist artist = optional.get();

            // Convert Artist to ArtistDTO using ModelMapper
            ArtistDTO artistDTO = modelMapper.map(artist, ArtistDTO.class);

            // Prepare response
            ResponseStrcture<ArtistDTO> responseStrcture = new ResponseStrcture<>();
            responseStrcture.setStauscode(HttpStatus.FOUND.value());
            responseStrcture.setMessage("Artist object found successfully by ArtistId!!");
            responseStrcture.setArtist(artistDTO);

            return new ResponseEntity<>(responseStrcture, HttpStatus.FOUND);
        } else {
            throw new ArtistNotFoundByIdException("Artist not found");
        }
    }

    @Override
    public ResponseEntity<ResponseStrcture<ArtistDTO>> updateByArtistId(int artistId, ArtistDTO updateArtistDTO) {
        Optional<Artist> optional = artistRepository.findById(artistId);

        if (optional.isPresent()) {
            Artist existingArtist = optional.get();

            // Update fields from ArtistDTO to Artist entity using ModelMapper
            modelMapper.map(updateArtistDTO, existingArtist);

            // Save the updated Artist entity
            Artist updatedArtist = artistRepository.save(existingArtist);

            // Convert updated Artist entity back to ArtistDTO using ModelMapper
            ArtistDTO updatedArtistDTO = modelMapper.map(updatedArtist, ArtistDTO.class);

            // Prepare response
            ResponseStrcture<ArtistDTO> responseStrcture = new ResponseStrcture<>();
            responseStrcture.setStauscode(HttpStatus.OK.value());
            responseStrcture.setMessage("Artist object successfully updated by ArtistId!!");
            responseStrcture.setArtist(updatedArtistDTO);

            return new ResponseEntity<>(responseStrcture, HttpStatus.OK);
        } else {
            throw new ArtistNotFoundByIdException("Artist not found");
        }
    }

    @Override
    public ResponseEntity<ResponseStrcture<ArtistDTO>> deleteByArtistId(int artistId) {
        Optional<Artist> optional = artistRepository.findById(artistId);

        if (optional.isPresent()) {
            Artist existingArtist = optional.get();

            // Convert Artist to ArtistDTO before deletion using ModelMapper
            ArtistDTO deletedArtistDTO = modelMapper.map(existingArtist, ArtistDTO.class);

            // Delete the Artist entity
            artistRepository.delete(existingArtist);

            // Prepare response
            ResponseStrcture<ArtistDTO> responseStrcture = new ResponseStrcture<>();
            responseStrcture.setStauscode(HttpStatus.OK.value());
            responseStrcture.setMessage("Artist object successfully deleted by ArtistId!!");
            responseStrcture.setArtist(deletedArtistDTO);

            return new ResponseEntity<>(responseStrcture, HttpStatus.OK);
        } else {
            throw new ArtistNotFoundByIdException("Artist not found");
        }
    }

    @Override
    public ResponseEntity<ResponseStrcture<List<ArtistDTO>>> findByArtistName(String artistName) {
        List<Artist> artists = artistRepository.findByArtistName(artistName);

        if (artists.isEmpty()) {
            throw new ArtistNotFoundByNameException("Artist not found");
        } else {
            // Convert List<Artist> to List<ArtistDTO> using ModelMapper
            List<ArtistDTO> artistDTOs = artists.stream()
                    .map(artist -> modelMapper.map(artist, ArtistDTO.class))
                    .collect(Collectors.toList());

            // Prepare response
            ResponseStrcture<List<ArtistDTO>> responseStrcture = new ResponseStrcture<>();
            responseStrcture.setStauscode(HttpStatus.FOUND.value());
            responseStrcture.setMessage("Artist objects found successfully by ArtistName!!");
            responseStrcture.setArtist(artistDTOs);

            return new ResponseEntity<>(responseStrcture, HttpStatus.FOUND);
        }
    }

    @Override
    public ResponseEntity<ResponseStrcture<List<ArtistDTO>>> findByIndustry(String industry) {
        System.out.println("Inside service method: Looking for artists in industry " + industry); // Log inside service
        List<Artist> artists = artistRepository.findByIndustry(industry);

        if (artists.isEmpty()) {
            throw new ArtistNotFoundByIndustryException("Artist not found");
        } else {
            // Convert List<Artist> to List<ArtistDTO> using ModelMapper
            List<ArtistDTO> artistDTOs = artists.stream()
                    .map(artist -> modelMapper.map(artist, ArtistDTO.class))
                    .collect(Collectors.toList());

            // Prepare response
            ResponseStrcture<List<ArtistDTO>> responseStrcture = new ResponseStrcture<>();
            responseStrcture.setStauscode(HttpStatus.FOUND.value());
            responseStrcture.setMessage("Artist objects found successfully by Industry!!");
            responseStrcture.setArtist(artistDTOs);

            return new ResponseEntity<>(responseStrcture, HttpStatus.FOUND);
        }
    }
}