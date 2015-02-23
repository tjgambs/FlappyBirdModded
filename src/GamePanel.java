import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePanel extends JPanel implements Runnable {

    private Game game;
    public GamePanel() {
        game = new Game();
        new Thread(this).start();
    }

    public void update() {
        game.update();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        for (Render r : game.getRenders())
            if (r.transform != null)
                g2D.drawImage(r.image, r.transform, null);
            else
                g.drawImage(r.image, r.x, r.y, null);


        g2D.setColor(Color.WHITE); //Change from black

        if (!game.started) {
            g2D.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g2D.drawString("Press SPACE to start", 170, 240);
        } else {
            g2D.setFont(new Font("TimesRoman", Font.PLAIN, 24));
            if(game.score>game.highScore) game.highScore = game.score;
            g2D.drawString("Score: " + Integer.toString(game.score), 10, 465);
            g2D.drawString("High Score: " + Integer.toString(game.highScore), 10, 30);
        }

        if (game.gameover) {
            try {
                refreshAll();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            g2D.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g2D.drawString("Press R to restart", 150, 240);
            if(game.score>game.highScore) game.highScore = game.score;
        }
    }

    public void run() {
        try {
            while (true) {
                update();
                setHighScore();
                Thread.sleep(25);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void refreshAll() throws FileNotFoundException, UnsupportedEncodingException
    {
        try 
        {
            PrintWriter HighScore = new PrintWriter("HighScore.txt", "UTF-8");
                HighScore.println(game.highScore + "\n");
            HighScore.close();
        }
        catch(Exception e){}
    }
    public void setHighScore()
    {
        File file = new File("HighScore.txt");
            try 
            {
                Scanner scanner = new Scanner(file);
                String line="";
                while (scanner.hasNextLine()) 
                {
                    line += scanner.nextLine();
                    game.highScore = Integer.parseInt(line);
                }
                scanner.close();
            } catch (FileNotFoundException e) {}
    }
}
