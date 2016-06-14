package marca;

import java.io.IOException;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Main menu class of the game
 */
public class menu extends Application implements Constantt {

  @Override
  public void start(Stage primaryStage) {
    Pane root = new Pane();
    Image image = new Image(getClass().getResourceAsStream("main.jpg"));
    ImageView img = new ImageView(image);
    img.setFitHeight(MENUHEIGHT);
    img.setFitWidth(MENUWIDTH);
    root.getChildren().add(img);

    MenuItem newGame = new MenuItem("New game");
    MenuItem replay = new MenuItem("Replay last game");
    MenuItem autogen = new MenuItem("Generate saves");
    MenuItem options = new MenuItem("Settings");
    MenuItem exitGame = new MenuItem("Exit");
    SubMenu mainMenu = new SubMenu(newGame, replay, autogen, options, exitGame);
    MenuItem sound = new MenuItem("Sound");
    MenuItem diff = new MenuItem("Difficult");
    MenuItem keys = new MenuItem("Configuration");
    MenuItem optionsBack = new MenuItem("Back");
    MenuItem easy = new MenuItem("Easy");
    MenuItem medium = new MenuItem("Medium");
    MenuItem hard = new MenuItem("Hard");
    MenuItem diffback = new MenuItem("Back");
    SubMenu optionsMenu = new SubMenu(sound, diff, keys, optionsBack);
    SubMenu difficultOption = new SubMenu(easy, medium, hard, diffback);
    MenuItem NG1 = new MenuItem("MArcanoid");
    MenuItem NG2 = new MenuItem("AI game");
    MenuItem NG3 = new MenuItem("Back");
    SubMenu newGameMenu = new SubMenu(NG1, NG2, NG3);
    MenuBox menuBox = new MenuBox(mainMenu);

    Game game = new Game();
    GameAI gameai = new GameAI();
    LoadGame lg = new LoadGame();
    JavaSort sort = new JavaSort();
    
    newGame.setOnMouseClicked(event -> menuBox.setSubMenu(newGameMenu));
    NG1.setOnMouseClicked(event -> game.start(primaryStage));
    replay.setOnMouseClicked(event -> lg.start(primaryStage));
    autogen.setOnMouseClicked(event -> sort.Sorting());
    options.setOnMouseClicked(event -> menuBox.setSubMenu(optionsMenu));
    NG2.setOnMouseClicked(event -> gameai.start(primaryStage));
    exitGame.setOnMouseClicked(event -> System.exit(0));
    optionsBack.setOnMouseClicked(event -> menuBox.setSubMenu(mainMenu));
    NG3.setOnMouseClicked(event -> menuBox.setSubMenu(mainMenu));
    diff.setOnMouseClicked(event -> menuBox.setSubMenu(difficultOption));
    diffback.setOnMouseClicked(event -> menuBox.setSubMenu(mainMenu));

    root.getChildren().addAll(menuBox);

    Scene scene = new Scene(root, MENUWIDTH, MENUHEIGHT);
    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        FadeTransition ft = new FadeTransition(Duration.seconds(1), menuBox);
        if (!menuBox.isVisible()) {
          ft.setFromValue(0);
          ft.setToValue(1);
          ft.play();
          menuBox.setVisible(true);
        } else {
          ft.setFromValue(1);
          ft.setToValue(0);
          ft.setOnFinished(evt -> menuBox.setVisible(false));
          ft.play();
        }
      }
    });
    primaryStage.setTitle("MArcanoid");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private static class MenuItem extends StackPane {
    public MenuItem(String name) {
      Rectangle bg = new Rectangle(ITEMWIDTH, ITEMHEIGHT, Color.WHITE);
      bg.setOpacity(0.5);

      Text text = new Text(name);
      text.setFill(Color.WHITE);
      text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

      setAlignment(Pos.CENTER);
      getChildren().addAll(bg, text);
      FillTransition st = new FillTransition(Duration.seconds(0.5), bg);
      setOnMouseEntered(event -> {
        st.setFromValue(Color.DARKGRAY);
        st.setToValue(Color.DARKGOLDENROD);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
      });
      setOnMouseExited(event -> {
        st.stop();
        bg.setFill(Color.WHITE);
      });
    }
  }
  private static class MenuBox extends Pane {
    static SubMenu subMenu;

    public MenuBox(SubMenu subMenu) {
      MenuBox.subMenu = subMenu;

      setVisible(false);
      Rectangle bg = new Rectangle(BOXWIDTH, BOXHEIGHT, Color.LIGHTBLUE);
      bg.setOpacity(0.4);
      getChildren().addAll(bg, subMenu);
    }

    public void setSubMenu(SubMenu subMenu) {
      getChildren().remove(MenuBox.subMenu);
      MenuBox.subMenu = subMenu;
      getChildren().add(MenuBox.subMenu);
    }
  }

  private static class SubMenu extends VBox {
    public SubMenu(MenuItem... items) {
      setSpacing(15);
      setTranslateY(100);
      setTranslateX(50);
      for (MenuItem item : items) {
        getChildren().addAll(item);
      }
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
