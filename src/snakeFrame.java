import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

class snakeFrame extends JFrame {

    Snake game;
    Menu main;
    instructions ins;
    settings sett;

    int score, xVel, newX, yVel, newY, f;
    boolean p = true, fromGame = false;
    int startLength;

    Timer update, vel, rainbow, countDown;
    String countDownNum = "3";
    int countDownFont = 0, countDownIteration = 0;
    Clip clip;

    snakeFrame(int rows, int cols, int size, int numSnakes) {
        //Initialize the frame, and screens
        super("Snake");
        this.setSize(cols * size, (rows * size) + 20);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.startLength = numSnakes;

        this.main = new Menu(this.getWidth(), this.getHeight());
        this.ins = new instructions(this.getWidth(), this.getHeight());
        this.game = new Snake(rows, cols , size, numSnakes);
        this.sett = new settings();

        // loops music
        try {
            File f = new File(this.getClass().getResource("resources/Marshmello-Alone.wav").toURI());
            clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            clip.open(ais);
            clip.start();
            clip.loop(-1);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        //Add main screen
        this.add(this.main);
        //this.add(this.sett);

        //Define keybinds
        int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
        Keybinds[] keybinds = new Keybinds[]{
                new Keybinds(KeyEvent.VK_UP, "UP", new dirAction(0, -1)),
                new Keybinds(KeyEvent.VK_W, "UP"),
                new Keybinds(KeyEvent.VK_RIGHT, "RIGHT", new dirAction(1, 0)),
                new Keybinds(KeyEvent.VK_D, "RIGHT"),
                new Keybinds(KeyEvent.VK_DOWN, "DOWN", new dirAction(0, 1)),
                new Keybinds(KeyEvent.VK_S, "DOWN"),
                new Keybinds(KeyEvent.VK_LEFT, "LEFT", new dirAction(-1, 0)),
                new Keybinds(KeyEvent.VK_A, "LEFT"),
                new Keybinds(KeyEvent.VK_ENTER, "ESC"),
                new Keybinds(KeyEvent.VK_SPACE, "ESC"),
                new Keybinds(KeyEvent.VK_ESCAPE, "ESC")
        };

        //Set up keybinds
        for (Keybinds keybind : keybinds) {
            this.game.getInputMap(IFW).put(KeyStroke.getKeyStroke(keybind.c, 0), keybind.name);
            if (keybind.n) {
                this.game.getActionMap().put(keybind.name, keybind.action);
            }
        }

        Timer countDownHelper = new Timer(1, null);
        countDownHelper.addActionListener(e -> {
            this.game.countdown(this.countDownNum, this.countDownFont);
            this.countDownFont++;
        });
        countDown = new Timer(1000, null);
        countDown.addActionListener(e -> {
            if(this.countDownIteration <= 1) {
                this.countDownNum = this.countDownIteration == 0 ? "2" : "1";
                this.countDownFont = 0;
                this.countDownIteration++;
            } else {
                countDownHelper.stop();
                countDown.stop();
                this.countDownIteration = this.countDownFont = 0;
                this.countDownNum = "3";
                this.game.countdownDone();

                vel.start();
                update.start();
                if(this.game.bRainbow) {
                    rainbow.start();
                }

                p = false;
            }
        });
        //Timer that controls logo animation
        Timer animation = new Timer(60, null);
        animation.addActionListener(e -> {
            this.main.wiggle();
            this.main.repaint();
        });
        animation.start();

        //Timer that controls the snake being a rainbow, if set to true
        rainbow = new Timer(100, null);
        rainbow.addActionListener(e -> {
            game.ind++;
            if(game.ind >= game.rainbow.length){game.ind = 0;}
            game.repaint();
        });

        //Timer that serves as a buffer, so that you aren't able to input commands too fast which caused an issue of flipping
        //velocities
        vel = new Timer(1, null);
        vel.addActionListener(e -> {
            if(this.xVel != this.newX || this.yVel != this.newY) {
                if (Math.abs(this.newX - game.xVel) != 2 && Math.abs(this.newY - game.yVel) != 2) {
                    this.xVel = this.newX;
                    this.yVel = this.newY;
                }
            }
        });

        //Timer that updates the snake
        update = new Timer(81, null);
        update.addActionListener(e -> {

            //Checks for food each update, if found increases score
            if(game.foodCheck()) {
                score++;
            }

            //Calls update function in snake to move to next section of grid
            game.update(this.xVel, this.yVel);

            //Checks for snake collision
            if(game.collision) {

                //If true sets velocity to zero and sets vel timer to stop so no more updates to the variables can be made
                this.xVel = this.yVel = 0;
                vel.stop();

                //Runs a function to make snake flash red a certain amount of times
                if (f < 12) {
                    this.game.dead();
                    f++;

                //Runs a function to fade snake out, and fade Game Over screen in
                } else if (f < 24) {
                    this.game.gameOver(score);
                    f++;
                }
            }
        });

        //Refers to the button in the Game Over screen
        this.game.cont.addActionListener(e -> {

            //Resets Game Over screen to be invisible again
            this.game.cont();

            //Resets the snake to middle of screen, and with starting length and all variables in snakeFrame
            reset();
            animation.start();
            fromGame = false;

            //Swaps the game component out with the menu component
            swap(this.game, this.main);
        });

        //Refers to the play button in menu
        this.main.play.addActionListener(e -> {

            //Swaps the menu component out with the game component
            swap(this.main, this.game);
            animation.stop();
            countDown.start();
            countDownHelper.start();
        });

        //Refers to the instructions button in menu
        this.main.instructions.addActionListener(e -> {
            swap(this.main, this.ins);
        });

        //Refers to the settings button in menu
        this.main.settings.addActionListener(e -> {
            swap(this.main, this.sett);
        });

        //Refers to the back button in instructions
        this.ins.back.addActionListener(e -> {

            //Swaps out instructions component for menu component
            swap(this.ins, this.main);
        });

        this.sett.back.addActionListener(e -> {
            if(fromGame) {
                swap(this.sett, this.game);
            } else {
                swap(this.sett, this.main);
            }
        });

        this.game.settings.addActionListener(e -> {
            swap(this.game, this.sett);
            fromGame = true;
        });

        this.game.exit.addActionListener(e -> {

            //Unpause game
            game.unpaused();
            animation.start();
            p = false;
            clip.start();
            clip.loop(-1);

            //Resets the snake to middle of screen, and with starting length and all variables in snakeFrame, stops timers
            reset();

            //Swaps the game component out with the menu component
            swap(this.game, this.main);
        });

        this.game.restart.addActionListener(e -> {
            this.game.gameOver.remove(this.game.cont); this.game.gameOver.remove(this.game.restart1);
            clip.start(); clip.loop(-1);
            this.game.resetGameOver();
            //Resets the snake to middle of screen, and with starting length and all variables in snakeFrame, stops timers
            reset();

            countDown.start();
            countDownHelper.start();
            if(this.game.bRainbow) {
                rainbow.start();
            }

            p = false;
        });

        this.game.restart1.addActionListener(e -> this.game.restart.doClick());

        this.sett.snakeSpeed.set.addActionListener(e -> {
            JSlider temp = (JSlider) this.sett.snakeSpeed.main;
            update.setDelay(81 - temp.getValue());
        });

        this.sett.snakeSpeed.reset.addActionListener(e -> {
            JSlider temp = (JSlider) this.sett.snakeSpeed.main;
            temp.setValue(this.sett.snakeSpeed.def);
            this.sett.snakeSpeed.main = temp;
            update.setDelay(81 - temp.getValue());
        });

        this.sett.snakeHead.set.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.snakeHead.main;
            this.game.head = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.game.updateColor();
        });

