package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

public class RheaBot extends Bot {

    private String nextMessage = null;

    private String[] killMessages = {"ugly", "imbecile", "LALALALALALALA", "L", "Hahahahahahahahahaha :-)"};

    Image current, up, down, right, left;

    private String name = "atomBot";

    private int moveCount = 99;

    private int msgCounter = 0;

    private int targetNum = (int) (Math.random() * BattleBotArena.NUM_BOTS);

    private int sleep = (int) (Math.random() * 5 + 1);
	
    private boolean overheat = false;

    private int moveUpCounter = 0; // counter to track upward movements

    private int moveUp = 1;

    public String[] imageNames() {
        String[] paths = {"starfish4.png", "starfish4.png", "starfish4.png", "starfish4.png"};
        return paths;
    }

    public void loadedImages(Image[] images) {
        if (images != null) {
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

    public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
        if (overheat) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                return BattleBotArena.SEND_MESSAGE;
            }
        }

        double starX = me.getX();
        double starY = me.getY();
        // BotHelper botHelper1 = new BotHelper();
        // Bullet closestBullet = botHelper1.findClosest(me, bullets);

        //double distX = closestBullet - atomX;

        
        // System.out.println(BattleBotArena.TOP_EDGE);
        // System.out.println(BattleBotArena.BOTTOM_EDGE);

        if (starY == 574){ // if bot touches the bottom then come back up
            moveUp = 1;
        }
        if (starY == 10){ // if bot touches the top then come down
            moveUp = 2;
        }
        
        // } else {
        //  moveUp = true;
        // }

        // printing all the info to track
        System.out.println("X: " + starX + " Y: " + starY + " Bullet: " + bullets + " Bot Number: " + botNumber);

        // increase the move counter
        moveCount++;

        // Move up by one, shoot left, shoot right, and repeat
        if (moveUpCounter < 1) {
            moveUpCounter++;
            if (moveUp == 1){
                return BattleBotArena.UP;
            } else {
                return BattleBotArena.DOWN;
            }
        } else if (moveUpCounter == 1) {
            moveUpCounter++;
            return BattleBotArena.FIRELEFT;
        } else if (moveUpCounter == 2) {
            moveUpCounter = 0;
            return BattleBotArena.FIRERIGHT;
        }
        // } else if (moveUpCounter == 3) {
        //  moveUpCounter = 4;
        //  return BattleBotArena.FIREUP;
        // } else if (moveUpCounter == 4) {
        //  moveUpCounter = 0;
        //  return BattleBotArena.FIREDOWN;
        // }

        // Is it time to send a message?
        if (--msgCounter == 0) {
            return BattleBotArena.SEND_MESSAGE;
        }

        return BattleBotArena.UP; 
    }

    public void newRound() {
        if (botNumber >= targetNum - 3 && botNumber <= targetNum + 3)
            overheat = true;
        moveUpCounter = 0; // Reset the counter
    }

    public String outgoingMessage() {
        String msg = nextMessage;
        nextMessage = null;
        return msg;
    }

    public String getName() {
        if (name == null)
            name = "Rand" + (botNumber < 10 ? "0" : "") + botNumber;
        return name;
    }

    public String getTeamName() {
        return "ARGARGARG";
    }

    public void draw(Graphics g, int x, int y) {
        if (current != null)
            g.drawImage(current, x, y, Bot.RADIUS * 2, Bot.RADIUS * 2, null);
        else {
            g.setColor(Color.lightGray);
            g.fillOval(x, y, Bot.RADIUS * 2, Bot.RADIUS * 2);
        }
    }

    public void incomingMessage(int botNum, String msg) {
        if (botNum == BattleBotArena.SYSTEM_MSG && msg.matches(".*destroyed by " + getName() + ".*")) {
            int msgNum = (int) (Math.random() * killMessages.length);
            nextMessage = killMessages[msgNum];
            msgCounter = (int) (Math.random() * 30 + 30);
        }
    }
}