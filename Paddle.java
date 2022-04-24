import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle {
    int xVelocity;
    int speed = 10;

    public Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT) {
        super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
    }
    public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_A) {
                    setXDirection(-speed);
                    move();
                }
                if (e.getKeyCode()==KeyEvent.VK_D) {
                    setXDirection(speed);
                    move();
                }
    }
    public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_A) {
                    setXDirection(0);
                    move();
                }
                if (e.getKeyCode()==KeyEvent.VK_D) {
                    setXDirection(0);
                    move();
                }
    }
    public void setXDirection(int xDirection) {
        xVelocity = xDirection;
    }
    public void move() {
        x = x + xVelocity;
    }
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
//      g.setColor(Color.red);
        g.fillRect(x,y,width,height);
    }
}

