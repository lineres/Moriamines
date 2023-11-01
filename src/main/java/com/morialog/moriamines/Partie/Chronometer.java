package com.morialog.moriamines.Partie;

import java.io.Serializable;

public class Chronometer implements Serializable{

	private long startedAt = 0;
	private long endedAt = 0;
	private long pausedAt = 0;
	private long pauseEndedAt = 0;
	private long duration = 0;

	public void start() {
		startedAt = System.currentTimeMillis();
		endedAt = 0;
		pausedAt = 0;
		pauseEndedAt = 0;
		duration = 0;
	}

	public void pause() {
		if( startedAt == 0 ) {
			return;
		}
		pausedAt = System.currentTimeMillis();
	}

	public void resume() {
		if( startedAt == 0 || pausedAt == 0 )
			return;
		pauseEndedAt = System.currentTimeMillis();
		startedAt = startedAt + pauseEndedAt - pausedAt;
		endedAt = 0;
		pausedAt = 0;
		pauseEndedAt = 0;
		duration = 0;
	}

	public void stop() {
		if( startedAt == 0 )
			return;
		endedAt = System.currentTimeMillis();
		duration = ( endedAt - startedAt ) - ( pauseEndedAt - pausedAt );
		startedAt = 0;
		endedAt = 0;
		pausedAt = 0;
		pauseEndedAt = 0;
	}

	public int getDurationInSeconds() {
		return (int) duration / 1000;
	}

	public static String formatDuration( int seconds ) {
		String res = " in ";
		int minutes = seconds / 60;
		if( minutes > 0 )
			res += minutes + " minutes and ";
		res += seconds % 60 + " seconds\n";
		return res;
	}

}
