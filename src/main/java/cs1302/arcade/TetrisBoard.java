package cs1302.arcade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Predicate;

/**
 * The class {@code TetrisBoard} represents the Board or grid used in Tetris.
 * This is the main class for Tetris game as it contains the moving TetrisShape,
 * a list of Points of the already settled TetrisShapes, and all the statistics
 * about the game. It contains methods used for collision detection,
 * manipulating/rotating the falling TetrisShape, and clearing full lines. It
 * also keeps updating the current score, current level, number of lines
 * cleared, as well as the speed of the moving TetrisShape.
 * 
 * @author Sahil Balhara
 */
public class TetrisBoard {

    private boolean gameOver;
    private int linesCleared;
    private int currentLevel;
    private int currentScore;
    private int currentTimeToMove;

    /**
     * A list of Points for the already settled TetrisShapes
     */
    private List<Point> points;
    private Random random;

    /**
     * The TetrisShape that is currently moving
     */
    private TetrisShape currentTetrisShape;

    public static final int WIDTH = 11;
    public static final int HEIGHT = 22;
    public static final int TIME_INTERVAL = 750;

    /**
     * Constructor of the TetrisBoard class
     */
    public TetrisBoard() {

        this.points = new ArrayList<Point>();
        this.random = new Random();
        this.gameOver = false;
        this.linesCleared = 0;
        this.currentLevel = 0;
        this.currentScore = 0;
        this.currentTimeToMove = TIME_INTERVAL;

        createCurrentTetrisShape();
    }

    /**
     * Creates a new TetrisShape using random class and sets it as the current
     * TetrisShape
     */
    public void createCurrentTetrisShape() {

        int randomInt = random.nextInt(8);

        if (randomInt == 7 || (currentTetrisShape != null 
                && randomInt == currentTetrisShape.getType())) {
            randomInt = random.nextInt(7);
        }

        if (currentTetrisShape != null) {
            points.addAll(currentTetrisShape.getPoints());
        }

        currentTetrisShape = new TetrisShape(randomInt + 1);
    }

