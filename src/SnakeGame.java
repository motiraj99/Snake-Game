import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{

    public class SoundEffect{
        Clip clip;
        public void setFile(File file) {
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioStream);

            } catch (Exception e){
                System.out.println(e);
            }
        }

        public void play(){
            clip.stop();
            clip.setFramePosition(0);

            clip.start();
        }

        public void play_in_loop(){
            clip.stop();
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

        public void stop(){
            clip.stop();
        }
    }

    private class Tile{
        int x,y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth, boardHeight;
    int tileSize = 10;
    int number_of_tiles_width, number_of_tiles_height;

    // Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;
    Random random = new Random();

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;

    boolean gameOver = false;

    File file = new File("C:\\Users\\motir\\Downloads\\Snake_Game\\src\\music\\bonus.wav");
    SoundEffect se1 = new SoundEffect();

    File file2 = new File("C:\\Users\\motir\\Downloads\\Snake_Game\\src\\music\\game_over.wav");
    SoundEffect se2 = new SoundEffect();

    File file3 = new File("C:\\Users\\motir\\Downloads\\Snake_Game\\src\\music\\background.wav");
    SoundEffect se3 = new SoundEffect();

    SnakeGame(int boardWidth, int boardHeight) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        number_of_tiles_height = boardHeight/tileSize;
        number_of_tiles_width = boardWidth/tileSize;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        se1.setFile(file);
        se2.setFile(file2);
        se3.setFile(file3);

        start_new_game();
    }

    public void start_new_game(){
        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        placeFood();

        velocityX = 1;
        velocityY = 0;

        gameLoop = new Timer(35,this);
        gameOver = false;

        se3.play_in_loop();
        gameLoop.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //Grid - for visualization purpose
//        for(int i=0;i<boardWidth/tileSize;i++) {
//            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
//            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
//        }

        //Food
        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize*2, tileSize*2);

        //Snake
        g.setColor(Color.GREEN);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        //Snake Body
        for(int i=0; i<snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        //Score
        g.setFont(new Font("Ariel", Font.PLAIN, 16));
        if(gameOver){
            se2.play();
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize, tileSize*2);
            g.setColor(Color.green);
            g.drawString("SPACE : start New Game      ESC : to EXIT", tileSize, tileSize*4);

        } else{
            g.drawString("Score: "+ String.valueOf(snakeBody.size()), tileSize*2, tileSize*2);
        }
    }

    public void placeFood(){
        food.x = random.nextInt(number_of_tiles_width-1);// 600/25=24
        food.y = random.nextInt(number_of_tiles_height-1);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x &&  tile1.y == tile2.y;
    }

    public boolean collision_food(Tile snake, Tile food){
        return ((snake.x == food.x &&  snake.y == food.y) || (snake.x == food.x+1 &&  snake.y == food.y)
        || (snake.x == food.x &&  snake.y == food.y+1) || (snake.x == food.x+1 &&  snake.y == food.y+1));
    }

    public void move()  {
        //eat food
        if(collision_food(snakeHead, food)){
            se1.play();
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //Snake body
        for (int i = snakeBody.size()-1; i>=0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake Head
        snakeHead.x = (snakeHead.x + velocityX + number_of_tiles_width) % number_of_tiles_width;
        snakeHead.y = (snakeHead.y + velocityY + number_of_tiles_height) % number_of_tiles_height;

        //game over conditions
        for (int i=0; i<snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //collide with the snake head
            if(collision(snakeHead, snakePart)){
                gameOver = true;
                se3.stop();
                //se2.play();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            start_new_game();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !gameOver) {
            se3.stop();
            gameOver = true;
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && gameOver) {
            System.exit(0);
        }
    }

    //@Override; but do not need these functions
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
}
