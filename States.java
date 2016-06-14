package marca;

import java.io.Serializable;

public class States implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private double paddlePos;

  public States(double paddlePos) {
    this.paddlePos = paddlePos;
  }

  public double getPaddlePos() {
    return paddlePos;
  }

}
