package com.planet_ink.coffee_mud.CharClasses.interfaces;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;
import java.util.*;
/* 
   Copyright 2000-2006 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
/**
 * This class represents a player or mobs character class.  One of more of these
 * objects are associated with each mob through the mob interfaces charStats() object.
 * @see com.planet_ink.coffee_mud.MOBS.interfaces.MOB#charStats()
 * @see com.planet_ink.coffee_mud.Common.interfaces.CharStats
 * @author Bo Zimmerman
 */
public interface CharClass extends Tickable, StatsAffecting, MsgListener, CMObject
{
	public String name();
    public String name(int classLevel);
    public String[] nameSet();
	public String baseClass();
	public int availabilityCode();
	public boolean qualifiesForThisClass(MOB mob, boolean quiet);
	public String classParms();
	public void setClassParms(String parms);
	public boolean isGeneric();
    public Vector getSecurityGroups(int classLevel);

	public void startCharacter(MOB mob, boolean isBorrowedClass, boolean verifyOnly);
	public void endCharacter(MOB mob);
	public void gainExperience(MOB mob, MOB victim, String homage, int amount, boolean quiet);
	public void loseExperience(MOB mob, int amount);
	public int getLevelExperience(int level);
	public void dispenseExperience(HashSet killers, MOB killed);
    public boolean isValidClassBeneficiary(MOB killer, MOB killed, MOB mob, HashSet followers);
    
	public void level(MOB mob);
	public void unLevel(MOB mob);
	public Vector outfit();
	
	public int classDurationModifier(MOB myChar, Ability skill, int duration);

	public MOB fillOutMOB(MOB mob, int level);

	public int getLevelMana(MOB mob);
	public double getLevelSpeed(MOB mob);
	public int getLevelMove(MOB mob);
	public int getLevelAttack(MOB mob);
	public int getLevelArmor(MOB mob);
	public int getLevelDamage(MOB mob);
	public int getBonusPracLevel();
	public int getBonusAttackLevel();
	public int getAttackAttribute();
	public int getPracsFirstLevel();
	public int getTrainsFirstLevel();
	public int getLevelsPerBonusDamage();
	public int getMovementMultiplier();
	public int getHPDivisor();
	public int getHPDice();
	public int getHPDie();
	public int getManaDivisor();
	public int getManaDice();
	public int getManaDie();
	public String weaponLimitations();
	public String armorLimitations();
	public String otherLimitations();
	public String otherBonuses();
	public String statQualifications();
	public int[] maxStatAdjustments();
	
	public String[] getStatCodes();
	public String getStat(String code);
	public void setStat(String code, String val);
	public boolean sameAs(CharClass E);
	
	public boolean raceless();
	public boolean leveless();
	public boolean expless();
	
	public static final int ARMOR_ANY=0;
	public static final int ARMOR_CLOTH=1;
	public static final int ARMOR_LEATHER=2;
	public static final int ARMOR_NONMETAL=3;
	public static final int ARMOR_VEGAN=4;
	public static final int ARMOR_METALONLY=5;
	public static final int ARMOR_OREONLY=6;
	public static long ARMOR_WEARMASK=Item.WORN_TORSO|Item.WORN_LEGS|Item.WORN_ARMS|Item.WORN_WAIST|Item.WORN_HEAD;
	public static final String[] ARMOR_DESCS={
		"ANY","CLOTH","LEATHER","NONMETAL","VEGAN","METALONLY","OREONLY"
	};
	
	public static final String[] ARMOR_LONGDESC={
		"May wear any armor.",
		"Must wear cloth, vegetation, or paper based armor.",
		"Must wear leather, cloth, or vegetation based armor.",
		"Must wear non-metal armor.",
		"Must wear wood or vegetation based armor.",
		"Must wear metal armor",
		"Must wear stone, crystal, or metal armor."
	};
	