    /**
     * Method for detecting collision with other shapes in the downward direction
     * 
     * @return true if there are Point(s) down the current TetrisShape
     */
    private boolean hasPointsDown() {

        for (Point i : currentTetrisShape.getPoints()) {
            if (points.contains(new Point(i.getX(), i.getY() + 1))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method for detecting collision with other shapes in the right direction
     * 
     * @return true if there are Point(s) to the right of the current TetrisShape
     */
    private boolean hasPointsRight() {

        for (Point i : currentTetrisShape.getPoints()) {
            if (points.contains(new Point(i.getX() + 1, i.getY()))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method for detecting collision with other shapes in the left direction
     * 
     * @return true if there are Point(s) to the left of the current TetrisShape
     */
    private boolean hasPointsLeft() {

        for (Point i : currentTetrisShape.getPoints()) {
            if (points.contains(new Point(i.getX() - 1, i.getY()))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method for deciding when the game is over
     * 
     * @return true if the current TetrisShape is close to the top
     */
    private boolean closeToTopBorder() {

        for (Point i : currentTetrisShape.getPoints()) {
            if (i.getY() == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method for detecting whether the current shape can move in the left direction
     * 
     * @return true if the current TetrisShape is close to the left
     */
    private boolean closeToLeftBorder() {

        for (Point i : currentTetrisShape.getPoints()) {
            if (i.getX() == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method for detecting whether the current shape can move in the right
     * direction
     * 
     * @return true if the current TetrisShape is close to the right
     */
    private boolean closeToRightBorder() {

        for (Point i : currentTetrisShape.getPoints()) {
            if (i.getX() == WIDTH - 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method for detecting whether the current shape can move in the downward
     * direction
     * 
     * @return true if the current TetrisShape is close to the bottom
     */
    private boolean closeToBottomBorder() {

        for (Point i : currentTetrisShape.getPoints()) {
            if (i.getY() == HEIGHT - 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method for determining whether the current shape can rotate
     * 
     * @return true if the next rotation is within the TetrisBoard and does not
     *         collide with other TetrisShapes
     */
    private boolean canRotate() {

        List<Point> rotated = currentTetrisShape.getRotatedPoints();

        // check the new location for each point of the shape after rotation
        for (Point i : rotated) {
            if (i.getX() >= WIDTH || i.getY() >= HEIGHT || i.getX() < 0 
                    || i.getY() < 0 || points.contains(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Rotates the current TetrisShape. First we need to check if it can be rotated.
     */
    public void rotate() {

        // checking if it can be rotated
        if (canRotate()) {
            currentTetrisShape.rotate();
        }
    }

    /**
     * Move the current TetrisShape to the left
     */
    public void moveLeft() {

        // checking if it can be moved left
        if (!hasPointsLeft() && !closeToLeftBorder()) {
            currentTetrisShape.moveLeft();
        }
    }

    /**
     * Move the current TetrisShape to the right
     */
    public void moveRight() {

        // checking if it can be moved right
        if (!hasPointsRight() && !closeToRightBorder()) {
            currentTetrisShape.moveRight();
        }
    }

    /**
     * Moves the current TetrisShape down, checks if the game is finished and
     * creates another TetrisShape if it can't move down
     */
    public void moveDown() {

        // checking if it can be moved down
        if (!hasPointsDown() && !closeToBottomBorder()) {
            currentTetrisShape.moveDown();
        } else {
            // checking if the shape is touching the top border
            if (closeToTopBorder()) {
                gameOver = true;
            } else {
                createCurrentTetrisShape();
                removeLines();
            }
        }
    }

    /**
     * Clears completed horizontal lines, updates the current score and level
     */
    private void removeLines() {

        List<Integer> fullLines = new ArrayList<Integer>(HEIGHT);
        List<Point> allPoints = getPoints();

        if (allPoints.size() != 0) {

            for (int i = 0; i < HEIGHT; i++) {
                boolean lineFull = true;

                // check if all points for current row exist in the all points collection
                for (int j = 0; j < WIDTH; j++) {
                    if (!allPoints.contains(new Point(j, i))) {
                        lineFull = false;
                        break;
                    }
                }

                if (lineFull) {
                    fullLines.add(i);
                }
            }
        }

        // if there are full lines that need to be cleared
        if (fullLines.size() != 0) {

            linesCleared += fullLines.size();
            currentScore += calculateCurrentScore(fullLines.size());

            for (int i : fullLines) {

                Predicate<Point> pointsPredicate = p -> p.getY() == i;
                points.removeIf(pointsPredicate);

                // dropping points/shapes for each cleared line
                for (int j = 0; j < points.size(); j++) {
                    if (points.get(j).getY() < i) {
                        points.get(j).modY(1);
                    }
                }
            }
        }

        // update the current level based on the number of lines cleared.
        currentLevel = linesCleared / 10;

        // update the current speed based on the current level
        updateSpeed();
    }

    /**
     * This method calculates the score for the cleared lines. The score depends on
     * number of lines cleared at a time and the current playing level.
     * 
     * @param lines number of cleared lines at once
     * @return the score for clearing that number of lines
     */
    private int calculateCurrentScore(int lines) {

        int baseScore = 50;

        // if number of lines cleared (at once) are more than 1, increase base score
        if (lines == 2) {
            baseScore = 150;
        } else if (lines == 3) {
            baseScore = 400;
        } else if (lines == 4) {
            baseScore = 800;
        }

        // multiply the base score by (currentLevel + 1) to return net score
        return baseScore * (currentLevel + 1);
    }

    /**
     * This method updates the time (in milliseconds) taken by the current block to
     * move one step. As the current Level increases, the time decreases, thus
     * increasing the difficulty level.
     */
    private void updateSpeed() {

        double baseInterval = TIME_INTERVAL;

        // reduce the speed by 10% for each next level
        if (currentLevel > 0) {
            currentTimeToMove = (int) (baseInterval * Math.pow(0.9, currentLevel));
        } else {
            currentTimeToMove = (int) baseInterval;
        }
    }

    /**
     * Returns a list of points for shapes already settled at the bottom and points
     * taken by the current Tetris Shape
     * 
     * @return list containing all the Points on the TetrisBoard
     */
    public List<Point> getPoints() {

        List<Point> points = new ArrayList<Point>();

        points.addAll(this.points);
        points.addAll(currentTetrisShape.getPoints());

        // to take care of any duplicate points
        Set<Point> setOfPoints = new HashSet<Point>();
        setOfPoints.addAll(points);

        points.clear();
        points.addAll(setOfPoints);

        return points;
    }

    /**
     * This method returns the number of lines cleared
     * 
     * @return the total number of lines cleared
     */
    public int getLinesCleared() {

        return linesCleared;
    }

    /**
     * Returns the game's current status
     * 
     * @return true if the game is over
     */
    public boolean isGameOver() {

        return gameOver;
    }

    /**
     * Returns the game's current level of play
     * 
     * @return the current level of play
     */
    public int getLevel() {

        return currentLevel;
    }

    /**
     * Returns the time taken by the current shape to move one step
     * 
     * @return the time taken by the current shape to move one step
     */
    public int getTimeToMove() {

        return currentTimeToMove;
    }

    /**
     * Returns the game's current score
     * 
     * @return the score of the current game
     */
    public int getScore() {

        return currentScore;
    }

}
