package marca;

import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class InterObj extends Thread {
  private boolean checking = false;
  private Object monitor;

  private boolean BlockBallCol = false;
  private boolean BallSideWall = false;
  private boolean BallUpWall = false;
  private boolean BallDownWall = false;
  private boolean BallPaddle = false;
  private int removeI;

  private Rectangle tempPaddle;
  private Circle tempCircle;
  private Group tempBricks;

  public InterObj(Object monitor) {
    this.monitor = monitor;
  }

  @Override
  public void run() {
    while (true) {
      synchronized (monitor) {
        try {
          if (!checking) {
            monitor.wait();
          }
          checkGameEvents();
        } catch (Throwable error) {
          error.printStackTrace();
        }
        checking = false;
        monitor.notifyAll();
      }
    }
  }

  public void startChecking(Rectangle paddle, Circle ball, Group bricks) {
    tempCircle = ball;
    tempPaddle = paddle;
    tempBricks = bricks;
  }


  public void checkGameEvents() throws Throwable {
    if (tempCircle.getBoundsInParent().intersects(tempPaddle.getBoundsInParent())
        && tempCircle.getCenterY() <= tempPaddle.getY()) {
      BallPaddle = true;
    }

    if (tempCircle.getCenterX() <= 0 || tempCircle.getCenterX() >= 820) {
      BallSideWall = true;
    }

    if (tempCircle.getCenterY() <= 0) {
      BallUpWall = true;
    }

    if (tempCircle.getCenterY() >= 650) {
      BallDownWall = true;
    }

    for (int i = 0; i < tempBricks.getChildren().size(); i++) {
      if (tempCircle.getBoundsInParent()
          .intersects(tempBricks.getChildren().get(i).getBoundsInParent())) {
        BlockBallCol = true;
        removeI = i;
      }
    }
  }

  public void setChecking() {
    checking = true;
  }

  public boolean getChecking() {
    return checking;
  }

  public boolean getBlockBallCol() {
    if (BlockBallCol) {
      BlockBallCol = false;
      return !BlockBallCol;
    }
    return BlockBallCol;
  }

  public boolean getBallUpCol() {
    if (BallUpWall) {
      BallUpWall = false;
      return !BallUpWall;
    }
    return BallUpWall;
  }

  public boolean getBallDownCol() {
    if (BallDownWall) {
      BallDownWall = false;
      return !BallDownWall;
    }
    return BallDownWall;
  }

  public boolean getBallSideCol() {
    if (BallSideWall) {
      BallSideWall = false;
      return !BallSideWall;
    }
    return BallSideWall;
  }

  public boolean getBallPaddleCol() {
    if (BallPaddle) {
      BallPaddle = false;
      return !BallPaddle;
    }
    return BallPaddle;
  }

  public int getRemoveI() {
    return removeI;
  }
}
