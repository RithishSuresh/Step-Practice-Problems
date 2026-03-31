# 3rd and 4th Week Problems: Sorting and Searching

This branch contains the implementation for the 6 advanced sorting and searching problems in a banking/financial system context.

The project is structured within the `src/` directory containing models, sorting logic, searching logic, and problem implementations inside `main.Main`.

## 🔹 Problem 1: Transaction Fee Sorting
* Class: Transaction (id, fee, timestamp)
* Bubble Sort (ascending fee, for small <=100)
* Insertion Sort (fee + timestamp)
* Stable sorting (maintain order for duplicates)
* Count passes and swaps
* Flag high-fee transactions (>50)

## 🔹 Problem 2: Client Risk Ranking
* Class: Client (name, riskScore, accountBalance)
* Bubble Sort (ascending riskScore)
* Insertion Sort (riskScore DESC + balance)
* Identify top 10 highest risk clients
* Show swap count

## 🔹 Problem 3: Trade Volume Analysis
* Class: Trade (id, volume)
* Merge Sort (ascending volume, stable)
* Quick Sort (descending volume)
* Merge two sorted arrays
* Compute total volume

## 🔹 Problem 4: Portfolio Return Sorting
* Class: Asset (name, returnRate, volatility)
* Merge Sort (ascending returnRate)
* Quick Sort (return DESC + volatility ASC)
* Implement pivot strategies: Random pivot, Median-of-3

## 🔹 Problem 5: Account ID Lookup
* Linear Search:
  * First occurrence
  * Last occurrence
* Binary Search:
  * Exact match
  * Count duplicates
* Count comparisons
* Handle duplicates

## 🔹 Problem 6: Risk Threshold Lookup
* Linear search (unsorted)
* Binary search (sorted)
* Find:
  * Floor value
  * Ceiling value
  * Insertion index