
/**
 * Program Name: Game.java 
 * Purpose: main class for the game of ELI
 * Coder: Shunyi Wang
 * Date: July	29, 2017 
 */

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;

public class Game extends JApplet implements Runnable
{

	private static final long serialVersionUID = 1L;

	private ArrayList<Meteor> meteors;

	private int screenWidth, screenHeight;
	private int meteorInterval, dropInterval, gunLastInterval;
	private int hitX, hitY;
	private int score, bombavailble, bombflashCount, possibility, beamWidth;
	private boolean gunShotting; //indicating laser beam drawing on screen 
	private volatile boolean isRunning;
	
	private Polygon gunBeam; // draw laser beam
	
	private Image backgroundImg, bufferImage, nukeImg, cannonImg;
	private Graphics bufferBrush; 
	private AudioClip gunaudioClip, explosinClip, bombaudioClip;
		
	private Thread birthThread, speedThread, hostThread;
	private ShotMouseListener mouseListener;
	private BombKeyListener keyListener;
	
	private Timer gunShotTimer; // Timer to controller the lasting time of laser beam

	
	@Override
	public void init()
	{
		screenWidth = 700;
		screenHeight = 700;

		bombflashCount = 0;
		bombavailble = 0;
		score = 0;
		meteorInterval= 10;
		gunLastInterval = 300;
		beamWidth = 3;
		possibility = 1;
		dropInterval = 200;
		
		gunShotting = false;
	
		meteors = new ArrayList<Meteor>();

		backgroundImg = getImage(getCodeBase(), "resource/img/Starfield.jpg");
		nukeImg = getImage(getCodeBase(), "resource/img/nuke.jpg");
		cannonImg = getImage(getCodeBase(), "resource/img/cannon.jpg");
		bufferImage = createImage(screenWidth, screenHeight);
  	bufferBrush = bufferImage.getGraphics();
  	
		URL gunaudioUrl = getClass().getResource("resource/wav/GunLuger.wav");
		gunaudioClip = Applet.newAudioClip(gunaudioUrl);
		URL bombaudioUrl = getClass().getResource("resource/wav/Bomb.wav");
		bombaudioClip = Applet.newAudioClip(bombaudioUrl);
		explosinClip = Applet.newAudioClip(getClass().getResource("resource/wav/Explosion.wav"));
		
		
		setFocusable(true);
		this.setSize(screenWidth, screenHeight);
		

	}

	@Override 
	public void start()
	{

		isRunning = true;	
		hostThread = new Thread(this);
		hostThread.start();
		
	}
		
	@Override
	public void stop()
	{

		isRunning = false;	
		birthThread = null;
		speedThread = null;
		hostThread = null;
		
	}
	
	
	@Override
	public void paint(Graphics g)
	{

		bufferBrush.clearRect(0, 0, screenWidth, screenHeight);
		bufferBrush.drawImage(backgroundImg, 0, 0, this);
		
		bufferBrush.drawImage(cannonImg, 20, screenHeight - 80, this);

		bufferBrush.setFont(new Font("SANS_SERIF", Font.BOLD, 18));
		bufferBrush.setColor(Color.YELLOW);
		bufferBrush.drawString("Score:" + score, 20, 30);
		
		//draw nuclear icon and hint string
		if(bombavailble >= 10)
		{
			bufferBrush.drawImage(nukeImg, 560, 20, this);
			bufferBrush.setFont(new Font("SANS_SERIF", Font.PLAIN, 14));
			bufferBrush.setColor(Color.GRAY);
			bufferBrush.drawString("Nuke available now", 560, 70);
		}
		
		// flicker the screen as nuclear bombing
		while(bombflashCount > 0)
		{
			try
			{
				bufferBrush.setColor(Color.RED);
				bufferBrush.fillRect(0, 0, 700, 700);
				g.drawImage(bufferImage, 0, 0, this);

				Thread.sleep(30);

				bufferBrush.drawImage(backgroundImg, 0, 0, this);
				g.drawImage(bufferImage, 0, 0, this);
				
				Thread.sleep(100);
				
				bufferBrush.setColor(Color.WHITE);
				bufferBrush.fillRect(0, 0, 700, 700);
				g.drawImage(bufferImage, 0, 0, this);
				
				Thread.sleep(30);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bombflashCount--;
		}

		// draw meteors on the screen
		if(meteors.size() > 0)
		{
			for(int i = 0; i < meteors.size(); i++)
			{
				bufferBrush.setColor(meteors.get(i).getColor());
				bufferBrush.fillPolygon(meteors.get(i).getShape());
			}
		}
		
		// draw laser beam on the screen
		if(gunShotting)
		{
			
			bufferBrush.setColor(Color.RED);
//				bufferBrush.drawLine(80, screenHeight - 70, hitX, hitY);
			bufferBrush.fillPolygon(gunBeam);

		}
		g.drawImage(bufferImage, 0, 0, this);
		repaint();
	}
	
	//OVER-RIDE of method update() here
	@Override
	public void update(Graphics g)
	{
		//just make the call to paint() directly
		paint(g);//avoids the bucket of paint being thrown on the applet canvas. 

	}
	
	//Space key listener for nuclear bomb
	private class BombKeyListener extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_SPACE)
			{

