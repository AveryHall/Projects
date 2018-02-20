/*
    Genetic Algorithm: Yahoo Stock
    CS 4453: Artificial Intelligence
    Dr. Arisoa Randrianasolo
    Avery Hall
    02/12/18
    Description: The purpose
 */

import java.io.*;
import java.nio.file.*;
import java.util.*;
//import java.awt.*;
import java.lang.*;

public class geneticStock {

    public static void eval_fitness() {

    }

    public static void roulette() {

    }

    public static int select() {

        return -1;
    }

    public static void mutate() {

        Random prob = new Random();
        Random val = new Random();
        Random choice = new Random();

        double changeVal;
        char changeChar;
        char changeOp;


        //Chance to mutate a digit value

        //Chance to mutate a character

        //Chance to mutate an operator
    }



    public static int answerSoSar() {
        return -1;
    }

    public static void main(String[] args) {

        List<String> population = new ArrayList<>();

        //Collect input
        try {
            population = Files.readAllLines(Paths.get("input.txt"));
        } catch(IOException error) {
            System.out.println("Could not find input file.");
        }

        //CHECK
        for (String i: population) {
            System.out.println(i);
        }



        // Output result


    }

}
