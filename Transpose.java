import java.awt.*;
import java.util.*;
import java.io.*;

public class Transpose {

   public static final int CON = 25; 

   public static void main(String[] args) throws FileNotFoundException{
   
      Scanner input = new Scanner(new File("og_notes.txt"));
      ArrayList<String> newNotes = new ArrayList<String>();
      
      newNotes = txtReader(input, newNotes);
      
      String[] notes = new String[] {"Bb", "B", "C", "C#", "D", "Eb", "E", "F", "F#", "G", "Ab", "A"};
      String[] notesForDraw = new String[] {"B", "C", "D", "E", "F", "G", "A"}; 
   
      HashMap<String, String> instrumentMap = 
         new HashMap<String, String>() {
            {
               put(notes[2], "piano, violin, viola, cello, flute, oboe, bassoon, trombone, tuba, piccolo, guitar, contrabassoon");
               put(notes[5], "alto saxophone, baritone saxophone, eb clarinet");
               put(notes[7], "english horn, french horn");
               put(notes[0], "bb clarinet, bb trumpet, soprano saxophone, tenor saxophone, flugel horn");
            }};                                       
   
      Scanner console = new Scanner(System.in);
            
      String first = "";
      String second = "";
      String firstKey = "";
      String secondKey = "";
            
      first = intro(console, first);
      second = transedInst(console, second);
      
      firstKey = findKeyOne(console, first, firstKey, instrumentMap);
      secondKey = findKeyTwo(console, second, secondKey, instrumentMap);
      
      int halfSteps = findSteps(firstKey, secondKey, notes);
      
      System.out.println(Arrays.toString(newNotes.toArray()));
      newNotes = halfStepper(console, halfSteps, notes, newNotes);  
      
      System.out.println(halfSteps + " half steps later...");
      System.out.println(Arrays.toString(newNotes.toArray()));
      
      
      DrawingPanel panel = new DrawingPanel(40 * CON, 20 * CON);

      Graphics g = panel.getGraphics();
      Font font = new Font("Times New Roman", Font.PLAIN, 24);
      g.setFont(font);
      
      System.out.println("Now drawing sheet music...");
      drawIt(g, newNotes, notesForDraw);
   }
   
   //takes console input for what instrument you want to transpose your music from
   public static String intro(Scanner console, String first) {
      System.out.println("Music Transposition Tool.");
      System.out.println("What instrument's music would you like to transpose?");
      first = console.nextLine();
      
      return first.toLowerCase();
   }
   
   //takes console input for what instrument you want to transpose your music to
   public static String transedInst(Scanner console, String second) {
      System.out.println("To what instrument would you like to transpose your music to?");
      second = console.nextLine();
      return second.toLowerCase();
   }
   
   //finds the key by looking through the instrumentMap Hash Map and finding which value matches the specified instrument, then 
   //returns the key for that value in the Hash Map as the literal key for the musical instrument
   public static String findKeyOne(Scanner console, String first, String firstKey, Map<String, String> instrumentMap) {
      for (Map.Entry<String, String> entry: instrumentMap.entrySet()) {
         String inst = (String)entry.getValue();
         if (inst.contains(first)) {
            firstKey = (String)entry.getKey();
            System.out.println("Your " + first + " is in the key of " + firstKey);
         }
      }
      return firstKey;
   }
   
   //finds the key by looking through the instrumentMap Hash Map and finding which value matches the specified instrument, then 
   //returns the key for that value in the Hash Map as the literal key for the musical instrument
   public static String findKeyTwo(Scanner console, String second, String secondKey, Map<String, String> instrumentMap) {
      for (Map.Entry<String, String> entry: instrumentMap.entrySet()) {
         String inst = (String)entry.getValue();
         if (inst.contains(second)) {
            secondKey = (String)entry.getKey();
            secondKey = secondKey.substring(0, 1).toUpperCase() + secondKey.substring(1);
            System.out.println("The " + second + " is in the key of " + secondKey);
         }
      }
      return secondKey;
   }

   //finds the difference in half steps between the keys of the two specified instruments
   //using their respective indecies in the notes array
   public static int findSteps(String firstKey, String secondKey, String[] notes) {
      int firstPos = 0;
      int secondPos = 0;
      int catcherA = 0;
      int catcherB = 0;
      int steps = 0;
      while (firstPos == 0 && secondPos == 0) {
         for (int i = 0; i < notes.length; i++) {
            if (firstKey.equals(notes[i]) && catcherA == 0) {
               firstPos = i;
               catcherA++;
            } if (secondKey.equals(notes[i]) && catcherB == 0) {
               secondPos = i;
               catcherB++;
            }
         }
      }
      steps = (secondPos - firstPos) * -1;
      return steps;      
   }

   //takes every note in the inputted text document and puts it in an Array List

   public static ArrayList<String> txtReader(Scanner input, ArrayList<String> newNotes) {
      while (input.hasNext()) {
         newNotes.add(input.next());
   }
      return newNotes;
   }
   
   public static ArrayList<String> halfStepper(Scanner console, int halfSteps, String[] notes, ArrayList<String> newNotes) {
      //first loop handles going to each note in the array
      for (int i = 0; i < newNotes.size(); i++) {
         //second loop handles changing each note 
         for (int j = 0; j <= notes.length; j++) {
            if (newNotes.get(i).equals(notes[j])) {
               if ((j + halfSteps) > notes.length - 1) { 
                  newNotes.set(i, notes[(j + halfSteps) - notes.length]); 
               } else {
                  newNotes.set(i, notes[j + halfSteps]);
               }
               break;
            } 
         }
      }
      return newNotes;
   }
   
   public static void drawIt(Graphics g, ArrayList<String> newNotes, String[] notesForDraw) {
      int subtractor = 0;
      for (int i = 1; i <= 6; i++) {
         g.drawLine(CON, CON + (CON * i), 39 * CON, CON + (CON * i));
      }
      g.drawLine(CON, CON * 2, CON, CON * 6);
      g.drawLine(CON * 39, CON * 2, CON * 39, CON * 6);
      for (int i = 0; i < newNotes.size(); i++) {
         for (int j = 0; j < notesForDraw.length; j++) {
            if (newNotes.get(i).equals(notesForDraw[j])) {
               subtractor = j;
               break;
            }
         }
         g.drawString(newNotes.get(i), CON * 2 + (i * 2 * CON), ((CON / 2) * (16 - subtractor)));
      }
      
   }
}
   
// add numbers to the original notes scheme to identify octaves 