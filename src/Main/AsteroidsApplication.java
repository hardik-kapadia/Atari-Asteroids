package Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import Characters.Asteroid;
import Characters.Projectile;
import Characters.Ship;
import Characters.Character;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * @author thecoderenroute
 */

public class AsteroidsApplication extends Application {

    public static int WIDTH = 600, HEIGHT = 400;
    private AtomicInteger points;
    private AnimationTimer runner;
    private BorderPane layout;
    private VBox startMenu;
    private Pane pane;
    private static ArrayList<AtomicInteger> allPoints;
    private Font font;
    private ArrayList<Hyperlink> buttons;

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }

    @Override
    public void init() {
        allPoints = new ArrayList<>();
        this.font = Font.loadFont("file:res/pixelated.ttf", 20);
    }

    @Override
    public void start(Stage stage) {
        this.layout = new BorderPane();

        this.startMenu = new VBox();

        startMenu.setAlignment(Pos.CENTER);
        startMenu.setSpacing(20);

        Image titleFile = new Image("file:res/icon.png");
        ImageView title = new ImageView(titleFile);
        title.setFitHeight(100);
        title.setFitWidth(220);


        Hyperlink start = new Hyperlink("START");

        Hyperlink scores = new Hyperlink("Scores");
        Hyperlink exit = new Hyperlink("Exit");
        this.buttons = new ArrayList<>();

        buttons.add(start);
        buttons.add(scores);
        buttons.add(exit);

        for (Hyperlink button : buttons) {
            button.setFont(font);
            button.setVisited(false);
        }


        startMenu.getChildren().addAll(title, start, scores, exit);

        this.pane = new Pane();
        pane.setPrefSize(600, 400);

        layout.setCenter(startMenu);
        layout.setBackground(new Background(new BackgroundFill(Color.web("#000033"), null, null)));
        layout.setPrefSize(600, 400);

        Text text = new Text(10, 20, "Points: 0");
        text.setText("Points: 0");
        text.setFill(Color.WHITE);
        pane.getChildren().add(text);

        this.points = new AtomicInteger();

        Scene scene = new Scene(layout);

        Ship ship = new Ship(300, 200);
        ship.getCharacter().setFill(Color.web("#990000"));

        Random random = new Random();

        for (int i = 0; i < random.nextInt(50) + 50; i++) {
            int tempX = random.nextInt(590);
            int tempY = random.nextInt(390);
            layout.getChildren().add(new Circle(tempX, tempY, 1, Color.WHITE));
        }

        ArrayList<Asteroid> asteroids = this.getAsteroids();

        start.setOnAction((event) -> this.startGame());
        scores.setOnAction((event) -> this.showScores());
        exit.setOnAction((actionEvent -> Platform.exit()));

        asteroids.forEach(asteroid
                -> pane.getChildren().add(asteroid.getCharacter()));

        HashMap<KeyCode, Boolean> input = new HashMap<>();

        scene.setOnKeyPressed(
                (event) -> input.put(event.getCode(), Boolean.TRUE));
        scene.setOnKeyReleased(
                (event) -> input.put(event.getCode(), Boolean.FALSE));

        this.runner = new AnimationTimer() {

            private ArrayList<Asteroid> asteroids;
            private ArrayList<Projectile> projectiles;
            private Ship ship;

            @Override
            public void start() {
                pane.getChildren().clear();
                pane.getChildren().add(text);
                super.start();
                asteroids = getAsteroids();
                projectiles = new ArrayList<>();
                ship = new Ship(300, 200);
                ship.getCharacter().setFill(Color.web("#990000"));
                pane.getChildren().add(ship.getCharacter());
                asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));
            }

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
                projectiles.forEach(projectile -> asteroids.forEach(asteroid -> {
                    if (asteroid.collision(projectile)) {
                        projectile.setIsDead(true);
                        asteroid.setIsDead(true);
                        points.addAndGet(100);
                        text.setText("Points: " + points);
                    }
                }));

                projectiles.forEach(Projectile::isOutBounds);

                projectiles.stream().filter(Character::isDead).forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));

                projectiles.removeAll(projectiles.stream()
                        .filter(Character::isDead)
                        .collect(Collectors.toList()));

                asteroids.stream().filter(Character::isDead).forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));

                asteroids.removeAll(asteroids.stream()
                        .filter(Character::isDead)
                        .collect(Collectors.toList()));

                ship.move();
                projectiles.forEach(Character::move);
                asteroids.forEach(Asteroid::move);
                asteroids.forEach(asteroid -> {
                    if (asteroid.collision(ship)) {
                        layout.setCenter(startMenu);
                        stop();
                        stopGame();
                    }
                });

                if (Math.random() < 0.015) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if (!asteroid.collision(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }

            }
        };

        stage.setScene(scene);

        stage.setTitle("Asteroids by Atari");
        stage.getIcons().add(new Image("file:res/icon.png"));
        stage.show();

    }

    private void stopGame() {
        runner.stop();
        allPoints.add(points);
        for (Hyperlink button : buttons) {
            button.setVisited(false);
        }
        this.layout.setCenter(this.startMenu);
        pane.getChildren().clear();
    }

    private void startGame() {
        this.points = new AtomicInteger();
        layout.setCenter(pane);
        this.runner.start();
    }

    private void showScores() {
        for (Hyperlink button : buttons) {
            button.setVisited(false);
        }
        ArrayList<Label> scores = new ArrayList<>();
        for (AtomicInteger score : AsteroidsApplication.allPoints) {
            Label temp = new Label(score.toString());
            temp.setFont(this.font);
            temp.setTextFill(Color.web("#00ccff"));
            temp.setTextAlignment(TextAlignment.CENTER);
            scores.add(temp);
        }
        Label Title = new Label("Scores");
        Title.setFont(this.font);
        Title.setTextFill(Color.web("#00ccff"));
        Title.setFont(Font.loadFont("file:res/pixelated.ttf", 40));
        Hyperlink back = new Hyperlink("Back to Main Menu");
        back.setFont(this.font);
        GridPane sLayout = new GridPane();
        sLayout.setVgap(10);
        Title.setTextAlignment(TextAlignment.CENTER);
        sLayout.setAlignment(Pos.CENTER);
        sLayout.add(Title, 1, 0);
        for (int i = 0; i < scores.size(); i++) {
            sLayout.add(scores.get(i), 1, i + 2);
        }
        sLayout.add(back, 1, scores.size() + 3);
        this.layout.setCenter(sLayout);

        back.setOnAction((event) -> this.layout.setCenter(this.startMenu));
    }

    private ArrayList<Asteroid> getAsteroids() {

        ArrayList<Asteroid> asteroids = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10 + random.nextInt(15); i++) {
            int tempX = random.nextInt(590);
            int tempY = random.nextInt(390);
            if (tempX > 280 && tempX < 320 || tempY > 185 && tempY < 215) {
                i--;
                continue;
            }
            asteroids.add(new Asteroid(tempX, tempY));
        }
        return asteroids;
    }

}