        this.sett.snakeHead.reset.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.snakeHead.main;
            temp.setSelectedIndex(this.sett.snakeHead.def);
            this.sett.snakeHead.main = temp;
            this.game.head = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.game.updateColor();
        });

        this.sett.snakeBody.set.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.snakeBody.main;
            this.game.body = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.game.updateColor();
        });

        this.sett.snakeBody.reset.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.snakeBody.main;
            temp.setSelectedIndex(this.sett.snakeBody.def);
            this.sett.snakeBody.main = temp;
            this.game.body = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.game.updateColor();
        });

        this.sett.snakeFood.set.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.snakeFood.main;
            this.game.foodColor = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.game.updateColor();
        });

        this.sett.snakeFood.reset.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.snakeFood.main;
            temp.setSelectedIndex(this.sett.snakeFood.def);
            this.sett.snakeFood.main = temp;
            this.game.foodColor = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.game.updateColor();
        });

        this.sett.snakeDeath.set.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.snakeDeath.main;
            this.game.deathColor = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.game.updateColor();
        });

        this.sett.snakeDeath.reset.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.snakeDeath.main;
            temp.setSelectedIndex(this.sett.snakeDeath.def);
            this.sett.snakeDeath.main = temp;
            this.game.deathColor = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.game.updateColor();
        });

        this.sett.gameBackground.set.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.gameBackground.main;
            this.game.background = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.game.updateColor();
        });

        this.sett.gameBackground.reset.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.gameBackground.main;
            temp.setSelectedIndex(this.sett.gameBackground.def);
            this.sett.gameBackground.main = temp;
            this.game.background = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.game.updateColor();
        });

        this.sett.menuBackground.set.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.menuBackground.main;
            this.main.background = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.main.updateColor();
        });

        this.sett.menuBackground.reset.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.menuBackground.main;
            temp.setSelectedIndex(this.sett.menuBackground.def);
            this.sett.menuBackground.main = temp;
            this.main.background = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.main.updateColor();
        });

        this.sett.menuLogo.set.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.menuLogo.main;
            this.main.logoColor = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.main.updateColor();
        });

        this.sett.menuLogo.reset.addActionListener(e -> {
            JComboBox<settings.colorObject> temp = (JComboBox) this.sett.menuLogo.main;
            temp.setSelectedIndex(this.sett.menuLogo.def);
            this.sett.menuLogo.main = temp;
            this.main.logoColor = temp.getItemAt(temp.getSelectedIndex()).getColor();
            this.main.updateColor();
        });

        this.sett.startFood.set.addActionListener(e -> {
            int errorTest = (rows * cols) - this.startLength;
            try {
                JTextField temp = (JTextField) this.sett.startFood.main;
                if (temp.getText().equals("")) {this.sett.startFood.reset.doClick();}
                else {
                    if(Integer.parseInt(temp.getText()) > errorTest) {
                        temp.setText(Integer.toString(errorTest));
                        this.sett.startFood.main = temp;
                        this.game.startFood = errorTest;
                    } else {
                        this.game.startFood = Integer.parseInt(temp.getText());
                    }
                    if (animation.isRunning()) {reset();}
                }
            } catch(Exception d) {
                this.sett.startFood.reset.doClick();
                System.out.println(d.getMessage());
            }
        });

        this.sett.startFood.reset.addActionListener(e -> {
            JTextField temp = (JTextField) this.sett.startFood.main;
            temp.setText(Integer.toString(this.sett.startFood.def));
            this.sett.startFood.main = temp;
            this.game.startFood = Integer.parseInt(temp.getText());
            if(animation.isRunning()) {reset();}
        });

        this.sett.startLength.set.addActionListener(e -> {
            int errorTest = (rows * cols) - this.game.startFood;
            try {
                JTextField temp = (JTextField) this.sett.startLength.main;
                if (temp.getText().equals("")) {this.sett.startLength.reset.doClick();}
                else {
                    if(Integer.parseInt(temp.getText()) > errorTest) {
                        temp.setText(Integer.toString(errorTest));
                        this.sett.startLength.main = temp;
                        this.startLength = errorTest;
                    } else {
                        this.startLength = Integer.parseInt(temp.getText());
                    }
                    if (animation.isRunning()) {reset();}
                }
            } catch(Exception d) {
                this.sett.startLength.reset.doClick();
                System.out.println(d.getMessage());
            }
        });

        this.sett.startLength.reset.addActionListener(e -> {
            JTextField temp = (JTextField) this.sett.startLength.main;
            temp.setText(Integer.toString(this.sett.startLength.def));
            this.sett.startLength.main = temp;
            this.startLength = Integer.parseInt(temp.getText());
            if(animation.isRunning()) {reset();}
        });

        this.sett.setAll.addActionListener(e -> {
            this.sett.snakeSpeed.set.doClick();
            this.sett.snakeHead.set.doClick();
            this.sett.snakeBody.set.doClick();
            this.sett.snakeFood.set.doClick();
            this.sett.snakeDeath.set.doClick();
            this.sett.gameBackground.set.doClick();
            this.sett.menuBackground.set.doClick();
            this.sett.menuLogo.set.doClick();
            this.sett.startFood.set.doClick();
            this.sett.startLength.set.doClick();
        });

        this.sett.resetAll.addActionListener(e -> {
            this.sett.snakeSpeed.reset.doClick();
            this.sett.snakeHead.reset.doClick();
            this.sett.snakeBody.reset.doClick();
            this.sett.snakeFood.reset.doClick();
            this.sett.snakeDeath.reset.doClick();
            this.sett.gameBackground.reset.doClick();
            this.sett.menuBackground.reset.doClick();
            this.sett.menuLogo.reset.doClick();
            this.sett.startFood.reset.doClick();
            this.sett.startLength.reset.doClick();
        });

        //Action that toggles paused on and off
        this.game.getActionMap().put("ESC", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!countDown.isRunning() && !game.collision) {
                    if (p) {
                        vel.start();
                        update.start();
                        p = false;
                        game.unpaused();
                        clip.start();
                        clip.loop(-1);
                    } else {
                        vel.stop();
                        update.stop();
                        p = true;
                        game.paused();
                        clip.stop();
                    }
                }
            }
        });

        //Initial reset to assign all variables
        reset();
    }

    //Class that handles switching the snakes velocity
    private class dirAction extends AbstractAction {
        int tempX;
        int tempY;
        dirAction(int x, int y) {
            this.tempX = x;
            this.tempY = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            //Stops the changing of the velocity when paused
            if (!p && !countDown.isRunning()) {

                //Checks if player is trying to flip snake around
                if (Math.abs(xVel - this.tempX) != 2 && Math.abs(yVel - this.tempY) != 2) {

                    //Assigns new velocities
                    newX = this.tempX;
                    newY = this.tempY;
                }
            }
        }
    }

    //Class that handles keybinds
    public class Keybinds {
        int c; String name; dirAction action;
        boolean n = false;
        Keybinds(int c, String n, dirAction action) {
            this.c = c;
            this.name = n;
            this.action = action;
            this.n = true;
        }
        Keybinds(int c, String n) {
            this.c = c;
            this.name = n;
        }

    }

    //Function that resets all variables in snakeFrame
    public void reset() {
        this.game.update(0, 0);
        this.game.countdownSet();
        this.newY = -1;
        this.newX = 0;
        this.f = 0;
        score = 0;
        this.game.reset(this.startLength);

        update.stop();
        vel.stop();
        rainbow.stop();
    }

    public void toggleTimers() {
    }

    //Function that swaps input components
    public void swap(Component a, Component b) {
        this.remove(a); this.add(b);
        this.revalidate(); this.repaint();
    }

    public static void main(String[] args) {
//        try {
//            FileWriter fstream = new FileWriter("HighScore");
//            BufferedWriter out = new BufferedWriter(fstream);
//            out.write("hello");
//            out.close();
//        }catch (Exception e){
//            System.err.println("Error: " + e.getMessage());
//        }
        snakeFrame main = new snakeFrame(55, 55, 12, 3);
        main.setVisible(true);
    }
}
