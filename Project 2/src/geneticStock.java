/*
    Genetic Algorithm: Yahoo Stock
    CS 4453: Artificial Intelligence
    Dr. Arisoa Randrianasolo
    Avery Hall
    02/12/18
    Description: The purpose of this project is to implement a genetic algorithm to determine a
    good rule for buying and selling stocks based on historical data. The input consists of 5-30 csv's
    each with 5 years of historical data and an initial population of 20 somewhat diverse stock rules. The fitness for
    each rule is always calculated on a 3-year period, starting at 2015-02-13 and ending on 2018-02-16. The crossover,
    mutation, fitness evaluation, individual representation, and number of generations simulated is
    all based on the hand-out from class, with probabilities for each specified where applicable.
 */

import java.io.*;
import java.nio.file.*;
import java.util.*;
//import java.awt.*;
import java.lang.*;

public class geneticStock {

    private static double totalFitnessSum = 0.0;
    private static Individual bestIndiv = new Individual("");

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

            // Gets all company(ies) data and stores them in HashMap
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

        //CHECK that input is gathered correctly for the companies
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

        //Initalize bestIndiv's Fitness to 0 so that it will be replaced with a member of the population later with the best fitness
        bestIndiv.setFitness(0);

        //CHECK checks if MAX works correctly
        //System.out.println(MAX(200, 501, Companies.get("AAPL")));
        //CHECK checks if SMA works correctly
        //System.out.println(SMA(10, 501, Companies.get("AAPL")));
        //CHECK checks if EMA works correctly
        //System.out.println(EMA(10, 501, Companies.get("AAPL")));
        //CHECK checks if eval_fitness works correctly
        System.out.println(eval_fitness(InitPopulationRules.get(2), Companies));

        //Creates Individual Objects from the initial rules and add them to the population
        //Then set up initial run for GA algorithm
        for(String rule: InitPopulationRules) {
            Individual indiv = new Individual(rule);
            indiv.setFitness(eval_fitness(rule, Companies));
            Population.add(indiv);
        }

        //CHECK checks if crossover function works correctly
//        Individual[] testchildren = crossover(0, 1, Population);
//        for (Individual i: testchildren) {
//            System.out.println(i.getRule());
//        }

        //CHECK checks if mutate function works correctly
        //mutate(Population.get(0));

        double totalFitness = 0.0;

