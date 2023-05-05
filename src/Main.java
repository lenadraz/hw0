import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;

    public static void battleshipGame() {
        System.out.println("Enter the board size");
        Scanner board_size1 = new Scanner(System.in);
        String board_size = board_size1.nextLine();
        String[] parts = split_by(board_size, "X");  // escape the period with a backslash
        int row = Integer.parseInt(parts[0]);
        int column = Integer.parseInt(parts[1]);
        char[][] computer_board = new char[row][column];
        System.out.println("Enter battleships sizes");
        Scanner battleships_sizes1 = new Scanner(System.in);
        String battleships_sizes = battleships_sizes1.nextLine();
        String[] parts1 = split_by(battleships_sizes, " ");
        int length = parts1.length;
        System.out.println("Your current game board:");
        char[][] user_board = new char[row][column];
        char[][] guessing_user_board = new char[row][column];
        first_board(row, column, user_board);
        int number_of_user_battleships = 0;
        int number_of_computer_battleships = 0;
        int max_size_battleship = 0;
        for (int i = 0; i < length; i++) {
            String[] split_by_X = split_by(parts1[i], "X"); // Split by "x" delimiter
            int number_of_user_battleships_eachRound = Integer.parseInt(split_by_X[0]);
            number_of_user_battleships = number_of_user_battleships + number_of_user_battleships_eachRound;
            number_of_computer_battleships = number_of_user_battleships;
            int size_of_battleship = Integer.parseInt(split_by_X[1]);
            for (int j = 0; j < number_of_user_battleships_eachRound; j++) {
                System.out.println("Enter location and orientation of battleships of size " + size_of_battleship);
                Scanner location = new Scanner(System.in);
                while (location.hasNext()) {
                    String place = location.nextLine();
                    String[] parts2 = split_by(place, ",");
                    int location_at_x = Integer.parseInt(parts2[0]);
                    int location_at_y = Integer.parseInt(parts2[1]);
                    int orientation = Integer.parseInt(parts2[2]);
                    if (!((orientation == 0) || (orientation == 1))) {
                        System.out.println("Illegal orientation ,try again!");
                    } else if ((location_at_x > row) || (location_at_y > column)) {
                        System.out.println("Illegal tile,try again!");
                    } else if ((orientation == 0 && column - location_at_y < size_of_battleship) || orientation == 1 && row - location_at_x < size_of_battleship) {
                        System.out.println("Battleship exceeds the boundaries of the board ,try again! ");
                    } else if (user_board[location_at_x][location_at_y] == '#') {
                        System.out.println("Battleship overlaps another battleship, try again!");
                    } else if (if_adjacent_battleship(size_of_battleship, user_board, location_at_x, location_at_y, row, column)) {
                        System.out.println("Adjacent battleship detected, try again!");
                    } else {
                        System.out.println("Your current game board:");
                        user_board(row, column, orientation, location_at_x, location_at_y, size_of_battleship, user_board);
                        System.out.println("Your current guessing board:");
                        guessing_board(guessing_user_board, row, column);
                        break;
                    }
                }
                String location_of_battleships_of_computer = location_of_battleships_of_computer(computer_board, row, column, size_of_battleship);
                update_computer_board(computer_board, location_of_battleships_of_computer, row, column, size_of_battleship);
                if (size_of_battleship > max_size_battleship) {
                    max_size_battleship = size_of_battleship;
                }
            }
        }
        while (number_of_computer_battleships > 0 && number_of_user_battleships > 0) {
            System.out.println("Enter a tile to attack");
            number_of_computer_battleships = user_attack(number_of_computer_battleships, computer_board, row, column, guessing_user_board, user_board, max_size_battleship);
            if (number_of_computer_battleships == 0) {
                System.out.println("You won the game!");
                break;
            }
            String location_of_computer_attack = location_of_computer_attack(row, column);
            number_of_user_battleships = computer_attack(number_of_user_battleships, location_of_computer_attack, user_board, max_size_battleship, row, column);
            if (number_of_user_battleships == 0) {
                System.out.println("You lost):");
                break;
            }
            System.out.println("Your current game board:");
            updated_user_board(user_board, row, column);
            System.out.println("Your current guessing board:");
            updated_guessing_board(guessing_user_board, row, column);
        }
    }
    public static void user_board(int row, int column, int orientation, int x, int y, int size, char[][] board) {
        int max_digit_of_row = numbers_of_digits(row);
        for (int u = 0; u < max_digit_of_row + 1; u++) {
            System.out.print(" ");
        }
        for (int i1 = 0; i1 < size; i1++) {
            if (orientation == 1) {
                board[x + i1][y] = '#';
            } else {
                board[x][y + i1] = '#';
            }
        }
        for (int o = 0; o < column; o++) {
            System.out.print(o + " ");
        }
        System.out.println();
        for (int k = 0; k < row; k++) {
            int number_of_digit = numbers_of_digits(k);
            int max_subtract_current = max_digit_of_row - number_of_digit;
            for (int i = 0; i < max_subtract_current; i++) {
                System.out.print(" ");
            }
            System.out.print(k);
            for (int j = 0; j < column; j++) {
                System.out.printf(" " + board[k][j]);
            }
            System.out.println();
        }
    }
    public static void first_board(int row, int column, char[][] board) {
        for (int r = 0; r < row; r++) {
            for (int j = 0; j < column; j++) {
                board[r][j] = '-';
            }
        }
        int max_digit_of_row = numbers_of_digits(row);
        for (int u = 0; u < max_digit_of_row + 1; u++) {
            System.out.print(" ");
        }
        for (int o = 0; o < column; o++) {
            System.out.print(o + " ");
        }
        System.out.println();
        for (int k = 0; k < row; k++) {
            int number_of_digit = numbers_of_digits(k);
            int max_subtract_current = max_digit_of_row - number_of_digit;
            for (int i = 0; i < max_subtract_current; i++) {
                System.out.print(" ");
            }
            System.out.print(k);
            for (int j = 0; j < column; j++) {
                System.out.print(" " + board[k][j]);
            }
            System.out.println();
        }
    }
    public static void updated_guessing_board(char[][] board, int row, int column) {
        for (int i = 0; i < row; i++) {
            for (int x = 0; x < column; x++) {
                if ((board[i][x] != 'X') && (board[i][x] != 'V')) {
                    board[i][x] = '-';
                }
            }
        }
        int max_digit_of_row = numbers_of_digits(row);
        for (int u = 0; u < max_digit_of_row + 1; u++) {
            System.out.print(" ");
        }
        for (int o = 0; o < column; o++) {
            System.out.print(o + " ");
        }
        System.out.println();
        for (int k = 0; k < row; k++) {
            int number_of_digit = numbers_of_digits(k);
            int max_subtract_current = max_digit_of_row - number_of_digit;
            for (int i = 0; i < max_subtract_current; i++) {
                System.out.print(" ");
            }
            System.out.print(k);
            for (int j = 0; j < column; j++) {
                System.out.print(" " + board[k][j]);
            }
            System.out.println();
        }
    }
    public static void guessing_board(char[][] board, int row, int column) {
        for (int r = 0; r < row; r++) {
            for (int j = 0; j < column; j++) {
                board[r][j] = '-';
            }
        }
        int max_digit_of_row = numbers_of_digits(row);

        for (int u = 0; u < max_digit_of_row + 1; u++) {
            System.out.print(" ");
        }
        for (int o = 0; o < column; o++) {
            System.out.print(o + " ");
        }
        System.out.println();
        for (int k = 0; k < row; k++) {
            int number_of_digit = numbers_of_digits(k);
            int max_subtract_current = max_digit_of_row - number_of_digit;
            for (int i = 0; i < max_subtract_current; i++) {
                System.out.print(" ");
            }
            System.out.print(k);
            for (int j = 0; j < column; j++) {
                System.out.print(" " + board[k][j]);
            }
            System.out.println();
        }
    }
    public static String[] split_by(String what_to_split, String punctuation_mark) {
        return what_to_split.split(punctuation_mark);
    }
    public static int numbers_of_digits(int number) {
        String strNumber = Integer.toString(number);
        int numOfDigits = strNumber.length();
        return numOfDigits;
    }
    public static String location_of_battleships_of_computer(char[][] computer_board, int row, int column, int size_of_battleship) {
        boolean valid = false;
        String location = "";
        while (!(valid)) {
            Random random_x = new Random();
            int rad_x = rnd.nextInt(row);
            String char_x = String.valueOf(rad_x);
            Random random_y = new Random();
            int rad_y = rnd.nextInt(column);
            String char_y = String.valueOf(rad_y);
            Random orientation2 = new Random();
            int computer_orientation = rnd.nextInt(2);
            String char_or = String.valueOf(computer_orientation);
            if ((computer_orientation == 0 && column - rad_y < size_of_battleship) || computer_orientation == 1 && row - rad_x < size_of_battleship) {
                valid = false;
            } else if (computer_board[rad_x][rad_y] == '#') {
                valid = false;
            } else if (if_adjacent_battleship(size_of_battleship, computer_board, rad_x, rad_y, row, column)) {
                valid = false;
            } else {
                valid = true;
            }
            location = char_x + "," + char_y + "," + char_or;
        }
        return location;
    }
    public static void update_computer_board(char[][] board, String location, int row, int column, int size) {
        String[] parts2 = split_by(location, ",");
        int location_at_x = Integer.parseInt(parts2[0]);
        int location_at_y = Integer.parseInt(parts2[1]);
        int orientation = Integer.parseInt(parts2[2]);
        for (int r = 0; r < row; r++) {
            for (int j = 0; j < column; j++) {
                if (board[r][j] != '#') {
                    board[r][j] = '-';
                }
            }
        }
        for (int i1 = 0; i1 < size; i1++) {
            if (orientation == 1) {
                board[location_at_x + i1][location_at_y] = '#';
            } else {
                board[location_at_x][location_at_y + i1] = '#';
            }
        }
    }
    public static boolean input_is_legal(char[][] user_guessing_board, String user_tile_to_attack, int row, int column) {
        boolean legal_input = true;
        while (legal_input) {
            if (!(user_tile_to_attack.matches("\\d,\\d"))){
                legal_input = false;
            }
            // Check if input is in the format of x,y
            else if (user_tile_to_attack.matches("\\d,\\d")) {
                String[] coordinates = user_tile_to_attack.split(",");
                int user_x = Integer.parseInt(coordinates[0]);
                int user_y = Integer.parseInt(coordinates[1]);
                // Check if x and y values are within the board
                if ((user_guessing_board[user_x][user_y] == 'V') || (user_guessing_board[user_x][user_y] == 'X')) {
                    System.out.println("Tile already attacked, try again!");
                    legal_input = false;
                }
                if ((user_x >= 0 && user_x < row) && (user_y >= 0) && (user_y < column)) {
                    break;
                } else {
                    System.out.println("Illegal tile, try again!");
                    legal_input = false;
                }
            }
        }
        return legal_input;
    }
    public static String location_of_computer_attack(int row, int column) {
        String location_of_computer_attack = "";
        Random computer_attack_at_x1 = new Random();
        int computer_attack_at_x = rnd.nextInt(row);
        String char_computer_attack_at_x = String.valueOf(computer_attack_at_x);
        Random computer_attack_at_y1 = new Random();
        int computer_attack_at_y = rnd.nextInt(column);
        String char_computer_attack_at_y = String.valueOf(computer_attack_at_y);
        location_of_computer_attack = char_computer_attack_at_x + "," + char_computer_attack_at_y;
        return location_of_computer_attack;
    }
    public static int computer_attack(int number_of_user_battleships, String location, char[][] user_board, int max_battleship_size, int row, int column) {
        String[] parts = split_by(location, ",");
        int computer_attack_at_x = Integer.parseInt(parts[0]);
        int computer_attack_at_y = Integer.parseInt(parts[1]);
        System.out.printf("The computer attacked (%d,%d)", computer_attack_at_x, computer_attack_at_y);
        System.out.println();
        if (user_board[computer_attack_at_x][computer_attack_at_y] == '#') {
            System.out.println("That is a hit!");
            user_board[computer_attack_at_x][computer_attack_at_y] = 'X';
            if (complete_battleship_sunk(user_board, computer_attack_at_x, computer_attack_at_y, max_battleship_size, row, column)) {
                number_of_user_battleships--;
                System.out.printf("Your battleship has been drowned, you have left %d more battleships!", number_of_user_battleships);
                System.out.println();
            }
        } else {
            System.out.println("That is a miss!");
        }
        return number_of_user_battleships;
    }
    public static boolean complete_battleship_sunk(char[][] board, int x, int y, int max_battleship_size, int row, int column) {
        int orientation3 = 0;
        boolean sunk = true;
        int c = 1;
        int g = 1;
        int z = 1;
        int a = 1;
        if ((y + 1 < column) && (y - 1 >= 0)) {
            if ((board[x][y + 1] == '-') && (board[x][y - 1] == '-')) {
                orientation3 = 1;
            }
        }
        if (y + 1 > column) {
            if (board[x][y - 1] == '-') {
                orientation3 = 1;
            }
        }
        if (y - 1 <= 0) {
            if (board[x][y + 1] == '-') {
                orientation3 = 1;
            }
        }
        if (orientation3 == 1) {
            while (x + c < row) {
                if (board[x + c][y] == '#') {
                    System.out.println(board[x + c][y] + "1");
                    sunk = false;
                    break;
                }
                if (board[x + c][y] == '-') {
                    break;
                } else {
                    c++;
                }
            }
            while (x - g >= 0) {
                if (board[x - g][y] == '#') {
                    System.out.println(board[x - g][y] + "2");
                    sunk = false;
                    break;
                }
                if (board[x - g][y] == '-') {
                    break;
                } else {
                    g++;
                }
            }
        }
        if (orientation3 == 0) {
            while (y + z < column) {
                if (board[x][y + z] == '#') {
                    System.out.println(board[x][y + z] + "3");
                    sunk = false;
                    break;
                }
                if (board[x][y + z] == '-') {
                    break;
                } else {
                    z++;
                }
            }
            while (y - a >= 0) {
                if (board[x][y - a] == '#') {
                    System.out.println(board[x][y - a] + "4");
                    sunk = false;
                    break;
                }
                if (board[y - a][x] == '-') {
                    break;
                } else {
                    a++;
                }
            }
        }
        return sunk;
    }
    public static int user_attack(int number_of_computer_battleships, char[][] computer_board, int row,
                                  int column, char[][] user_guessing_tile, char[][] user_board, int max_size_battleship) {
        Scanner tile = new Scanner(System.in);
        while (true) {
            String user_tile_to_attack = tile.nextLine();
            String[] parts3 = split_by(user_tile_to_attack, ",");
            int user_tile_x = Integer.parseInt(parts3[0]);
            int user_tile_y = Integer.parseInt(parts3[1]);
            if (input_is_legal(user_guessing_tile, user_tile_to_attack, row, column)) {
                if (computer_board[user_tile_x][user_tile_y] == '#') {
                    System.out.println("That is a hit!");
                    computer_board[user_tile_x][user_tile_y] = 'X';
                    user_guessing_tile[user_tile_x][user_tile_y] = 'V';
                    if (complete_battleship_sunk(computer_board, user_tile_x, user_tile_y, max_size_battleship, row, column)) {
                        number_of_computer_battleships--;
                        System.out.printf("The computer's battleship has been drowned, %d more battleships to go!", number_of_computer_battleships);
                        System.out.println();
                    }
                } else {
                    System.out.println("That is a miss!");
                    user_guessing_tile[user_tile_x][user_tile_y] = 'X';

                }
                break;// Exit the loop since input is valid// Ask for input again
            }
        }
        return number_of_computer_battleships;
    }
    public static void updated_user_board(char[][] user_board, int row, int column) {
        int max_digit_of_row = numbers_of_digits(row);
        for (int u = 0; u < max_digit_of_row + 1; u++) {
            System.out.print(" ");
        }
        for (int o = 0; o < column; o++) {
            System.out.print(o + " ");
        }
        System.out.println();
        for (int k = 0; k < row; k++) {
            int number_of_digit = numbers_of_digits(k);
            int max_subtract_current = max_digit_of_row - number_of_digit;
            for (int i = 0; i < max_subtract_current; i++) {
                System.out.print(" ");
            }
            System.out.print(k);
            for (int j = 0; j < column; j++) {
                System.out.printf(" " + user_board[k][j]);
            }
            System.out.println();
        }
    }
    public static boolean if_adjacent_battleship(int size_of_battleship, char[][] user_board, int location_at_x,
                                                 int location_at_y, int row, int column) {
        boolean is_adjacent = false;
        for (int g = 0; g < size_of_battleship; g++) {
            if ((location_at_x + g + 1) < row) {
                if (user_board[location_at_x + g + 1][location_at_y] == '#') {
                    is_adjacent = true;
                    break;
                }
            }
            if ((location_at_x + g - 1 < row) && (location_at_x + g - 1 >= 0)) {
                if (user_board[location_at_x + g - 1][location_at_y] == '#') {
                    is_adjacent = true;
                    break;
                }
            }
            if (location_at_y + g + 1 < column) {
                if (user_board[location_at_x][location_at_y + g + 1] == '#') {
                    is_adjacent = true;
                    break;
                }

                if ((location_at_y + g - 1 < column) && (location_at_y + g - 1 >= 0) && (location_at_y + g - 1 >= 0) && (location_at_y + g - 1 >= 0)) {
                    if (user_board[location_at_x][location_at_y + g - 1] == '#') {
                        is_adjacent = true;
                        break;
                    }
                }
                if (location_at_x + g + 1 < row) {
                    if (user_board[location_at_x + g + 1][location_at_y] == '#') {
                        is_adjacent = true;
                        break;
                    }
                }
                if ((location_at_x + g + 1 < row) && (location_at_y + 1 < column)) {
                    if (user_board[location_at_x + g + 1][location_at_y + 1] == '#') {
                        is_adjacent = true;
                        break;
                    }
                }
                if ((location_at_x + g + 1 < row) && (location_at_y - 1 < column) && (location_at_y - 1 >= 0)) {
                    if (user_board[location_at_x + g + 1][location_at_y - 1] == '#') {
                        is_adjacent = true;
                        break;
                    }
                }
                if ((location_at_x + 1 < row) && (location_at_y + g + 1 < column)) {
                    if (user_board[location_at_x + 1][location_at_y + g + 1] == '#') {
                        is_adjacent = true;
                        break;
                    }
                }
                if ((location_at_x - 1 < row) && (location_at_x - 1 >= 0) && (location_at_y + g + 1 < column)) {
                    if (user_board[location_at_x - 1][location_at_y + g + 1] == '#') {
                        is_adjacent = true;
                        break;
                    }
                }
            }

        }
        return is_adjacent;
    }
    public static void main(String[] args) throws IOException {
        String path = args[0];
        scanner = new Scanner(new File(path));
        int numberOfGames = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Total of " + numberOfGames + " games.");
        for (int i = 1; i <= numberOfGames; i++) {
            scanner.nextLine();
            int seed = scanner.nextInt();
            rnd = new Random(seed);
            scanner.nextLine();
            System.out.println("Game number " + i + " starts.");
            battleshipGame();
            System.out.println("Game number " + i + " is over.");
            System.out.println("------------------------------------------------------------");
        }
        System.out.println("All games are over.");
    }
}