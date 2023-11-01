
package com.morialog.moriamines.GUIs;

import com.morialog.moriamines.Config;
import com.morialog.moriamines.FileManager;
import com.morialog.moriamines.MainFrame;
import com.morialog.moriamines.Partie.Item;
import com.morialog.moriamines.Partie.Partie;
import com.morialog.moriamines.profile.Profile;
import com.morialog.moriamines.profile.ProfileManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class MainMenu {

	@FXML
	public Label currentProfileLabel;

	@FXML
	public void initialize() {
		MainFrame.mainInstance.primaryStage.setTitle( "MoriaMines - Main menu" );

		if( ProfileManager.currentProfile != null ) {
			String leaderboardString = "";
			var profilesString = Config.getProfiles();
			if( profilesString != null && profilesString.size() > 1 ) {
				leaderboardString = "Top 3 profiles : \n";

				ArrayList<Profile> profiles = new ArrayList<>();
				profilesString.forEach( ( p ) -> profiles.add( ProfileManager.getProfileFromName( p ) ) );
				//sorting profiles
				profiles.sort( ( p1, p2 ) -> {
					if( p1.getMaxScore() != p2.getMaxScore() )
						return p2.getMaxScore() - p1.getMaxScore();
					else
						return p2.getSecondsForMaxScore() - p1.getSecondsForMaxScore();
				} );
				for( Profile profile : profiles ) {
					int score = profile.getMaxScore();
					leaderboardString += "  " + profile.getName() + " : " + ( score == 0 ? "none" : score + "/" + Item.count() ) + "\n";
				}
				currentProfileLabel.setMaxHeight( Double.MAX_VALUE );
			}

			currentProfileLabel.setText(
					  " Current profile : " + ProfileManager.currentProfile.getName() + "\n" +
					  " Creation date : " + ProfileManager.currentProfile.getCreationDate() + "\n" +
					  " Best score : " + ( ProfileManager.currentProfile.getMaxScore() == 0 ? "none\n" : ProfileManager.currentProfile.getMaxScore() + "/" + Item.count() +
																										 ProfileManager.currentProfile.getTimeForMaxScore() ) + "\n" +
					  leaderboardString
									   );

		}
	}

	@FXML
	private void newGameButtonPressed() { // Start new game
		new Partie();
		MainFrame.transferTo( GUI.PARTIE );
	}

	public void continueSavedGameButtonPressed() { // Load saved game and start it
		Partie savedGame = (Partie) FileManager.deSerialize( "Save/" + ProfileManager.currentProfile.getName() + ".msa" );
		if( savedGame == null )
			return;
		PartieGUI.loadingSave = true;
		new Partie();
		MainFrame.transferTo( GUI.PARTIE );
	}

	public void settingsButtonPressed() { // Open settings menu
		MainFrame.transferTo( GUI.SETTINGS );
	}

	@FXML
	private void aboutUs() {
		Alert dialog = new Alert(
				  Alert.AlertType.INFORMATION,
				  "We are a group of five students of the University of Paris : \n" +
				  "Arbi, Virgile, Changrui, Sandra and Yasmina\n" +
				  "MoriaMines is our educational project we did during the 4th semester.\n",
				  ButtonType.OK
		);
		dialog.initOwner( MainFrame.mainInstance.primaryStage );
		dialog.setTitle( "About us" );
		dialog.setHeaderText( null );
		dialog.showAndWait();
	}

	public void exitButtonPressed() {
		MainFrame.mainInstance.stop();
	}

}