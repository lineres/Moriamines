package com.morialog.moriamines.GUIs;

import com.morialog.moriamines.Config;
import com.morialog.moriamines.MainFrame;
import com.morialog.moriamines.SoundsManager;
import com.morialog.moriamines.Utils;
import com.morialog.moriamines.profile.ProfileManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;

public class Settings {

	private static boolean bgMessageAdded = false;

	ObservableList<String> listBackGroundImages = FXCollections.observableArrayList( "white", "black", "gradient_1", "rocks" );

	@FXML
	public CheckBox fullscreenCheckBox;

	@FXML
	private void changeFullScreen() {
		Stage primaryStage = MainFrame.mainInstance.primaryStage;
		if( !fullscreenCheckBox.isSelected() ) {
			if( primaryStage.isFullScreen() )
				primaryStage.setFullScreen( false );
			Config.setStageHeight( Config.getStageHeightBeforeMaximized() );
			Config.setStageWidth( Config.getStageWidthBeforeMaximized() );
			primaryStage.setHeight( Config.getStageHeightBeforeMaximized() );
			primaryStage.setWidth( Config.getStageWidthBeforeMaximized() );
		} else {
			Config.setStageHeightBeforeMaximized( primaryStage.getHeight() );
			Config.setStageWidthBeforeMaximized( primaryStage.getWidth() );
		}
		Config.setFullsreenStatus( fullscreenCheckBox.isSelected() );
		primaryStage.setFullScreen( fullscreenCheckBox.isSelected() );
	}

	// profiles related :

	@FXML
	private ComboBox<String> profileSelector;

	public void onProfileSelectorAction() {
		if( !ProfileManager.currentProfile.getName().equals( profileSelector.getValue() ) ) {
			ProfileManager.switchProfileTo( profileSelector.getValue() );
		}
	}

	TextInputDialog profileCreatorDialog = new TextInputDialog( "" );

	@FXML
	public void createNewProfileEvent() {
		Optional<String> result = profileCreatorDialog.showAndWait();
		if( result.isPresent() ) {
			String profileName = result.get().replaceAll( ",", "" );
			if( profileName.length() > 12 )
				profileName = profileName.substring( 0, 12 );
			if( profileName.isBlank() )
				return;
			ProfileManager.createProfile( profileName );
			profileSelector.setItems( FXCollections.observableArrayList( Config.getProfiles() ) );
			profileCreatorDialog.getEditor().clear();
		}
	}

	@FXML
	public void resetCurrentProfileEvent() {
		ButtonType yes = new ButtonType( "Yes", ButtonBar.ButtonData.YES );
		ButtonType no = new ButtonType( "No", ButtonBar.ButtonData.NO );
		Alert alert = new Alert(
				  Alert.AlertType.CONFIRMATION,
				  "Are you sure you want to reset the current profile " + ProfileManager.currentProfile.getName() + " ?\n",
				  yes, no
		);
		alert.initOwner( MainFrame.mainInstance.primaryStage );
		alert.setHeaderText( null );
		alert.showAndWait();
		if( alert.getResult() == yes ) {
			ProfileManager.resetCurrentProfile();
		}
	}

	@FXML
	public void deleteCurrentProfileEvent() {
		ButtonType yes = new ButtonType( "Yes", ButtonBar.ButtonData.YES );
		ButtonType no = new ButtonType( "No", ButtonBar.ButtonData.NO );
		Alert alert = new Alert(
				  Alert.AlertType.CONFIRMATION,
				  "Are you sure you want to delete the current profile " + ProfileManager.currentProfile.getName() + " ?\n",
				  yes, no
		);
		alert.initOwner( MainFrame.mainInstance.primaryStage );
		alert.setHeaderText( null );
		alert.showAndWait();
		if( alert.getResult() == yes ) {
			ProfileManager.deleteProfile( ProfileManager.currentProfile.getName() );
			profileSelector.setItems( FXCollections.observableArrayList( Config.getProfiles() ) );
			profileSelector.setValue( ProfileManager.currentProfile.getName() );
		}
	}

	// audio related :

	@FXML
	private CheckBox backgroundMusicCheckBox;

	@FXML
	private void changeBackgroundMusic() {
		Config.setBackgroundMusicStatus( backgroundMusicCheckBox.isSelected() );
		SoundsManager.playBackgroundMusic();
	}

	@FXML
	private void changeBackgroundMusicLabelClicked() {
		backgroundMusicCheckBox.setSelected( !backgroundMusicCheckBox.isSelected() );
		changeBackgroundMusic();
	}

	@FXML
	private Slider backgroundMusicSlider;

	@FXML
	private void backgroundMusicSliderScroll() {
		Config.setBackgroundMusicVolume( backgroundMusicSlider.getValue() );
		SoundsManager.backgroundMusic.setVolume( Config.getBackgroundMusicVolume() );
	}

	@FXML
	private CheckBox effectsSoundsCheckBox;

	@FXML
	private void changeEffectsMusic() {
		Config.setEffectsSoundMusicStatus( effectsSoundsCheckBox.isSelected());
	}

