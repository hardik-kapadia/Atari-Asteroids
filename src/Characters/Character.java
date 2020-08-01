package Characters;

import Main.AsteroidsApplication;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author tylderDurden
 */
public abstract class Character {

    private boolean isAlive;

    private Polygon character;
    private Point2D movement;

    public Character(Polygon polygon, int x, int y) {
        this.character = polygon;

        character.setTranslateX(x);
        character.setTranslateY(y);

        movement = new Point2D(0, 0);
        this.isAlive = true;
    }

    public Polygon getCharacter() {
        return this.character;
    }

    public boolean isIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(character.getRotate())) * 0.05;
        double changeY = Math.sin(Math.toRadians(character.getRotate())) * 0.05;

        this.movement = this.movement.add(changeX, changeY);
    }

    public void turnLeft() {
        character.setRotate(character.getRotate() - 5);
    }

    public void turnRight() {
        character.setRotate(character.getRotate() + 5);
    }

    public void move() {
        character.setTranslateX(character.getTranslateX() + movement.getX());
        character.setTranslateY(character.getTranslateY() + movement.getY());

        if (this.character.getTranslateX() < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + AsteroidsApplication.WIDTH);
        }

        if (this.character.getTranslateX() > AsteroidsApplication.WIDTH) {
            this.character.setTranslateX(this.character.getTranslateX() % AsteroidsApplication.WIDTH);
        }

        if (this.character.getTranslateY() < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + AsteroidsApplication.HEIGHT);
        }

        if (this.character.getTranslateY() > AsteroidsApplication.HEIGHT) {
            this.character.setTranslateY(this.character.getTranslateY() % AsteroidsApplication.HEIGHT);
        }
    }

    public Point2D getMovement() {
        return movement;
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

    public boolean collision(Character other) {
        Shape collisionShape = Shape.intersect(this.character, other.getCharacter());
        return collisionShape.getLayoutBounds().getWidth() != -1;
    }

}
