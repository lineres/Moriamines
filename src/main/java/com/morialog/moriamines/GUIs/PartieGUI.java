package com.morialog.moriamines.GUIs;

import com.morialog.moriamines.FileManager;
import com.morialog.moriamines.MainFrame;
import com.morialog.moriamines.Partie.*;
import com.morialog.moriamines.SoundsManager;
import com.morialog.moriamines.profile.Profile;
import com.morialog.moriamines.profile.ProfileManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Random;

public class PartieGUI {

	@FXML
	Pane gamePane;
	@FXML
	ListView<Element> listPane;
	@FXML
	Label scoreLabel;
	ImageView binImageView;
	Alert randomElementAlert;

	private static final int HEIGHT = 100;
	private static final int WIDTH = 100;
	public static boolean loadingSave = false;

	@FXML
	public void initialize() {
		listPane.setCellFactory( listView -> new ListCell<>() {
			@Override
			protected void updateItem( Element element, boolean empty ) {
				super.updateItem( element, empty );
				if( empty )
					setGraphic( null );
				else {
					VBox vBox = new VBox( 5 );
					vBox.setAlignment( Pos.CENTER );
					vBox.getChildren().add( element.getImageView() );
					vBox.getChildren().add( new Label( element.getID().getName() ) );
					setGraphic( vBox );
				}
			}
		} );

		if( Partie.currentGame.unlockedElements.size() != 0 ) {
			for( Element element : Partie.currentGame.unlockedElements ) {
				setListPaneEvent( element );
				listPane.getItems().add( element );
			}
		}

		// the four starter elements
		Partie a = (Partie) FileManager.deSerialize( "Save/" + ProfileManager.currentProfile.getName() + ".msa" );
		unlock( Item.OAK_LOG, false );
		unlock( Item.GRASS, false );
		unlock( Item.STONE, false );
		unlock( Item.STONE_PICKAXE, false );
		unlock( Item.STONE_AXE, false );
		unlock( Item.STONE_SHOVEL, false );
		unlock( Item.ANIMAL_EGG, false );
		if( !loadingSave || a == null ) {
			Partie.currentGame.chrono.start();
		} else {
			for( int i = 7; i < a.unlockedElements.size(); i++ )
				unlock( a.unlockedElements.get( i ).getID(), false );
			Partie.currentGame.chrono = a.chrono;
			Partie.currentGame.chrono.resume();
			loadingSave = false;
		}

		// creating & placing the bin element
		binImageView = new MyImageView( TexturesManager.getTexture( "BIN", 100, 100, false, false ) );
		binImageView.setX( 0 );
		binImageView.setY( 10 );
		gamePane.getChildren().add( binImageView );
		binImageView.setPickOnBounds( true );
		binImageView.setOnDragOver( event -> {
			Dragboard db = event.getDragboard();
			if( db.hasImage() && event.getGestureSource() instanceof MyImageView && event.getGestureSource() != binImageView )
				event.acceptTransferModes( TransferMode.ANY );
		} );
		binImageView.setOnDragDropped( event -> {
			Dragboard db = event.getDragboard();
			if( db.hasImage() && event.getGestureSource() instanceof MyImageView && event.getGestureSource() != binImageView ) {
				MyImageView draggedImageView = (MyImageView) event.getGestureSource();
				Partie.currentGame.elementsOnGamePane.removeIf( elem -> elem.getImageView() == draggedImageView );
				refreshGamePane();
			}
		} );

		scoreLabel.setText( Partie.currentGame.unlockedElements.size() + " / " + Item.count() );
		scoreLabel.setLayoutY( MainFrame.mainInstance.primaryStage.getScene().getHeight() - (double) HEIGHT * 0.9 );
		MainFrame.mainInstance.primaryStage.getScene().heightProperty().addListener( ( e, oldvalue, newValue ) -> {
			if( Partie.currentGame != null ) {
				scoreLabel.setLayoutY( MainFrame.mainInstance.primaryStage.getScene().getHeight() - (double) HEIGHT * 0.8 );
				refreshGamePane();
			}
		} );
		refreshGamePane();
	}

	private void setListPaneEvent( Element element ) {
		element.getImageView().setOnMouseClicked( ( e ) -> {
			if( e.getClickCount() % 2 == 0 ) {
				Element elementToAddToGamePane = new Element( element.getID() );
				elementToAddToGamePane.getImageView().setX( MainFrame.mainInstance.primaryStage.getScene().getHeight() / 2 - (double) WIDTH / 2 );
				elementToAddToGamePane.getImageView().setY( MainFrame.mainInstance.primaryStage.getScene().getHeight() / 2 - (double) HEIGHT / 2 );
				setDragNDropEvents( elementToAddToGamePane );
				Partie.currentGame.elementsOnGamePane.add( elementToAddToGamePane );
				refreshGamePane();
			}
		} );
	}