	@FXML
	private void changeEffectsMusicClicked() {
		effectsSoundsCheckBox.setSelected( !effectsSoundsCheckBox.isSelected() );
		changeEffectsMusic();
	}

	@FXML
	private Slider effectsSoundsSlider;

	@FXML
	private void effectsSoundSliderScroll() {
		Config.setEffectsSoundVolume( effectsSoundsSlider.getValue() );
	}

	//back ground related :

	@FXML
	private ComboBox<String> bgImage;//skin choice

	@FXML
	public void initialize() {
		backgroundMusicCheckBox.setSelected( Config.getBackgroundMusicStatus() );
		backgroundMusicSlider.setValue( Config.getBackgroundMusicVolume() * 100 );
		effectsSoundsCheckBox.setSelected( Config.getEffectsSoundMusicStatus() );
		effectsSoundsSlider.setValue( Config.getEffectsSoundVolume() * 100 );
		fullscreenCheckBox.setSelected( Config.getFullscreenStatus() );
		String savedColor = Config.getBackGroundColor();
		bgImage.setValue( "Choose a skin" );
		bgImage.setItems( listBackGroundImages );
		if( savedColor.contains( "image" ) ) {
			bgImage.setValue( savedColor.substring( savedColor.indexOf( "bgImage/" ) + 8, savedColor.indexOf( " \");-fx-background-size:" ) - 4 ) );
		} else
			colorPicker.setValue( Color.web( savedColor.substring( 21, 28 ) ) );
		profileSelector.setItems( FXCollections.observableArrayList( Config.getProfiles() ) );
		profileSelector.setValue( ProfileManager.currentProfile.getName() );
		profileCreatorDialog.setTitle( "New profile creator" );
		profileCreatorDialog.setContentText( "Write the new profile's name :" );
		profileCreatorDialog.initOwner( MainFrame.mainInstance.primaryStage );
		profileCreatorDialog.setHeaderText( null );
		( (Button) profileCreatorDialog.getDialogPane().lookupButton( ButtonType.CANCEL ) ).setText( "Cancel" );
	}

	@FXML
	private ColorPicker colorPicker;

	@FXML
	private void darkMode() {
		MainFrame.setBg( "-fx-background-color:#121212;" );
		colorPicker.setValue( Color.web( "#121212" ) );
		bgImageMessageReset();
	}

	@FXML
	private void bgImageShownAction() {
		if( !bgMessageAdded ) {
			bgImage.setItems( listBackGroundImages );
			Settings.bgMessageAdded = true;
		}
	}

	@FXML
	private void bgImageHiddenAction() {
		if( bgImage.getValue() != null && !bgImage.getValue().equals( "Choose a skin" ) ) {
			URL imageURL = Utils.getResource( "/bgImage/" + bgImage.getValue() + ".jpg" ); // trying to find the jpg image
			if( imageURL == null )
				imageURL = Utils.getResource( "/bgImage/" + bgImage.getValue() + ".png" );
			if( imageURL == null )
				return;
			String bg = "-fx-background-image:url(\" " + imageURL + " \");";
			MainFrame.setBg( bg );
			colorPicker.setValue( Color.web( "#dddddd" ) );
		}
	}

	public void bgImageMessageReset() {//reset selection skin message
		bgImage.getSelectionModel().clearSelection();
		bgImage.setButtonCell( new ListCell<>() {
			@Override
			protected void updateItem( String item, boolean empty ) {
				super.updateItem( item, empty );
				if( empty || item == null ) {
					setText( "Choose a skin" );
				} else {
					setText( item );
				}
			}
		});
	}

	@FXML
	private void bgReset() {
		bgImageMessageReset();
		MainFrame.setBg( "-fx-background-color:#dddddd;" );
		colorPicker.setValue( Color.web( "#dddddd" ) );
	}

	@FXML
	private void colorPicker() {
		String color = colorPicker.getValue().toString().substring( 2 );
		String bgColor = "-fx-background-color:#" + color + ";";
		MainFrame.setBg( bgColor );
		bgImageMessageReset();
	}

	// other

	@FXML
	private void changeFullScreenLabelClicked() {
		fullscreenCheckBox.setSelected( !fullscreenCheckBox.isSelected() );
		changeFullScreen();
	}

	@FXML
	private void returnButtonEvent() {
		MainFrame.transferTo( GUI.MAIN_MENU );
	}

	@FXML
	private void deleteAllSettingsOnAction() {
		SoundsManager.playSound( "warning.mp3" );
		ButtonType yes = new ButtonType( "Yes", ButtonBar.ButtonData.YES );
		ButtonType no = new ButtonType( "No", ButtonBar.ButtonData.CANCEL_CLOSE );
		Alert dialog = new Alert(
				  Alert.AlertType.CONFIRMATION,
				  "Are you sure you want to reset all settings ?\n" +
				  "This will automatically close the game and delete all your scores, profiles and saves !",
				  yes, no
		);
		dialog.initOwner( MainFrame.mainInstance.primaryStage );
		dialog.setHeaderText( null );
		dialog.showAndWait();
		if( dialog.getResult() == yes ) {
			Config.resetAllSettings();
			Platform.exit();
		}

	}
}

