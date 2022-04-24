import java.awt.*;

public class Brick extends Rectangle {

    public Brick(int x, int y, int BRICK_WIDTH, int BRICK_HEIGHT) {
        super(x,y,BRICK_WIDTH,BRICK_HEIGHT);

    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(x,y,width,height);
    }
}
