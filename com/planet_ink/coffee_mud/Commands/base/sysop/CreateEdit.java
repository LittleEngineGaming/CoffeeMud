package com.planet_ink.coffee_mud.Commands.base.sysop;

import com.planet_ink.coffee_mud.Commands.base.Socials;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import java.io.*;
import java.util.*;
public class CreateEdit
{
	public Rooms rooms=new Rooms();
	public Exits exits=new Exits();
	public Items items=new Items();
	public Mobs mobs=new Mobs();
	public Generic generic=new Generic();
	public Utils utils=new Utils();
	public Socials socials=null;
	public SysopSocials sysopSocials=null;


	public CreateEdit(Socials ourSocials)
	{
		socials=ourSocials;
		sysopSocials=new SysopSocials(ourSocials);
	}

	public void destroy(MOB mob, Vector commands)
		throws Exception
	{
		String commandType="";

		if(commands.size()>1)
		{
			commandType=((String)commands.elementAt(1)).toUpperCase();
		}
		if(((String)commands.elementAt(0)).equalsIgnoreCase("UNLINK"))
		{
			commands.insertElementAt("ROOM",1);
			rooms.destroy(mob,commands);
		}
		else
		if(commandType.equals("EXIT"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			exits.destroy(mob,commands);
		}
		else
		if(commandType.equals("ITEM"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			items.destroy(mob,commands);
		}
		else
		if((commandType.equals("AREA"))&&(mob.isASysOp(null)))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			rooms.destroyArea(mob,commands);
		}
		else
		if(commandType.equals("ROOM"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			rooms.destroy(mob,commands);
		}
		else
		if((commandType.equals("USER"))&&(mob.isASysOp(null)))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			mobs.destroyUser(mob,commands);
		}
		else
		if(commandType.equals("SOCIAL"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			sysopSocials.destroy(mob,commands);
		}
		else
		if(commandType.equals("BUG"))
		{
			int which=-1;
			if(commands.size()>2)
				which=Util.s_int((String)commands.elementAt(2));
			if(which<=0)
				mob.tell("Please enter a valid bug number to delete.  Use List Bugs for more information.");
			else
			{
				ExternalPlay.DBDeleteJournal("SYSTEM_BUGS",which-1);
				mob.tell("Bug deletion submitted.");
			}
		}
		else
		if(commandType.equals("IDEA"))
		{
			int which=-1;
			if(commands.size()>2)
				which=Util.s_int((String)commands.elementAt(2));
			if(which<=0)
				mob.tell("Please enter a valid idea number to delete.  Use List ideas for more information.");
			else
			{
				ExternalPlay.DBDeleteJournal("SYSTEM_IDEAS",which-1);
				mob.tell("Idea deletion submitted.");
			}
		}
		else
		if(commandType.equals("TYPO"))
		{
			int which=-1;
			if(commands.size()>2)
				which=Util.s_int((String)commands.elementAt(2));
			if(which<=0)
				mob.tell("Please enter a valid typo number to delete.  Use List typos for more information.");
			else
			{
				ExternalPlay.DBDeleteJournal("SYSTEM_TYPOS",which-1);
				mob.tell("Typo deletion submitted.");
			}
		}
		else
		if(commandType.equals("MOB"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			mobs.destroy(mob,commands);
		}
		else
		{
			String allWord=Util.combine(commands,1);
			Environmental thang=mob.location().fetchFromMOBRoomFavorsItems(mob,null,allWord,Item.WORN_REQ_ANY);
			if((thang!=null)&&(thang instanceof Item))
			{
				commands.insertElementAt("ITEM",1);
				destroy(mob,commands);
			}
			else
			if((thang!=null)&&(thang instanceof MOB))
			{
				if(((MOB)thang).isMonster())
					commands.insertElementAt("MOB",1);
				else
					commands.insertElementAt("USER",1);
				destroy(mob,commands);
			}
			else
			{
				Room theRoom=null;
				if(allWord.length()>0)
				for(int m=0;m<CMMap.numRooms();m++)
				{
					Room room=CMMap.getRoom(m);
					if(room.ID().equalsIgnoreCase(allWord))
					{
						theRoom=room;
						break;
					}
				}
				if(theRoom!=null)
				{
					commands=new Vector();
					commands.addElement("DESTROY");
					commands.addElement("ROOM");
					commands.addElement(theRoom.ID());
					destroy(mob,commands);
				}
				else
				{
					if(Directions.getGoodDirectionCode(allWord)>=0)
					{
						commands=new Vector();
						commands.addElement("DESTROY");
						commands.addElement("ROOM");
						commands.addElement(allWord);
						destroy(mob,commands);

						commands=new Vector();
						commands.addElement("DESTROY");
						commands.addElement("EXIT");
						commands.addElement(allWord);
						destroy(mob,commands);
					}
					else
					if(socials.FetchSocial(allWord)!=null)
					{
						commands.insertElementAt("SOCIAL",1);
						destroy(mob,commands);
					}
					else
					mob.tell(
						"\n\rYou cannot destroy a '"+commandType+"'. "
						+"However, you might try an "
						+"EXIT, ITEM, USER, MOB, SOCIAL, BUG, TYPO, IDEA, or a ROOM.");
				}
			}
		}
	}

	public void save(MOB mob, Vector commands)
		throws Exception
	{
		String commandType="";
		mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
		if(commands.size()>1)
			commandType=((String)commands.elementAt(1)).toUpperCase();
		if(commandType.equals("USERS"))
		{
			for(int s=0;s<Sessions.size();s++)
			{
				Session session=(Session)Sessions.elementAt(s);
				if(session.mob()!=null)
				{
					ExternalPlay.DBUpdateMOB(session.mob());
					ExternalPlay.DBUpdateFollowers(session.mob());
				}
			}
			mob.location().showHappens(Affect.MSG_OK_ACTION,"A feeling of permanency envelopes everyone.\n\r");
		}
		else
		if(commandType.equals("ITEMS"))
		{
			rooms.clearDebriAndRestart(mob.location(),1);
			Resources.removeResource("HELP_"+mob.location().name().toUpperCase());
			mob.location().showHappens(Affect.MSG_OK_ACTION,"A feeling of permanency envelopes the area.\n\r");
		}
		else
		if(commandType.equals("ROOM"))
		{
			rooms.clearDebriAndRestart(mob.location(),0);
			Resources.removeResource("HELP_"+mob.location().name().toUpperCase());
			mob.location().show(mob,null,Affect.MSG_OK_ACTION,"A feeling of permanency envelopes the area.\n\r");
		}
		else
		if(commandType.equals("MOBS"))
		{
			rooms.clearDebriAndRestart(mob.location(),2);
			Resources.removeResource("HELP_"+mob.location().name().toUpperCase());
			mob.location().showHappens(Affect.MSG_OK_ACTION,"A feeling of permanency envelopes the area.\n\r");
		}
		else
		{
			mob.tell(
				"\n\rYou cannot save '"+commandType+"'. "
				+"However, you might try "
				+"ITEMS, USERS, MOBS, or ROOM.");
		}
	}

	public void edit(MOB mob, Vector commands)
		throws Exception
	{
		String commandType="";
		if(commands.size()>1)
			commandType=((String)commands.elementAt(1)).toUpperCase();
		if(commandType.equals("ITEM"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			items.modify(mob,commands);
		}
		else
		if(commandType.equals("ROOM"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			rooms.modify(mob,commands);
		}
		else
		if(commandType.equals("AREA"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			rooms.modifyArea(mob,commands);
		}
		else
		if(commandType.equals("EXIT"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			exits.modify(mob,commands);
		}
		else
		if(commandType.equals("SOCIAL"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			sysopSocials.modify(mob,commands);
		}
		else
		if(commandType.equals("MOB"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			mobs.modify(mob,commands);
		}
		else
		if(commandType.equals("NEW"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			mobs.modify(mob,commands);
		}
		else
		{
			String allWord=Util.combine(commands,1);
			Environmental thang=mob.location().fetchFromMOBRoomFavorsItems(mob,null,allWord,Item.WORN_REQ_ANY);
			if((thang!=null)&&(thang instanceof Item))
			{
				if(!thang.isGeneric())
				{
					generic.genLevel(mob,thang);
					generic.genAbility(mob,thang);
					generic.genRejuv(mob,thang);
					generic.genUses(mob,(Item)thang);
					generic.genMiscText(mob,thang);
				}
				else
					generic.genMiscSet(mob,thang);
				thang.recoverEnvStats();
				mob.location().show(mob,null,Affect.MSG_OK_VISUAL,thang.name()+" shake(s) under the transforming power.");
				Log.sysOut("CreateEdit",mob.ID()+" modified item "+thang.ID()+".");
			}
			else
			if((thang!=null)&&(thang instanceof MOB))
			{
				if(!thang.isGeneric())
				{
					generic.genLevel(mob,thang);
					generic.genAbility(mob,thang);
					generic.genRejuv(mob,thang);
					generic.genMiscText(mob,thang);
				}
				else
					generic.genMiscSet(mob,thang);
				thang.recoverEnvStats();
				mob.location().show(mob,null,Affect.MSG_OK_VISUAL,thang.name()+" shake(s) under the transforming power.");
				Log.sysOut("CreateEdit",mob.ID()+" modified mob "+thang.ID()+".");
			}
			else
			if((Directions.getGoodDirectionCode(allWord)>=0)||(thang instanceof Exit))
			{
				if(Directions.getGoodDirectionCode(allWord)>=0)
					thang=mob.location().rawExits()[Directions.getGoodDirectionCode(allWord)];

				if(thang!=null)
				{
					generic.genMiscText(mob,thang);
					thang.recoverEnvStats();
					for(int m=0;m<CMMap.numRooms();m++)
					{
						Room room=CMMap.getRoom(m);
						for(int e2=0;e2<Directions.NUM_DIRECTIONS;e2++)
						{
							Exit exit=room.rawExits()[e2];
							if((exit!=null)&&(exit==thang))
							{
								ExternalPlay.DBUpdateExits(room);
								break;
							}
						}
					}
					mob.location().show(mob,null,Affect.MSG_OK_VISUAL,thang.name()+" shake(s) under the transforming power.");
					Log.sysOut("CreateEdit",mob.ID()+" modified exit "+thang.ID()+".");
				}
				else
				{
					commands.insertElementAt("EXIT",1);
					edit(mob,commands);
				}
			}
			else
			if(socials.FetchSocial(allWord)!=null)
			{
				commands.insertElementAt("SOCIAL",1);
				edit(mob,commands);
			}
			else
				mob.tell("\n\rYou cannot modify a '"+commandType+"'. However, you might try an ITEM, EXIT, MOB, SOCIAL, or ROOM.");
		}
	}

	public void link(MOB mob, Vector commands)
	{
		mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
		rooms.link(mob,commands);
	}

	public void create(MOB mob, Vector commands)
		throws Exception
	{
		String commandType="";
		if(commands.size()>1)
			commandType=((String)commands.elementAt(1)).toUpperCase();

		if(commandType.equals("EXIT"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			exits.create(mob,commands);
		}
		else
		if(commandType.equals("AREA"))
		{
			mob.tell("To create a new Area, you must first create a new room, then give that new room a new Area name.");
			return;
		}
		else
		if(commandType.equals("ITEM"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			items.create(mob,commands);
		}
		else
		if(commandType.equals("ROOM"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			rooms.create(mob,commands);
		}
		else
		if(commandType.equals("SOCIAL"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			sysopSocials.create(mob,commands);
		}
		else
		if(commandType.equals("MOB"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			mobs.create(mob,commands);
		}
		else
		if(commandType.equals("NEW"))
		{
			mob.location().show(mob,null,Affect.MSG_OK_VISUAL,"<S-NAME> wave(s) <S-HIS-HER> arms...");
			utils.newSomething(mob,commands);
		}
		else
		{
			String allWord=Util.combine(commands,1);
			String lastWord=null;
			if(commands.size()>2)
				lastWord=(String)commands.elementAt(commands.size()-1);
			Environmental E=null;
			E=CMClass.getItem(allWord);
			if((E!=null)&&(E instanceof Item))
			{
				commands.insertElementAt("ITEM",1);
				create(mob,commands);
			}
			else
			{
				E=CMClass.getMOB(allWord);
				if((E!=null)&&(E instanceof MOB))
				{
					commands.insertElementAt("MOB",1);
					create(mob,commands);
				}
				else
				if((lastWord!=null)&&(Directions.getGoodDirectionCode(lastWord)>=0))
				{
					commands.removeElementAt(commands.size()-1);
					allWord=Util.combine(commands,1);

					E=CMClass.getLocale(allWord);
					if(E==null)
						E=CMClass.getExit(allWord);
					if(E==null)
						E=CMClass.getAreaType(allWord);
					if((E!=null)&&(E instanceof Room))
					{
						commands=new Vector();
						commands.addElement("CREATE");
						commands.addElement("ROOM");
						commands.addElement(lastWord);
						commands.addElement(allWord);
						create(mob,commands);
					}
					else
					if((E!=null)&&(E instanceof Exit))
					{
						commands=new Vector();
						commands.addElement("CREATE");
						commands.addElement("EXIT");
						commands.addElement(lastWord);
						commands.addElement(allWord);
						create(mob,commands);
					}
					else
					if((E!=null)&&(E instanceof Area))
						mob.tell("To create a new Area, you must first create a new room, then give that new room a new Area name.");
					else
						mob.tell("\n\rYou cannot create a '"+commandType+"'. However, you might try an EXIT, ITEM, MOB, ROOM, or a NEW copy of something.");
				}
				else
					mob.tell("\n\rYou cannot create a '"+commandType+"'. However, you might try an EXIT, ITEM, MOB, ROOM, or a NEW copy of something.");
			}
		}
	}

}
