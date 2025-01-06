//Name: Shazil Khan (UCID: 30241942), Naaila Aziz (UCID: 30230879) - Tutorial 05
// Purpose: Implement a simple snake game in Java.
import java.util.Scanner;
import java.util.Random;
public class SnakeGame {

    public static char[][] grid;

    public static int[][] snake;

    // The current length of the snake
    public static int snakeLength;

    // The game over flag
    public static boolean isGameOver = false;

    public static final int GRID_SIZE_X = 20;// the number of columns
    public static final int GRID_SIZE_Y = 10; // the number of rows
    public static final char SNAKE_CHAR = 'S';
    public static final char FOOD_CHAR = '@';
    public static final char EMPTY_CHAR = '.';
    public static final char WALL_CHAR = '#';
    public static final char OBSTACLE_CHAR = '%';
    public static final char TELEPORT_CHAR = 'O';
    public static final int MAX_SNAKE_LENGTH = (GRID_SIZE_Y) * (GRID_SIZE_X)/2;

    /**
     * Initializes the grid, creates the wall, snake and food
     */
    public static void initializeGame() {
        grid = new char[GRID_SIZE_Y][GRID_SIZE_X];
        for (int y=0; y<GRID_SIZE_Y; y++) {
            for (int x=0; x<GRID_SIZE_X; x++) {
                if (y == 0 || x == GRID_SIZE_X - 1 || x == 0 || y == GRID_SIZE_Y - 1)
                    grid[y][x] = WALL_CHAR;
                else{
                    grid[y][x] = EMPTY_CHAR;
                }
            }
            grid [4][17] = OBSTACLE_CHAR; //obstacle locations (3 in total)
            grid [2][4] = OBSTACLE_CHAR;
            grid [8][6] = OBSTACLE_CHAR;

            grid [0][10] = TELEPORT_CHAR; //wrap around movements or teleport locations
            grid [5][0] = TELEPORT_CHAR;
            grid [9][10] = TELEPORT_CHAR;
            grid [5] [19] = TELEPORT_CHAR;
        }


        //After initializing the grid, we generate the snake and food by calling their functions
        generateSnake();
        generateFood();
    }

    /**
     * Adds the snake to the game grid.
     */
    public static void generateSnake(){
        snake= new int [MAX_SNAKE_LENGTH][2]; //represents 100 segments with an x and y coordinate
        Random coordinate = new Random();

        int ogsnakex = 1; //stores initial x coordinate of snake segment
        int ogsnakey = 1; // stores initial y coordinate of snake segment

        do {
            ogsnakex = 1 + coordinate.nextInt(GRID_SIZE_X - 2); //randomly generates a value of x between x=1 and x=18
            //cite stackoverflow * https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
        }
        while(ogsnakex == 10); //we can't have the S come down the teleport hole initially

        snake[0][0] = ogsnakex; //passing the x coordinate to snake segment 0
        snake[0][1] = ogsnakey; //passing the y coordinate to snake segment 1

        snakeLength = 3; //initial length of the snake
        grid[ogsnakey][ogsnakex] = SNAKE_CHAR;
    }

    /**
     * Updates the snake's position on the grid and adjusts the snake's body
     * coordinates. Note: coordinates are given in the order of (X, Y).
     *
     * @param newX The new X coordinate of the snake's head.
     * @param newY The new Y coordinate of the snake's head.
     */
    public static void updateSnakePosition(int newX, int newY) {
        int snaketailX, snaketailY; //pre-storing the tail coordinates of the snake

        if(grid[newY][newX] == TELEPORT_CHAR){
            int[] teleport_coordinates = teleport(newX, newY); //calls teleport array and obtains new head positions in opposite side of grid
            newX = teleport_coordinates[0];
            newY = teleport_coordinates[1];

        }

        snaketailX = snake[snakeLength-1][0];
        snaketailY = snake[snakeLength-1][1];
        grid[snaketailY][snaketailX] = EMPTY_CHAR;


        //Move entire body
        for (int i=snakeLength-1;i>0;i--){
            snake[i][0] = snake[i-1][0]; //for eg. snake tail = snake middle, snake middle = snake head
            snake[i][1] = snake[i-1][1];

        }
        snake[0][0] = newX; //store new head coordinates of the snake to the snake array
        snake[0][1] = newY;

        if(grid[newY][newX] == FOOD_CHAR){
            snake[snakeLength][0] = snaketailX; //adding a new S to the old snake tail location after eating food
            snake[snakeLength][1] = snaketailY;
            snakeLength++;  //increases the snakelength/array size by 1
            generateFood(); //needs to generate new food since @ is gone
        }


        for (int i = 0; i <snakeLength; i++) {
            int x = snake[i][0];
            int y = snake[i][1];
            grid[y][x] = SNAKE_CHAR; //saving new locations of the snake array onto the grid to display it
        }

        // TODO: Trim the tail only if it's not the initial stages of the game
        //  i.e., the first three moves the length of the snake will be 1, 2, and 3
        //  respectively.

        // TODO: Move the segments of the body 2 <- 1, 1 <- 0, new 0

        // TODO: update the snake's head position
    }

