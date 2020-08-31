package jfugueLearning;

//import java.awt.*;
import java.util.*;
import java.io.*;
import org.jfugue.player.Player;

public class Transpose {
	   
   public static void main(String[] args) throws FileNotFoundException{
      
      //scans in the source file for the notes, their octave values, and time signature
      Scanner inputNotes = new Scanner(new File("notes.txt"));
      Scanner inputOct = new Scanner(new File("notes.txt"));
      ArrayList<String> oldNotes = new ArrayList<>();
      ArrayList<String> newNotes = new ArrayList<>();
      //ArrayList<String> timeSig = new ArrayList<String>();
      ArrayList<Integer> octaves = new ArrayList<Integer>();

      //rips notes, time signature, and octave values from .txt file, respectively
      oldNotes = noteReader(inputNotes, oldNotes);
      //timeSig = timeReader(inputNotes, timeSig);
      octaves = octReader(inputOct, octaves);
      
      String[] notes = new String[] {"Bb", "B", "C", "C#", "D", "Eb", "E", "F", "F#", "G", "Ab", "A"};
      //String[] notesForDraw = new String[] {"B", "C", "D", "E", "F", "G", "A"}; 
      
      //stores all instruments in a map based on key, ranging from C, Eb, F, and Bb (keys based
      //on the strings in the notes array.
      HashMap<String, String> instrumentMap = 
         new HashMap<String, String>() {
            {
               put(notes[2], "piano, violin, viola, cello, flute, oboe, bassoon, trombone, tuba, piccolo, guitar, contrabassoon");
               put(notes[5], "alto saxophone, baritone saxophone, eb clarinet");
               put(notes[7], "english horn, french horn");
               put(notes[0], "bb clarinet, bb trumpet, soprano saxophone, tenor saxophone, flugel horn");
            }};                                       
   
      Scanner console = new Scanner(System.in);
            
      String firstInstrument = "";
      String secondInstrument = "";
      String firstKey = "";
      String secondKey = "";
            
      firstInstrument = intro(console);
      secondInstrument = transedInst(console);
      
      firstKey = findKeyOne(firstInstrument, firstKey, instrumentMap);
      System.out.println("Would you like to hear how the notes sound before transposition? (y/n)");
      String ans = console.next();
      if (ans.equals("y")) {
         playNotes(oldNotes, firstInstrument);
      }
      secondKey = findKeyTwo(secondInstrument, secondKey, instrumentMap);
      
      int halfSteps = findSteps(firstKey, secondKey, notes);
      
      System.out.println(Arrays.toString(oldNotes.toArray()));
      newNotes = halfStepper(console, halfSteps, notes, oldNotes);  
      
      
      System.out.println(halfSteps + " half steps later...");
      for (int i = 0; i < 5; i++) {
         System.out.println('.');
      }
      System.out.println(Arrays.toString(newNotes.toArray()));
      System.out.println("Would you like to hear how the notes sound after transposition? (y/n)");
      ans = console.next();
      if (ans.equals("y")) {
         playNotes(newNotes, secondInstrument);
      }
      
      /*
      FOR DRAWING MAKESHIFT "SHEET MUSIC"
      
      DrawingPanel panel = new DrawingPanel(40 * CON, 10 * CON);

      Graphics g = panel.getGraphics();
      Font font = new Font("Times New Roman", Font.PLAIN, 25);
      g.setFont(font);
      
      System.out.println("Now drawing sheet music...");
      drawIt(g, newNotes, notesForDraw, timeSig, octaves);
      */
   }
   
   //pre: takes in scanner object and arraylist that stores the notes
   //post:takes every note in the inputted text document and puts it in an Array List
   public static ArrayList<String> noteReader(Scanner inputNotes, ArrayList<String> oldNotes) {
      if (inputNotes.hasNextLine()) {
         String line = inputNotes.nextLine();
         Scanner newLine = new Scanner(line);
         while (newLine.hasNext()) {
            oldNotes.add(newLine.next().substring(0, 1));
         }
      }
      return oldNotes;
   }
   
   //returns time signature from txt doc
   /*
   public static ArrayList<String> timeReader(Scanner inputNotes, ArrayList<String> timeSig) {
      if (inputNotes.hasNextLine()) {
         String line = inputNotes.nextLine();
         Scanner newLine = new Scanner(line);
         while (newLine.hasNext()) {
            timeSig.add(newLine.next());
         }
      }
      return timeSig;
   }
   */
   
