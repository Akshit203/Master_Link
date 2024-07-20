import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConnectFourGame {
    private char[][] board;
    private String currentPlayer;
    private int numRows;
    private int numCols;
    private int lastMoveRow;
    private int lastMoveCol;
    private Map<String, String> userCredentials;
    private String player1Name;
    private String player2Name;

    public ConnectFourGame(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        board = new char[numRows][numCols];
        currentPlayer = "";

        // Initialize the board with empty cells
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                board[row][col] = '-';
            }
        }

        // Initialize user credentials
        userCredentials = new HashMap<>();
        lastMoveRow = -1;
        lastMoveCol = -1;
    }

    public void displayBoard() {
        System.out.println(" 1 2 3 4 5 6 7");
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean dropToken(int column) {
        if (column < 1 || column > numCols || board[0][column - 1] != '-') {
            System.out.println("Invalid move. Please try again.");
            return false;
        }

        for (int row = numRows - 1; row >= 0; row--) {
            if (board[row][column - 1] == '-') {
                board[row][column - 1] = currentPlayer.charAt(0);
                lastMoveRow = row;
                lastMoveCol = column - 1;
                return true;
            }
        }
        return false;
    }

    public boolean checkWin() {
        // Check horizontally
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col <= numCols - 4; col++) {
                if (board[row][col] != '-' && board[row][col] == board[row][col + 1] &&
                    board[row][col] == board[row][col + 2] && board[row][col] == board[row][col + 3]) {
                    return true;
                }
            }
        }

        // Check vertically
        for (int row = 0; row <= numRows - 4; row++) {
            for (int col = 0; col < numCols; col++) {
                if (board[row][col] != '-' && board[row][col] == board[row + 1][col] &&
                    board[row][col] == board[row + 2][col] && board[row][col] == board[row + 3][col]) {
                    return true;
                }
            }
        }

        // Check diagonally (top-left to bottom-right)
        for (int row = 0; row <= numRows - 4; row++) {
            for (int col = 0; col <= numCols - 4; col++) {
                if (board[row][col] != '-' && board[row][col] == board[row + 1][col + 1] &&
                    board[row][col] == board[row + 2][col + 2] && board[row][col] == board[row + 3][col + 3]) {
                    return true;
                }
            }
        }

        // Check diagonally (top-right to bottom-left)
        for (int row = 0; row <= numRows - 4; row++) {
            for (int col = 3; col < numCols; col++) {
                if (board[row][col] != '-' && board[row][col] == board[row + 1][col - 1] &&
                    board[row][col] == board[row + 2][col - 2] && board[row][col] == board[row + 3][col - 3]) {
                    return true;
                }
            }
        }

        return false;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer.equals(player1Name)) ? player2Name : player1Name;
    }

    public void undoLastMove() {
        if (lastMoveRow != -1 && lastMoveCol != -1) {
            board[lastMoveRow][lastMoveCol] = '-';
            switchPlayer();
            System.out.println("Last move reversed. It's now " + currentPlayer + "'s turn.");
        } else {
            System.out.println("No moves to reverse.");
        }
    }

    public void registerUser(String username, String password) {
        userCredentials.put(username, password);
        System.out.println("User registered successfully.");
    }

    public boolean loginUser(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    public void playGame(Scanner scanner) {
        while (true) {
            displayBoard();
            if (currentPlayer == null) {
                System.out.println("Please log in with valid credentials for both players.");
                break;
            }
            System.out.println(currentPlayer + "'s turn.");
            System.out.print("Enter column (1-" + numCols + ") or 'r' to reverse the move: ");
            String input = scanner.next();
            if (input.equals("r")) {
                undoLastMove();
                continue;
            }
            try {
                int column = Integer.parseInt(input);
                if (column < 1 || column > numCols) {
                    System.out.println("Invalid column. Please enter a number between 1 and " + numCols + ".");
                    continue;
                }
                if (dropToken(column)) {
                    if (checkWin()) {
                        displayBoard();
                        System.out.println(currentPlayer + " wins!");
                        break;
                    }
                    switchPlayer();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of rows: ");
        int rows = scanner.nextInt();
        System.out.print("Enter number of columns: ");
        int cols = scanner.nextInt();

        ConnectFourGame game = new ConnectFourGame(rows, cols);
        boolean loggedInX = false;
        boolean loggedInO = false;

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Start Game");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter username for Player 1: ");
                    String usernameX = scanner.next();
                    System.out.print("Enter password: ");
                    String passwordX = scanner.next();
                    game.registerUser(usernameX, passwordX);
                    System.out.print("Enter username for Player 2: ");
                    String usernameO = scanner.next();
                    System.out.print("Enter password: ");
                    String passwordO = scanner.next();
                    game.registerUser(usernameO, passwordO);
                    break;
                case 2:
                    System.out.print("Enter username for Player 1: ");
                    String username1 = scanner.next();
                    System.out.print("Enter password: ");
                    String password1 = scanner.next();
                    if (game.loginUser(username1, password1)) {
                        game.player1Name = username1;
                        loggedInX = true;
                    } else {
                        System.out.println("Invalid credentials.");
                    }
                    System.out.print("Enter username for Player 2: ");
                    String username2 = scanner.next();
                    System.out.print("Enter password: ");
                    String password2 = scanner.next();
                    if (username2.equals(username1)) {
                        loggedInX = game.loginUser(username2, password2);
                        if (!loggedInX) {
                            System.out.println("Invalid credentials.");
                        }
                    } else {
                        game.player2Name = username2;
                        loggedInO = game.loginUser(username2, password2);
                        if (!loggedInO) {
                            System.out.println("Invalid credentials.");
                        }
                    }
                    break;
                case 3:
