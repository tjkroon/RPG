package Diablo;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import Diablo.items.Item;
import Diablo.items.ManaPotion;
import Diablo.items.SpeedPotion;

public class Game {

	//static final public Path root = Paths.get(System.getProperty("user.dir")).getParent();
	
	//this is only works for Fan
	static final public String root = Paths.get(System.getProperty("user.dir")).getParent()+"";
	public DialogueUI dialogueObj= null; //no dialogue instances yet
	boolean saved=false;
	boolean dialogue=false;
	boolean continueDialogue =false;
	boolean hovering = false;
	boolean responsing = false;
	private LoadGame loadFile; 
	public LoadGame getLoadFile() {return loadFile;};

	public void createDialogue(Entity entity) {
		
		dialogueObj = new DialogueUI(entity);
		//dialogue=true;
		//dialogueObj= new DialogueManager(entity);
	}
	public String getDialogue() {
		return dialogueObj.getDialogue();
	}
	
	//GameState enum
	public enum GameState {
		MAINMENU_STATE,
		MAINGAME_STATE,
		PAUSE_STATE,
		DEAD_STATE
	}
	public static GameState gameState;

	static int windowX = 1024;
	static int windowY = 576;
//	static int windowX = 1280;
//	static int windowY = 720;
	//static int windowX = 1920;
	//static int windowY = 1080;

	static int centerX = windowX /2 ;
	static int centerY = windowY /2 ;

	static int fps = 30;

	static int[] mapDimension = new int[2];
	//static int timer = 1000 / fps;

	static Display display;
	static Map map;

	private ArrayList<Entity> list = new ArrayList<Entity>();
	public ArrayList<Entity> getEntityList(){return list;}
	
	private ArrayList<Entity> projectileList= new ArrayList<Entity>();
	public ArrayList<Entity> getProjectileList(){return projectileList;}
	
	private MouseControl mouse;
	public Diablo.MouseControl getMouseControl(){return mouse;}
	
	private KeyboardControl keyboard = new KeyboardControl(this);
	public Diablo.KeyboardControl getKeyboardControl(){return keyboard;}
	
	private ArrayList<Entity> obstacle = new ArrayList<Entity>();
	public ArrayList<Entity> getObstacles(){return obstacle;}
	
	private ArrayList<Node> obstacleLocation = new ArrayList<Node>();
	public ArrayList<Node> getObstacleLocation(){return obstacleLocation;}
	
	int mapWidth = 5000;
	int[] obsMap = new int[mapWidth * mapWidth];
	
	static Timer timer = new Timer();
	static int gameTime = 0;
	
	

