import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    JButton play, instructions, settings, highScores;
    int buttonWidth = 280, buttonHeight = 45, xPos, yPos;
    int [][] snakeLogoCords = {
            {1, 2, 3, 6}, {0, 3, 6}, {0, 3, 6}, {0, 3, 6}, {0, 4, 5}, {}, //S
            {0, 1, 2, 3, 4, 5, 6}, {2}, {3}, {4}, {0, 1, 2, 3, 4, 5, 6}, {}, //N
            {1, 2, 3, 4, 5, 6}, {0, 3}, {0, 3}, {0, 3}, {1, 2, 3, 4, 5, 6}, {}, //A
            {0, 1, 2, 3, 4, 5, 6}, {3}, {2, 4}, {1, 5}, {0, 6}, {}, //K
            {0, 1, 2, 3, 4, 5, 6}, {0, 3, 6,}, {0, 3, 6}, {0, 3, 6}, {0, 6} //E
            };
    shapeItem[][] snakeLogo;
    int offsetY, offsetX, size, col = 0, ind = 0;
    Color logoColor = Color.WHITE, background = Color.BLACK;
    // constructor
    public Menu(int w, int h) {
        this.size = w / 35;
        this.offsetY = ((((h / 2) / this.size) - 7) / 2) * this.size;
        this.offsetX = (((w / this.size) - 28) / 2) * this.size;
        snakeLogo = new shapeItem[snakeLogoCords.length][];
        for (int r = 0; r < snakeLogoCords.length; r++) {
            snakeLogo[r] = new shapeItem[snakeLogoCords[r].length];
            for (int sn = 0; sn < snakeLogoCords[r].length; sn++) {
                int y = snakeLogoCords[r][sn] * this.size;
                snakeLogo[r][sn] = new shapeItem(new Rectangle(
                        (r * this.size) + this.offsetX,
                        y + this.offsetY,
                        this.size - 2,
                        this.size - 2), this.logoColor);
            }
        }
        //creating components & setting positions
        xPos = w / 2 - (buttonWidth / 2);
        yPos = h / 2 - (buttonHeight / 2);
        this.setLayout(null);

        play = new JButton("Play");
        newButton(play, 0);

        instructions = new JButton("Instructions");
        newButton(instructions, 50);

        settings = new JButton("Settings");
        newButton(settings, 100);

        highScores = new JButton("High Scores");
        newButton(highScores, 150);

        // editing colors and fonts
        this.setBackground(this.background);

    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        for(shapeItem[] rows : snakeLogo) {
            for (shapeItem snake : rows) {
                g2.setColor(snake.getColor());
                g2.fill(snake.getShape());
            }
        }
    }

    public void wiggle() {
        int max = 10;
        int inc = 1;
        int curr = col < 29 ? col : 29;
        for(shapeItem[] rows : snakeLogo) {
            for (shapeItem snake : rows) {
                snake.vel = snake.getDiff() == 0 ?
                        -inc : snake.getDiff() == max ? inc : snake.vel;
                snake.setRec(snake.getX(), snake.getY() + snake.vel, snake.getH());
            }
            if (curr == 0) {break;}
            curr--;
        }
        col++;
    }

    public void newButton(Component b, int offset){
        b.setBounds(xPos, yPos + offset, buttonWidth, buttonHeight);
        b.setBackground(Color.WHITE);
        b.setForeground(Color.BLUE);
        b.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        this.add(b);
    }

    public void updateColor() {
        this.setBackground(this.background);
        for(shapeItem[] cols: snakeLogo) {
            for(shapeItem logo: cols) {
                logo.setColor(this.logoColor);
            }
        }
        this.revalidate(); this.repaint();
    }
}