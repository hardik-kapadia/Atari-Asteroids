package Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import Characters.Asteroid;
import Characters.Projectile;
import Characters.Ship;
import Characters.Character;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {

    public static int WIDTH = 600, HEIGHT = 400;

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(Color.web("#000033"), null, null)));
        pane.setPrefSize(600, 400);

        Text text = new Text(10, 20, "Points: 0");
        text.setText("Points: 0");
        text.setFill(Color.WHITE);
        pane.getChildren().add(text);

        AtomicInteger points = new AtomicInteger();

        Scene scene = new Scene(pane);

        Ship ship = new Ship(300, 200);
        ship.getCharacter().setFill(Color.web("#990000"));
        pane.getChildren().add(ship.getCharacter());

        Random random = new Random();

        for (int i = 0; i < random.nextInt(50) + 50; i++) {
            int tempX = random.nextInt(590);
            int tempY = random.nextInt(390);
            pane.getChildren().add(new Circle(tempX, tempY, 1, Color.WHITE));
        }

        List<Asteroid> asteroids = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();

        for (int i = 0; i < 10 + random.nextInt(10); i++) {
            int tempX = random.nextInt(590);
            int tempY = random.nextInt(390);
            if (tempX > 280 && tempX < 320 || tempY > 180 && tempY < 220) {
                i--;
                continue;
            }
            asteroids.add(new Asteroid(tempX, tempY));
        }

        asteroids.forEach(asteroid
                -> pane.getChildren().add(asteroid.getCharacter()));

        HashMap<KeyCode, Boolean> input = new HashMap<>();

        scene.setOnKeyPressed(
                (event) -> {
                    input.put(event.getCode(), Boolean.TRUE);
                }
        );
        scene.setOnKeyReleased(
                (event) -> {
                    input.put(event.getCode(), Boolean.FALSE);
                }
        );

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (input.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                if (input.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
                if (input.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }

                if (input.getOrDefault(KeyCode.SPACE, false) && projectiles.size() <= 6) {
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                    pane.getChildren().add(projectile.getCharacter());
                }
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if (asteroid.collision(projectile)) {
                            projectile.setIsAlive(false);
                            asteroid.setIsAlive(false);
                            text.setText("Points: " + points.addAndGet(100));
                        }
                    });
                });

                projectiles.stream().filter(projectile -> !projectile.isIsAlive()).forEach(projectile -> {
                    pane.getChildren().remove(projectile.getCharacter());
                });

                projectiles.removeAll(projectiles.stream()
                        .filter(projectile -> !projectile.isIsAlive())
                        .collect(Collectors.toList()));

                asteroids.stream().filter(asteroid -> !asteroid.isIsAlive()).forEach(asteroid -> {
                    pane.getChildren().remove(asteroid.getCharacter());
                });

                asteroids.removeAll(asteroids.stream()
                        .filter(asteroid -> !asteroid.isIsAlive())
                        .collect(Collectors.toList()));

                ship.move();
                projectiles.forEach(projectile -> projectile.move());
                asteroids.forEach(asteroid -> asteroid.move());
                asteroids.forEach(asteroid -> {
                    if (asteroid.collision(ship)) {
                        stop();
                    }
                });

                if (Math.random() < 0.01) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if (!asteroid.collision(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }

            }

        }.start();

        stage.setScene(scene);

        stage.setTitle("Asteroids by Atari");
        stage.getIcons().add(new Image("file:icon.png"));
        stage.show();

    }

    public static int partsCompleted() {
        // State how many parts you have completed using the return value of this method
        return 4;
    }

}
