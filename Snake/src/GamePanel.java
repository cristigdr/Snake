import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;



public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 95;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 3;
	int foodEaten;
	int foodX;
	int foodY;
	int poisonX;
	int poisonY;
	int superFoodX;
	int superFoodY;
	int low = 15000;
	int high = 20000;
	int result;
	char direction = 'R';
	boolean running = false;
	boolean SF = false;
	Timer timer;
	Random random1;
	private Image background;

	
	
	GamePanel(){
		random1 = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		x[0] = SCREEN_WIDTH/2;
		y[0] = SCREEN_HEIGHT/2;
	}
	
	
	public void startGame() {
		Food();
		Poison();
		SuperFood();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		g.drawImage(background, 0, 0,null);
		
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.green);
		g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
		
		g.setColor(Color.green);
		g.fillOval(poisonX, poisonY, UNIT_SIZE, UNIT_SIZE);
		
		if(SF == true) {
		g.setColor(Color.orange);
		g.fillOval(superFoodX, superFoodY, UNIT_SIZE, UNIT_SIZE);
		}
		
		if(running) {
			for(int i = 0; i<bodyParts;i++) {
				if(i == 0) {
					g.setColor(Color.yellow);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 35));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+foodEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	
	public void Food() {
		foodX = random1.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		foodY = random1.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
	}
	
	public void Poison() {
		poisonX = random1.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		poisonY = random1.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}

	public void SuperFood() {
		
	ActionListener taskPerformer = new ActionListener() {
		
	      public void actionPerformed(ActionEvent evt) {
	    	  
	    	  superFoodX = random1.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
	    	  superFoodY = random1.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	    	  SF = true;
	    	  result = random1.nextInt(high-low)+low;
	    	  
	      }
	  };
	  
	  new Timer(result = random1.nextInt(high-low)+low, taskPerformer).start();
	  
	}
	
	public void move() {
		
		for(int i = bodyParts; i >0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
			
		}
	}
	
	public void checkFood() {
		if((x[0] == foodX) && (y[0] == foodY)) {
			bodyParts++;
			foodEaten++;
			Food();
			Poison();
		}
	}
	
	public void checkPoison() {
		if((x[0] == poisonX) && (y[0] == poisonY)) {
			bodyParts--;
			foodEaten--;
			Poison();
			Food();
			
		}
	}
	
	public void checkSuperFood() {
		if((x[0] == superFoodX) && (y[0] == superFoodY)) {
			bodyParts = bodyParts + 5;
			foodEaten = foodEaten + 5;
			SF = false;
			
	}
		
			
	}
	
	public void checkCollisions(){
		//snake collides with its own body
		for(int i = bodyParts;i > 0;i--) {
			if((x[0] == x[i]) && (y[0] == y[i]))
				running = false;
		}
		//snake collides with left border
		if(x[0] < 0) {
			running = false;
		}
		//snake collides with right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//snake collides with upper border
		if(y[0] < 0 ) {
			running = false;
		}
		//snake collides with lower border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	
	
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 35));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+foodEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+foodEaten))/2, g.getFont().getSize());
		
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 100));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkFood();
			checkPoison();
			checkSuperFood();
			checkCollisions();
			
		}
		repaint();
		
	}
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			}
		}
	}

}
