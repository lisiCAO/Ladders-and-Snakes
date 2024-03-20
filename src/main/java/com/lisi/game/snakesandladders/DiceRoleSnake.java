package com.lisi.game.snakesandladders;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DiceRoleSnake extends Application {

    private int random;
    private Label randomResult;

    private static final int TITLE_SIZE = 80;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;

    private final Circle player1 = new Circle(20, Color.AQUA);
    private final Circle player2 = new Circle(20, Color.CHOCOLATE);

    private int playerPosition1 = 0;
    private int playerPosition2 = 0;

    private boolean player1Turn = true;
    private boolean player2Turn = false;

    private boolean gameStart = false;
    private Button gameButton;

    private final int[][] ladderAndSnakePositions = {
            {1, 38}, {4, 14}, {9, 31}, {17, 7}, {21, 42}, {28, 84}, {51, 67}, {54, 34},
            {62, 19}, {64, 60}, {72, 91}, {80, 99}, {87, 36}, {93, 73}, {95, 75}, {98, 79}
    };

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TITLE_SIZE, (HEIGHT * TITLE_SIZE) + 80);

        Image bgImage = new Image("board.jpg");
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setFitWidth(WIDTH * TITLE_SIZE);
        bgImageView.setFitHeight(HEIGHT * TITLE_SIZE);
        root.getChildren().add(bgImageView);

        root.getChildren().addAll(player1, player2);
        initUIComponents(root);

        // Set initial player positions
        updatePlayerPosition(player1, playerPosition1);
        updatePlayerPosition(player2, playerPosition2);

        return root;
    }

    private void initUIComponents(Pane root) {
        gameButton = new Button("Start Game");
        gameButton.setTranslateX(380);
        gameButton.setTranslateY(820);
        gameButton.setOnAction(this::handleStartGame);

        Button button1 = new Button("Player 1");
        button1.setTranslateX(80);
        button1.setTranslateY(820);
        button1.setOnAction(e -> handlePlayerMove(1));

        Button button2 = new Button("Player 2");
        button2.setTranslateX(700);
        button2.setTranslateY(820);
        button2.setOnAction(e -> handlePlayerMove(2));

        randomResult = new Label("Roll Result: 0");
        randomResult.setTranslateX(300);
        randomResult.setTranslateY(820);

        root.getChildren().addAll(gameButton, button1, button2, randomResult);
    }

    private void handleStartGame(ActionEvent event) {
        gameStart = !gameStart;
        gameButton.setText(gameStart ? "Game Started" : "Start Game");
        playerPosition1 = playerPosition2 = 0;
        updatePlayerPosition(player1, playerPosition1);
        updatePlayerPosition(player2, playerPosition2);
        randomResult.setText("Roll Result: 0");
    }

    private void handlePlayerMove(int playerNum) {
        if (!gameStart) return;

        Circle player = (playerNum == 1) ? player1 : player2;
        int playerPosition = (playerNum == 1) ? playerPosition1 : playerPosition2;

        rollDice();
        randomResult.setText("Roll Result: " + random);

        int newPosition = playerPosition + random;

        if (newPosition > 100) {
            newPosition = 100;
        } else {
            newPosition = checkForLaddersOrSnakes(newPosition);
        }

        if (playerNum == 1) {
            playerPosition1 = newPosition;
        } else {
            playerPosition2 = newPosition;
        }

        updatePlayerPosition(player, newPosition);

        player1Turn = !player1Turn;
        player2Turn = !player2Turn;

        if (newPosition == 100) {
            gameStart = false;
            randomResult.setText((playerNum == 1 ? "Player 1" : "Player 2") + " Wins!");
            gameButton.setText("Start New Game");
        }
    }

    private void rollDice() {
        random = (int) (Math.random() * 6) + 1;
    }

    private int checkForLaddersOrSnakes(int position) {
        for (int[] pos : ladderAndSnakePositions) {
            if (position == pos[0]) {
                return pos[1]; // Move to the end position of ladder or snake
            }
        }
        return position; // Remain at current position if no ladder or snake
    }

    private void updatePlayerPosition(Circle player, int position) {
        int[] pixelPos = convertPositionToPixel(position);
        translatePlayer(pixelPos[0], pixelPos[1], player);
    }

    private int[] convertPositionToPixel(int position) {
        if (position == 0) {
            return new int[]{-TITLE_SIZE, -TITLE_SIZE};
        }

        int row = (HEIGHT - 1) - ((position - 1) / WIDTH);
        int col = (position - 1) % WIDTH;

        // Adjust for snaking rows
        if (row % 2 == (WIDTH % 2)) {
            col = (WIDTH - 1) - col;
        }

        int x = col * TITLE_SIZE + TITLE_SIZE / 2;
        int y = row * TITLE_SIZE + TITLE_SIZE / 2;

        return new int[]{x, y};
    }

    private void translatePlayer(int x, int y, Circle player) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), player);
        transition.setToX(x);
        transition.setToY(y);
        transition.play();
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createContent());
        stage.setTitle("Snakes and Ladders!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
