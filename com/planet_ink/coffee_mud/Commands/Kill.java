package com.planet_ink.coffee_mud.Commands;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;

public class Kill extends StdCommand
{
	public Kill(){}

	private String[] access={"KILL","K"};
	public String[] getAccessWords(){return access;}
	public boolean execute(MOB mob, Vector commands)
		throws java.io.IOException
	{
		MOB target=null;
		if(commands.size()<2)
		{
			if(!mob.isInCombat())
			{
				mob.tell("Kill whom?");
				return false;
			}
			else
			if(CommonStrings.getIntVar(CommonStrings.SYSTEMI_COMBATSYSTEM)!=MUDFight.COMBAT_DEFAULT)
				return false;
			else
				target=mob.getVictim();
		}
		
		boolean reallyKill=false;
		String whomToKill=Util.combine(commands,1);
		if(CMSecurity.isAllowed(mob,mob.location(),"KILLDEAD")&&(!mob.isMonster()))
		{
			if(((String)commands.elementAt(commands.size()-1)).equalsIgnoreCase("DEAD"))
			{
				commands.removeElementAt(commands.size()-1);
				whomToKill=Util.combine(commands,1);
				reallyKill=true;
			}
		}

		if(target==null)
		{
			target=mob.location().fetchInhabitant(whomToKill);
			if((target==null)||((target!=null)&&(!Sense.canBeSeenBy(target,mob))))
			{
				mob.tell("I don't see '"+whomToKill+"' here.");
				return false;
			}
		}
		
		if(reallyKill)
		{
			FullMsg msg=new FullMsg(mob,target,null,CMMsg.MSG_OK_ACTION,"^F<S-NAME> touch(es) <T-NAMESELF>.^?");
			if(mob.location().okMessage(mob,msg))
			{
				mob.location().send(mob,msg);
				target.curState().setHitPoints(0);
				MUDFight.postDeath(mob,target,null);
			}
			return false;
		}
		
		if(mob.isInCombat())
		{
			if(((mob.getVictim()!=null)
			&&(mob.getVictim()==target)
			&&(CommonStrings.getIntVar(CommonStrings.SYSTEMI_COMBATSYSTEM)==MUDFight.COMBAT_DEFAULT)))
			{
				mob.tell("^FYou are already fighting "+mob.getVictim().name()+".^?");
				return false;
			}
			
			if(mob.location().okMessage(mob,new FullMsg(mob,target,CMMsg.MSG_WEAPONATTACK,null)))
			{
				mob.tell("^FYou are now targeting "+target.name()+".^?");
				mob.setVictim(target);
				return false;
			}
		}
		
		if((!mob.mayPhysicallyAttack(target)))
			mob.tell("You are not allowed to attack "+target.name()+".");
		else
			MUDFight.postAttack(mob,target,mob.fetchWieldedItem());
		return false;
	}
	public int ticksToExecute(){return 1;}
	public boolean canBeOrdered(){return true;}

	public int compareTo(Object o){ return CMClass.classID(this).compareToIgnoreCase(CMClass.classID(o));}
}
