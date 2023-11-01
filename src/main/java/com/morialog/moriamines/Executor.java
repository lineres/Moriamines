package com.morialog.moriamines;

import com.morialog.moriamines.profile.ProfileManager;

public class Executor {

	public static void main( String[] args ) {
		ProfileManager.initialise();
		MainFrame.main();
	}

}
