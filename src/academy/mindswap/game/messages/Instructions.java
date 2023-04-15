package academy.mindswap.game.messages;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Instructions {

    /**
     * returns the instructions for the player after reading them from a text file aggregated with legend
     * for the colors and legend to show result feedback.
     * @return
     */
    public static String readInstruction() {
        try {

            File file = new File("resources/GameRules.txt");
            Scanner scanner = new Scanner(file);

            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
                stringBuilder.append("\n");
            }
            scanner.close();
            return stringBuilder + Messages.RESULT_RULES + Messages.LEGEND;
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e.getMessage());
        }
        return null;
    }
}