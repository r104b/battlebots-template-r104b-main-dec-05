package bots;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

/**
 * The RandBot is a very basic Bot that moves and shoots randomly. Sometimes it overheats.
 * It trash talks when it kills someone.
 *
 * @author Sam Scott
 * @version 1.0 (March 3, 2011)
 */
public class RheaBot2 extends Bot {
	

	//get bothelper methods
	private BotHelper bothelper = new BotHelper();

	/**
	 * Next message to send, or null if nothing to send.
	 */
	private String nextMessage = null;
	/**
	 * An array of trash talk messages.
	 */
	private String[] killMessages = {"Woohoo!!!", "In your face!", "Pwned", "Take that.", "Gotcha!", "Too easy.", "Hahahahahahahahahaha :-)"};
	/**
	 * Bot image
	 */
	Image current, up, down, right, left;
	/**
	 * My name (set when getName() first called)
	 */
	private String name = null;
	/**
	 * Counter for timing moves in different directions
	 */
	private int moveCount = 99;

	//countdown
	private int countDown = 0;
	/**
	 * Next move to make
	 */
	//get warned about other bots
	private boolean[] warned;

	//messages warning
	String[] messages = {"Stand back ","You are too close, ","Get back or face the consequences, ","Hands up, "};

	private int move = BattleBotArena.UP;
	/**
	 * Counter to pause before sending a victory message
	 */
	private int msgCounter = 0;
	/**
	 * Used to decide if this bot should overheat or not
	 */
	private int targetNum = (int)(Math.random()*BattleBotArena.NUM_BOTS);
	/**
	 * The amount to sleep to simulate overheating because of excessive CPU
	 * usage.
	 */
	private int sleep = (int)(Math.random()*5+1);
	/**
	 * Set to True if we are trying to overheat
	 */
	private boolean overheat = false;

	/**
	 * Return image names to load
	 */
	public String[] imageNames()
	{
		String[] paths = {"starfish4.png", "starfish4.png", "starfish4.png", "starfish4.png"};
		return paths;
	}

	/**
	 * Store the images loaded by the arena
	 */
	public void loadedImages(Image[] images)
	{
		if (images != null)
		{
			if (images.length > 0)
				up = images[0];
			if (images.length > 1)
				down = images[1];
			if (images.length > 2)
				right = images[2];
			if (images.length > 3)
				left = images[3];
			current = up;
		}
	}

	/**
	 * Generate a random direction, then stick with it for a random count between
	 * 30 and 90 moves. Randomly take a shot when done each move.
	 */
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets)
	{

			    int choice = (int)(Math.random()*4);

                if (choice == 0)
                {
                    move = BattleBotArena.UP;
                }
                else if (choice == 1)
                {
                    move = BattleBotArena.DOWN;
                }
                else if (choice == 2)
                {
                    move = BattleBotArena.LEFT;
                }
                else if (choice == 3)
                {
					move = BattleBotArena.RIGHT;
				} 
        
        return move;

	}

	public int getShot(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets)
	{
        for (int i=0; i<liveBots.length; i++) {
            double d = Math.abs(me.getX()-liveBots[i].getX())+Math.abs(me.getY()-liveBots[i].getY());

            if (d > 50) {
			    int choiceShot = (int)(Math.random()*4);

                if (choiceShot == 0)
                {
                    move = BattleBotArena.FIREUP;
                }
                else if (choiceShot == 1)
                {
                    move = BattleBotArena.FIREDOWN;
                }
                else if (choiceShot == 2)
                {
                    move = BattleBotArena.FIRELEFT;
                }
                else if (choiceShot == 3)
                {
                    move = BattleBotArena.FIRERIGHT;
                }
			}
		}
		return move;
	}


	public int shootBot(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) 
	{
        for (int i=0; i<liveBots.length; i++) {
            double d = Math.abs(me.getX()-liveBots[i].getX())+Math.abs(me.getY()-liveBots[i].getY());
            if (d < 50) {
			    double dX = Math.abs(me.getX()-liveBots[i].getX());
			    double dY = Math.abs(me.getY()-liveBots[i].getY());

                	if (dY < 50) {
                        move = BattleBotArena.FIREUP;
                    }

                    if (dX < 50) {
                        move = BattleBotArena.FIRERIGHT;
					}
				}
			}
			return move;
		}
	



	/**
	 * Decide whether we are overheating
	 */
	public void newRound()
	{}

	/**
	 * Send the message and then blank out the message string
	 */
	public String outgoingMessage()
	{
		String msg = nextMessage;
		nextMessage = null;
		return msg;
	}

	/**
	 * Construct and return my name
	 */
	public String getName()
	{
		if (name == null)
			name = "Rhea"+(botNumber<10?"0":"")+botNumber;
		return name;
	}

	/**
	 * Team "Arena"
	 */
	public String getTeamName()
	{
		return "Arena";
	}

	/**
	 * Draws the bot at x, y
	 * @param g The Graphics object to draw on
	 * @param x Left coord
	 * @param y Top coord
	 */
	public void draw (Graphics g, int x, int y)
	{
		if (current != null)
			g.drawImage(current, x, y, Bot.RADIUS*2, Bot.RADIUS*2, null);
		else
		{
			g.setColor(Color.lightGray);
			g.fillOval(x, y, Bot.RADIUS*2, Bot.RADIUS*2);
		}
	}

	/**
	 * If the message is announcing a kill for me, schedule a trash talk message.
	 * @param botNum ID of sender
	 * @param msg Text of incoming message
	 */
	public void incomingMessage(int botNum, String msg)
	{
		if (botNum == BattleBotArena.SYSTEM_MSG && msg.matches(".*destroyed by "+getName()+".*"))
		{
			int msgNum = (int)(Math.random()*killMessages.length);
			nextMessage = killMessages[msgNum];
			msgCounter = (int)(Math.random()*30 + 30);
		}
	}
}
