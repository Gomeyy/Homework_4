import javax.swing.*;
import java.awt.*;

public class settings extends JPanel {
    colorObject[] colors = {new colorObject("White", Color.WHITE), new colorObject("Black", Color.BLACK),
                            new colorObject("Green", Color.GREEN), new colorObject("Yellow", Color.YELLOW),
                            new colorObject("Orange", Color.ORANGE), new colorObject("Blue", Color.BLUE),
                            new colorObject("Red", Color.RED), new colorObject("Cyan", Color.CYAN),
                            new colorObject("Dark Gray", Color.DARK_GRAY), new colorObject("Gray", Color.GRAY),
                            new colorObject("Light Gray", Color.LIGHT_GRAY), new colorObject("Magenta", Color.MAGENTA),
                            new colorObject("Pink", Color.PINK)};
    setting snakeSpeed, snakeHead, snakeBody, snakeFood, gameBackground, menuBackground, menuLogo, startFood, startLength,
            snakeDeath;
    JButton back, setAll, resetAll;

    settings() {
        this.setLayout(new GridLayout(11, 4, 10, 20));
        this.setBackground(Color.BLACK);

        JSlider speed = new JSlider(0, 80); speed.setValue(0);
        speed.setPaintTicks(true); speed.setPaintLabels(true);
        speed.setMajorTickSpacing(20); speed.setMinorTickSpacing(5);
        speed.setForeground(Color.white);
        snakeSpeed = new setting("Snake Speed Increase", speed, 0);
        snakeSpeed.addTo(this);

        JComboBox<colorObject> head = new JComboBox<>(colors);
        head.setEditable(false); head.setSelectedIndex(2);
        snakeHead = new setting("Snake Head Color", head, 2);
        snakeHead.addTo(this);

        JComboBox<colorObject> body = new JComboBox<>(colors);
        body.setEditable(false); body.setSelectedIndex(0);
        snakeBody = new setting("Snake Body Color", body, 0);
        snakeBody.addTo(this);

        JComboBox<colorObject> cFood = new JComboBox<>(colors);
        cFood.setEditable(false); cFood.setSelectedIndex(6);
        snakeFood = new setting("Food Color", cFood, 6);
        snakeFood.addTo(this);

        JComboBox<colorObject> death = new JComboBox<>(colors);
        death.setEditable(false); death.setSelectedIndex(6);
        snakeDeath = new setting("Death Color", death, 6);
        snakeDeath.addTo(this);

        JComboBox<colorObject> gameBack = new JComboBox<>(colors);
        gameBack.setEditable(false); gameBack.setSelectedIndex(1);
        gameBackground = new setting("Game Background Color", gameBack, 1);
        gameBackground.addTo(this);

        JComboBox<colorObject> menuBack = new JComboBox<>(colors);
        menuBack.setEditable(false); menuBack.setSelectedIndex(1);
        menuBackground = new setting("Menu Background Color", menuBack, 1);
        menuBackground.addTo(this);

        JComboBox<colorObject> logo = new JComboBox<>(colors);
        logo.setEditable(false); logo.setSelectedIndex(0);
        menuLogo = new setting("Menu Logo Color", logo, 0);
        menuLogo.addTo(this);

        JTextField food = new JTextField("10");
        food.setHorizontalAlignment(SwingConstants.CENTER);
        startFood = new setting("Total amount of food", food, 10);
        startFood.addTo(this);

        JTextField length = new JTextField("3");
        length.setHorizontalAlignment(SwingConstants.CENTER);
        startLength = new setting("Initial length of snake", length, 3);
        startLength.addTo(this);

        back = new JButton("←");
        back.setFont(new Font("Sans Serif", Font.BOLD, 25));
        setAll = new JButton("Set All");
        setAll.setBackground(Color.WHITE);
        setAll.setForeground(Color.BLUE);
        setAll.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        resetAll = new JButton("Reset All");
        resetAll.setBackground(Color.WHITE);
        resetAll.setForeground(Color.BLUE);
        resetAll.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        this.add(back);
        this.add(new JLabel(""));
        this.add(setAll);
        this.add(resetAll);
    }


    public class setting{
        JLabel label;
        Component main;
        int def;
        JButton set, reset;

        setting(String s, Component b, int d){
            this.label = new JLabel(s);
            this.label.setHorizontalAlignment(SwingConstants.RIGHT);
            this.label.setForeground(Color.white);
            this.main = b;
            this.def = d;
            this.set = new JButton("Set");
            this.set.setBackground(Color.WHITE);
            this.set.setForeground(Color.BLUE);
            this.set.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            this.reset = new JButton("Reset");
            this.reset.setBackground(Color.WHITE);
            this.reset.setForeground(Color.BLUE);
            this.reset.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        }

        public void addTo(JPanel b) {
            b.add(this.label);
            b.add(this.main);
            b.add(this.set);
            b.add(this.reset);
        }
    }

    public class colorObject{
        private String label;
        private Color color;

        colorObject(String l, Color c) {
            this.label = l;
            this.color = c;
        }

        public Color getColor() {
            return this.color;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

}