   //returns octave values for each note from txt doc
   public static ArrayList<Integer> octReader(Scanner inputOct, ArrayList<Integer> octaves) {
      if (inputOct.hasNextLine()) {
         String line = inputOct.nextLine();
         Scanner newLine = new Scanner(line);
         while (newLine.hasNext()) {
            octaves.add(Integer.parseInt(newLine.next().substring(1, 2)));
         }
      }
      return octaves; 
   }
   
   //takes console input for what instrument you want to transpose your music from
   public static String intro(Scanner console) {
      System.out.println("Music Transposition Tool.");
      System.out.println("What instrument's music would you like to transpose?");
      String s = console.nextLine();
      
      return s.toLowerCase();
   }
   
   //takes console input for what instrument you want to transpose your music to
   public static String transedInst(Scanner console) {
      System.out.println("To what instrument would you like to transpose your music to?");
      String s = console.nextLine();
      return s.toLowerCase();
   }
   
   //finds the key by looking through the instrumentMap Hash Map and finding which value matches 
   //the specified instrument, then returns the key for that value in the Hash Map as the literal 
   //key for the musical instrument
   public static String findKeyOne(String first, String firstKey, Map<String,
                                  String> instrumentMap) {
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
   public static String findKeyTwo(String second, String secondKey, Map<String, String> instrumentMap) {
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
   
   //CHANGE ALGORITHM SO THAT THE NOTE LOOP ONLY TAKES EVERY OTHER TOKEN STARTING AT THE FIRST ONE (TO ONLY TAKE THE LETTERS)
   //AND THE OCTAVE TOKEN LOOP STARTS AT THE SECOND TOKEN AND TAKES EVERY OTHER TOKEN ALSO, SO THE HALFSTEP ALGORITHM DOESN'T GET 
   //CONFUSED WITH THE NUMBERS.
   
   public static ArrayList<String> halfStepper(Scanner console, int halfSteps, String[] notes,
                                              ArrayList<String> oldNotes) {
      //first loop handles going to each note in the array
      for (int i = 0; i < oldNotes.size(); i++) {
         //second loop handles changing each note 
         for (int j = 0; j <= notes.length; j++) {
            if (oldNotes.get(i).equals(notes[j])) {
               if ((j + halfSteps) > notes.length - 1) { 
                  oldNotes.set(i, notes[(j + halfSteps) - notes.length]);
               } else if ((j + halfSteps) < 0) {
                  oldNotes.set(i, notes[(j + halfSteps) + notes.length]);
               } else {
                  oldNotes.set(i, notes[j + halfSteps]);
               }
               break;
            } 
         }
      } 
      return oldNotes;
   }
   
   public static void playNotes(ArrayList<String> notes, String instrument) {
      String noteaggregate = "I[" + instrument + "] ";
      for (int i = 0; i < notes.size() - 1; i++) {
         noteaggregate += notes.get(i) + " ";
      }
      noteaggregate += notes.get(notes.size() - 1);
      Player player = new Player();
      player.play(noteaggregate);
   }
   
   //post: draws makeshift "sheet music" without the note length values, only their tones, using the drawing panel
   //relatively low res
   /*
   public static void drawIt(Graphics g, ArrayList<String> newNotes, String[] notesForDraw, ArrayList<String> timeSig,
                              ArrayList<Integer> octaves) {
      
      int subtractor = 0;
      
      for (int i = 1; i <= 5; i++) {
         g.drawLine(0, CON + (CON * i), 39 * CON, CON + (CON * i));
      }
      g.drawLine(CON, CON * 2, CON, CON * 6);
      g.drawLine(CON * 39, CON * 2, CON * 39, CON * 6);
      for (int i = 0; i < newNotes.size(); i++) {
         
         int j = 0;
         String note = newNotes.get(i);
         int oct = octaves.get(i);
         
         for (j = 0; j < notesForDraw.length; j++) {
            if (newNotes.get(i).contains(notesForDraw[j])) {
               subtractor = j;
               if (j < 2) {
                  g.drawLine(CON * 2 + (i * 2 * CON), CON + (CON * 6), CON * 2 + 20 + (i * 2 * CON), CON + (CON * 6));
               }
               g.drawString(newNotes.get(i), CON * 2 + (i * 2 * CON), (CON / 2) * (16 - subtractor) + (4 - oct) * 75);
               break;
            }
         }
      }
      
      g.drawString(timeSig.get(0), 5, CON * 4);
      g.drawString(timeSig.get(1), 5, CON * 5);
   }
   */
}
