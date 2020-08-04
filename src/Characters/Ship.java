package Characters;

import Main.AsteroidsApplication;
import javafx.scene.shape.Polygon;

/**
 * @author tylderDurden
 */
public class Ship extends Character {

    public Ship(int x, int y) {
        super(new Polygon(-6, -6, -6, 6, 12, 0), x, y);
    }

    @Override
    public void move() {
        super.getCharacter().setTranslateX(super.getCharacter().getTranslateX() + super.getMovement().getX());
        super.getCharacter().setTranslateY(super.getCharacter().getTranslateY() + super.getMovement().getY());
        if (super.getCharacter().getTranslateX() < 0) {
            super.getCharacter().setTranslateX(super.getCharacter().getTranslateX() - super.getMovement().getX());
        }

        if (super.getCharacter().getTranslateX() > AsteroidsApplication.WIDTH) {
            super.getCharacter().setTranslateX(super.getCharacter().getTranslateX() - super.getMovement().getX());
        }

        if (super.getCharacter().getTranslateY() <= 0) {
            super.getCharacter().setTranslateY(super.getCharacter().getTranslateY() - super.getMovement().getY());

        }

        if (super.getCharacter().getTranslateY() >= AsteroidsApplication.HEIGHT) {
            super.getCharacter().setTranslateY(super.getCharacter().getTranslateY() - super.getMovement().getY());

        }
    }
}
