package Characters;
import javafx.scene.shape.Polygon;

/**
 *
 * @author tylderDurden
 */
public class Ship extends Character {

    public Ship(int x, int y) {
        super(new Polygon(-6, -6, -6, 6, 12, 0), x, y);
    }

}
