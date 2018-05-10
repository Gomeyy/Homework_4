import com.sun.javafx.iio.ImageFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import javax.swing.border.TitledBorder;
public class instructions extends JPanel {
    JButton back;
    int width, height;
    // constructor
    public instructions(int w, int h) {

        this.width = w;
        this.height = h;
        int xPos=w/30;
        int yPos=h/10;
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "INSTRUCTIONS", TitledBorder.CENTER, TitledBorder.TOP, new Font("Times New Roman", Font.PLAIN, 45), Color.WHITE));

        // back button to main menu
        back=new JButton("←");
        this.add(back);
        back.setBounds(xPos, yPos+545, 75, 25);
        back.setFont(new Font("Sans Serif", Font.BOLD, 25));
        back.setBackground(Color.WHITE);

        // labels
        JLabel moveKeys=new JLabel();
        moveKeys.setIcon(new ImageIcon(getClass().getResource("resources/wasd.png")));
        moveKeys.setBounds(w/25, yPos, 700, 170);
        this.add(moveKeys);
        JLabel movement=new JLabel("Use the arrow keys or WASD to move your snake towards the food.");
        movement.setFont(new Font("Serif", Font.BOLD, 20));
        movement.setForeground(Color.WHITE);
        movement.setBounds(w/17, yPos+80, 700, 200);
        this.add(movement);

        JLabel pause=new JLabel("Press Esc to pause and unpause.");
        pause.setIcon(new ImageIcon(getClass().getResource("resources/esc.png")));
        pause.setFont(new Font("Serif", Font.BOLD, 20));
        pause.setForeground(Color.WHITE);
        pause.setBounds(w/3-85, yPos+160, 400, 170);
        this.add(pause);

        int test = (w - (225 * 2)) / 3;
        int bor = 2;
        JLabel dieGif1=new JLabel();
        JPanel border1 = new JPanel();
        border1.setBackground(Color.white);
        border1.setBounds(test - bor, (yPos+290) - bor, 225 + (bor * 2), 225 + (bor * 2));
        dieGif1.setIcon(new ImageIcon(getClass().getResource("resources/SnakeDieWallSmall.gif")));
        dieGif1.setBounds(test, yPos+290, 225, 225);
        JLabel dieGif2=new JLabel();
        JPanel border2 = new JPanel();
        border2.setBackground(Color.white);
        border2.setBounds(((test * 2) + 225) - bor, (yPos+290) - bor, 225 + (bor * 2), 225 + (bor * 2));
        dieGif2.setIcon(new ImageIcon(getClass().getResource("resources/SnakeDieSelfSmall.gif")));
        dieGif2.setBounds((test * 2) + 225, yPos+290, 225, 225);
        this.add(dieGif1); this.add(dieGif2); this.add(border1); this.add(border2);
        JLabel avoid=new JLabel("Avoid hitting yourself and the walls.");
        avoid.setFont(new Font("Serif", Font.BOLD, 20));
        avoid.setForeground(Color.WHITE);
        avoid.setBounds(w/4, yPos+510, 700, 40);
        this.add(avoid);
    }
}
