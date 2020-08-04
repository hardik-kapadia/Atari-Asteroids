package Characters;

import Main.AsteroidsApplication;
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

    public void isOutBounds() {

        if (super.getCharacter().getTranslateY() >= AsteroidsApplication.HEIGHT ||
                super.getCharacter().getTranslateY() <= 0 ||
                super.getCharacter().getTranslateX() > AsteroidsApplication.WIDTH ||
                super.getCharacter().getTranslateX() < 0) {
            super.setIsAlive(false);
        }

    }

}
