//PokemonArena.java
//Priyonto Saha
//A fun pokemon text mok-up game using object oriented design, that takes a datafile of PokeStats 
//and uses them to make Pokemon objects and have battles with these objects. There are different attack specials and 
//moves, and each pokemon is unique. You choose 4 pokemon to fight with, and to win you must defeat the ones you do not choose.
import java.util.*;
import java.math.*;
import java.io.*;
public class PokemonArena{
	
	private static Pokemon[]allPokes;//Array of all pokemon
	private static int totPokes;//# of pokemon
	private static Pokemon[]friendlyPokes=new Pokemon[4];//Friendly team
	private static Pokemon[]enemyPokes;//Enemy team
	private static Pokemon friendlyPoke;//The current pokemon battling on player's team
	private static Pokemon enemyPoke;//Current pokemon battling on enemy team
	
	public static boolean has(Pokemon[]pokeray,Pokemon poke){//Similar to .contains method for arraylists, but for primitive arrays instead
		for(int i=0;i<pokeray.length;i++){
			if (pokeray[i]!= null && pokeray[i].equals(poke)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean pokesAliveCheck(Pokemon[]pokeray){//Checks if at least one poke in array provided is alive
		for(int i=0;i<pokeray.length;i++){
			if (pokeray[i].getBool("alive")){
				return true;
			}
		}
		return false;
	}
	
	public static void displayPokeray(Pokemon[]pokes){//Displays a list of pokes(Most often friendlypokes)
		int counter=1;
		for (Pokemon poke:pokes){
			System.out.printf("%-2d %-10s HP: %-3d TYPE: %-8s RESISTANCE: %-8s WEAKNESS: %-8s Alive:%s\n",counter,poke.getStr("name"),poke.getInt("hp"),poke.getStr("type"),poke.getStr("resistance"),poke.getStr("weakness"),poke.getBool("alive"));
			counter+=1;
		}
	}
	
	public static void loadPokes(){//Loads all pokemon from the datafile, and displays all of them as well.
		String pinfo;//Pokemon info all in one String
		String[]pinfoList;//A list of pokemon info
		try{
			Scanner in=new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
			totPokes=Integer.parseInt(in.nextLine());
			allPokes=new Pokemon[totPokes];
			for(int i=0;i<totPokes;i++){
				pinfo=in.nextLine();
				pinfoList=pinfo.split(",");
				allPokes[i]= new Pokemon(pinfo);
				for(int j=0;j<pinfoList.length;j++){
					if (pinfoList[j].equals(" ")){
						pinfoList[j]="none";
					}
					pinfoList[j] = pinfoList[j].substring(0, 1).toUpperCase() + pinfoList[j].substring(1);//Capitalizes the displayed info
				}
				System.out.printf("%-2d %-10s HP: %-3s TYPE: %-8s RESISTANCE: %-8s WEAKNESS: %-8s\n",i+1,pinfoList[0],pinfoList[1],pinfoList[2],pinfoList[3],pinfoList[4]);				
			}
		}
		catch(IOException ex){
			System.out.println("pokemon.txt not found");
		}
	}
	public static void choosePokes(){//Player choosing pokemon for their team
		Scanner kb=new Scanner(System.in);
		int selection;//The pokemon chosen
		System.out.print("\nType a pokemon's number to choose it for your team!");
		for(int i=0;i<4;i++){
			System.out.printf("(%d more to choose)\n",4-i);
			selection=Integer.parseInt(kb.nextLine());
			if (selection<1||selection>totPokes){//If pokemon chosen is out of the list
				System.out.println("That isn't a pokemon! Pick again.");
				i--;	
			}
			else if (has(friendlyPokes,allPokes[selection-1])){//If pokemon chosen has already been chosen
				System.out.println("You have already chosen that one! Pick again.");
				i--;	
			}
			else{
				friendlyPokes[i]=allPokes[selection-1];
				System.out.printf("You have chosen %s!",allPokes[selection-1].getStr("name"));
			}		
		}
		System.out.print("\n");//Adds a line to make it nice
		enemyPokes=new Pokemon[totPokes-4];//Array hat wll ultimately be used
		ArrayList<Pokemon>enemyPokesArraylis=new ArrayList<Pokemon>();//Making an arrayList to shuffle pokes in a random order to fight
		for(Pokemon poke:allPokes){//Adding all nonchosen pokes to ArrayLIst
			if (!has(friendlyPokes,poke)){
				enemyPokesArraylis.add(poke);	
			}	
		}
		Collections.shuffle(enemyPokesArraylis);
		for(int i=0;i<enemyPokesArraylis.size();i++){//Turning shuffled arrayList to primitive array
			enemyPokes[i]=enemyPokesArraylis.get(i);
		}	
	}
	public static void battle(){
		Scanner kb=new Scanner(System.in);
		int pokeChosen;//Stores pokemon chosen
		int actionChosen;//Stores action chosen
		int atkChosen;//Stores attack chosen
		enemyPoke=enemyPokes[0];//Initializes first enemy poke to fight
		int enemyCounter=0;//Counter for everytime enemy gets defeated, to fight the next enemy 
		int atkTryCount=0;//A counter for the enemy choosing attacks randomly. If it reaches 20 attacks tried wihout success, the enmy passes instead
		int turn=1+(int)(Math.random()*2);//Determines enemy turn or pokemon turn, who goes first is random 1=Friendly turn 2=Enemy turn
		System.out.println("\nYour team:");
		displayPokeray(friendlyPokes);
		System.out.printf("A %s has appeared! Defeat it!\n",enemyPokes[0].getStr("name"));
		System.out.println("Choose a Pokemon to attack with!");
		pokeChosen=Integer.parseInt(kb.nextLine());
		friendlyPoke=friendlyPokes[pokeChosen-1];
		System.out.printf("%s! I choose you!\n",friendlyPoke.getStr("name"));
		while(pokesAliveCheck(friendlyPokes)&&pokesAliveCheck(enemyPokes)){
			while(turn==1){//While friendly turn / player turn
				System.out.println("\nYour Turn!");
				while(!friendlyPoke.getBool("alive")){//If your pokemon is dead you gotta chose a new one right away
					System.out.printf("%s has been defeated!\n",friendlyPoke.getStr("name"));
					displayPokeray(friendlyPokes);
					System.out.println("Chose a new pokemon to attack with!");
					pokeChosen=Integer.parseInt(kb.nextLine());
					friendlyPoke=friendlyPokes[pokeChosen-1];
					System.out.printf("%s! I choose you!\n",friendlyPoke.getStr("name"));
					turn=1+(int)(Math.random()*2);//Randomly chooses the side that attcks first in the next battle
				}
				if (turn==1){
					System.out.printf("Your pokemon %s has %d health and %d energy! ",friendlyPoke.getStr("name"),friendlyPoke.getInt("hp"),friendlyPoke.getInt("energy"));
					System.out.printf("The enemy pokemon %s has %d health! ",enemyPoke.getStr("name"),enemyPoke.getInt("hp"));
					System.out.println("Choose an action! ");
					System.out.println("Attack:1 Retreat:2 Pass:3");
					actionChosen=Integer.parseInt(kb.nextLine());
					if (actionChosen==1){//Attacking
						System.out.println(friendlyPoke.getStr("atkDisplay"));
						System.out.printf("Your pokemon %s has %d energy!\n",friendlyPoke.getStr("name"),friendlyPoke.getInt("energy"));
						System.out.printf("The enemy %s has %d health!\n",enemyPoke.getStr("name"),enemyPoke.getInt("hp"));
						System.out.println("Choose an attack!");
						atkChosen=Integer.parseInt(kb.nextLine());
						System.out.printf("You chose %s! ",friendlyPoke.getStr("name",atkChosen));
						if (friendlyPoke.getBool("stunned")){//If you are stunned you can't attack
							System.out.println("You are stunned and can not attack! You pass instead.");
						}
						else if (friendlyPoke.getInt("energy")-friendlyPoke.getInt("cost",atkChosen)<0){//If you don't have the energy you can't attack
							System.out.println("Not enough energy! You pass instead.");
						}
						else{
							friendlyPoke.attack(enemyPoke,atkChosen);//Otherwise, you can attack
						}
					}
					else if (actionChosen==2){//Retreating
						if (!friendlyPoke.getBool("stunned")){
							displayPokeray(friendlyPokes);
							System.out.println("Choose a new Pokemon to fight with!");
							friendlyPoke=friendlyPokes[Integer.parseInt(kb.nextLine())-1];
							while(!friendlyPoke.getBool("alive")){//Gotta make sure the pokemon replacing the retreating pokemon has not been defeated yet
								System.out.println("That pokemon has already been defeated! Choose a different one on your team!");
								friendlyPoke=friendlyPokes[Integer.parseInt(kb.nextLine())-1];
							}
						}
						System.out.println("You are stunned and cannot retreat! You pass instead.");
					}
					else if (actionChosen==3){//Passing
						System.out.println("You have passed");//Passing does nothing, just ends turn
					}
					else{
						System.out.println("That's not an action!");
					}
					for(int i=0;i<friendlyPokes.length;i++){
						friendlyPokes[i].energize("norm");//Energizing friendly pokemon, i.e. adding 10 energy and resets stun
					}
				}
				turn=2;//Changes turn to enemy's turn
			}
			while(turn==2){//While enemy's turn
				System.out.println("\nEnemy's turn!");
				while(!enemyPoke.getBool("alive")){//When the enemy is killed
					for(int i=0;i<friendlyPokes.length;i++){//Heal all player pokes
						friendlyPokes[i].heal();
					}
					for(int i=0;i<friendlyPokes.length;i++){//Reset friendly pokemon to 50 energy and resets stun and disabled
						friendlyPokes[i].energize("full");
					}
					for(int i=0;i<enemyPokes.length;i++){//Reset enemy pokemon to 50 energy and resets stun and disabled 
						enemyPokes[i].energize("full");//(for if an extension is ever added to game, uneeded atm)
					}
					enemyCounter+=1;//Next pokemon
					if(enemyPokes.length>enemyCounter){//Making sure there are still enemy pokemon left to fight
						enemyPoke=enemyPokes[enemyCounter];//Next pokemon
						System.out.printf("There are %d enemy pokemon left to defeat!\n",enemyPokes.length-enemyCounter);
						System.out.printf("A %s has appeared! Defeat it!\n",enemyPokes[enemyCounter].getStr("name"));
						turn=1+(int)(Math.random()*2);//Randomly chooses the side that attcks first in the next battle
					}
				}
				if (turn==2){
					atkTryCount=0;//Enemy will always try to attack if it has the energy and is not stunned
					while(atkTryCount<20){// This counter lets the enemy pokemon to try to choose a valid attack 20 times
						int randAtk=1+(int)(Math.random()*enemyPoke.getInt("numAt"));//Choosing an attack
						if (enemyPoke.getInt("cost",randAtk)<enemyPoke.getInt("energy") && !enemyPoke.getBool("stunned")){//If enough energy and not stunned
							System.out.printf("%s is trying to use %s!",enemyPoke.getStr("name"),enemyPoke.getStr("name",randAtk));
							enemyPoke.attack(friendlyPoke,randAtk);
							break;	
						}
						atkTryCount+=1;
					}
					if (atkTryCount>=20){//If the enemy goes 20 invalid attempts at making an attack, it passes instead
						System.out.println("The enemy has passed!");//This will happen if the pokemon is stunned or does not have enough energy to make any move
					}
					for(int i=0;i<enemyPokes.length;i++){//Enemy pokemon gain 10 energy at the end of their turn
						enemyPokes[i].energize("norm");
					}
				}
				turn=1;// Passing turn on to the player
			}
		}
		//The statements below trigger only when the main loop above is finished
		if(!pokesAliveCheck(friendlyPokes)){//Player loses
			System.out.println("All your pokemon have fainted :(\nYou Lose!");
		}
		if(!pokesAliveCheck(enemyPokes)){//Player wins
			System.out.println("You defeated all the bad pokemon! Congrats!!! You are Trainer Supreme!!!");
		}
	}
	public static void main(String[]args){
		Scanner kb=new Scanner(System.in);
		System.out.println("Welcome to the PokemonArena!");
		System.out.println("<Any Key to Begin>");
		kb.nextLine();
		loadPokes();
		choosePokes();
		battle();
	}
}
class Pokemon{
	private String name;
	private int hp;
	private String type;
	private String resistance;
	private String weakness;
	private int energy;
	private boolean stunned;
	private boolean disabled;
	private boolean alive;
	private Attack[]attacks;//A list of attack classes for each pokemon
	private int numAt;
	private int totalhp;
	public Pokemon(String statstr){//Takes in a line from data file, splits it, and uses the info to create the needed stats
		String[]stats=statstr.split(",");
		name=stats[0];
		hp=Integer.parseInt(stats[1]);
		type=stats[2];
		resistance=stats[3];
		weakness=stats[4];
		energy=50;
		stunned=false;
		disabled=false;
		alive=true;
		totalhp=hp;
		numAt=Integer.parseInt(stats[5]);
		attacks = new Attack[numAt];
		for (int i=0;i<numAt;i++){//Creating atttacks
			attacks[i] = new Attack(stats[6+(4*i)],stats[7+(4*i)],stats[8+(4*i)],stats[9+(4*i)]);
		}
	}
	class Attack{//An inner class that only Pokemon can access
		String name;
		int cost;
		int dmg;
		String special;	
		public Attack(String name,String cost,String dmg,String special){
			this.name=name;
			this.cost=Integer.parseInt(cost);
			this.dmg=Integer.parseInt(dmg);
			this.special=special;
		}
		
	}
	public void attack(Pokemon enemy,int atknum){//Attack method for enemies and friendlies
		double multiplier=1;//Multiplier for resistances and weaknesses
		Attack atko=attacks[atknum-1];//The attack being used
		if((enemy.weakness).equals(type)){//If the opposition is weak to the type
			multiplier=2.0;
			System.out.println("It's super effective!");
		}
		else if((enemy.resistance).equals(type)){//If the opposition is resistant to the type
			multiplier=0.5;
			System.out.println("It's not very effective...");
		}
		else{//If the opposition is neither weak or resistant to the type
			System.out.println("It's about average.");
		}
		energy-=atko.cost;//Subtracting the cost (We know the move is valid(i.e. the pokemon is not stunned/doesn't have enough energy)
		//because it is checked in the battle method before an attack is used.
		if(atko.special.equals("wild card")){
			if(Math.random()<.5){//50 % chance of hitting
				if(disabled){
					(enemy.hp)-=Math.max((atko.dmg-10)*multiplier,0);//Disabled dmg, making sure negative damage not dealt
					System.out.printf("%s used %s, attacking for %.0f! \n",name,atko.name,Math.max((atko.dmg-10)*multiplier,0));
				}
				else{
					(enemy.hp)-=atko.dmg*multiplier;//Normal dmg
					System.out.printf("%s used %s, attacking for %.0f! \n",name,atko.name,atko.dmg*multiplier);	
				}										
			}
			else{//Missed
				System.out.printf("%s missed trying to use %s! \n",name,atko.name);
			}
		}
		if(atko.special.equals("wild storm")){
			int curdmg=0;//Counter for damage
			int counter=0;//Counter for how many wildstorm attacks hit successfully
			while(true){
				if (Math.random()<.5){//50% change of hitting
					if(disabled){//Disabled dmg being added, making sure negative damage not dealt
						curdmg+=Math.max((atko.dmg-10),0);
					}
					else{//normal dmg being added
						curdmg+=atko.dmg;
					}
					counter+=1;					
				}
				else{
					break;//Breaking out of loop when not hit
				}							
			}
			enemy.hp-=curdmg*multiplier;//Subtracting the actual hp of the opposing pokemon, with the multiplier
			System.out.printf("%s dealt a total of %.0f damage with %s, attacking %d times! \n",name,curdmg*multiplier,atko.name,counter);	
		}
		if(atko.special.equals("recharge")){
			energy+=20;
			if (energy>50){//Making sure added energy doesn't go over the max of 50
				energy=50;
			}
			if(disabled){
				(enemy.hp)-=Math.max((atko.dmg-10)*multiplier,0);//Disabled dmg, making sure negative damage not dealt
				System.out.printf("%s attacks for %.0f using %s and regained energy! \n",name,Math.max((atko.dmg-10)*multiplier,0),atko.name);	
			}
			else{
				(enemy.hp)-=atko.dmg*multiplier;//Normal dmg
				System.out.printf("%s attacks for %.0f using %s and regained energy! \n",name,atko.dmg*multiplier,atko.name);
			}
		}
		if(atko.special.equals("stun")){
			if (Math.random()<.5){//50% chance to stun
				(enemy.stunned)=true;
				if(disabled){
					(enemy.hp)-=Math.max((atko.dmg-10)*multiplier,0);//Disabled dmg, making sure negative damage not dealt
					System.out.printf("%s used %s, stunning %s and dealing %.0f damage! \n",name,atko.name,enemy.name,Math.max((atko.dmg-10)*multiplier,0));	
				}
				else{
					(enemy.hp)-=atko.dmg*multiplier;//Normal dmg
					System.out.printf("%s used %s, stunning %s and dealing %.0f damage! \n",name,atko.name,enemy.name,atko.dmg*multiplier);	
				}										
			}
			else{//50% chance to not stun
				if(disabled){
					(enemy.hp)-=Math.max((atko.dmg-10)*multiplier,0);//Disabled dmg, making sure negative damage not dealt
					System.out.printf("%s uses %s! It attacks for %.0f but did not stun %s. \n",name,atko.name,Math.max((atko.dmg-10)*multiplier,0),enemy.name);	
				}
				else{
					(enemy.hp)-=atko.dmg*multiplier;//Normal dmg
					System.out.printf("%s uses %s! It attacks for %.0f but did not stun %s. \n",name,atko.name,atko.dmg*multiplier,enemy.name);	
				}
			}	
		}
		if(atko.special.equals("disable")){
			(enemy.disabled)=true;//100% chance disabling opposing pokemon
			if(disabled){
				(enemy.hp)-=Math.max((atko.dmg-10)*multiplier,0);//Disabled dmg, making sure negative damage not dealt
				System.out.printf("%s uses %s, attacking for %.0f and disabling %s! \n",name,atko.name,Math.max((atko.dmg-10)*multiplier,0),enemy.name);
			}
			else{
				(enemy.hp)-=atko.dmg*multiplier;//Normal dmg
				System.out.printf("%s uses %s, attacking for %.0f and disabling %s! \n",name,atko.name,atko.dmg*multiplier,enemy.name);
			}
		}
		if(atko.special.equals(" ")){
			if(disabled){
				(enemy.hp)-=Math.max((atko.dmg-10)*multiplier,0);//Disabled dmg, making sure negative damage not dealt
				System.out.printf("%s uses %s, attacking for %.0f! \n",name,atko.name,Math.max((atko.dmg-10)*multiplier,0));	
			}
			else{
				(enemy.hp)-=atko.dmg*multiplier;//Normal dmg
				System.out.printf("%s uses %s, attacking for %.0f! \n",name,atko.name,atko.dmg*multiplier);
			}
		}
		System.out.printf("%d energy was used! \n",atko.cost);//Printing the energy used
		//Showing if enemy died/how much health remaining:
		if(enemy.hp<=0){
			enemy.alive=false;
			enemy.hp=0;
			System.out.printf("%s has defeated %s! \n",name,enemy.name);
		}
		else{
			System.out.printf("%s has %d health remaining!\n",enemy.name,enemy.hp);
		}	
	}
	public void energize(String type){//Adding energy and resetting disabled and stunned
		if(type.equals("norm")){//Adding 10 energy at the end of every turn
			energy+=10;
			if (energy>50){//Making sure energy not over 50
				energy=50;
			}
		}
		if(type.equals("full")){//Full energy at the end of battle
			energy=50;
			disabled=false;
		}
		stunned=false;
	}
	public void heal(){//Healing for after every battle
		if(alive){
			hp+=20;
			if(hp>totalhp){//Making sure health doesn't go over the total health
				hp=totalhp;
			}
		}
	}
	public String getStr(String watuwant){//get commands for various Strings, i.e name, type, etc.
		if (watuwant.equals("name")){
			return name;
		}
		if (watuwant.equals("type")){
			return type;
		}
		if (watuwant.equals("resistance")){
			return resistance;
		}
		if (watuwant.equals("weakness")){
			return weakness;
		}
		if (watuwant.equals("atkDisplay")){//Also can return a display of all the pokemon's attacks
			String atkDisplay="\nYour Pokemon's attacks:\n";
			for(int i=0;i<numAt;i++){
				atkDisplay+=String.format("%d Attack: %-15s EnergyCost:%-3d Damage:%-3d Special: %-10s\n",i+1,attacks[i].name,attacks[i].cost,attacks[i].dmg,(attacks[i].special).substring(0,1).toUpperCase()+(attacks[i].special).substring(1));
			}
			return atkDisplay.substring(0,atkDisplay.length()-1);
		}	
		System.out.println("Error for getStr");//Printing an error if nothing is returned
		return "Error for getStr";
	}
	public int getInt(String watuwant){//get commands for various ints, i.e hp, energy, etc.
		if (watuwant.equals("hp")){
			return hp;
		}
		if (watuwant.equals("energy")){
			return energy;
		}
		if (watuwant.equals("numAt")){
			return numAt;
		}
		System.out.println("Error for getInt");//Printing an error if nothing is returned
		return -1;
	}
	public boolean getBool(String watuwant){//get commands for various booleans, i.e alive, stunned, etc.
		if (watuwant.equals("stunned")){
			return stunned;
		}
		if (watuwant.equals("disabled")){
			return disabled;
		}
		if (watuwant.equals("alive")){
			return alive;
		}
		System.out.println("Error for getBool");//Printing an error if nothing is returned
		return false;
	}
	public int getInt(String watuwant,int atknum){//Method overloading for getting ints of an attack of a Pokemon based on atk number, i.e. cost, dmg
		Attack atko=attacks[atknum-1];
		if (watuwant.equals("cost")){
			return atko.cost;
		}
		if (watuwant.equals("dmg")){
			return atko.dmg;
		}
		System.out.println("Error for getInt attack");//Printing an error if nothing is returned
		return -1;
	}
	public String getStr(String watuwant,int atknum){//Method overloading for getting Strings of an attack of a Pokemon based on atk number, i.e. name, special
		Attack atko=attacks[atknum-1];
		if (watuwant.equals("name")){
			return atko.name;
		}
		if (watuwant.equals("special")){
			return atko.special;
		}
		System.out.println("Error for getStr attack");//Printing an error if nothing is returned
		return "Error for getStr attack";
	}
}
