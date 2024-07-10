import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception{
        int boardWidth = 760;
        int boardHeight = 760;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Close button in title bar

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        frame.pack();// to exclude title bar from our 600X600 frame, otherwise titlebar in included in 600*600
        snakeGame.requestFocus();//snake  game will listen for key presses
    }
}