        //Runs GA Algorithm for 200 generations
        int generationCount = 0;
        while(generationCount < 200) {

            // Set up the roulette wheel using the fitness values of each individual in population
            // (Basically this function just assigns each member in the Population a selection value using its fitness)
            roulette();

            // Select two parents from the population (returns the indices)
            int p1 = select();
            int p2 = select();

            //Perform crossover (probability is set at 0.8)
            Individual[] children = crossover(p1, p2, Population);

            //Perform mutation for each Individual's rule (probability 0.001 for each character)
            for(Individual child: children) {
                if( !(child == null)) {
                    mutate(child);
                }
            }

            //Evaluate the fitness for each Individual's rule
            for(Individual child: children) {
                if( !(child == null)) {
                    eval_fitness(child.getRule(), Companies);
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

    private static double eval_fitness(String rule, HashMap<String, ArrayList<String[]>> Companies) {

        // Initialize a variable to store the total gains from all companies using this rule (this is will determine fitness)
        double totalGains = 0;

        // Run this rule for each company
        for (String company: Companies.keySet()) {

            // Variables
            int stocks = 0;                 // Initialize variable to keep track of stocks currently owned in company
            double startUpAccount = 20000;  // Initialize the startup account at $20,000
            double gains = 0;               // And gains at $0
            boolean hasTraded = false;      // Boolean to make sure rule has participated in the market
            double tFee = 7;                // Transaction fee


            // Get the company data for this company
            ArrayList<String[]> currentData = Companies.get(company);


            // The start will be set at 2015-02-13, or index 501 of each csv. This will allow a simulation of exactly 3 years for each company
            int currentDay = 501;
            // Simulate the current rule for 3 years
            while(currentDay < currentData.size()) {

                // Get today's price
                double todaysPrice = Double.parseDouble(currentData.get(currentDay)[1]);

                //CHECK
                //System.out.println("today's = " + todaysPrice);

                //Check that startUpAccount contains $20,000 and if not see if you have enough money in gains account to transfer
                if(startUpAccount < 20000) {
                    if(startUpAccount + gains >= 20000) {
                        gains -= (20000 - startUpAccount);
                        startUpAccount = 20000;
                    } else {
                        startUpAccount += gains;
                        gains = 0;
                    }
                }


                // String for buy and sell signal
                String signal = "";

                //Evaluate the rule:
                String[] results = new String[3];

                //Evaluate SMA rule
                int si = rule.indexOf("s");
                results[si/5] = SMA(Integer.parseInt(rule.substring(si+1, si+4)), currentDay, currentData);
                //Evaluate EMA rule
                int ei = rule.indexOf("e");
                results[ei/5] = EMA(Integer.parseInt(rule.substring(ei+1, ei+4)), currentDay, currentData);
                //Evaluate MAX rule
                int mi = rule.indexOf("m");
                results[mi/5] = MAX(Integer.parseInt(rule.substring(mi+1, mi+4)), currentDay, currentData);

                //Combine results to give either buy or sell signal
                String firstExp = "";
                if(rule.charAt(4) == '|') {
                    if(results[0].equals("buy") | results[1].equals("buy")) { firstExp = "buy"; }
                    else { firstExp = "sell"; } //Both results say to sell
                } else { // &
                    if(results[0].equals("buy") & results[1].equals("buy")) { firstExp = "buy"; }
                    else { firstExp = "sell"; } //Either result say to sell
                }
                if(rule.charAt(9) == '|') {
                    if(firstExp.equals("buy") || results[2].equals("buy")) { signal = "buy"; }
                    else { signal = "sell"; } //Both results say to sell
                } else { // &
                    if(firstExp.equals("buy") && results[2].equals("buy")) { signal = "buy"; }
                    else { signal = "sell"; } //Either result say to sell
                }

                //Reads signal from rule and tries to buy if told to buy
                if(signal.equals("buy")) {
                    //Determine how many stocks you can buy and if you can, buy them at today's price
                    if(startUpAccount - tFee > 0) {

                        // Determine the number of stocks you can buy and their price
                        int buyStocks = (int) Math.floor((startUpAccount - tFee) / todaysPrice);
                        double priceOfStocks = buyStocks * todaysPrice;

                        // If you can buy any stocks, buy them
                        if(buyStocks != 0) {
                            stocks += buyStocks;
                            startUpAccount -= (priceOfStocks + tFee);
                            hasTraded = true;
                        }

                    }
                } else if (signal.equals("sell")) {     // Or tries to sell if told to sell
                    //Determine if you have any stocks to sell and if so sell them at today's price
                    if(stocks > 0) {

                        double soldPrice = (stocks * todaysPrice) - tFee;
                        stocks = 0;

                        if(soldPrice > 20000) {
                            startUpAccount = 20000;
                            gains += soldPrice - 20000;
                        } else {
                            startUpAccount = soldPrice;
                        }

                        hasTraded = true;

                    }
                } //else signal did not tell to buy or sell, so do nothing

                currentDay++;
            }

            //Sells remaining stocks if any remain after trading period
            if(stocks > 0) {
                gains += stocks * Double.parseDouble(currentData.get(currentDay - 1)[1]);
            }

            // Adds total money left after trading to fitness score for company
            if(hasTraded) {
                totalGains += startUpAccount + gains;
                System.out.println("has traded: " + totalGains + ", " + startUpAccount + ", " + gains);
            } else {    // halved if rule didn't participate in market
                totalGains += (startUpAccount + gains) / 2;
                //System.out.println("has not traded: " + totalGains + ", " + startUpAccount + ", " + gains);
            }
        }

        if(totalGains < 0) { totalGains = 0; }
        totalFitnessSum += totalGains;

        return totalGains;
    }

    private static String EMA(int N, int currentDay, ArrayList<String[]> currentData) {

        double todaysPrice = Double.parseDouble(currentData.get(currentDay)[1]);
        double num = 0;
        double den = 0;
        double alpha = (2.0 / (double) (N + 1));

        //CHECK
        //System.out.println("alpha = " + alpha);
        //System.out.println("today's p = " + todaysPrice);

        double exponent = 0;
        for(int i = currentDay; i > currentDay - N; i--) {
            num += (Double.parseDouble(currentData.get(i)[1]) * Math.pow((1-alpha), exponent));
            den += Math.pow((1-alpha), exponent);

            //CHECK
            //System.out.println("newn = " + (Double.parseDouble(currentData.get(i)[1]) * Math.pow((1-alpha), exponent)));
            //System.out.println("newd = " + Math.pow((1-alpha), exponent));

            exponent++;

            //CHECK
            //System.out.println("num = " + num);
            //System.out.println("den = " + den);
        }

        double value = num / den;

        //CHECK
        //System.out.println("value = " + value);

        if(todaysPrice > value) {
            return "buy";
        }
        return "sell";
    }

    private static String SMA(int N, int currentDay, ArrayList<String[]> currentData) {

        double todaysPrice = Double.parseDouble(currentData.get(currentDay)[1]);
        double value = 0;

        //CHECK
        //System.out.println("today's p = " + todaysPrice);

        for(int i = currentDay - 1; i > currentDay - (N + 1); i--) {
            value += Double.parseDouble(currentData.get(i)[1]);
            //CHECK
            //System.out.println("value = " + value);
        }

        value = value / N;

        //CHECK
        //System.out.println("final value = " + value);

        if(todaysPrice > value) {
            return "buy";
        }
        return "sell";
    }

    private static String MAX(int N, int currentDay, ArrayList<String[]> currentData) {

        double todaysPrice = Double.parseDouble(currentData.get(currentDay)[1]);
        double max = Double.NEGATIVE_INFINITY;
        double value = 0;

        //CHECK
        //System.out.println("today's p = " + todaysPrice);

        for(int i = currentDay - 1; i > currentDay - (N + 1); i--) {

            //CHECK
            //System.out.println("value = " + value);

            value = Double.parseDouble(currentData.get(i)[1]);

            if (value > max) {
                //CHECK
                //System.out.println("old max = " + max);
                max = value;
                //CHECK
                //System.out.println("new max = " + max);
            }

        }

        if(max < todaysPrice) {
            return "buy";
        }
        return "sell";
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
            //c1.setFitness(eval_fitness(cr1, Companies));      Never mind, don't do this yet!
            //c2.setFitness(eval_fitness(cr2, Companies));

            // Return both children
            Individual[] children = {c1, c2};
            return children;
        } else {
            //Don't do a crossover -- return null
            Individual[] children = new Individual[2];
            children[0] = null;
            children[1] = null;
            return children;
        }
    }

    private static void mutate(Individual indiv) {


        // Initialize random generators for function
        Random prob = new Random();
        Random val = new Random();
        Random choice = new Random();

        // And boolean to tell if any mutation occurred during function call
        boolean mutated = false;

        //Chance to mutate any character in the String, so roll for mutation 13
        String rule = indiv.getRule();                                              // Get the Individual's rule
        for (int i = 0; i < 14; i++) {                                              // For each character in rule,
            if(prob.nextDouble() <= 0.001) {                                        // Roll for mutation

                mutated = true;

                //CHECK
                System.out.println("mutation");
                System.out.println(indiv.getRule());

                char mutationCandidate = rule.charAt(i);                            // If mutation grab current char

                switch(i){ // Switch-case handles how to rebuild String based on index of mutating character
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

        //CHECK
        System.out.println(rule);       // Prints new String with mutation

        //Checks to make sure mutation does not violate any rules, if so keeps mutation, else ignores mutation
        if(!(rule.charAt(0) == rule.charAt(5) || rule.charAt(5) == rule.charAt(10) || rule.charAt(0) == rule.charAt(10))) {
            if (!((Integer.parseInt(rule.substring(1,4)) > 365) || (Integer.parseInt(rule.substring(6,9)) > 365) ||
                    (Integer.parseInt(rule.substring(11,rule.length())) > 365)) && mutated) {

                //CHECK
                System.out.println("Rule was set (new mutation kept)");

                // Keeps new mutation -- updates Individual's rule with mutated rule
                indiv.setRule(rule);
            }
        }

    }

    private static void roulette() {



    }

    private static int select() {

        return -1;
    }

}
