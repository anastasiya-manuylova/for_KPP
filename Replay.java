package marca;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class Replay {
  private static List<States> state;
  private static Integer iterator;
  private static States seed;

  public static void addState(double savePad) {
    if (state == null) {
      state = new ArrayList<>();
    }
    state.add(new States(savePad));
  }

  public static void writeToFile() throws IOException {
    int i = 0;
    for (;;) {
      StringBuilder fileName = new StringBuilder();
      fileName.append("save");
      fileName.append(i);
      fileName.append(".txt");
      File newFile = new File(fileName.toString());
      if (!newFile.exists()) {
        break;
      }
      i++;
    }
    StringBuilder fileName = new StringBuilder();
    fileName.append("save");
    fileName.append(i);
    fileName.append(".txt");
    File newFile = new File(fileName.toString());
    newFile.createNewFile();

    
    FileOutputStream fileOutputStream = new FileOutputStream(newFile);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

    for (int j = 0; j < state.size(); ++j) {
      objectOutputStream.writeObject(state.get(j));
    }
    state.clear();
    objectOutputStream.flush();
    objectOutputStream.close();
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  public static void writeSeed() throws IOException {

    int i = 0;
    for (;;) {
      StringBuilder fileName = new StringBuilder();
      fileName.append("seed");
      fileName.append(i);
      fileName.append(".txt");
      File newFile = new File(fileName.toString());
      if (!newFile.exists()) {
        break;
      }
      i++;
    }
    StringBuilder fileName = new StringBuilder();
    fileName.append("seed");
    fileName.append(i);
    fileName.append(".txt");
    File newFile = new File(fileName.toString());
    newFile.createNewFile();
    FileOutputStream fileOutputStream = new FileOutputStream(newFile);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

    objectOutputStream.writeObject(seed);

    objectOutputStream.flush();
    objectOutputStream.close();
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  public static void readFromFile(String replayName) throws IOException, ClassNotFoundException {

    File file = new File(replayName + ".txt");
    FileInputStream fileInputStream = new FileInputStream(file);
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

    while (true) {
      States stateOf;
      try {
        stateOf = (States) objectInputStream.readObject();
      } catch (EOFException e) {
        break;
      }

      if (stateOf == null) {
        break;
      }
      if (state == null) {
        state = new ArrayList<>();
      }
      state.add(stateOf);
    }

    objectInputStream.close();
    fileInputStream.close();
  }

  public static void readSeed(String replayName) throws IOException, ClassNotFoundException {

    File file = new File(replayName + ".txt");
    FileInputStream fileInputStream = new FileInputStream(file);
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

    seed = (States) objectInputStream.readObject();

    objectInputStream.close();
    fileInputStream.close();
  }

  public static States getSeed() {
    return seed;
  }

  public static void setSeed(States seed) {
    Replay.seed = seed;
  }

  public static States getNextState() {
    if (iterator == null) {
      iterator = 0;
    }
    if (state.get(iterator) != null) {
      iterator++;
      return state.get(iterator);
    }
    iterator = null;
    return null;
  }

  
  
  
  public void stopReplay() {
    state = new ArrayList<>();
  }
}
