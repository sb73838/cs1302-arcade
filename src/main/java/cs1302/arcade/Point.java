package cs1302.arcade;

/**
 * The class {@code Point} is used to keep track of the position of the shapes
 * on the Tetris board. It has x and y coordinates, as well as the type of shape
 * the point belongs to.
 * 
 * @author Sahil Balhara
 */
public class Point {

    private int x;
    private int y;
    private int type;

    /**
     * Constructor of the class specifying the coordinates of the point
     * 
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     */
    public Point(int x, int y) {

        this.x = x;
        this.y = y;
    }

    /**
     * Constructor of the class specifying the coordinates of the point and the type
     * of shape the point belongs to.
     * 
     * @param x    the x-coordinate of the point
     * @param y    the y-coordinate of the point
     * @param type the type of shape the point belongs to
     */
    public Point(int x, int y, int type) {

        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Updates both the coordinates for the point
     * 
     * @param x the new x-coordinate of the point
     * @param y the new y-coordinate of the point
     */
    public void setLocation(int x, int y) {

        this.x = x;
        this.y = y;
    }

    /**
     * Returns the type of shape the point belongs to
     * 
     * @return the type of the shape
     */
    public int getType() {
        return type;
    }

    /**
     * Updates the x-coordinate
     * 
     * @param x the new x-coordinate of the point
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the x-coordinate
     * 
     * @return the x-coordinate of the point
     */
    public int getX() {
        return x;
    }

    /**
     * Modifies the value of the x-coordinate
     * 
     * @param mod the value to be added to the x-coordinate
     */
    public void modX(int mod) {
        x += mod;
    }

    /**
     * Updates the y-coordinate
     * 
     * @param y the new y-coordinate of the point
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the y-coordinate
     * 
     * @return the y-coordinate of the point
     */
    public int getY() {
        return y;
    }

    /**
     * Modifies the value of the y-coordinate
     * 
     * @param mod the value to be added to the y-coordinate
     */
    public void modY(int mod) {
        y += mod;
    }

    /**
     * Overrides the default equality behavior, checks both the x and y coordinates
     * individually
     * 
     * @param object the object that will be compared with the current object
     * @return true if the two objects are equal
     */
    @Override
    public boolean equals(Object object) {

        // if the object passed in, is of type Point
        if (object instanceof Point) {
            return x == ((Point) object).x && y == ((Point) object).y;
        }
        return false;
    }

}
