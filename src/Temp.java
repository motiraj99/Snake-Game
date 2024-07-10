import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.*;

public class Temp {
    public static void func() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        File file = new File("C:\\Users\\motir\\Downloads\\Snake_Game\\src\\music\\bonus.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException{

        Scanner scanner = new Scanner(System.in);
        func();

//        File file = new File("C:\\Users\\motir\\Downloads\\Snake_Game\\src\\music\\bonus.wav");
//        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
//        Clip clip = AudioSystem.getClip();
//        clip.open(audioStream);
//        clip.start();
       //  clip.loop(Clip.LOOP_CONTINUOUSLY);;

        String response = "";


        while(!response.equals("Q")) {
            System.out.println("P = play, S = Stop, R = Reset, Q = Quit");
            //System.out.print("Enter your choice: ");

           // response = scanner.next();
            response = response.toUpperCase();

            switch(response) {
                case ("P"): ;
                    break;
                case ("S"):;
                    break;
                case ("R"): ;
                    break;
                case ("Q"):;
                    break;
                default: System.out.println("Not a valid response");
            }

        }
        System.out.println("Byeeee!");
    }
}