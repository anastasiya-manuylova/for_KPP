package marca;

import java.io.IOException;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Main game class in MArcanoid game
 */
public class Game extends Application implements Constantt {
  private Stage gameStage;
  private Timeline gameLoop;
  private Group root;
  private Scene gameScene;
  private Rectangle paddle;
  private Circle ball;
  private int ballSpeedX;
  private int ballSpeedY;
  private Group bricks;
  private int paddleWidth = PADDLEWIDTH;
  private Random generator;
  Integer seed = 0;
  private Button endGame;


  Difficult diffCont = new Difficult();
  Replay replay = new Replay();

  @Override
  public void start(Stage primaryStage) {
    gameStage = primaryStage;
    gameStage.setTitle("MArcanoid");
    Integer seed = new Random().nextInt();
    double seedForRandom = seed.doubleValue();
    Replay.addState(seedForRandom);
    generator = new Random(seed);

    initGame();

    gameStage.setScene(gameScene);
    gameStage.setMinWidth(SCREENWIDTH);
    gameStage.setMinHeight(SCREENHEIGHT);
    gameStage.setMaxWidth(SCREENWIDTH);
    gameStage.setMaxHeight(SCREENHEIGHT);
    gameStage.show();
    gameLoop.play();
  }

  /*
   * Difficult settings for MArcanoid game
   */
  public void setDiff(int temp) {
    if (temp == 1) {
      temp = diffCont.easyDiff();
    } else if (temp == 2) {
      temp = diffCont.mediumDiff();
    } else if (temp == 3) {
      temp = diffCont.hardDiff();
    }
    paddleWidth = temp;
  }

  /*
   * Game initialization
   */
  public void initGame() {

    root = new Group();
    gameScene = new Scene(root, SCREENWIDTH, SCREENHEIGHT, Color.DODGERBLUE);
    paddle = new Rectangle(gameScene.getWidth() / 2 - OFFSET_WID_SCR,
        gameScene.getHeight() - OFFSET_HIGH_SCR, paddleWidth, PADDLEHEIGHT);
    paddle.setFill(Color.KHAKI);
    ball = new Circle(gameScene.getWidth() / 2, CIRCLECENTER, RADIUS, Color.MEDIUMAQUAMARINE);
    ballSpeedX = 0;
    ballSpeedY = 7;
    bricks = new Group();
    root.getChildren().addAll(paddle, ball, bricks);

    Color[] brickColor = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE};

    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < BRICKNUMBER; j++) {
        Rectangle thisBrick = new Rectangle(j * CORDINATEX + OFFSET_X, i * CORDINATEY + OFFSET_Y,
            BRICKWIDTH, BRICKHEIGHT);
        thisBrick.setFill(brickColor[i]);
        bricks.getChildren().add(thisBrick);
      }
    }
    move();
  }

  /*
   * Mouse moving in game
   */
  public void move() {
    gameScene.setOnMouseMoved(event -> {
      paddle.setX(event.getX() - paddleWidth / 2);
    });
    gameLoop = new Timeline();
    KeyFrame kf = new KeyFrame(Duration.millis(10), ae -> updateUserGame());
    gameLoop.getKeyFrames().add(kf);
    gameLoop.setCycleCount(Timeline.INDEFINITE);
  }

  /*
   * Ball moving
   */
  public void updateUserGame() {
    detectCollision();
    ball.setCenterX(ball.getCenterX() + ballSpeedX);
    ball.setCenterY(ball.getCenterY() + ballSpeedY);
  }

  /*
   * Detecting collisions with bricks and gamescreen
   */
  public void detectCollision() {
    if (ball.getBoundsInParent().intersects(paddle.getBoundsInParent())
        && ball.getCenterY() <= paddle.getY()) {
      ballSpeedX = randomNumber();
      ballSpeedY = ballSpeedY * -1;
    }

    if (ball.getCenterX() <= 0 || ball.getCenterX() >= gameScene.getWidth()) {
      ballSpeedX = ballSpeedX * -1;
    }

    if (ball.getCenterY() <= 0) {
      ballSpeedY *= -1;
    }

    if (ball.getCenterY() >= gameScene.getHeight()) {
      Text youLose =
          new Text(gameScene.getWidth() / 2 - OFFSET_WID, gameScene.getHeight() / 2, "You Lose!");
      ballSpeedX = 0;
      ballSpeedY = 0;
      Font font = new Font(50);
      youLose.setFont(font);
      youLose.setFill(Color.WHITE);
      root.getChildren().add(youLose);
      gameScene.setOnMouseMoved(null);
      try {
        Replay.writeToFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
      endGame = new Button();
      endGame.setLayoutX(SCREENWIDTH / 2 - 65); 
      endGame.setLayoutY(SCREENHEIGHT / 2 + OFFSET_HIGH_BUT); 
      endGame.setMinSize(BUTTONWIDTH,BUTTONHEIGHT); 
      endGame.setMaxSize(BUTTONWIDTH,BUTTONHEIGHT);
      endGame.setStyle("-fx-font: 16 arial; -fx-base: #b6e7c9;");
      endGame.setText("Again");
      endGame.setOnAction(new EventHandler <ActionEvent>()
      {
        public void handle(ActionEvent event) {
          initGame();
          gameStage.setScene(gameScene);
      }
      });
      root.getChildren().add(endGame);

    }

    for (int i = 0; i < bricks.getChildren().size(); i++) {
      if (ball.getBoundsInParent().intersects(bricks.getChildren().get(i).getBoundsInParent())) {
        bricks.getChildren().remove(i);
        ballSpeedY *= -1;
        increaseBallSpeedY();

        if (bricks.getChildren().size() == 0) {
          Text youWin = new Text(gameScene.getWidth() / 2 - OFFSET_WID, gameScene.getHeight() / 2,
              "You Win!");
          ballSpeedX = 0;
          ballSpeedY = 0;
          Font font = new Font(50);
          youWin.setFont(font);
          youWin.setFill(Color.WHITE);
          root.getChildren().add(youWin);
          gameScene.setOnMouseMoved(null);          
          try {
            Replay.writeToFile();
          } catch (IOException e) {
            e.printStackTrace();
          }
          endGame = new Button();
          endGame.setLayoutX(SCREENWIDTH / 2 - 65); 
          endGame.setLayoutY(SCREENHEIGHT / 2 + OFFSET_HIGH_BUT); 
          endGame.setMinSize(BUTTONWIDTH,BUTTONHEIGHT); 
          endGame.setMaxSize(BUTTONWIDTH,BUTTONHEIGHT);
          endGame.setStyle("-fx-font: 16 arial; -fx-base: #b6e7c9;");
          endGame.setText("Again");
          endGame.setOnAction(new EventHandler <ActionEvent>()
          {
            public void handle(ActionEvent event) {
              initGame();
              gameStage.setScene(gameScene);
          }
          });
          root.getChildren().add(endGame);
        }
      }
    }
  }

  /*
   * Increasing ball speed in X direction
   */
  public int randomNumber() {
    int randNumber = generator.nextInt(7) - 3;
    return randNumber;
  }

  /*
   * Increasing ball speed in Y direction
   */
  public void increaseBallSpeedY() {
    if (ballSpeedY < 7 && ballSpeedY > -7) {
      if (ballSpeedY > 0) {
        ballSpeedY++;
      } else if (ballSpeedY < 0) {
        ballSpeedY--;
      }
    }
  }

  /**
   * @param args the command line arguments
   */

}
