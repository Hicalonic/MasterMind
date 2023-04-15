package academy.mindswap.game;

import academy.mindswap.game.messages.Items;

import java.util.ArrayList;
import java.util.List;

public class Board {

    List<String> board;

    ArrayList<String> guessColor;

    ArrayList<String> resultColor;


    public Board() {
        this.board = new ArrayList<>();
    }




    /**
     * Returns the board after transformation of symbols and updating the board.
     */

    public List<String> updatedBoard(List<String> playerGuess, List<String> turnResult) {
        transformGuessResult(playerGuess, turnResult);
        updateBoard();
        return this.board;
    }

    /**
     *Creates a new string with the colors and adds that string to the board List.
     */
    public void updateBoard() {
        String newTry = Items.WB.color + Items.HTBAR.color.repeat(48) + Items.R.color + "\n" +
                Items.WB.color + Items.VBAr.color + guessColor.get(0) + Items.WB.color +
                Items.VBAr.color + guessColor.get(1) + Items.WB.color +
                Items.VBAr.color + guessColor.get(2) + Items.WB.color +
                Items.VBAr.color + guessColor.get(3) + Items.WB.color + Items.VBAr.color + Items.WB.color + " [==] " +
                Items.WB.color + resultColor.get(0) + Items.WB.color +
                resultColor.get(1) + Items.WB.color +
                resultColor.get(2) + Items.WB.color +
                resultColor.get(3) + Items.R.color + "\n" + Items.WB.color
                + Items.HBBAR.color.repeat(48) + Items.R.color;
        board.add(newTry);
    }

    /**
     * Changes player guess  and the turnResult into colors.
     * @param playerGuess   List of chars for the player try for in this turn
     * @param turnResult   List of chars of symbols "+" "-" for this player turn
     */
    public void transformGuessResult(List<String> playerGuess, List<String> turnResult) {
        guessColor = new ArrayList<>();
        resultColor = new ArrayList<>();
        for (String letter : playerGuess) {
            switch (letter) {
                case "G" -> guessColor.add(Items.GREEN.color);
                case "B" -> guessColor.add(Items.BLUE.color);
                case "Y" -> guessColor.add(Items.YELLOW.color);
                case "O" -> guessColor.add(Items.ORANGE.color);
                case "P" -> guessColor.add(Items.PURPLE.color);
            }
        }
        for (String symbol : turnResult) {
            if (symbol.equals("+")) resultColor.add(Items.RED.color);
            if (symbol.equals("-")) resultColor.add(Items.BLACK.color);
            if (symbol.equals(" ")) resultColor.add(Items.WHITE.color);
        }
    }
}