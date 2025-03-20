package com.jsp.springboot.film.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Artist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ Auto-generate ID
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
	

    // ✅ Default Constructor (Required for JPA)
    public Artist() {}

    // ✅ Full Constructor with all fields (Including ID)
    public Artist(int artistId, String artistName, String gender, int age, String nationality, String industry) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.gender = gender;
        this.age = age;
        this.nationality = nationality;
        this.industry = industry;
    }

    // ✅ Constructor without `artistId` (For Adding New Artists)
    public Artist(String artistName, String gender, int age, String nationality, String industry) {
        this.artistName = artistName;
        this.gender = gender;
        this.age = age;
        this.nationality = nationality;
        this.industry = industry;
    }
	
	
	
	

}
