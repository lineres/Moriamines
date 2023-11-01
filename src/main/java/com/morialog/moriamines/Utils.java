package com.morialog.moriamines;

import com.morialog.moriamines.GUIs.GUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class Utils {

	public static Parent getParentFromRessource( GUI gui ) throws IOException {
		String address = "/FXML/" + gui.name + ".fxml";
		return FXMLLoader.load( getResource( address ) );
	}

	public static URL getResource( String address ) {
		return Utils.class.getResource( address );
	}

}
