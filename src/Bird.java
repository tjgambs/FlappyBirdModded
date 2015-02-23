import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class Bird {

    public int x;
    public int y;
    public int width;
    public int height;

    public boolean dead;

    public double yvel;
    public double xvel;
    public double gravity;

    private int jumpDelay;
    private double rotation;

    private Image image;
    private Keyboard keyboard;

    public Bird() {
        x = 100;
        y = 150;
        yvel = 0;
        xvel = 3;
        width = 41; //changed from 45
        height = 61; //changed from 32
        gravity = .3; //changed from .5
        jumpDelay = 0;
        rotation = 0.0;
        dead = false;

        keyboard = Keyboard.getInstance();
    }

    public void update() {
        yvel += gravity;

        if (jumpDelay > 0)
            jumpDelay--;

        if (!dead && keyboard.isDown(KeyEvent.VK_SPACE) && jumpDelay <= 0) {
            yvel = -3; //changed from -10
            //GOT RID OF THE JUMPDELAY, SO IT IS SMOOTHER
            //jumpDelay = 10;
        }

        y += (int)yvel;
    }

    public Render getRender() {
        Render r = new Render();
        r.x = x;
        r.y = y;

        if (image == null) {
            image = Util.loadImage("lib/hurley.png");     
        }
        r.image = image;
//THIS STOPS THE IMAGE FROM ROTATING
//        rotation = (90 * (yvel + 20) / 20) - 90;
//        rotation = rotation * Math.PI / 180;
//
//        if (rotation > Math.PI / 2)
//            rotation = Math.PI / 2;
//
//        r.transform = new AffineTransform();
//        r.transform.translate(x + width / 2, y + height / 2);
//        r.transform.rotate(rotation);
//        r.transform.translate(-width / 2, -height / 2);

        return r;
    }
}
