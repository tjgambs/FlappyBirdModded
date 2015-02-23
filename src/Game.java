import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game {

    public static final int PIPE_DELAY = 50; //Changed this from 100

    private Boolean paused;

    private int pauseDelay;
    private int restartDelay;
    private int pipeDelay;

    private Bird bird;
    private ArrayList<Pipe> pipes;
    private Keyboard keyboard;

    public int score;
    int highScore = 0; // adds high score functionality
    public Boolean gameover;
    public Boolean started;

    public Game() {
        keyboard = Keyboard.getInstance();
        restart();
    }

    public void restart() {
        paused = false;
        started = false;
        gameover = false;

        score = 0;
        pauseDelay = 0;
        restartDelay = 0;
        pipeDelay = 0;

        bird = new Bird();
        pipes = new ArrayList<Pipe>();
    }

    public void update() {
        watchForStart();

        if (!started)
            return;

        watchForPause();
        watchForReset();

        if (paused)
            return;

        bird.update();

        if (gameover)
            return;

        movePipes();
        checkForCollisions();
    }

    public ArrayList<Render> getRenders() {
        ArrayList<Render> renders = new ArrayList<Render>();
        renders.add(new Render(0, 0, "lib/chicago.jpg"));
        for (Pipe pipe : pipes)
            renders.add(pipe.getRender());
                
        //GETS RID OF THE FLOOR
        //renders.add(new Render(0, 0, "lib/foreground.png"));
        
        renders.add(bird.getRender());
        return renders;
    }

    private void watchForStart() {
        if (!started && keyboard.isDown(KeyEvent.VK_SPACE)) {
            started = true;
        }
    }

    private void watchForPause() {
        if (pauseDelay > 0)
            pauseDelay--;

        if (keyboard.isDown(KeyEvent.VK_P) && pauseDelay <= 0) {
            paused = !paused;
            pauseDelay = 10;
        }
    }

    private void watchForReset() {
        if (restartDelay > 0)
            restartDelay--;

        if (keyboard.isDown(KeyEvent.VK_R) && restartDelay <= 0) {
            restart();
            restartDelay = 10;
            return;
        }
    }
    private void movePipes() {
        pipeDelay--;
        if (pipeDelay < 0) {
            pipeDelay = PIPE_DELAY;
            Pipe northPipe = null;
            Pipe southPipe = null;

            // Look for pipes off the screen
            for (Pipe pipe : pipes) {
                
                if (pipe.x - pipe.width < -4000) { //change from 0
                    if (northPipe == null) {
                        northPipe = pipe;
                    } else if (southPipe == null) {
                        southPipe = pipe;
                        break;
                    }
                }
            }

            if (northPipe == null) {
                Pipe pipe = new Pipe("north");
                pipes.add(pipe);
                northPipe = pipe;
            } else {
                northPipe.reset();
            }

            if (southPipe == null) {
                Pipe pipe = new Pipe("south");
 
                pipes.add(pipe);
                southPipe = pipe;
            } else {
                southPipe.reset();
            }

            northPipe.y = southPipe.y + southPipe.height + 200; //changes the gap between blocks
        }
        
        for (int i = 0; i<pipes.size(); i++) { 

//        if(i%10 == 0 || i%10==1)   //moves only every fifth starting with the first
//        {
            if(i%2 == 0)
            {
                pipes.get(i).y+=pipes.get(i).getDirectionNorth();
                pipes.get(i+1).y+=pipes.get(i).getDirectionSouth();
            }
//        }
            pipes.get(i).update();
        }
    }

    private void checkForCollisions() {

        for (Pipe pipe : pipes) {
            if (pipe.collides(bird.x, bird.y, bird.width, bird.height)) {
                gameover = true;
                bird.dead = true;

            } else if (pipe.x == bird.x && pipe.orientation.equalsIgnoreCase("south")) {
                score++;
            }
        }
        // Ground + Bird collision
        //THIS IS IF THERE WAS A FLOOR
//        if (bird.y + bird.height > App.HEIGHT - 80) {
//            gameover = true;
//            bird.y = App.HEIGHT - 80 - bird.height;
//        }
        if (bird.y + bird.height > App.HEIGHT) {
            gameover = true;
            bird.y = App.HEIGHT - bird.height;
        }
    }
}
