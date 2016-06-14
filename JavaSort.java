package marca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class JavaSort implements Constantt{
  
  public void Sorting () {
    try {
      showSort();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void showSort() throws IOException
  {   
      int i = 0;
      int [] arr = new int[100];
      for(int j = 0; j < 100; j++)
          arr[j] = 0;
      for(;;)
      {
          StringBuilder fileName = new StringBuilder();
          fileName.append("save");
          fileName.append(i);
          fileName.append(".txt");
          File newFile = new File(fileName.toString());
          if(!newFile.exists()) break;
          BufferedReader localFromFile = new BufferedReader(new FileReader(newFile.getAbsoluteFile()));
          @SuppressWarnings("unused")
          String stringFromFile;
          int buffer = 0;
          int sum = 0;
          while(buffer != -1)
          {
            buffer = localFromFile.read();
              sum++;
          }
          arr[i] = sum;
          localFromFile.close();
          i++;
      }
      int copyArr[];
      copyArr = arr.clone();
      long timeStart, timeEnd, scalaTime, javaTime;
      timeStart = System.currentTimeMillis();     
      sortSavings(arr);
      timeEnd = System.currentTimeMillis();
      javaTime = timeEnd - timeStart;
      timeStart = System.currentTimeMillis();
      ScalaFun newScalaFun = new ScalaFun();
      newScalaFun.sort(arr);
      timeEnd = System.currentTimeMillis();
      scalaTime = timeEnd - timeStart;
      int j = i;
      //i = 0;
      for(;;)
      {
          if(arr[i] != 0) break;
          i++;
      }
      j += i;
      System.out.print("Count of log files: ");
      System.out.println(j - i);
      System.out.println("Scores: ");
      for(; i < j; i++)
      {
          System.out.println(arr[i]);
      }
      i--;
      int bestSave = 0;
      for(int k = 0; copyArr[k] != 0; k++)
      {
          if(copyArr[k] == arr[i])
          {
              System.out.print("Best save in file save");
              bestSave = k;
              System.out.println(bestSave + ".txt");
          }
      }
      
      System.out.print("Java sort time: ");
      System.out.print(javaTime);
      System.out.println(" ms");
      System.out.print("Scala sort time: ");
      System.out.print(scalaTime);
      System.out.println(" ms");
      System.out.println();
  }

private void sortSavings(int[] arr)
  { 
      for(int i = arr.length-1 ; i > 0 ; i--)
      {
          for(int j = 0 ; j < i ; j++)
          {
              if( arr[j] > arr[j+1] )
              {
                  int tmp = arr[j];
                  arr[j] = arr[j+1];
                  arr[j+1] = tmp;
              }
          }
      }
  }
  
}
