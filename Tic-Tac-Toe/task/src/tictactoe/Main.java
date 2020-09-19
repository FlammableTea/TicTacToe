package tictactoe;

import java.util.Scanner;

@SuppressWarnings("SpellCheckingInspection")
class Board{
    char[] cells;
    boolean finished;
    String game_state;

    Board(){
        cells = "         ".toCharArray();
        finished = false;
        game_state = "Game not finished";
    }

    boolean gameIsFinished(){
        return finished;
    }

    boolean importIsValid(String input){
        char[] validChars = {'O', 'X', '_'};
        for(int i = 0; i < input.length(); i++){
            char c = input.charAt(i);
            boolean charIsInvalid = c != validChars[0] && c != validChars[1] && c != validChars[2];
            if(charIsInvalid) {
                return false;
            }
        }
        //loop through string, if any of the characters are invalid
        return true;
    }

    //imports a board state
    void importBoard(String input){
        if(importIsValid(input)){
            for(int i = 0; i<input.length(); i++){
                cells[i] = input.charAt(i);
            }
            updateGameState();
        }
        else{
            System.out.println("Invalid input: try again");
        }
    }

    void printBoard(){
        char[] ch = cells;
        System.out.println("\n---------");
        System.out.println("| "+ ch[0] + " " + ch[1] + " " + ch[2] +" |");
        System.out.println("| "+ ch[3] + " " + ch[4] + " " + ch[5] +" |");
        System.out.println("| "+ ch[6] + " " + ch[7] + " " + ch[8] +" |");
        System.out.println("---------");
    }

    void updateGameState(){
        boolean xWin = false;
        boolean oWin = false;
        boolean spaces_remaining = false;

        int numX = 0;
        int numO = 0;

        char[] game = cells;
        for(char c : game){
            if (c == 'X') numX++;
            else if (c == 'O') numO++;
            else spaces_remaining = true;
        }
        if (Math.abs(numX - numO) >= 2){
            game_state = "Impossible";
            finished = true;
        }
        //Board state:
        // 0 1 2
        // 3 4 5
        // 6 7 8

        else {

            //check column and row victories
            for (int i = 0; i <= 2; i++) {
                //check a column first
                boolean oColumnWin = game[i] == 'O' && game[i + 3] == 'O' && game[i + 6] == 'O';
                boolean xColumnWin = game[i] == 'X' && game[i + 3] == 'X' && game[i + 6] == 'X';
                //then check a row
                int j = i*3;
                boolean oRowWin = game[j] == 'O' && game[j+1] == 'O' && game[j+2] == 'O';
                oWin = oWin || oColumnWin || oRowWin;
                boolean xRowWin = game[j] == 'X' && game[j+1] == 'X' && game[j+2] == 'X';
                xWin = xWin || xColumnWin || xRowWin;
            }

            //check diagonal victories
            boolean oDiagWin = game[4] == 'O' && ((game[0] == 'O' && game[8] == 'O') || (game[2] == 'O' && game[6] == 'O'));
            boolean xDiagWin = game[4] == 'X' && ((game[0] == 'X' && game[8] == 'X') || (game[2] == 'X' && game[6] == 'X'));
            oWin = oWin || oDiagWin;
            xWin = xWin || xDiagWin;

            if (xWin || oWin) {
                if (xWin && oWin){
                    game_state = "Impossible";
                    finished = false;
                }
                else if(xWin){
                    finished = true;
                    game_state = "X wins";
                }
                else {
                    finished = true;
                    game_state = "O wins";
                }
            }

            else if (spaces_remaining){
                game_state = "Game not finished";
                finished = false;
            }
            else {
                game_state = "Draw";
                finished = true;
            }
        }

    }

    String getGameState(){
        return game_state;
    }

    boolean moveIsValid(String str){
        String move = str.trim();
        for (char c : move.toCharArray()){
            if(!Character.isDigit(c) && !Character.isWhitespace(c)){
                System.out.println("You should enter numbers!");
                return false;
            }
        }
        String[] moves = move.split(" ");
        if(moves.length != 2){
            System.out.println("Enter exactly two coordinates, with one space in between.");
            return false;
        }

        for (String s : moves){
            int num = Integer.parseInt(s);
            if (num < 1 || num > 3){
                System.out.println("Coordinates should be from 1 to 3!");
                return false;
            }
        }

        int col = Integer.parseInt(moves[0]);
        int row = Integer.parseInt(moves[1]);
        //Equation: Take the column, add 5 (1 maps to 6, 2 maps to 7, 3 maps to 8)
        //Then, take the row, and subtract 3*(1-row)
        int boardArrLoc = (col+5) - 3*(row-1);
        if(cells[boardArrLoc] != ' ') {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }
        return true;
    }

    void makeMove(String str, char c){
            String[] moves = str.split(" ");
            int col = Integer.parseInt(moves[0]);
            int row = Integer.parseInt(moves[1]);
            //Equation: Take the column, add 5 (1 maps to 6, 2 maps to 7, 3 maps to 8)
            //Then, take the row, and subtract 3*(1-row)
            int boardArrLoc = (col + 5) - 3 * (row-1);
            cells[boardArrLoc] = c;
    }
}


public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        Board myBoard = new Board();
        myBoard.printBoard();
        int count = 1;

        while (!myBoard.gameIsFinished()){
            //System.out.println("Count: "+ count);
            //System.out.println("Count modulo 2: " + count%2);
            boolean inputIsValid = false;
            char c = ' ';
            int turn = count%2;
            switch (turn){
                case 0 :
                    //System.out.println("Time for O!");
                    c ='O';
                    break;
                case 1 :
                    //System.out.println("Time for X!");
                    c = 'X';
                    break;
            }
            while(!inputIsValid){
                System.out.print("Enter the coordinates: ");
                String userMove = in.nextLine();
                inputIsValid = myBoard.moveIsValid(userMove);
                if(inputIsValid){
                    myBoard.makeMove(userMove, c);
                    myBoard.printBoard();
                    myBoard.updateGameState();
                    count++;
                }
            }
        }
        System.out.println(myBoard.getGameState());
        in.close();
    }
}
