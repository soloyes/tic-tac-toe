package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import View.*;
/**
 * Created by sol
 * on 05.06.17 11:05.
 * Basic idea: use 1-dimension LinkedList instead 2-dimension's ([] instead [][])
 * This case i need translate position with special method and check input by another (in Controller.UserAI class)
 */

public class Main {
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) {

        String in = "";
        try {
            for (;;) {
                System.out.println("1 - Console\n2 - GUI");
                in = reader.readLine();
                if (in.equals("1") || in.equals("2")) break;
            }
        } catch (IOException e) { e.printStackTrace(); }

        switch (in) {
            case "1":
                Console.gameConsole();
                break;
            case "2":
                System.out.println("Starting GUI");
                GUI.gameGUI();
                break;
            default:
                System.out.println("Something went wrong!");
                break;
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}