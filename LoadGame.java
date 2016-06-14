package marca;


import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Class for Artificial Intelligence game
 */
public class LoadGame extends Application implements Constantt {
  private Stage gameStage;
  private Timeline gameLoop;
  private Group root;
  private Scene gameScene;
  private Rectangle paddle;
  private Circle ball;
  private int ballSpeedX;
  private int ballSpeedY;
  private Group bricks;
  private Random generator;
  Integer seed = 0;

  
  @Override
  public void start(Stage primaryStage) {
    gameStage = primaryStage;
    gameStage.setTitle("MArcanoid");

    try {
      Replay.readFromFile("save2");
      Replay.readSeed("seed");
    } catch (Exception e) {
      e.printStackTrace();
    }
    double seedForRandom = Replay.getSeed().getPaddlePos();
    System.out.println((int)seedForRandom);
    System.out.println(seedForRandom);
    generator = new Random((int)seedForRandom);
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
   * Game initialization
   */
  public void initGame() {
    root = new Group();
    gameScene = new Scene(root, SCREENWIDTH, SCREENHEIGHT, Color.DODGERBLUE);
    paddle = new Rectangle(gameScene.getWidth() / 2 - OFFSET_WID_SCR,
            gameScene.getHeight() - OFFSET_HIGH_SCR, PADDLEWIDTH, PADDLEHEIGHT);
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
    moveAI();
  }

  public void moveAI() {
    gameLoop = new Timeline();
    KeyFrame kf = new KeyFrame(Duration.millis(5), ae -> updateAIGame());
    gameLoop.getKeyFrames().add(kf);
    gameLoop.setCycleCount(Timeline.INDEFINITE);
  }

  /*
   * Moving paddle by "AI"
   */
  public void updateAIGame() {
    paddle.setX(Replay.getNextState().getPaddlePos() + PADDLEWIDTH / 2);
    detectCollision();
    ball.setCenterX(ball.getCenterX() + ballSpeedX);
    ball.setCenterY(ball.getCenterY() + ballSpeedY);
  }

  /*
   * Detecting collisions with bricks ad gamescreen
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
        }
      }
    }
  }

  /*
   * Increasing ball speed in X direction
   */
  public int randomNumber() {
    // Random generator = new Random(seed);
    int randNumber = generator.nextInt(7) - 3;
    return randNumber;
  }

  /*
   * Increasing ball speed in Y direction
   */
  public void increaseBallSpeedY() {
    if (ballSpeedY < 7 && ballSpeedY > -7) {
      if (ballSpeedY > 0)
        ballSpeedY++;
      else if (ballSpeedY < 0)
        ballSpeedY--;
    }
  }
}
