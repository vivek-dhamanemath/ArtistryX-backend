package com.jsp.springboot.film.dto;

import com.jsp.springboot.film.entity.Artist;

public class ArtistDTO {

	private int artistId;
	private String artistName;
	private String gender;
	private int age;
	private String nationality;
	private String industry;

	public int getArtistId() {
		return artistId;
	}
	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}


	// ✅ Default Constructor (Required for DTOs)
    public ArtistDTO() {}

    // ✅ Constructor to Create DTO from Entity
    public ArtistDTO(Artist artist) {
        this.artistName = artist.getArtistName();
        this.gender = artist.getGender();
        this.age = artist.getAge();
        this.nationality = artist.getNationality();
        this.industry = artist.getIndustry();
    }

    // ✅ Constructor with Fields
    public ArtistDTO(String artistName, String gender, int age, String nationality, String industry) {
        this.artistName = artistName;
        this.gender = gender;
        this.age = age;
        this.nationality = nationality;
        this.industry = industry;
    }
}







