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

    public static void main(String[] args) {

        ///
        ///      Initialization Section: For collecting input
        ///

        // Initialize an array list for the initial population and an array list to contain a population of individuals
        List<String> InitPopulationRules = new ArrayList<>();
        ArrayList<Individual> Population = new ArrayList<>();
        //Initialize String array containing file paths stock data for various companies
        String[] files = {"AAPL.csv", "F.csv", "GOOG.csv", "NATI.csv", "NKE.csv"};
        //Initialize a HashMap to organize the stock data by company name
        HashMap<String, ArrayList<String[]>> Companies = new HashMap<>();

        //Collect input for in population and for companies
        try {
            //Gets Init population
            InitPopulationRules = Files.readAllLines(Paths.get("input.txt"));

            for(int i = 0; i < files.length; i++) {
                // Initialize a File for each Company's data, and a Scanner to read them
                File file = new File(files[i]);
                Scanner sc = new Scanner(file);
                // Read the contents of the file
                ArrayList<String[]> contents = new ArrayList<>();
                while (sc.hasNextLine()) {
                    String[] currentLine = sc.nextLine().split(",");
                    String[] info = {currentLine[0],currentLine[4]};
                    contents.add(info);
                }
                Companies.put(files[i].substring(0, files[i].indexOf(".")), contents);
            }


        } catch(IOException error) {
            System.out.println("Could not find input file(s).");
        }

        //CHECK that input is gathered correctly
//        for (String i: Companies.keySet()) {
//            for (String[] j: Companies.get(i)) {
//                for(String k: j) {
//                    System.out.println(k);
//                }
//            }
//        }

        ///
        /// GA Algorithm Section
        ///

        //Creates Individual Objects from the initial rules and add them to the population
        //Then set up initial run for GA algorithm
        for(String rule: InitPopulationRules) {
            Individual indiv = new Individual(rule);
            indiv.setFitness(eval_fitness(rule));
            Population.add(indiv);
        }

        //CHECK checks if crossover function works correctly
//        Individual[] testchildren = crossover(0, 1, Population);
//        for (Individual i: testchildren) {
//            System.out.println(i.getRule());
//        }

        double totalFitness = 0.0;
        int generationCount = 0;
        while(generationCount < 200) {

            //Set up the roulette wheel using the fitness values of each individual in population
            roulette();

            //Select two parents from the population (returns the indices)
            int p1 = select();
            int p2 = select();

            //Perform crossover (probability is set at 0.8)
            Individual[] children = crossover(p1, p2, Population);

            //Perform mutation for each Individual's rule (probability 0.001 for each character)
            for(Individual child: children) {
                if( !child.equals(null)) {
                    mutate(child);
                }
            }

            //Add the two children to Population
            for(Individual child: children) {
                if( !child.equals(null)) {
                    Population.add(child);
                }
            }

            generationCount++;
        }


    }

    private static double eval_fitness(String rule) {

        return 0.0;
    }

    private static Individual[] crossover(int p1, int p2, ArrayList<Individual> pop) {

        Random prob = new Random();

        if(prob.nextDouble() <= 0.8) {
            Random value = new Random();
            int pivotPoint = value.nextInt(14); //generate a random int from 0-13

            String pr1 = pop.get(p1).getRule();
            String pr2 = pop.get(p2).getRule();

            //Get first part of p1's rule and concatenate it with the second part of p2's rule
            String cr1;
            if (pivotPoint == 0) {
                cr1 = pr1.charAt(0) + pr2.substring(1, pr2.length());
            } else if (pivotPoint == 13) {
                cr1 = pr1.substring(0, 13) + pr2.charAt(13);
            } else {
                cr1 = pr1.substring(0, pivotPoint + 1) + pr2.substring(pivotPoint + 1, pr2.length());
            }

            //Get first part of p2's rule and concatenate it with the second part of p1's rule
            String cr2;
            if (pivotPoint == 0) {
                cr2 = pr2.charAt(0) + pr1.substring(1, pr1.length());
            } else if (pivotPoint == 13) {
                cr2 = pr2.substring(0, 13) + pr1.charAt(13);
            } else {
                cr2 = pr2.substring(0, pivotPoint + 1) + pr1.substring(pivotPoint + 1, pr1.length());
            }

            // Create new Individuals using new rules
            Individual c1 = new Individual(cr1);
            Individual c2 = new Individual(cr2);

            // Give new solutions a fitness value
            c1.setFitness(eval_fitness(cr1));
            c2.setFitness(eval_fitness(cr2));

            // Return both children
            Individual[] children = {c1, c2};
            return children;
        } else {
            //Don't do a crossover -- return null
            Individual[] children = {null, null};
            return children;
        }
    }

    private static void mutate(Individual indiv) {

        Random prob = new Random();
        Random val = new Random();
        Random choice = new Random();

        double changeVal;
        char changeChar;
        char changeOp;

        //Chance to mutate any character in the String
        String rule = indiv.getRule();
        for (int i = 0; i < 14; i++) {
            if(prob.nextDouble() <= 0.001) {
                char mutationCandidate = rule.charAt(i);
                switch(i){
                    case 0:                                     //Character
                        if(mutationCandidate == 's') {
                            if(choice.nextBoolean()) {
                                rule = ("m"+rule.substring(1, rule.length()));
                            } else {
                                rule = ("e"+rule.substring(1, rule.length()));
                            }
                        } else if (mutationCandidate == 'm') {
                            if(choice.nextBoolean()) {
                                rule = ("s"+rule.substring(1, rule.length()));
                            } else {
                                rule = ("e"+rule.substring(1, rule.length()));
                            }
                        } else {
                            if(choice.nextBoolean()) {
                                rule = ("s"+rule.substring(1, rule.length()));
                            } else {
                                rule = ("m"+rule.substring(1, rule.length()));
                            }
                        }
                        break;
                    case 1:                                     //Value
                        rule = (rule.substring(0,1) + val.nextInt(10) + rule.substring(2, rule.length()));
                        break;
                    case 2:
                        rule = (rule.substring(0,2) + val.nextInt(10) + rule.substring(3, rule.length()));
                        break;
                    case 3:
                        rule = (rule.substring(0,3) + val.nextInt(10) + rule.substring(4, rule.length()));
                        break;
                    case 4:                                     //Operator
                        if(mutationCandidate == '&') {
                            rule = (rule.substring(0,4) + "|" + rule.substring(5, rule.length()));
                        } else {
                            rule = (rule.substring(0,4) + "&" + rule.substring(5, rule.length()));
                        }
                        break;
                    case 5:                                     //Character
                        if(mutationCandidate == 's') {
                            if(choice.nextBoolean()) {
                                rule = (rule.substring(0, 5)+ "m" +rule.substring(6, rule.length()));
                            } else {
                                rule = (rule.substring(0, 5)+ "e" +rule.substring(6, rule.length()));
                            }
                        } else if (mutationCandidate == 'm') {
                            if(choice.nextBoolean()) {
                                rule = (rule.substring(0, 5)+ "s" +rule.substring(6, rule.length()));
                            } else {
                                rule = (rule.substring(0, 5)+ "e" +rule.substring(6, rule.length()));
                            }
                        } else {
                            if(choice.nextBoolean()) {
                                rule = (rule.substring(0, 5)+ "s" +rule.substring(6, rule.length()));
                            } else {
                                rule = (rule.substring(0, 5)+ "m" +rule.substring(6, rule.length()));
                            }
                        }
                        break;
                    case 6:                                     //Value
                        rule = (rule.substring(0,6) + val.nextInt(10) + rule.substring(7, rule.length()));
                        break;
                    case 7:
                        rule = (rule.substring(0,7) + val.nextInt(10) + rule.substring(8, rule.length()));
                        break;
                    case 8:
                        rule = (rule.substring(0,8) + val.nextInt(10) + rule.substring(9, rule.length()));
                        break;
                    case 9:                                     //Operator
                        if(mutationCandidate == '&') {
                            rule = (rule.substring(0,9) + "|" + rule.substring(10, rule.length()));
                        } else {
                            rule = (rule.substring(0,9) + "&" + rule.substring(10, rule.length()));
                        }
                        break;
                    case 10:                                    //Character
                        if(mutationCandidate == 's') {
                            if(choice.nextBoolean()) {
                                rule = (rule.substring(0, 10)+ "m" +rule.substring(11, rule.length()));
                            } else {
                                rule = (rule.substring(0, 10)+ "e" +rule.substring(11, rule.length()));
                            }
                        } else if (mutationCandidate == 'm') {
                            if(choice.nextBoolean()) {
                                rule = (rule.substring(0, 10)+ "s" +rule.substring(11, rule.length()));
                            } else {
                                rule = (rule.substring(0, 10)+ "e" +rule.substring(11, rule.length()));
                            }
                        } else {
                            if(choice.nextBoolean()) {
                                rule = (rule.substring(0, 10)+ "s" +rule.substring(11, rule.length()));
                            } else {
                                rule = (rule.substring(0, 10)+ "m" +rule.substring(11, rule.length()));
                            }
                        }
                        break;
                    case 11:                                    //Value
                        rule = (rule.substring(0,11) + val.nextInt(10) + rule.substring(12, rule.length()));
                        break;
                    case 12:
                        rule = (rule.substring(0,12) + val.nextInt(10) + rule.substring(13, rule.length()));
                        break;
                    case 13:
                        rule = (rule.substring(0,13) + val.nextInt(10));
                        break;
                }
            }
        }

        //After mutations are done, get current state of rule and sees if it is valid
        //If not discards mutation
        if(!(rule.charAt(0) == rule.charAt(5) || rule.charAt(5) == rule.charAt(10) || rule.charAt(0) == rule.charAt(10))) {
            if (!((Integer.parseInt(rule.substring(1,4)) > 365) || (Integer.parseInt(rule.substring(6,9)) > 365) || (Integer.parseInt(rule.substring(11,rule.length())) > 365))) {
                indiv.setRule(rule);
            }
        }

    }

    private static void roulette() {



    }

    private static int select() {

        return -1;
    }

    public static double EMA(int N) {

        for(int i = 0; i > N; i++) {



        }

        return 0.0;

    }

    public static double alpha(int N) {return 2 / (N + 1); }

    public static double SMA(int N) { return 0.0; }

    public static double MAX(int N) {

        double max = Double.NEGATIVE_INFINITY;
        double value = 0;

//        for() {
//
//            if (value > max) {
//                max = value;
//            }
//        }
//
//        return max;

        return 1;
    }

    public static int answerSoSar() {
        return -1;
    }



}
