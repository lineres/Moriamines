package com.morialog.moriamines;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundsManager {

	public static MediaPlayer backgroundMusic;

	private static final Map<String, Media> sounds = new HashMap<>();

	public static void playSound( String music ) {
		if( !Config.getEffectsSoundMusicStatus() )
			return;
		Media media;
		if( sounds.containsKey( music ) )
			media = sounds.get( music );
		else {
			URL url = Utils.getResource( "/Audio/" + music );
			if( url == null )
				return;
			media = new Media( url.toString() );
			sounds.put( music, media );
		}
		MediaPlayer mediaplayer = new MediaPlayer( media );
		mediaplayer.setVolume( Config.getEffectsSoundVolume() );
		mediaplayer.setCycleCount( 1 );
		mediaplayer.play();
	}


	public static void playBackgroundMusic() {
		if( backgroundMusic == null ) {
			Media media = new Media( Utils.getResource( "/Audio/bg_music.mp3" ).toString() );
			backgroundMusic = new MediaPlayer( media );
		}
		if( Config.getBackgroundMusicStatus() ) {
			backgroundMusic.setVolume( Config.getBackgroundMusicVolume() );
			backgroundMusic.setCycleCount( AudioClip.INDEFINITE );
			backgroundMusic.play();
		} else {
			backgroundMusic.stop();
		}
	}

}