				if(bombavailble >= 10)
				{
					if(bombaudioClip != null)
						bombaudioClip.play();
					
					bombavailble = 0;
					meteors.removeAll(meteors);
					
					
					//increase difficulty as penalty
					if(dropInterval > 50)
						dropInterval -= 10;

					if(meteorInterval > 5)
						meteorInterval--;
					
					//serve as counter for flicker on display
					bombflashCount = 3;
					bombavailble = 0; //reset bomb
				}
			}
		}
	}//end of BombKeyListener
	
	//mouse listener to judge hit or not
	private class ShotMouseListener extends MouseAdapter
	{

		@Override
		public void mousePressed(MouseEvent e)
		{
			
			if(gunaudioClip != null)
				gunaudioClip.play();
			
			//go through all meteors 
			for(int i = 0; i < meteors.size(); i++)
			{
				if(meteors.get(i).getTarget(e.getX(), e.getY()))
				{

					gunShotting = true;
					gunShotTimer.start();
					hitX = (int)meteors.get(i).getX();
					hitY = (int)meteors.get(i).getY();
					
					//generate laser beam polygon to draw on screen
					gunBeam = ELIAlgorithm.generateBeam(80, screenHeight - 70, hitX, hitY, beamWidth);

					if(meteors.get(i).scoredHit() == 0)
					{
						explosinClip.play();
						if(meteors.get(i).isTwin())
						{
							Meteor childR = new Meteor(meteors.get(i), meteors.get(i).getRadius());
							Meteor childL = new Meteor(meteors.get(i), - meteors.get(i).getRadius());
							meteors.remove(i);
							meteors.add(childR);
							meteors.add(childL);
						}
						else
						{
							meteors.remove(i);
						}
						
						score++;			
						bombavailble++;
					
						//Win!
						if(score == 50)
						{
							isRunning = false;
							JOptionPane.showMessageDialog(null, "Congratulations! YOU SAVE THE WORLD!") ;
							meteors.clear();
						}
					}
				}
			}
		}
	}//end MyMouseListener class
	
	//Thread to generate new meteor
	private class GenerateMeteor implements Runnable
	{
			@Override
			public void run()
			{
				while(isRunning)
				{

					if((int)(Math.random() * 100) < possibility)
						meteors.add(new Meteor());	
					
					try
					{
						Thread.sleep(meteorInterval);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		
	} //end class GenerateMeteor
	
	//Thread to update meteors' position
	private class UpdatePosition implements Runnable
	{

		@Override
		public void run()
		{
			while(isRunning)
			{
				if(meteors.size() > 0)
				{
					for(int i = 0; i < meteors.size(); i++)
					{
						meteors.get(i).updatePos();
						if(meteors.get(i).getY() >= (screenHeight - meteors.get(i).getRadius()))
						{
							isRunning = false;					
							JOptionPane.showMessageDialog(null, "Extinction Level Incident has occurred¡­BYE-BYE!") ;
							meteors.clear();
						}
					}
				}
				
				try
				{
					Thread.sleep(dropInterval);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
		}
	}//end class UpdatePosition
	
	
	//Timer to control laser beam display time
	private class GunShotTimerListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent ev)
		{
			gunShotting = false;
			
		}
		
	}

	@Override
	public void run()
	{
		
		// choose difficulty level
		String[] levelStr = {"Easy", "Not Hard", "Normal", "Hard", "Nightmare"};
		String level = (String)JOptionPane.showInputDialog(null, "Select Difficulty Level:\n", "Take Your Challenge", 
													JOptionPane.PLAIN_MESSAGE, null, levelStr, "Normal");
		if(level != null)
		{
			switch(level)
			{
				case "Easy": dropInterval = 300; break;
				case "Not Hard": dropInterval = 250; break;
				case "Normal": dropInterval = 200; break;
				case "Hard": dropInterval = 120; break;
				case "Nightmare": dropInterval = 60; break;
				default: dropInterval = 200;
			}
		}

		gunShotTimer = new Timer(gunLastInterval, new GunShotTimerListener());
		
		mouseListener = new ShotMouseListener();
		keyListener = new BombKeyListener();
		birthThread = new Thread(new GenerateMeteor());
		
		speedThread = new Thread(new UpdatePosition());
		
		isRunning = true;	
		

		birthThread.start();
		speedThread.start();
		this.addMouseListener(mouseListener);
		this.addKeyListener(keyListener);
		
		while(isRunning)
		{
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		this.removeMouseListener(mouseListener);
		this.removeKeyListener(keyListener);
		bombavailble = 0;
	}

}