	// if the element is not unlocked yet, it unlock it, add to the list and set listeners
	public void unlock( Item elementID, boolean playSound ) {
		if( !Partie.currentGame.isAlreadyUnlocked( elementID ) ) {
			Element clone = new Element( elementID );
			Partie.currentGame.unlockedElements.add( clone );
			clone.getImageView().setPickOnBounds( true );
			clone.getImageView().setOnMouseClicked( e -> {
				if( e.getClickCount() % 2 == 0 ) {
					Element elementToAddToGamePane = new Element( elementID );
					elementToAddToGamePane.getImageView().setX( MainFrame.mainInstance.primaryStage.getScene().getHeight() / 2 - (double) WIDTH / 2 );
					elementToAddToGamePane.getImageView().setY( MainFrame.mainInstance.primaryStage.getScene().getHeight() / 2 - (double) HEIGHT / 2 );
					setDragNDropEvents( elementToAddToGamePane );
					Partie.currentGame.elementsOnGamePane.add( elementToAddToGamePane );
					refreshGamePane();
				}
			} );
			listPane.getItems().add( clone );
			if( playSound )
				SoundsManager.playSound( "new_element_unlocked.mp3" );
		}
	}

	private void setDragNDropEvents( Element element ) {
		MyImageView imageView = element.getImageView();
		imageView.setOnDragDetected( event -> {
			Dragboard db = imageView.startDragAndDrop( TransferMode.ANY );
			ClipboardContent content = new ClipboardContent();
			content.putImage( imageView.getImage() );
			content.putString( element.getID().toString() );
			db.setContent( content );
			imageView.toBack();
		} );

		imageView.setOnDragOver( event -> {
			Dragboard db = event.getDragboard();
			if( db.hasImage() && !db.getString().equals( element.getID().getName() ) && event.getGestureSource() instanceof MyImageView ) {
				MyImageView draggedImageView = ( (MyImageView) event.getGestureSource() );
				if( imageView != draggedImageView ) // prevent that  it drag and drop on itself
					event.acceptTransferModes( TransferMode.ANY );
			}
		} );

		imageView.setOnDragDropped( event -> {
			Dragboard db = event.getDragboard();
			if( db.hasImage() && !db.getString().equals( element.getID().getName() ) && event.getGestureSource() instanceof MyImageView ) {
				MyImageView draggedImageView = ( (MyImageView) event.getGestureSource() );
				Item mergeResult = Element.merge( element.getID(), Item.valueOf( db.getString() ) );
				if( imageView == draggedImageView || mergeResult == null ) {
					SoundsManager.playSound( "merge_failed.mp3" );
					return;
				}
				var list = Partie.currentGame.elementsOnGamePane;
				list.removeIf( elem -> elem.getImageView() == draggedImageView || elem.getImageView() == imageView );
				Element newElement = new Element( mergeResult );
				newElement.getImageView().setX( event.getX() - (double) WIDTH / 2 );
				newElement.getImageView().setY( event.getY() - (double) HEIGHT / 2 );
				unlock( mergeResult, true );
				setDragNDropEvents( newElement );
				list.add( newElement );
				refreshGamePane();
			}
		} );
	}

	public void refreshGamePane() {
		gamePane.getChildren().clear();
		for( Element elementGUI : Partie.currentGame.elementsOnGamePane ) {
			checkAndFixCoordinates( elementGUI.getImageView() );
			gamePane.getChildren().add( elementGUI.getImageView() );
		}
		gamePane.getChildren().add( binImageView );
		gamePane.getChildren().add( scoreLabel );
		scoreLabel.setText( Partie.currentGame.unlockedElements.size() + " / " + Item.count() );
		if( Partie.currentGame.victory() && !Partie.currentGame.victory ) {
			Partie.currentGame.victory = true;
			victoryGui();
		}
	}

	public void gamePaneOnDragOver( DragEvent event ) { // for moving the imageviews
		if( event.getGestureSource() instanceof ImageView && event.getGestureSource() != binImageView ) {
			MyImageView draggedImageView = ( (MyImageView) event.getGestureSource() );
			draggedImageView.setX( event.getX() - ( (double) WIDTH / 2 ) );
			draggedImageView.setY( event.getY() - ( (double) HEIGHT / 2 ) );
			checkAndFixCoordinates( draggedImageView );
		}
	}

	public void checkAndFixCoordinates( ImageView imageView ) {
		if( imageView.getX() < 0 )
			imageView.setX( 0 );
		if( imageView.getY() < 0 )
			imageView.setY( 0 );
		if( imageView.getX() + WIDTH > MainFrame.mainInstance.primaryStage.getScene().getWidth() - WIDTH )
			imageView.setX( MainFrame.mainInstance.primaryStage.getScene().getWidth() - WIDTH * 2.3 );
		if( imageView.getY() + HEIGHT > MainFrame.mainInstance.primaryStage.getScene().getHeight() )
			imageView.setY( MainFrame.mainInstance.primaryStage.getScene().getHeight() - HEIGHT * 1.2 );

	}

