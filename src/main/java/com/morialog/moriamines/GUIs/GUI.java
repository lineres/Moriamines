package com.morialog.moriamines.GUIs;

public enum GUI {

	// nom du menu( nom du fichier .FXML )
	MAIN_MENU( "MainMenu" ),
	SETTINGS( "Settings" ),
	PARTIE( "PartieGUI" );

	public String name;

	GUI( String label ) {this.name = label;}

}
