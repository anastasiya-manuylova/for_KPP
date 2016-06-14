package marca;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
// import javafx.scene.text.Font;
// import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Class for Artificial Intelligence game
 */
public class GameAI extends Application implements Constantt {
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
  private Button endGame;


  @Override
  public void start(Stage primaryStage) {
    gameStage = primaryStage;
    gameStage.setTitle("MArcanoid");
    Integer seed = new Random().nextInt(10000);
    double seedForRandom = seed.doubleValue();
    System.out.println(seed);
    System.out.println(seedForRandom);
    Replay.setSeed(new States(seedForRandom));

    try {
      getInfo();
    } catch (Throwable e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    generator = new Random(seed);
    initGame();
    moveAI();

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
  }

  public void moveAI() {
    Object monitor = new Object();
    InterObj newInterObj = new InterObj(monitor);
    newInterObj.setDaemon(true);
    newInterObj.start();
    gameLoop = new Timeline();
    KeyFrame kf = new KeyFrame(Duration.millis(0.1), ae -> updateAIGame(newInterObj, monitor));
    gameLoop.getKeyFrames().add(kf);
    gameLoop.setCycleCount(Timeline.INDEFINITE);
  }

  /*
   * Moving paddle by "AI"
   */
  public void updateAIGame(InterObj newInterObj, Object monitor) {
    double savePaddle;
    paddle.setX(ball.getCenterX() - paddle.getWidth() / 2);
    savePaddle = paddle.getX() - PADDLEWIDTH / 2;
    Replay.addState(savePaddle);
    newInterObj.startChecking(paddle, ball, bricks);
    synchronized (monitor) {
      newInterObj.setChecking();
      monitor.notifyAll();
      if (newInterObj.getChecking()) {
        try {
          monitor.wait();
        } catch (Exception e) {

        }
      }
    }
    if (newInterObj.getBallDownCol()) {
      gameOver();
    }
    if (newInterObj.getBallPaddleCol()) {
      ballPaddleCol();
    }
    if (newInterObj.getBallSideCol()) {
      ballSpeedX = ballSpeedX * -1;
    }
    if (newInterObj.getBallUpCol()) {
      ballSpeedY *= -1;
    }
    if (newInterObj.getBlockBallCol()) {
      winMethod(newInterObj.getRemoveI());
    }
    ball.setCenterX(ball.getCenterX() + ballSpeedX);
    ball.setCenterY(ball.getCenterY() + ballSpeedY);
  }

  /*
   * Detecting collisions with bricks ad gamescreen
   */
  public void ballPaddleCol() {

    ballSpeedX = randomNumber();
    ballSpeedY = ballSpeedY * -1;
  }

  public void gameOver() {
    try {
      Replay.writeSeed();
      Replay.writeToFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    /*
     * Text youLose = new Text(gameScene.getWidth() / 2 - OFFSET_WID, gameScene.getHeight() / 2,
     * "You Lose!"); ballSpeedX = 0; ballSpeedY = 0; Font font = new Font(50);
     * youLose.setFont(font); youLose.setFill(Color.WHITE); root.getChildren().add(youLose);
     */
    initGame();
    gameStage.setScene(gameScene);
    
    try {
      showPopularAction();
    } catch (Throwable e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
    ballSpeedX = 0;
    ballSpeedY = 0;
  }

  public void winMethod(int i) {
    bricks.getChildren().remove(i);
    ballSpeedY *= -1;
    increaseBallSpeedY();

    if (bricks.getChildren().size() == 0) {
      try {
        Replay.writeSeed();
        Replay.writeToFile();
      } catch (IOException e) {
        e.printStackTrace();
      }

      ballSpeedX = 0;
      ballSpeedY = 0;

      Text youWin =
          new Text(gameScene.getWidth() / 2 - OFFSET_WID, gameScene.getHeight() / 2, "You Win!");
      ballSpeedX = 0;
      ballSpeedY = 0;
      Font font = new Font(50);
      youWin.setFont(font);
      youWin.setFill(Color.WHITE);
      root.getChildren().add(youWin);

    }

    endGame = new Button();
    endGame.setLayoutX(SCREENWIDTH / 2 - 65);
    endGame.setLayoutY(SCREENHEIGHT / 2 + OFFSET_HIGH_BUT);
    endGame.setMinSize(BUTTONWIDTH, BUTTONHEIGHT);
    endGame.setMaxSize(BUTTONWIDTH, BUTTONHEIGHT);
    endGame.setText("Again");
    endGame.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        initGame();
        gameStage.setScene(gameScene);
      }
    });
    root.getChildren().add(endGame);
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
      if (ballSpeedY > 0)
        ballSpeedY++;
      else if (ballSpeedY < 0)
        ballSpeedY--;
    }
  }

  private void showPopularAction() throws Throwable
  {
      int arr[] = new int[3];
      arr[0] = 0;
      arr[1] = 0;
      arr[2] = 0;
      int i = 0;
      for(;;)
      {
          File newFile = new File("save" + i + ".txt");
          if(!newFile.exists()) break;
          FileInputStream fileInputStream = new FileInputStream(newFile);
          ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
          States prevState = null;
          while (true) {
              States newState;
              try {
                  newState = (States) objectInputStream.readObject();
              } catch (EOFException e) {
                  break;
              }
              if(prevState == null) {
                  prevState = newState;
              } else {
                  if(newState.getPaddlePos() - prevState.getPaddlePos() > 0) {
                      arr[0]++;
                  } else {
                      if(newState.getPaddlePos() - prevState.getPaddlePos() < 0) {
                          arr[1]++;
                      } else {
                          arr[2]++;
                      }
                  }
              }
          }
          objectInputStream.close();
          fileInputStream.close();
          i++;
      }
      int countRights = arr[0];
      int countLefts = arr[1];
      int countPauses = arr[2];
      ScalaFun newScalaFun = new ScalaFun();
      newScalaFun.sort(arr);
      System.out.println("Popular actions:");
      for(int j = 0; j < 3; j++)
      {
          if(arr[j] == countRights)
              System.out.print("Count of 'Right': ");
          if(arr[j] == countLefts)
              System.out.print("Count of 'Left': ");
          if(arr[j] == countPauses)
              System.out.print("Count of 'Pause': ");
          System.out.println(arr[j]);
      }
  }
  
  
  
  private void getInfo() throws Throwable
  {
      File newFile = new File("save0.txt");
      FileInputStream fileInputStream = new FileInputStream(newFile);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
      StringBuilder textForInfo = new StringBuilder();
      States newState;
      while (true) {
          try {
              newState = (States) objectInputStream.readObject();
              textForInfo.append("Current paddle position: ");
              textForInfo.append(newState.getPaddlePos());
              textForInfo.append(".\n");
          } catch (EOFException e) {
              break;
          }
      }
      fileInputStream.close();
      objectInputStream.close();
      ScalaFun newScalaFun = new ScalaFun();
      newScalaFun.writeSaveFile("info.txt", textForInfo.toString());
  }
}
