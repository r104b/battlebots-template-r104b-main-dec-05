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
		String[] paths = {"pikachu_up.png", "pikachu_down.png", "pikachu_right.png", "pikachu_left.png"};
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


		// check for a bot that's too close

        public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
            // Initialize variables to track the closest bot and its distance
            BotInfo closestBot = null;
            double closestDistance = Double.MAX_VALUE;
        
            // Find the closest live bot
            for (BotInfo bot : liveBots) {
                if (bot.getBotNumber() != me.getBotNumber()) { // Exclude your own bot
                    double distance = Math.sqrt(Math.pow(me.getX() - bot.getX(), 2) + Math.pow(me.getY() - bot.getY(), 2));
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestBot = bot;
                    }
                }
            }
        
            // If a closest bot is found and shooting is allowed, aim at it
            if (closestBot != null && shotOK) {
                double dX = closestBot.getX() - me.getX();
                double dY = closestBot.getY() - me.getY();
        
                // Aim towards the closest bot based on its position relative to yours
                if (Math.abs(dX) > Math.abs(dY)) {
                    if (dX > 0) {
                        return BattleBotArena.FIRERIGHT;
                    } else {
                        return BattleBotArena.FIRELEFT;
                    }
                } else {
                    if (dY > 0) {
                        return BattleBotArena.FIREDOWN;
                    } else {
                        return BattleBotArena.FIREUP;
                    }
                }
            }
        
            return BattleBotArena.STAY; // Replace someDefaultValue with the appropriate default move
        }
		// for (int i=0; i<liveBots.length; i++)
		// {
		// 	if (!warned[liveBots[i].getBotNumber()]) // skip if already warned this Bot
		// 	{
		// 		// compute the Manhattan distance to the Bot
        //         double dX = ((me.getX()-liveBots[i].getX()));
		// 		double dY = Math.abs(+Math.abs(me.getY()-liveBots[i].getY()));
		// 		if (dY < 50) {
        //             move = BattleBotArena.FIREDOWN;
        //             move = BattleBotArena.FIREUP;
        //         } else if (dX < 50) {
        //             move = BattleBotArena.FIRERIGHT;
        //             move = BattleBotArena.FIRELEFT;
		// 	}
		// }

		// 	int dir = (int)(Math.random()*4);
		// 	if (dir == 0) {
		// 		return BattleBotArena.FIRERIGHT;
        //     }else if (dir == 1){
		// 		return BattleBotArena.FIRELEFT;
        //     }else if (dir == 2) {
		// 		return BattleBotArena.FIREDOWN;
        //     }else {
		// 		return BattleBotArena.FIREUP;
        
	/**
	 * Generate a random direction, then stick with it for a random count between
	 * 30 and 90 moves. Randomly take a shot when done each move.




	/**
	 * Decide whether we are overheating this round or not
	 */
	public void newRound()
	{

	 }

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

	public void avoidBullet() {

		
	}

	
	
}