	 public Game() throws IOException
	 {
		 //long time = System.currentTimeMillis();
		 //System.out.println(time);

		 try {
			 map = new Map("backGround", this);
		 } catch (IOException e1) {
			 // TODO Auto-generated catch block
			 e1.printStackTrace();
		 }
 loadFile = new LoadGame(this); //constructs loading/saving object
		 try {
			 map = new Map("backGround", this);
		 } catch (IOException e1) {
			 // TODO Auto-generated catch block
			 e1.printStackTrace();
		 }
		 String repo= Game.root+"\\resources\\images\\";
		 File saveFile= new File(root+ "/resources/text/savedGame.txt");
		
		 /*
		  * Creating Dialogue 
		  */
	
		 
		 Dialogue d3= new Dialogue("Did you encounter Sean?");
		 Dialogue d2= new Dialogue("I require you to please encounter Sean.  I will reward you for your efforts",d3);
		 Objective encounterSeanDialogue= new DialogueObjective(new Dialogue("Please ecounter Sean",d3), this);
		 Dialogue[] responses= {new Dialogue("yes",encounterSeanDialogue), new Dialogue("no")};
		 Dialogue d1= new Dialogue("Hello Traveler, I am the Tavern girl.  It is nice to meet you.  You have a trusthworthy face, will you help me for a reward?  I require assistance with a mission, would you like to hear more?",responses);
		 
		 
	//	 Item reward= new SeansItem();
		 Dialogue EncounteredSeanDialogue=new Dialogue("Oh, the Tavern Girl sent you, here is proof you met me");
		 Objective EncounteredSean= new DialogueObjective(EncounteredSeanDialogue,this );
		 Objective EncounterSean= new QuestObjective(this, encounterSeanDialogue, EncounteredSean);
		 Dialogue sean1= new Dialogue("Hello",EncounterSean);
		
		
		
		 int[] collisionBox= {50,100};
		 if(saveFile.exists()) {
			System.out.println("loading previous data");
			int[] loadData=loadFile.loadGame(); //load the game file
			 list.add(new Entity("player", new int[]{0, 0}, 100, 80, this, 100, 0));
				list.add(new Entity("friendly", new int[]{300, 300}, 100, 100, 80, this, 100, 0,ImageIO.read(new File(repo+"tavernGirl.png")),new Dialogue("Hello, good day", new Dialogue("Hello again")),collisionBox)); 
				list.add(new Entity("friendly", new int[]{600, 300}, 100, 100, 80, this, 100, 0,ImageIO.read(new File(repo+"player.png")),new Dialogue("Greetings"),collisionBox)); 
				list.add(new Entity("friendly", new int[]{1200, 300}, 100, 100, 80, this, 100, 0,ImageIO.read(new File(repo+"player.png")),d1,collisionBox)); 
			obstacle.add(new Entity(this, "tavern", 500, 500));
		 }
		 
		 else {
			 
		 list.add(new Entity("player", new int[]{0, 0}, 100, 80, this, 100, 0));
		list.add(new Entity("friendly", new int[]{300, 300}, 100,100, 80, this, 100, 0,ImageIO.read(new File(repo+"tavernGirl.png")),new Dialogue("hello", new Dialogue("next")),collisionBox)); 
		list.add(new Entity("friendly", new int[]{600, 300}, 100, 100, 80, this, 100, 0,ImageIO.read(new File(repo+"player.png")),new Dialogue("Tester"),collisionBox)); 
		list.add(new Entity("friendly", new int[]{1200, 300}, 100, 100, 80, this, 100, 0,ImageIO.read(new File(repo+"player.png")),d1,collisionBox)); 
		obstacle.add(new Entity(this, "tavern", 500, 500));
		 //list.add(new Entity("enemy", new int[]{0, 0},100, 80));
		 //list.add(new Entity("enemy", new int[]{-50, 0}, 100, 80));

		 //list.get(0).move.isLineOfSight();
		 }
		

		 display = new Display(this);

		 //gameLoop();

		 //System.out.println(System.currentTimeMillis());

		 int refreshTime = 1000/fps;
		 timer.scheduleAtFixedRate(dataUpdate, 0, 30);
		 timer.scheduleAtFixedRate(frameUpdate, 0, refreshTime);
		 timer.scheduleAtFixedRate(timeCounter, 0, 100);
		 mouse = new MouseControl(this);
		 
		 //new stuff
	        //temporary for testing
	        list.get(0).inventory.setInventoryItem(0, new ManaPotion(this, 10));
	        list.get(0).inventory.setInventoryItem(1, new ManaPotion(this, 1));
	        list.get(0).inventory.setInventoryItem(2, new ManaPotion(this, 1));
	        list.get(0).inventory.setInventoryItem(3, new SpeedPotion(this));
	        list.get(0).inventory.setBackpackItem(0, new ManaPotion(this, 1));
	        list.get(0).inventory.setBackpackItem(1, new ManaPotion(this, 1));
	        list.get(0).inventory.setBackpackItem(2, new ManaPotion(this, 1));
	        list.get(0).inventory.setBackpackItem(3, new ManaPotion(this, 1));
	        list.get(0).inventory.setBackpackItem(4, new ManaPotion(this, 1));
	        list.get(0).inventory.setBackpackItem(5, new ManaPotion(this, 1));
	        list.get(0).inventory.setBackpackItem(6, new ManaPotion(this, 1));
	        list.get(0).inventory.setBackpackItem(7, new ManaPotion(this, 1));
	        list.get(0).inventory.setBackpackItem(8, new ManaPotion(this, 1));
	        list.get(0).inventory.setBackpackItem(9, new ManaPotion(this, 1));
	        list.get(0).inventory.setBackpackItem(10, new ManaPotion(this, 1));
	        list.get(0).inventory.setBackpackItem(11, new ManaPotion(this, 1));
	 }

	public static void main(String[] args) throws IOException {
		new Game();
		
	}

	public void changeGameState(int i) {
		gameState = GameState.values()[i];
	}
	
	 private TimerTask dataUpdate = new TimerTask()
	  {
		public void run()
		{
			//System.out.println("here");
			gameLoop();
		}
	  };
	  
	  private TimerTask frameUpdate = new TimerTask()
	  {
		public void run()
		{
			//System.out.println("here");
			display.update();
		}
	  };
	  
	 private TimerTask timeCounter = new TimerTask()
	  {
		public void run()
		{
			//System.out.println("here");
			gameTime++;
		}
	  };

	public void gameLoop() {

		list.get(0).move.keyBoardUpdate(list.get(0));

				for(int i = 0; i < list.size(); i++)
				{
					if(list.get(i).hasPath == true)
					{
						if(i != 0)
						{
							list.get(i).ai.update();
						}
						list.get(i).move.update(list.get(i));
					}
				}
				//System.out.println(projectile.size());

				for(int i = 0; i < projectileList.size(); i++)
				{

					projectileList.get(i).move.update(projectileList.get(i));

					//System.out.println(projectile.get(i).collision);
				}

				//display.update();
				
				//list.get(0).takeDamage(list.get(0), 1);

				for(int i = 0; i < projectileList.size(); i++)
				{

					if((projectileList.get(i).visible == false)||(projectileList.get(i).active == false)) {
						projectileList.remove(i);
					}
				}

				for(int i = 0; i < list.size(); i++)
				{

					if (list.get(i).hp <= 0)
					{
						list.get(i).visible = false;
					}
				}
			
				//System.out.println(projectile.size());
				
				if(list.get(0).hp <= 0)
				{
					//break;
				}

			
			
	}


}