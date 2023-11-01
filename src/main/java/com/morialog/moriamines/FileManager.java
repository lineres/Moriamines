package com.morialog.moriamines;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class FileManager {

	public static <T> boolean serialize( T object, String path ) {
		try {
			path = getAppsDataFolderPath() + path;
			for( int i = path.length() - 1; i >= 0; i-- )
				if( path.charAt( i ) == '/' || path.charAt( i ) == '\\' ) {
					File f = new File( path.substring( 0, i + 1 ) );
					if( !f.exists() )
						if( !f.mkdirs() )
							return false;
					break;
				}
			ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( path ) );
			oos.writeObject( object );
			oos.close();
		} catch( IOException ignored ) {
			return false;
		}
		return true;
	}

	public static Object deSerialize( String path ) {
		try {
			File fichier = new File( getAppsDataFolderPath() + path );
			ObjectInputStream ois = new ObjectInputStream( new FileInputStream( fichier ) );
			Object res = ois.readObject();
			ois.close();
			return res;
		} catch( IOException | ClassNotFoundException ignored ) {
		}
		return null;
	}

	public static void deleteFile( String path ) {
		path = getAppsDataFolderPath() + path;
		try {
			Files.deleteIfExists( Paths.get( path ) );
		} catch( Exception ignored ) {
		}
	}

	public static String getAppsDataFolderPath() { // you should read this like "get App's data folder", not "Appdata"
		String workingDirectory;
		if( System.getProperty( "os.name" ).toUpperCase().contains( "WIN" ) ) {
			workingDirectory = System.getenv( "AppData" );
		} else {
			workingDirectory = System.getProperty( "user.home" );
			// on ubuntu, the files are saved in HOME/.MoriaMines/
		}
		return workingDirectory + "/.MoriaMines/";
	}

	public static void deleteDataFolder() {
		try {
			Files.walk( Path.of( getAppsDataFolderPath() ) )
				 .sorted( Comparator.reverseOrder() )
				 .map( Path::toFile )
				 .forEach( File::delete );
		} catch( NoSuchFileException ignored ) {
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}

}
