package Characters;

import java.util.Random;

import javafx.scene.paint.Color;

/**
 * @author tylderDurden
 */
public class Asteroid extends Character {

    private final double rotationalMovement;

    public Asteroid(int x, int y) {
        super(new PolygonFactory().createPolygon(), x, y);

        this.getCharacter().setFill(Color.web("#331a00"));

        Random rnd = new Random();

        super.getCharacter().setRotate(rnd.nextInt(360));

        int accelerateAmount = 1 + rnd.nextInt(12);
        for (int i = 0; i < accelerateAmount; i++) {
            super.accelerate();
        }
        this.rotationalMovement = 0.5 - rnd.nextDouble();

    }

    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + this.rotationalMovement);
    }

}