	public static final int WEAPONS_ANY=0;
	public static final int WEAPONS_DAGGERONLY=1;
	public static final int WEAPONS_THIEFLIKE=2;
	public static final int WEAPONS_NATURAL=3;
	public static final int WEAPONS_BURGLAR=4;
	public static final int WEAPONS_ROCKY=5;
	public static final int WEAPONS_MAGELIKE=6;
	public static final int WEAPONS_EVILCLERIC=7;
	public static final int WEAPONS_GOODCLERIC=8;
	public static final int WEAPONS_NEUTRALCLERIC=9;
	public static final int WEAPONS_ALLCLERIC=10;
	public static final int WEAPONS_FLAILONLY=11;
	public static final int[][] WEAPONS_SETS={
/*0*/{Weapon.CLASS_AXE,Weapon.CLASS_BLUNT,Weapon.CLASS_DAGGER,Weapon.CLASS_EDGED,Weapon.CLASS_FLAILED,Weapon.CLASS_HAMMER,Weapon.CLASS_NATURAL,Weapon.CLASS_POLEARM,Weapon.CLASS_RANGED,Weapon.CLASS_STAFF,Weapon.CLASS_SWORD,Weapon.CLASS_THROWN},
/*1*/{Weapon.CLASS_NATURAL,Weapon.CLASS_DAGGER},
/*2*/{Weapon.CLASS_SWORD,Weapon.CLASS_RANGED,Weapon.CLASS_THROWN,Weapon.CLASS_NATURAL,Weapon.CLASS_DAGGER,Weapon.CLASS_EDGED},
/*3*/{RawMaterial.MATERIAL_WOODEN,RawMaterial.MATERIAL_UNKNOWN,RawMaterial.MATERIAL_VEGETATION,RawMaterial.MATERIAL_FLESH,RawMaterial.MATERIAL_LEATHER},
/*4*/{Weapon.CLASS_NATURAL,Weapon.CLASS_SWORD,Weapon.CLASS_FLAILED,Weapon.CLASS_BLUNT,Weapon.CLASS_DAGGER,Weapon.CLASS_EDGED},
/*5*/{RawMaterial.MATERIAL_ROCK,RawMaterial.MATERIAL_UNKNOWN,RawMaterial.MATERIAL_GLASS,RawMaterial.MATERIAL_METAL,RawMaterial.MATERIAL_MITHRIL,RawMaterial.MATERIAL_PRECIOUS},
/*6*/{Weapon.CLASS_NATURAL,Weapon.CLASS_DAGGER,Weapon.CLASS_STAFF},
/*7*/{Weapon.CLASS_EDGED,Weapon.CLASS_POLEARM,Weapon.CLASS_AXE,Weapon.CLASS_SWORD,Weapon.CLASS_DAGGER},
/*8*/{Weapon.CLASS_BLUNT,Weapon.CLASS_HAMMER,Weapon.CLASS_FLAILED,Weapon.CLASS_NATURAL,Weapon.CLASS_STAFF},
/*9*/{Weapon.CLASS_BLUNT,Weapon.CLASS_RANGED,Weapon.CLASS_THROWN,Weapon.CLASS_STAFF,Weapon.CLASS_NATURAL,Weapon.CLASS_SWORD},
/*10*/{Weapon.CLASS_AXE,Weapon.CLASS_BLUNT,Weapon.CLASS_DAGGER,Weapon.CLASS_EDGED,Weapon.CLASS_FLAILED,Weapon.CLASS_HAMMER,Weapon.CLASS_NATURAL,Weapon.CLASS_POLEARM,Weapon.CLASS_RANGED,Weapon.CLASS_STAFF,Weapon.CLASS_SWORD,Weapon.CLASS_THROWN},
/*11*/{Weapon.CLASS_NATURAL,Weapon.CLASS_FLAILED},
	};
	public static final String[] WEAPONS_LONGDESC={
/*0*/"May use any weapons.",
/*1*/"Must use dagger-like or natural weapons.",
/*2*/"Must use swords, daggers, natural, or ranged weapons.",
/*3*/"Must use wooden, plant-based, or leather weapons.",
/*4*/"Must use sword, daggers, flailed, blunt, or natural weapons.",
/*5*/"Must use stone, crystal, metal, or glass weapons.",
/*6*/"Must use daggers, staves, or natural weapons.",
/*7*/"Must use polearms, axes, swords, daggers, or edged weapons.",
/*8*/"Must use hammers, staves, flailed, natural, or blunt weapons.",
/*9*/"Must use swords, staves, natural, ranged, or blunt weapons",
/*10*/"Evil must use polearm, sword, axe, edged, or natural.  Neutral must use blunt, ranged, thrown, staff, natural, or sword.  Good must use blunt, flailed, natural, staff, or hammer.",
/*11*/"Must use flailed weapons."
	};
		
	public final static int GENFLAG_NORACE=1;
	public final static int GENFLAG_NOLEVELS=2;
	public final static int GENFLAG_NOEXP=4;
	
}
