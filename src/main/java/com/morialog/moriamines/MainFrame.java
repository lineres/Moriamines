package com.morialog.moriamines;

import com.morialog.moriamines.GUIs.GUI;
import com.morialog.moriamines.Partie.Partie;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainFrame extends Application {

	public static MainFrame mainInstance;
	public Stage primaryStage;
	public static Parent layout;
	public static Scene scene;

	public static void transferTo( GUI destination ) {
		Stage primaryStage = mainInstance.primaryStage;

		if( !primaryStage.isMaximized() && ( !Config.getCurrentGUI().equals( GUI.SETTINGS.toString() ) || primaryStage.getHeight() > 700 ) ) {
			Config.setStageHeight( primaryStage.getHeight() );
			Config.setStageWidth( primaryStage.getWidth() );
		}
		FadeTransition fade = new FadeTransition();
		fade.setDuration( Duration.millis( 100 ) );
		fade.setFromValue( 20 );
		fade.setToValue( 0.3 );
		fade.setNode( MainFrame.layout );
		fade.setCycleCount( 1 );
		fade.setAutoReverse( true );
		fade.play();
		fade.setOnFinished( ( event ) -> {
			try {
				MainFrame.layout = Utils.getParentFromRessource( destination );
			} catch( IOException e ) {
				e.printStackTrace();
			}
			if( ( destination == GUI.SETTINGS && Config.getStageHeight() < 700 && !primaryStage.isMaximized() ) ) {
				primaryStage.setHeight( 700 );
			}
			primaryStage.getScene().setRoot( MainFrame.layout );
			Config.setCurrentGUI( destination );
			if( !primaryStage.isMaximized() && !( Config.getCurrentGUI().equals( GUI.SETTINGS.toString() ) ) ) {
				primaryStage.setWidth( Config.getStageWidth() );
				primaryStage.setHeight( Config.getStageHeight() );
			}
			primaryStage.setMinHeight( ( destination == GUI.SETTINGS ) ? 700 : 450 );
			bgRefresh();
			appearance();
		} );
		primaryStage.setTitle( "MoriaMines - " + destination.name );
		SoundsManager.playSound( "gui_switch.wav" );
	}

	//background
	public static void setBg( String bg ) {
		String finalbg = bg + ( bg.contains( "-fx-background-size: cover;" ) ? "" : "-fx-background-size: cover;" );
		Config.setBackGroundColor( finalbg );
		mainInstance.primaryStage.getScene().getRoot().setStyle( finalbg );
	}

	@Override
	public void stop() {
		if( Partie.currentGame != null )
			Partie.currentGame.endTheGame();
		Platform.exit();
	}

	@Override
	public void start( Stage primaryStage ) throws IOException {
		MainFrame.mainInstance = this;
		this.primaryStage = primaryStage;
		mainInstance.primaryStage.setWidth( 830 );
		mainInstance.primaryStage.setHeight( 450 );
		MainFrame.layout = Utils.getParentFromRessource( GUI.MAIN_MENU );
		MainFrame.scene = new Scene( MainFrame.layout );
		primaryStage.setScene( MainFrame.scene );
		primaryStage.setMinWidth( primaryStage.getWidth() );
		primaryStage.setMinHeight( primaryStage.getHeight() );

		primaryStage.setFullScreen( Config.getFullscreenStatus() );
		primaryStage.setFullScreenExitKeyCombination( KeyCombination.NO_MATCH );
		Config.setStageHeightBeforeMaximized( primaryStage.getHeight() );
		Config.setStageWidthBeforeMaximized( primaryStage.getWidth() );
		primaryStage.maximizedProperty().addListener( ( e ) -> {
			if( !primaryStage.isMaximized() ) {
				if( Config.getCurrentGUI().equals( GUI.SETTINGS.toString() ) && primaryStage.getHeight() < 700 )
					primaryStage.setHeight( 700 );
				else
					primaryStage.setHeight( Config.getStageHeightBeforeMaximized() );
				primaryStage.setWidth( Config.getStageWidthBeforeMaximized() );

			}
		} );


		primaryStage.heightProperty().addListener( ( e ) -> {
			if( !primaryStage.isMaximized() ) {
				Config.setStageHeightBeforeMaximized( primaryStage.getHeight() );
			}
			bgRefresh();
		} );
		primaryStage.widthProperty().addListener( ( e ) -> {
			if( !primaryStage.isMaximized() ) {
				Config.setStageWidthBeforeMaximized( primaryStage.getWidth() );
			}
			bgRefresh();
		} );
		bgRefresh();

		try {
			primaryStage.getIcons().add( new Image( String.valueOf( Utils.getResource( "/logo.png" ) ) ) );
		} catch( Exception ignored ) {
		}

		primaryStage.show();
		SoundsManager.playBackgroundMusic();
	}

	public static void appearance() {
		FadeTransition fade = new FadeTransition();
		fade.setDuration( Duration.millis( 100 ) );
		fade.setFromValue( 0.3 );
		fade.setToValue( 20 );
		fade.setNode( MainFrame.layout );
		fade.setCycleCount( 1 );
		fade.setAutoReverse( true );
		fade.play();
	}

	//background

	public static void bgRefresh() {
		setBg( Config.getBackGroundColor() );
	}

	public static void main() {
		launch();
	}

}