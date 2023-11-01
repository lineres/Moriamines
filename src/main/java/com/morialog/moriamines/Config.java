package com.morialog.moriamines;

import com.morialog.moriamines.GUIs.GUI;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

// TODO : issue 9 'Remplir la classe Config'
public class Config {

	private static final Preferences prefs = Preferences.userNodeForPackage( Config.class );

	public static boolean getBackgroundMusicStatus() {
		return prefs.getBoolean( "isBackgroundMusicusicOn", false );
	}

	public static void setBackgroundMusicStatus( boolean newValue ) {
		prefs.putBoolean( "isBackgroundMusicusicOn", newValue );
	}

	public static double getBackgroundMusicVolume() {
		return prefs.getDouble( "backgroundMusicVolume", 50.0 ) / 100;
	}

	public static void setBackgroundMusicVolume( double newValue ) {
		prefs.putDouble( "backgroundMusicVolume", newValue );
	}

	public static boolean getEffectsSoundMusicStatus() {
		return prefs.getBoolean( "isEffectsSoundMusicOn", true );
	}

	public static void setEffectsSoundMusicStatus( boolean newValue ) {
		prefs.putBoolean( "isEffectsSoundMusicOn", newValue );
	}

	public static double getEffectsSoundVolume() {
		return prefs.getDouble( "effectsSoundVolume", 50.0 ) / 100;
	}

	public static void setEffectsSoundVolume( double newValue ) {
		prefs.putDouble( "effectsSoundVolume", newValue );
	}

	public static boolean getFullscreenStatus() {
		return prefs.getBoolean( "fullscreen", false );
	}

	public static void setFullsreenStatus( boolean newValue ) {
		prefs.putBoolean( "fullscreen", newValue );
	}


	public static double getStageWidthBeforeMaximized() {
		return prefs.getDouble( "maximizedsizeWidth", 700 );
	}

	public static void setStageWidthBeforeMaximized( double width ) {
		prefs.putDouble( "maximizedsizeWidth", width );
	}

	public static double getStageHeightBeforeMaximized() {
		return prefs.getDouble( "maximizedsizeHeight", 700 );
	}

	public static void setStageHeightBeforeMaximized( double height ) {
		prefs.putDouble( "maximizedsizeHeight", height );
	}

	public static List<String> getProfiles() {
		String profilesStr = prefs.get( "profiles", null );
		if( profilesStr == null )
			return null;
		else
			return Arrays.asList( profilesStr.split( "," ) );
	}

	public static void addProfile( String profileName ) {
		var profiles = getProfiles();
		if( profiles != null && getProfiles().contains( profileName ) ) return;
		String profilesStr = prefs.get( "profiles", "" );
		profilesStr += ( profilesStr.isBlank() ? "" : "," ) + profileName;
		prefs.put( "profiles", profilesStr );
	}

	public static void deleteProfile( String profileName ) {
		var profiles = getProfiles();
		String profilesStr = "";
		if( profiles != null ) {
			for( int i = 0; i < profiles.size(); i++ ) {
				String profName = profiles.get( i );
				if( !profName.equals( profileName ) )
					profilesStr += profName + ( i == profiles.size() - 1 ? "" : "," );
			}
		}
		prefs.put( "profiles", profilesStr );
	}

	public static void resetAllSettings() {
		try {
			prefs.clear();
			FileManager.deleteDataFolder();
		} catch( BackingStoreException e ) {
			e.printStackTrace();
		}
	}

	public static String getLastUsedProfile() {
		return prefs.get( "lastUsedProfile", "Default" );
	}

	public static void setLastUsedProfile( String profileName ) {
		prefs.put( "lastUsedProfile", profileName );
	}

	public static String getBackGroundColor() {
		return prefs.get( "backgroundColor", "-fx-background-color:#dddddd;" );
	}

	public static void setBackGroundColor( String newColor ) {
		prefs.put( "backgroundColor", newColor );
	}
	
	public static double getStageWidth() {
		return prefs.getDouble("stageWidth",820 );
	}
	
	public static void setStageWidth(double val) {
		prefs.putDouble("stageWidth", val);
	}
	
	public static double getStageHeight() {
		return prefs.getDouble("stageHeight",450);
	}
	
	public static void setStageHeight(double val) {
		prefs.putDouble("stageHeight", val);
	}
	
	public static String getCurrentGUI() {
		return prefs.get("currentGUI",GUI.PARTIE.toString());
	}
	
	public static void setCurrentGUI(GUI gui) {
		prefs.put("currentGUI",gui.toString());
 	}
}