	public void saveAndExit() {
		if( Partie.currentGame.victory ) {
			exit();
			return;
		}
		Partie.currentGame.chrono.pause();
		FileManager.serialize( Partie.currentGame, "Save/" + ProfileManager.currentProfile.getName() + ".msa" );
		Partie.currentGame = null;
		MainFrame.transferTo( GUI.MAIN_MENU );
	}

	public void exit() {
		Partie.currentGame.endTheGame();
		MainFrame.transferTo( GUI.MAIN_MENU );
	}

	@FXML
	public void showRandomNotUnlockedElement() {
		if( randomElementAlert == null ) {
			randomElementAlert = new Alert( Alert.AlertType.INFORMATION );
			randomElementAlert.initOwner( MainFrame.mainInstance.primaryStage );
			randomElementAlert.setTitle( "Not unlocked elements" );
			randomElementAlert.getDialogPane().setMaxWidth( 250 );
			randomElementAlert.getDialogPane().setHeader( new GridPane() );
		}
		Random rd = new Random();
		Item item = Item.values()[rd.nextInt( Item.values().length )];
		while( Partie.currentGame.isAlreadyUnlocked( item ) )
			item = Item.values()[rd.nextInt( Item.values().length )];

		GridPane grid = (GridPane) randomElementAlert.getDialogPane().getHeader();
		grid.getChildren().clear();
		grid.add( new ImageView( TexturesManager.getTexture( item, 100, 100, false, false ) ), 0, 0 );
		Label headerLabel = new Label( " " + item.getName() );
		grid.add( headerLabel, 1, 0 );
		randomElementAlert.getDialogPane().setHeader( grid );
		randomElementAlert.show();
	}

	private void victoryGui() {
		SoundsManager.playSound( "victory.mp3" );
		Alert dialog = new Alert(
				  Alert.AlertType.CONFIRMATION,
				  "You completed the game !\nYou unlocked all " + Item.count() + " elements " + Chronometer.formatDuration( Partie.currentGame.chrono.getDurationInSeconds() ),
				  ButtonType.OK
		);
		dialog.initOwner( MainFrame.mainInstance.primaryStage );
		dialog.setHeaderText( null );
		dialog.showAndWait();
	}

	public void cleanTab() {
		SoundsManager.playSound( "warning.mp3" );
		ButtonType yes = new ButtonType( "Yes", ButtonBar.ButtonData.YES );
		ButtonType no = new ButtonType( "No", ButtonBar.ButtonData.CANCEL_CLOSE );
		Alert dialog = new Alert(
				  Alert.AlertType.CONFIRMATION,
				  "Are you sure you want to remove all elements from the game ?",
				  yes, no
		);
		dialog.initOwner( MainFrame.mainInstance.primaryStage );
		dialog.setHeaderText( null );
		dialog.showAndWait();
		if( dialog.getResult() == yes ) {
			Partie.currentGame.elementsOnGamePane.clear();
			gamePane.getChildren().clear();
			gamePane.getChildren().add( binImageView );
			gamePane.getChildren().add( scoreLabel );
		}
	}

	public void playerInfo() {
		Profile profile = ProfileManager.currentProfile;
		Alert dialog = new Alert(
				  Alert.AlertType.INFORMATION,
				  "Current profile : \n" +
				  "		Name : " + profile.getName() + "\n" +
				  "		Creation date : " + profile.getCreationDate() + "\n" +
				  "		Best score : " + ( profile.getMaxScore() == 0 ? "none" : profile.getMaxScore() + "/" + Item.count() + profile.getTimeForMaxScore() ),
				  ButtonType.OK
		);
		dialog.initOwner( MainFrame.mainInstance.primaryStage );
		dialog.setTitle( "Current profile informations" );
		dialog.setHeaderText( null );
		dialog.show();
	}

	public void gameRules() {

		Alert dialog = new Alert(
				  Alert.AlertType.INFORMATION,
				  "Your goal in this game is to discover all " + Item.count() + " elements.\n" +
				  "You start with only 4 of them, and you have to choose two of them, merge them together to find " +
				  "and unlock an another on elemet.\n\n" +
				  "How to play : just double click on any element on the right side of your screen, it will appear in" +
				  " the game tab, add an another element and try to take one element and drop it into the other one, if" +
				  " they can merge between them then you will unlock a new element and it will appear on the right " +
				  "side of your screen so you can have an unlimited amount of them",
				  ButtonType.OK
		);
		dialog.initOwner( MainFrame.mainInstance.primaryStage );
		dialog.setTitle( "Your goal & how to play" );
		dialog.setHeaderText( null );
		dialog.showAndWait();
	}

}
