package com.morialog.moriamines.profile;

import com.morialog.moriamines.FileManager;
import com.morialog.moriamines.Partie.Chronometer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Profile implements Serializable {

	private final String name;
	private String creationDate;
	private int maxScore = 0;
	private int secondsForMaxScore = 0;

	protected Profile( String name ) {
		this.name = name;
		this.creationDate = DateTimeFormatter.ofPattern( "dd/MM/yyyy" ).format( LocalDateTime.now() );
	}

	public String getName() {
		return name;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String newDate) {
		 creationDate = newDate;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore( int maxScore ) {
		this.maxScore = maxScore;
		saveProfile();
	}

	public String getTimeForMaxScore() {
		return Chronometer.formatDuration( secondsForMaxScore );
	}

	public int getSecondsForMaxScore() {
		return this.secondsForMaxScore;
	}

	public void setSecondsForMaxScore( int newTime ) {
		this.secondsForMaxScore = newTime;
		saveProfile();
	}

	@Override
	public String toString() {
		return name;
	}

	public void saveProfile() {
		FileManager.serialize( this, "Profiles/" + getName() + ".prof" );
	}

}
