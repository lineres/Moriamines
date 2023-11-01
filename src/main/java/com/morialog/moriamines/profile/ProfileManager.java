package com.morialog.moriamines.profile;

import com.morialog.moriamines.Config;
import com.morialog.moriamines.FileManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProfileManager {

	public static Profile currentProfile;

	public static void switchProfileTo( String pfName ) {
		Profile pf = getProfileFromName( pfName );
		if( pf != null ) {
			currentProfile = pf;
			Config.setLastUsedProfile( pfName );
		}
	}

	public static void switchProfileTo( Profile profile ) {
		if( profile != null ) {
			if( currentProfile != null )
				currentProfile.saveProfile();
			currentProfile = profile;
			Config.setLastUsedProfile( profile.getName() );
		}

	}

	public static void createProfile( String profileName ) {
		Profile newPf = new Profile( profileName );
		if( FileManager.serialize( newPf, "Profiles/" + newPf.getName() + ".prof" ) ) {
			Config.addProfile( profileName );
			if( currentProfile == null ) switchProfileTo( newPf );
		}
	}


	public static void deleteProfile( String profileName ) {
		if( currentProfile != null && currentProfile.getName().equals( profileName ) ) {
			var profiles = Config.getProfiles();
			String profileToSwitchOn = "Default";
			if( profiles != null ) {
				if( profiles.size() == 1 ) {
					if( profileName.equals( "Default" ) )
						resetCurrentProfile();
					else {
						createProfile( "Default" );
						deleteProfile( profileName );
						switchProfileTo( "Default" );
					}
					return;
				} else if( profiles.size() > 1 )
					profileToSwitchOn = profiles.get( 0 ).equals( profileName ) ? profiles.get( 1 ) : profiles.get( 0 );
			}
			switchProfileTo( getProfileFromName( profileToSwitchOn ) );
		}
		Config.deleteProfile( profileName );
		FileManager.deleteFile( "Profiles/" + profileName + ".prof" );
	}

	public static void resetCurrentProfile() {
		currentProfile.setCreationDate( DateTimeFormatter.ofPattern( "dd/MM/yyyy" ).format( LocalDateTime.now() ) );
		currentProfile.setMaxScore( 0 );
		currentProfile.setSecondsForMaxScore( 0 );
		currentProfile.saveProfile();
	}

	public static void initialise() {
		var profiles = Config.getProfiles();
		if( profiles != null )
			for( String pfName : profiles ) {
				Profile profile = getProfileFromName( pfName );
				if( profile == null ) {
					deleteProfile( pfName );
				}
			}
		Profile pf = getProfileFromName( Config.getLastUsedProfile() );
		if( pf != null ) {
			switchProfileTo( pf );
		} else {
			profiles = Config.getProfiles();
			if( profiles == null || profiles.size() <= 1 ) {
				createProfile( "Default" );
				switchProfileTo( "Default" );
			} else {
				switchProfileTo( profiles.get( 0 ) );
			}
		}
	}

	public static Profile getProfileFromName( String name ) {
		return (Profile) FileManager.deSerialize( "Profiles/" + name + ".prof" );
	}


}
