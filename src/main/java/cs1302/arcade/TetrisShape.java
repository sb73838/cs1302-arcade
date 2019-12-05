package cs1302.arcade;

import java.util.ArrayList;
import java.util.List;

/**
 * The class {@code TetrisShape} represents the various shapes used in Tetris.
 * This class can create, rotate, and move the shape. It also has a list of
 * points that represent the shape. Here is the mapping for the shapes:
 * 
 * 1 --> L, 2 --> I, 3 --> T, 4 --> S, 5 --> Z, 6 --> J, 7 --> O
 * 
 * @author Sahil Balhara
 */
public class TetrisShape {

    private int type;

    /**
     * Represents orientation for the current rotation (an integer from 0 to 3)
     */
    private int rotation;
    private List<Point> points;

    /**
     * Constructor of the class specifying the type of the shape
     * 
     * @param num type of shape
     */
    public TetrisShape(int num) {

        this.type = num;
        this.rotation = 0;
        this.points = new ArrayList<Point>();

        createPoints();
    }

    /**
     * Constructor of the class, copying from another TetrisShape
     * 
     * @param tetrisShape shape to be copied
     */
    public TetrisShape(TetrisShape tetrisShape) {

        this.type = tetrisShape.type;
        this.rotation = tetrisShape.rotation;
        this.points = new ArrayList<Point>(tetrisShape.points.size());

        // copy all the points
        for (Point i : tetrisShape.points) {
            this.points.add(new Point(i.getX(), i.getY(), type));
        }
    }

    /**
     * Creates the starting Points for the shape according to its type
     */
    private void createPoints() {

        // 1 --> L
        if (type == 1) {
            this.points.add(new Point(6, 0, type));
            this.points.add(new Point(5, 1, type));
            this.points.add(new Point(6, 1, type));
            this.points.add(new Point(4, 1, type));
        }
        // 2 --> I
        if (type == 2) {
            this.points.add(new Point(4, 0, type));
            this.points.add(new Point(5, 0, type));
            this.points.add(new Point(6, 0, type));
            this.points.add(new Point(7, 0, type));
        }
        // 3 --> T
        if (type == 3) {
            this.points.add(new Point(5, 0, type));
            this.points.add(new Point(5, 1, type));
            this.points.add(new Point(6, 1, type));
            this.points.add(new Point(4, 1, type));
        }
        // 4 --> S
        if (type == 4) {
            this.points.add(new Point(5, 0, type));
            this.points.add(new Point(6, 0, type));
            this.points.add(new Point(5, 1, type));
            this.points.add(new Point(4, 1, type));
        }
        // 5 --> Z
        if (type == 5) {
            this.points.add(new Point(4, 0, type));
            this.points.add(new Point(5, 0, type));
            this.points.add(new Point(5, 1, type));
            this.points.add(new Point(6, 1, type));
        }
        // 6 --> J
        if (type == 6) {
            this.points.add(new Point(4, 0, type));
            this.points.add(new Point(5, 1, type));
            this.points.add(new Point(6, 1, type));
            this.points.add(new Point(4, 1, type));
        }
        // 7 --> O
        if (type == 7) {
            this.points.add(new Point(5, 0, type));
            this.points.add(new Point(6, 0, type));
            this.points.add(new Point(5, 1, type));
            this.points.add(new Point(6, 1, type));
        }
    }

    /**
     * Moves the Tetris shape down one place
     */
    public void moveDown() {
        for (Point i : points) {
            i.modY(1);
        }
    }

    /**
     * Moves the Tetris shape left one place
     */
    public void moveLeft() {
        for (Point i : points) {
            i.modX(-1);
        }
    }

    /**
     * Moves the Tetris shape right one place
     */
    public void moveRight() {
        for (Point i : points) {
            i.modX(1);
        }
    }

    /**
     * This method rotates the Tetris shape to the right
     */
    public void rotate() {

        // type 7 (O) does not need rotation
        if (type != 7) {

            int minX = 100;
            int minY = 100;

            // adjust minX and minY based on the current location of the moving Tetris shape
            for (Point i : points) {
                if (i.getX() < minX) {
                    minX = i.getX();
                }

                if (i.getY() < minY) {
                    minY = i.getY();
                }
            }

            // if the current Tetris shape is of type I
            if (type == 2) {
                for (Point i : points) {
                    if (rotation == 0) {
                        i.setLocation(i.getY() - minY + minX + 2, i.getX() - minX + minY - 1);
                    } else if (rotation == 1) {
                        i.setLocation(i.getY() - minY + minX - 2, i.getX() - minX + minY + 2);
                    } else if (rotation == 2) {
                        i.setLocation(i.getY() - minY + minX + 1, i.getX() - minX + minY - 2);
                    } else {
                        i.setLocation(i.getY() - minY + minX - 1, i.getX() - minX + minY + 1);
                    }
                }
            } else {
                for (Point i : points) {
                    if (rotation == 1 || rotation == 2) {
                        i.setLocation(2 - (i.getY() - minY) + minX - 1,
                                (i.getX() - minX - 1 + (rotation % 2 * 2)) + minY);
                    } else {
                        i.setLocation(2 - (i.getY() - minY) + minX, (i.getX() - minX) + minY);
                    }
                }
            }
        }

        // update the rotation orientation
        rotation = (rotation + 1) % 4;
    }

    /**
     * This method returns the Points for the next rotation of the shape
     * 
     * @return list of rotated Points
     */
    public List<Point> getRotatedPoints() {

        TetrisShape rotated = new TetrisShape(this);
        rotated.rotate();

        return rotated.points;
    }

    /**
     * Returns the type of shape (numerical representation)
     * 
     * @return type of shape
     */
    public int getType() {
        return type;
    }

    /**
     * Returns all the Points for this shape
     * 
     * @return list of Points
     */
    public List<Point> getPoints() {
        return points;
    }

}
