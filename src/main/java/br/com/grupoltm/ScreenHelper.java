package br.com.grupoltm;

import java.util.HashMap;
import  java.util.Scanner;

public final class ScreenHelper {

    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    //Reset code
    public static final String RESET = "\u001B[0m";

    public static void showOptions(String message, HashMap<String, String> options){
        writeln(PURPLE+"\n\rChose one of the follow options:\n\r"+RESET);
        for (String key : options.keySet()){
            writeOption(key, options.get(key));
        }
    }

    public static Integer tryGetInt(String message, String cancelValue) {
        while (true) {
            write(message);
            String line = null;
            try {
                Scanner in = new Scanner(System.in);
                line = in.nextLine();
                int value = Integer.parseInt(line);
                return  value;
            } catch(Exception ex){
                if(line.equals(cancelValue))
                    return null;
            }
        }
    }

    private  static void writeOption(String option, String message) {
        writeln(PURPLE+option+" "+RESET+message);
    }

    public static void writeInit() {
        writeln(YELLOW + "\n\rEvent Hub sender started." + RESET+"\n\r");
    }

    public static void writeError(String message) {
        writeln(RED + "ERROR: "+ message + RESET);
    }

    public static void writeln(String message){
        System.out.printf("%s%n",  message);
    }

    public static void write(String message){
        System.out.print(message);
    }

    public  static  void writeType(){
        write("option:");
    }

}
