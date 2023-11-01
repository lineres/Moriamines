package com.morialog.moriamines.Partie;

import com.morialog.moriamines.profile.Profile;
import com.morialog.moriamines.profile.ProfileManager;

import java.io.Serializable;
import java.util.ArrayList;

public class Partie implements Serializable {

	public static Partie currentGame;

	public ArrayList<Element> elementsOnGamePane = new ArrayList<>();
	public ArrayList<Element> unlockedElements = new ArrayList<>();

	public Chronometer chrono = new Chronometer();

	public boolean victory = false;

	public Partie() {
		currentGame = this;
	}

	public boolean isAlreadyUnlocked( Item item ) {
		for( Element elem : unlockedElements )
			if( elem.getID() == item ) {
				return true;
			}
		return false;
	}

	public boolean victory() {
		if( unlockedElements.size() == Item.count() ) {
			chrono.stop();
			saveData();
			return true;
		} else
			return false;
	}

	public void saveData() {
		Profile profile = ProfileManager.currentProfile;
		int newScore = unlockedElements.size();
		if( profile.getMaxScore() < newScore ) {
			profile.setMaxScore( newScore );
			profile.setSecondsForMaxScore( chrono.getDurationInSeconds() );
		} else if( profile.getMaxScore() == newScore && profile.getSecondsForMaxScore() > chrono.getDurationInSeconds() ) {
			profile.setSecondsForMaxScore( chrono.getDurationInSeconds() );
		}
	}

	public void endTheGame() {
		if( victory ) {
			chrono.stop();
			saveData();
		}
		currentGame = null;
	}

}
