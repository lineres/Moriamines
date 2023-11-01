package com.morialog.moriamines.Partie;

public enum Item {

	STICK( "Stick" ),
	COAL( "Coal" ),
	COBBLESTONE( "Cobblestone" ),
	DIAMOND( "Diamond" ),
	DIAMOND_AXE( "Diamond Axe" ),
	DIAMOND_ORE( "Diamond Ore" ),
	DIAMOND_PICKAXE( "Diamond Pickaxe" ),
	DIAMOND_SHOVEL( "Diamond Shovel" ),
	DIAMOND_SWORD( "Diamond Sword" ),
	FURNACE( "Furnace" ),
	IRON_AXE( "Iron Axe" ),
	IRON_INGOT( "Iron Ingot" ),
	IRON_ORE( "Iron Ore" ),
	IRON_PICKAXE( "Iron Pickaxe" ),
	IRON_SHOVEL( "Iron Shovel" ),
	IRON_SWORD( "Iron Sword" ),
	OAK_LOG( "Oak Log" ),
	OAK_PLANKS( "Oak Planks" ),
	STONE( "Stone" ),
	STONE_AXE( "Stone Axe" ),
	STONE_PICKAXE( "Stone Pickaxe" ),
	STONE_SHOVEL( "Stone Shovel" ),
	ANIMAL_EGG( "Animal Egg" ),
	RAW_CHICKEN( "Raw Chicken" ),
	COOKED_CHICKEN( "Cooked Chicken" ),
	RAW_BEEF( "Raw Beef" ),
	COOKED_BEEF( "Cooked Beef" ),
	RAW_PORKCHOP( "Raw Porkchop" ),
	COOKED_PORKCHOP( "Cooked Porkchop" ),
	RAW_MUTTON( "Raw Mutton" ),
	COOKED_MUTTON( "Cooked Mutton" ),
	DIRT( "Dirt" ),
	GRAVEL( "Gravel" ),
	FLINT( "Flint" ),
	FLINT_AND_STEEL( "Flint and Steel" ),
	CAMPFIRE( "Campfire" ),
	GRASS( "Grass" );

	private final String name;

	Item( String name ) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public static int count() { // return the amount of Items
		return Item.values().length;
	}

}
