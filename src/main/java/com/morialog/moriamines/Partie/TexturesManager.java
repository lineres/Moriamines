package com.morialog.moriamines.Partie;


import com.morialog.moriamines.Utils;
import javafx.scene.image.Image;

import java.util.HashMap;

public class TexturesManager {

	private static final HashMap<String, Image> textures = new HashMap<>();

	public static Image getTexture( Item ID, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth ) {
		return getTexture( ID.toString(), requestedWidth, requestedHeight, preserveRatio, smooth );
	}

	public static Image getTexture( String textureName, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth ) {
		if( textures.containsKey( textureName ) ) return textures.get( textureName );
		try {
			String pathToImage = Utils.getResource( "/Textures/" + textureName + ".png" ).toString();
			Image img = new Image( pathToImage, requestedWidth, requestedHeight, preserveRatio, smooth );
			textures.put( textureName, img );
			return img;
		} catch( IndexOutOfBoundsException a ) {
			System.out.println( "Failed to load texture for" + textureName );
			a.printStackTrace();
		}
		return null;
	}

}
