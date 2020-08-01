package Characters;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * @author tylderDurden
 */
public class Projectile extends Character {

    public Projectile(int x, int y) {
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
        this.getCharacter().setFill(Color.web("#990000"));
    }

}
