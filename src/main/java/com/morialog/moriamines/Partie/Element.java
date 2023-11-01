package com.morialog.moriamines.Partie;

import com.morialog.moriamines.GUIs.MyImageView;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Element implements Serializable{

	private final Item ID;
	private final MyImageView imageView;

	public Element( Item ID ) {
		this.ID = ID;
		this.imageView = new MyImageView( TexturesManager.getTexture( this.ID, 100, 100, false, false )
		);
	}

	public Element( String itemName ) {
		this( Item.valueOf( itemName ) );
	}

	public String getName() {
		return this.ID.getName();
	}

	public Item getID() {
		return this.ID;
	}

	public MyImageView getImageView() { return imageView; }

	@Override
	public String toString() {
		return getName();
	}


	public static Item merge( Item a, Item b ) {
		BufferedReader reader = null;
		try {
			InputStream is = Element.class.getResourceAsStream( "/Craft/MergeRecipes.txt" );
			if( is == null )
				throw new NullPointerException();
			reader = new BufferedReader( new InputStreamReader( is ) );
			String tmp;
			while( ( tmp = reader.readLine() ) != null ) {
				if( tmp.equals( "IsRandomMining" ) ) // pour éviter allez dans les taux de drop
					break;

				ArrayList<Item> composant = transforme( tmp );
				Item resultat = composant.get( 0 );
				composant.remove( 0 );
				if( areTheSameElements( composant.get( 0 ), composant.get( 1 ), a, b ) ) {
					if( isRandomMerge( reader, resultat ) )
						resultat = randomMerge( reader, a, b );
					reader.close();
					return resultat;
				}
			}
			reader.close();
		} catch( FileNotFoundException e ) {
			e.printStackTrace();
			try {
				reader.close();
			} catch( IOException ioException ) {
				ioException.printStackTrace();
			}
		} catch( IOException ignored ) {
		}
		return null;
	}

	public static ArrayList<Item> transforme( String str ) { // transforme un string en une liste d'Element (permet de read un craft)
		ArrayList<Item> liste = new ArrayList<>();
		for( String elemName : str.split( "," ) )
			liste.add( Item.valueOf( elemName ) );
		return liste;
	}

	public static boolean areTheSameElements( Item a, Item b, Item c, Item d ) { // test si a et b == c et d
		return ( a == c || a == d ) && ( b == c || b == d );
	}

	public static boolean isRandomMerge( BufferedReader reader, Item a ) {
		String tmp;
		try {
			while( ( tmp = reader.readLine() ) != null ) { //pour aller jusqu'à la partie qui nous interresse
				if( tmp.equals( "IsRandomMining" ) )
					break;
			}
			while( ( tmp = reader.readLine() ) != null ) {
				if( a.toString().equals( tmp ) )
					return true;
				if( tmp.equals( "DROP" ) )
					return false;
			}
		} catch( IOException e ) {
			e.printStackTrace();
		}
		return false;
	}

	public static Item randomMerge( BufferedReader reader, Item a, Item b ) {

		String tmp;
		try {
			while( ( tmp = reader.readLine() ) != null ) {
				if( tmp.equals( "DROP" ) )
					break;
			}
			while( ( tmp = reader.readLine() ) != null ) {
				String[] composants = tmp.substring( 0, tmp.indexOf( "[" ) ).split( "," );
				if( areTheSameElements( Item.valueOf( composants[0] ), Item.valueOf( composants[1] ), a, b ) )
					return checkTauxDrope( tmp );
			}
		} catch( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}

	public static Item checkTauxDrope( String str ) {
		boolean checkElement = true;
		String strElem = "";
		String strInt = "";
		int dropRate;
		int min = 0;
		Random rand = new Random();
		int intRolled = rand.nextInt( 100 );
		for( int j = str.indexOf( '[' ) + 1; str.charAt( j ) != ']'; j++ ) {
			if( str.charAt( j ) != ':' && checkElement )
				strElem += str.charAt( j );
			else if( str.charAt( j ) == ':' )
				checkElement = false;
			else if( str.charAt( j ) != ',' && !checkElement )
				strInt += str.charAt( j );
			else if( str.charAt( j ) == ',' ) {
				dropRate = Integer.parseInt( strInt );
				if( intRolled < dropRate + min )
					return Item.valueOf( strElem );
				else {
					min += dropRate;
					strInt = "";
					strElem = "";
					checkElement = true;
				}
			}
		}
		return null;
	}


	public static void main( String[] args ) { // utilisez ce main pour faire des test


		for( int i = 0; i < 25; i++ ) {
			Item resultat = merge( Item.STONE, Item.DIAMOND_PICKAXE );
			if( resultat == null ) {
				System.out.println( "failed to merge or the craft do not exist" );
			} else
				System.out.println( resultat );
		}

		System.out.println( "-".repeat( 20 ) );
		System.out.println( merge( Item.DIAMOND, Item.IRON_PICKAXE ) );

	}


}