    /**
     * Checks if the given coordinates are a valid move for the snake.
     * A move is valid if it is within the grid boundaries and does not
     * result in the snake colliding with itself or the walls.
     *
     * @paramx The X coordinate to check.
     * @paramy The Y coordinate to check.
     * @return true if the move is valid, false otherwise.
     */

    //we made a seperate array for the teleport functionality
    //We asked ChatGPT to invert the location of a chained character (snake head) within a grid
    //or parameters without breaking its flow. We used it for inspiration but figured it out
    //through trial and error.
    public static int[] teleport(int newX, int newY){ //passing the new head location to this method
        int [] invert_array = new int[2]; //array size is 2 because we are flipping x and y coordinates

        if (newY ==0 && newX==10){ // from top to bottom
            invert_array[0] = 10;
            invert_array[1] = 8;
        }
        else if (newY ==5 && newX==0 ){ //from left to right
            invert_array[0] = 18;
            invert_array[1] = 5;
        }
        else if (newY==9 && newX==10){ //from bottom to top
            invert_array[0] = 10;
            invert_array[1] = 1;
        }
        else if (newY==5 && newX==19){ //from right to left
            invert_array[0] = 1;
            invert_array[1] = 5;
        }
        else{
            invert_array[0] = newX; //this is the default setting - nothing changes
            invert_array[1] = newY;
        }
        return invert_array;
    }


    public static boolean isValidMove(int x, int y) {
        if(x<0 || x>=GRID_SIZE_X || y<0 || y>=GRID_SIZE_Y || grid[y][x] == OBSTACLE_CHAR){
            System.out.println("Game Over");
            return false;

        }
        if(grid[y][x] == WALL_CHAR && grid[y][x]!= TELEPORT_CHAR){ // allowing the snake to move through teleports
            System.out.println("Game Over");
            return false;
        }
        return grid[y][x] == EMPTY_CHAR || grid[y][x] == FOOD_CHAR || grid[y][x] == TELEPORT_CHAR; //if it is any of these characters, the move is valid
    }

    /**
     * Displays the current state of the game grid to the console.
     * i.e., this is where you print the display to the console
     */
    public static void displayGrid() {
        for (int y=0; y<GRID_SIZE_Y; y++) {
            for (int x=0; x<GRID_SIZE_X; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }

    }

    /**
     * Updates the snake's position based on the player's input direction.
     * If the move is valid, it updates the grid and checks for food.
     * Ends the game if the snake collides with the walls or itself.
     *
     * @param direction The direction in which to move the snake
     *                  ('w' for up, 's' for down, 'a' for left, 'd' for right).
     */
    public static void moveSnake(char direction) {
        // TODO: Implement a switch statement to handle the direction
        int x, y, newheadx, newheady;
        x = snake [0][0]; //save old snake head x coordinate
        y = snake [0][1]; //save old snake head y coordinate
        newheadx = x;
        newheady = y;
        switch (direction) {
            case 'w':
                newheady--;
                break;
            case 's':
                newheady++;
                break;
            case 'a':
                newheadx--;
                break;
            case 'd':
                newheadx++;
                break;
            case 'q':
                isGameOver = true;
                break;
            default:
                System.out.println("Invalid direction");
        }
        boolean validMove; //check to see if move was valid
        validMove = isValidMove(newheadx, newheady);
        if (validMove){
            updateSnakePosition(newheadx, newheady);
            displayGrid();
        }
        else{
            isGameOver = true;
        }
        // check if the move is valid, if not, the game ends
    }

    /**
     * Generates a food item at a random empty position on the grid.
     */
    public static void generateFood() {
        Random food = new Random();
        int xrandom, yrandom;
        do {
            //randomly generates x and y value between 1 and x = 18 and y = 8
            xrandom = 1 + food.nextInt(GRID_SIZE_X - 2); //cite stackoverflow * https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
            yrandom = 1 + food.nextInt(GRID_SIZE_Y - 2);
        }
        while (grid[yrandom][xrandom] != EMPTY_CHAR && grid[yrandom][xrandom] != OBSTACLE_CHAR && grid[yrandom][xrandom] != SNAKE_CHAR); //all of these conditions met - food generates
        grid[yrandom][xrandom] = FOOD_CHAR;
    }

    public static void main(String[] args) {
        initializeGame(); //initializing game
        displayGrid(); //displaying grid

        Scanner scanner = new Scanner(System.in);
        char direction;

        while(!isGameOver){
            while (true) {
                System.out.println("Enter w:up, s:down, a:left, d:right, q:quit");

                direction = scanner.nextLine().charAt(0); //input from user

                if (direction == 'a' || direction == 'd' || direction == 's' || direction == 'w') {
                    moveSnake(direction);//call this method while passing input
                    break;
                }
                else if (direction == 'q') {
                    isGameOver = true;
                    System.out.println("Game Over");
                    break;
                }

                else{
                    System.out.println("Invalid direction, please try again");
                    displayGrid();
                }
            }

        }


        // TODO: Create a scanner for input

        // TODO: Create a game loop that continues until the game is over

        // TODO: Close the scanner before exiting the game
    }

}
