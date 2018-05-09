import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class Snake extends JPanel {
    private shapeItem[] snake;
    private shapeItem food;
    private shapeItem[] testFood;
    private int x, y, gSize, size, rows, cols, maxX, maxY, test = 0;
    int xVel, yVel;
    boolean collision, dead, bRainbow = false;
    private final Color VIOLET = new Color( 128, 0, 128 );
    private final Color INDIGO = new Color( 75, 0, 130 );
    Color[] c = {Color.green, Color.white};
    Color[] rainbow =  {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, INDIGO, VIOLET};
    Color head = Color.GREEN, body = Color.WHITE, background = Color.BLACK, foodColor = Color.RED, deathColor = Color.red;
    int startFood = 10;
    int ind = 0;
    JPanel gameOver, pause, countdown;
    JLabel game, over, score, paused, nums;
    JButton cont, settings, restart, restart1, exit;
    int opac = -20;
    int font;
    GridBagConstraints g = new GridBagConstraints();


    Snake(int rows, int cols, int gSize, int num){
        //Initialize variables
        this.setLayout(new GridBagLayout());
        this.setBackground(background);
        this.maxX = cols * gSize; this.maxY = rows * gSize;
        this.gSize = gSize;
        this.size = gSize - 2;
        this.rows = rows;
        this.cols = cols;

        //Create starting snake and food
        reset(num);

        //Initialize the gui
        gameOver = new JPanel(); pause = new JPanel(); countdown = new JPanel();
        game = new JLabel("GAME"); over = new JLabel("OVER"); score = new JLabel(); nums = new JLabel();
        paused = new JLabel("Paused");
        restart1 = new JButton("Restart"); cont = new JButton("Continue"); settings = new JButton("Settings");
        restart = new JButton("Restart"); exit = new JButton("Exit");

        //Load in custom font
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("resources/Game_Over.ttf")));
        } catch (IOException |FontFormatException e) {
            //Handle exception
            System.out.println(e.getMessage());
        }

        //Set the font
        font = (int) ((cols * gSize) / 3.5);
        Font customFont = new Font("Game Over", Font.PLAIN, font);
        Font customFontBig = new Font("Game Over", Font.PLAIN, (int) (font * 1.5));
        Font customFontMed = new Font("Game Over", Font.PLAIN, (int) (font / 1.5));
        Font customFontSmall = new Font("Game Over", Font.PLAIN, font / 2);
        game.setFont(customFont); over.setFont(customFont); paused.setFont(customFontBig);
        score.setFont(customFontSmall);
        cont.setFont(customFontSmall);
        restart1.setFont(customFontSmall);
        settings.setFont(customFontMed);
        restart.setFont(customFontMed);
        exit.setFont(customFontMed);

        //Align the labels to the center
        game.setHorizontalAlignment(SwingConstants.CENTER);
        over.setHorizontalAlignment(SwingConstants.CENTER);
        score.setHorizontalAlignment(SwingConstants.CENTER);
        nums.setHorizontalAlignment(SwingConstants.CENTER);

        //Make everything white and transparent
        game.setForeground(new Color(255, 255, 255, 0));
        over.setForeground(new Color(255, 255, 255, 0));
        score.setForeground(new Color(255, 255, 255, 0));
        cont.setOpaque(false); cont.setContentAreaFilled(false); cont.setBorderPainted(false);
        cont.setForeground(new Color(255,255,255,0));
        restart1.setOpaque(false); restart1.setContentAreaFilled(false); restart1.setBorderPainted(false);
        restart1.setForeground(new Color(255,255,255,0));
        settings.setOpaque(false); settings.setContentAreaFilled(false); settings.setBorderPainted(false);
        settings.setForeground(Color.white);
        restart.setOpaque(false); restart.setContentAreaFilled(false); restart.setBorderPainted(false);
        restart.setForeground(Color.white);
        exit.setOpaque(false); exit.setContentAreaFilled(false); exit.setBorderPainted(false);
        exit.setForeground(Color.white);
        paused.setForeground(Color.white);
        pause.setBackground(new Color(0, 0, 0, 150));
        nums.setForeground(Color.white);
        countdown.setOpaque(false);

        //Set layout for gameOver and add
        gameOver.setLayout(new GridLayout(6, 1));
        gameOver.setMaximumSize(new Dimension(maxX, maxY));
        gameOver.add(game); gameOver.add(over); gameOver.add(score); gameOver.add(new JLabel());
        gameOver.setOpaque(false);
        //this.add(gameOver, g);

        //Set layout for pause
        pause.setLayout(new GridLayout(8, 1));
        pause.add(paused);
        pause.add(new JLabel(""));
        pause.add(settings); pause.add(restart); pause.add(exit);
        pause.add(new JLabel("")); pause.add(new JLabel(""));

        //Set layout for countdown
        countdown.add(nums);
        //this.add(countdown, g);
    }

    //Draws all the squares
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;

        //Draw food
//        g2.setColor(food.getColor());
//        g2.fill(food.getShape());

        for(shapeItem food: testFood) {
            if(!food.noRoom) {
                g2.setColor(food.getColor());
                g2.fill(food.getShape());
            }
        }

        //Loop through all sections of the snake
        for(int s = 0; s < snake.length; s++) {
            int index = (ind + s) % (rainbow.length);
            if (!bRainbow) {
                g2.setColor(snake[s].getColor());
            } else {
                g2.setColor(rainbow[index]);
            }
            g2.fill(snake[s].getShape());
        }
    }

    //Set up snake
    public void reset(int num) {
        //If recovering from collision set it to false
        collision = false;

        //Find center of window and set starting cords
        this.x = ((((this.cols - 1) / 2) - 1) * this.gSize);
        this.y = ((((this.rows - 1) / 2) - 1) * this.gSize);

        //Set initial vertical offset to 0
        int offset = 0;

        //Set first square to be green
        Color tempC = head;
        snake = new shapeItem[num];

        //Create the snake
        for(int s = 0; s < num; s++){
            snake[s] = new shapeItem(new Rectangle(this.x, this.y + offset, this.size , this.size), tempC);
            offset += this.gSize;
            //Set the rest of the squares to be white
            tempC = body;
        }

        //Create a food
        testFood = new shapeItem[startFood];
        for(int f = 0; f < testFood.length; f++) {
            testFood[f] = newFood(f);
        }
        //food = newFood();
        this.revalidate(); this.repaint();
    }

    //Updates all squares
    public void update(int xVel, int yVel) {
        //Get supplied velocities
        this.xVel = xVel; this.yVel = yVel;

        //Set number of pixels for first square to move
        int xOff = xVel * this.gSize;
        int yOff = yVel * this.gSize;

        //Set new cords of first square
        this.x += xOff; this.y += yOff;

        //Check for collision at those cords
        collision(this.x, this.y);
        if(!collision) {

            //If no collision update back end of snake first
            for (int x = snake.length - 1; x > 0; x--) {
                int tempX = snake[x - 1].getShape().getBounds().x;
                int tempY = snake[x - 1].getShape().getBounds().y;
                snake[x].setRec(tempX, tempY, this.size);
            }

            //Then update first square to new position
            snake[0].setRec(this.x, this.y, this.size);
        }

        this.repaint();
    }

    //Adds a square to the snake
    public void addToSnake(){
        //Figure out on which side to place the next square
        int tempX1 = snake[snake.length - 1].getShape().getBounds().x;
        int tempY1 = snake[snake.length - 1].getShape().getBounds().y;
        int tempX2 = snake[snake.length - 2].getShape().getBounds().x;
        int tempY2 = snake[snake.length - 2].getShape().getBounds().y;

        if(tempX1 > tempX2 && tempY1 == tempY2){ tempX1 += gSize;
        } else if(tempX1 < tempX2 && tempY1 == tempY2){tempX1 -= gSize; }
        if(tempY1 > tempY2 && tempX1 == tempX2){ tempY1 += gSize;
        } else if(tempY1 < tempY2 && tempX1 == tempX2){ tempY1 -= gSize; }

        //Make a new array of snakes 1 length longer
        snake = Arrays.copyOf(snake, snake.length + 1);

        //Add new section
        snake[snake.length - 1] = new shapeItem(new Rectangle(tempX1, tempY1, this.size, this.size), body);
    }

    //Test for collision
    private void collision(int colX, int colY){
        collision = colX < 0 || colX >= this.maxX ||
                    colY < 0 || colY >= this.maxY ||
                    checkForSnake(colX, colY,"bodyCollision");
    }

    //Checks if the snake collides with wall/food/itself
    private boolean checkForSnake(double x, double y, String type) {
        //Initialize variables
        int start = 0;
        int length = snake.length;

        //If checking for food only check the head
        if(type.equals("foodEat")) {
            length = 1;

        //If checking for body collision skip the first square
        } else if(type.equals("bodyCollision")) {
            start = 1;
        }

        //Check snake against supplied cords
        for(int s = start; s < length; s++){

            //If equal return true
            if(snake[s].getX() == x && snake[s].getY() == y){
                return true;
            }
        }

        //If true never gets returned, no collision return false
        return false;
    }

    //Create a new food
    private shapeItem newFood(int ind){

        //Get random cords within the window
        int tempX = (int) (Math.random() * this.cols) * this.gSize;
        int tempY = (int) (Math.random() * this.rows) * this.gSize;

        //Set a new food at new cords
        Shape foodS = new Rectangle(tempX, tempY, this.size, this.size);
        //Shape foodS = new Ellipse2D.Double(tempX, tempY, this.size, this.size);

        //Set Color
        Color foodC = this.foodColor;

        //Check if food spawns on snake
        if(!checkForSnake(tempX, tempY, "") && !checkForFood(tempX, tempY, ind)){

            //If not create new shapeItem with supplied rectangle
            return new shapeItem(foodS, foodC);
        } else {

            //If true run newFood() again to get new cords
            //System.out.println("Food spawn attempt on snake or other food.");
            return newFood(ind);
            //newFood();
        }
    }

    //Check if snake ate a food
    public boolean checkFood(int i) {

        //Check collision against food cords
        //if(checkForSnake(food.getShape().getBounds().x, food.getShape().getBounds().y, "foodEat")){
        if(checkForSnake(testFood[i].getX(), testFood[i].getY(), "foodEat")){

            //If true add new square to snake, reset the food
            if(!collision) {
                addToSnake();
            }
            if(snake.length + testFood.length >= (rows * cols)) {
                System.out.println("No room for food");
                testFood[i].setNoRoom();
                //testFood[i].setRec(-1, -1, this.size);
            } else {
                testFood[i] = newFood(i);
            }

            //Return true to update score value in snakeFrame
            return true;
        }

        //If true isn't returned, no food collision return false
        return false;
    }

    public boolean foodCheck() {
        boolean result = false;
        for(int f = 0; f < testFood.length; f++) {
            result = checkFood(f);
            if(result){break;}
        }
        return !collision && result;
    }

    public boolean checkForFood(int x, int y, int ind){
        for(int f = 0; f < testFood.length; f++){
            if(f == ind){continue;}
            if(testFood[f] == null) {break;}
            if(testFood[f].getX() == x && testFood[f].getY() == y) {
                return true;
            }
        }
        return false;
    }

    //Function that runs when snake collides with self, or wall
    public void dead() {

        //For all sections of snake change color from standard to red, and back
        for(shapeItem snakes : snake){
            if(this.dead){
                snakes.setColor();
            } else {
                snakes.setColor(deathColor);
            }
        }

        //This is what alternates from turning red to turning back to standard
        //have to run function twice in order to get color change in order to slow down flashing, based off of
        //update timer in snakeFrame
        test++;
        if(test == 2) {
            this.dead = !this.dead;
            test = 0;
        }
    }

    //Function that runs after dead() is run a certain amount of times, determined in snakeFrame
    // takes current score as input
    public void gameOver(int fScore){

        //Increment all sections of the snakes and food opacity by set amount to fade out
        for(shapeItem snakes : snake){
            snakes.setOpacity(opac);
        }
        for(shapeItem food: testFood) {
            food.setOpacity(opac);
        }

        //If the score isn't 0 add score, if 0 leave empty
        if(fScore != 0) {
            score.setText("Score: " + fScore);
        }

        //Add continue button and revalidate
        gameOver.add(restart1);
        gameOver.add(cont);
        this.revalidate();

        //Fade in game over text and button as snake fades away
        int col = background == Color.WHITE ? 0 : 255;
        game.setForeground(new Color(col, col, col, game.getForeground().getAlpha() + -opac));
        over.setForeground(new Color(col, col, col, over.getForeground().getAlpha() + -opac));
        score.setForeground(new Color(col, col, col, score.getForeground().getAlpha() + -opac));
        cont.setForeground(new Color(col, col, col, cont.getForeground().getAlpha() + -opac));
        restart1.setForeground(new Color(col, col, col, restart1.getForeground().getAlpha() + -opac));
    }

    //Function that runs when the continue button is pressed
    public void cont() {

        //Reset score
        score.setText("");

        //Remove continue button from panel and revalidate
        gameOver.remove(cont); gameOver.remove(restart);
        this.revalidate();

        //Reset game over components to be transparent
        resetGameOver();
    }

    public void paused() {
        this.remove(gameOver); this.add(pause, g);
        this.revalidate(); this.repaint();
    }

    public void unpaused() {
        this.remove(pause); this.add(gameOver, g);
        this.revalidate(); this.repaint();
    }

    public void updateColor() {
        for (int x = snake.length - 1; x > 0; x--) {
            snake[x].setColor(body);
        }
        snake[0].setColor(head);

        for(shapeItem snake: snake) {
            snake.setStartColor(snake.getColor());
        }

        for(shapeItem food: testFood) {
            food.setColor(this.foodColor);
        }


        this.setBackground(background);
        this.revalidate(); this.repaint();
    }

    public void countdown(String s, int f) {
        Color col = background == Color.WHITE ? Color.BLACK : Color.WHITE;
        nums.setForeground(col);
        Font countDownFont = new Font("Ariel", Font.BOLD, (int) ((font) - f));
        nums.setFont(countDownFont);
        nums.setText(s);
        this.revalidate(); this.repaint();
    }

    public void countdownDone() {
        this.remove(countdown); this.add(gameOver, g);
        this.revalidate(); this.repaint();
    }

    public void countdownSet() {
        this.remove(gameOver); this.remove(pause); this.add(countdown, g);
        this.revalidate(); this.repaint();
    }

    public void resetGameOver() {
        game.setForeground(new Color(255, 255, 255, 0));
        over.setForeground(new Color(255, 255, 255, 0));
        score.setForeground(new Color(255, 255, 255, 0));
        cont.setForeground(new Color(255, 255, 255, 0));
        restart1.setForeground(new Color(255, 255, 255, 0));
    }

}
