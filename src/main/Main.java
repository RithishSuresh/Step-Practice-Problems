package main;

import model.Transaction;
import model.Client;
import model.Trade;
import model.Asset;

import problems.Problem1_TransactionFee;
import problems.Problem2_ClientRisk;
import problems.Problem3_TradeVolume;
import problems.Problem4_Portfolio;
import problems.Problem5_AccountLookup;
import problems.Problem6_RiskThreshold;

public class Main {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   STEP PRACTICE PROBLEMS EXECUTION MENU   ");
        System.out.println("===========================================\n");
        
        // Dummy data for Problem 1
        Transaction[] transactions = {
            new Transaction(1, 120.5, 1622500000L),
            new Transaction(2, 45.0, 1622500100L),
            new Transaction(3, 45.0, 1622500050L),
            new Transaction(4, 80.0, 1622500200L),
            new Transaction(5, 10.0, 1622500300L)
        };
        Problem1_TransactionFee.runProblem(transactions);
        
        // Dummy data for Problem 2
        Client[] clients = {
            new Client("Alice", 85, 1000.0),
            new Client("Bob", 40, 5000.0),
            new Client("Charlie", 90, 500.0),
            new Client("Diana", 85, 2000.0),
            new Client("Eve", 10, 10000.0)
        };
        Problem2_ClientRisk.runProblem(clients);
        
        // Dummy data for Problem 3
        Trade[] trades1 = {
            new Trade("T1", 500),
            new Trade("T2", 1500),
            new Trade("T3", 200)
        };
        Trade[] trades2 = {
            new Trade("T4", 300),
            new Trade("T5", 1000)
        };
        Problem3_TradeVolume.runProblem(trades1, trades2);
        
        // Dummy data for Problem 4
        Asset[] assets = {
            new Asset("AAPL", 0.15, 0.20),
            new Asset("GOOGL", 0.12, 0.15),
            new Asset("BTC", 0.80, 0.90),
            new Asset("Bonds", 0.05, 0.02),
            new Asset("TSLA", 0.40, 0.60)
        };
        Problem4_Portfolio.runProblem(assets);
        
        // Dummy data for Problem 5
        String[] accountIds = {"A100", "B500", "A100", "C300", "D900", "A100"};
        Problem5_AccountLookup.runProblem(accountIds, "A100");
        
        // Dummy data for Problem 6
        Integer[] riskScores = {15, 85, 40, 95, 60, 25, 50};
        Problem6_RiskThreshold.runProblem(riskScores, 55);
        
        System.out.println("\n--- END OF EXECUTION ---");
    }
}
