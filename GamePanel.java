import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = 1000;
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 200;
    static final int PADDLE_HEIGHT = 25;
    static final int ROW = 5;
    static final int COLUMNS = 10;
    static final int MAX_BRICKS = ROW * COLUMNS;
    static final int BRICK_WIDTH = 78;
    static final int BRICK_HEIGHT = 25;
    static final double GAP = (GAME_WIDTH - (double)(BRICK_WIDTH*COLUMNS))/(COLUMNS -1);

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Ball ball;
    Brick[] brick = new Brick[MAX_BRICKS];

    public GamePanel() {
        newPaddle();
        newBall();
        newBrick();
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }
    public void newBrick() {
        int x = 0;
        int y = 0;

        for (int i = 0; i < MAX_BRICKS; i++) {
            brick[i] = new Brick((int)(x * (GAP + BRICK_WIDTH)) , y * (int)(BRICK_HEIGHT +GAP), BRICK_WIDTH, BRICK_HEIGHT);
            x++;
            if (x >= COLUMNS){
                x=0;
                y++;
            }
        }
    }
    public void newBall(){
        random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2), (GAME_HEIGHT/2-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
    }
    public void newPaddle() {
        paddle1 = new Paddle((GAME_WIDTH/2-PADDLE_WIDTH/2),GAME_HEIGHT-PADDLE_HEIGHT,PADDLE_WIDTH,PADDLE_HEIGHT);
    }
    public void paint (Graphics g) {
        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }
    public void draw (Graphics g) {
        paddle1.draw(g);
        ball.draw(g);
        for (int i = 0; i < MAX_BRICKS; i++) {
            if (brick[i] != null) {
                brick[i].draw(g);
            }
        }
    }
    public void move() {
        paddle1.move();
        ball.move();
    }
    public void checkCollision() {

        //bounce ball off top & window edges
        if(ball.y <=0){
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.x <=0){
            ball.setXDirection(-ball.xVelocity);
        }
        if(ball.x >= GAME_WIDTH-BALL_DIAMETER){
            ball.setXDirection(-ball.xVelocity);
        }
//        if(ball.y >= GAME_HEIGHT-BALL_DIAMETER){
//            ball.setYDirection(-ball.yVelocity);
//        }
        //bounces ball off paddles
        if (ball.intersects(paddle1)){
            ball.yVelocity = Math.abs(ball.yVelocity);
//            ball.yVelocity++; //optional for more difficulty
//            if (ball.xVelocity>0)
//                ball.xVelocity++; //optional for more difficulty
//            else ball.xVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(-ball.yVelocity);
        }
        for (int i = 0; i < MAX_BRICKS; i++) {
            if (brick[i] != null){
                if (ball.intersects(brick[i])) {
                    ball.yVelocity = -ball.yVelocity;
//                    ball.yVelocity++; //optional for more difficulty
//                    if (ball.xVelocity > 0)
//                        ball.xVelocity++; //optional for more difficulty
//                    else ball.xVelocity--;
                    ball.setXDirection(ball.xVelocity);
                    ball.setYDirection(ball.yVelocity);
                    brick[i] = null;
                }
            }
        }

        //stops paddles at window edges
        if (paddle1.x <= 0 ){
            paddle1.x=0;}
        if (paddle1.x >= GAME_WIDTH -PADDLE_WIDTH) {
            paddle1.x = GAME_WIDTH-PADDLE_WIDTH;}

        //give a player 1 point and creates new paddles & ball
        if(ball.y >= GAME_HEIGHT) {

            newPaddle();
            newBall();
            System.out.println("OUT!");
        }
//        if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
//            score.player1++;
//            newPaddles();
//            newBall();
//            System.out.println(score.player1);
//        }
    }
    @Override
    public void run(){
        //game loop
        long lastTime = System.nanoTime();
        double amountOfTick = 60.0;
        double ns = 1000000000 / amountOfTick;
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime)/ns;
            lastTime = now;
            if (delta >= 1){
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }
    public class AL extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            paddle1.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            paddle1.keyReleased(e);
        }
    }
}
