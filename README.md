# Dining Philosophers Problem Simulation

This repository contains a multi-table extension of the classical Dining Philosophers problem, implemented using threads. The simulation demonstrates how philosophers interact with forks, and how deadlocks are avoided by moving philosophers between tables. The goal is to simulate the protocol and track the time it takes for the sixth table to enter a deadlocked state.

## Problem Description

In the classical Dining Philosophers problem, five philosophers sit around a table with a fork between each pair. A philosopher can only eat when they are holding both forks next to them. Philosophers alternate between thinking and trying to pick up forks. If successful, they eat; if not, they wait for the forks to become available. 

In this extended version:

- There are **5 tables**, each with 5 philosophers and 5 forks.
- A **6th table** initially has no philosophers, but 5 forks are placed in the same arrangement.
- If a deadlock occurs at any table, one philosopher drops their fork and moves to the sixth table.
- The simulation continues until the sixth table also enters a deadlocked state.

Philosophers are labeled with the first 25 uppercase letters of the English alphabet.

## Simulation Protocol

Each philosopher follows this protocol:

   1. Think for a random amount of time (between 0 and 10 seconds).
   2. Become hungry and try to pick up the left fork.
   3. If successful, wait 4 seconds, then try to pick up the right fork. Otherwise, keep waiting until the left fork becomes available.
   4. If successful in picking up the right fork, start eating. Otherwise, keep waiting until the right fork becomes available.
   5. Eat for a random amount of time (between 0 and 5 seconds).
   6. Put down the left fork.
   7. Put down the right fork, and return to step 1.

If a deadlock occurs at any table, one philosopher will:
- Drop any fork they are holding.
- Move to a random unoccupied seat at the sixth table.

The simulation ends when the sixth table also reaches a deadlocked state. The philosopher who last moved to the sixth table before the deadlock is recorded.

## Objectives

The main objectives of this simulation are:
- To observe how long it takes for the sixth table to enter a state of deadlock.
- To identify the philosopher who last moved to the sixth table before the deadlock.

## Implementation Details

- Each philosopher is represented by a thread.
- The simulation can run on either a **real-time clock** or a **simulated clock**.
- You can scale the time parameters to adjust the speed of the simulation.

## Running the Simulation

To run the simulation, follow these steps:

Clone the repository:
   ```bash
   git clone https://github.com/your-username/dining-philosophers-simulation.git
   ```
**Compiling** :
   To compile all the Java files, navigate to the root directory of the repository and run the following command:
   ```
   javac *.java
   ```
**Run**:
   After compiling, you can run the main class using:
   ```
   java simulation
   ```

## Output
```
Simulation starts at : Fri Sep 27 00:55:00 BDT 2024

In table-2, Deadlock detected at Fri Sep 27 00:55:10 BDT 2024

Philosopher O is removed from the table-2.

Philosopher O moved to seat 2 at the sixth table.

In table-3, Deadlock detected at Fri Sep 27 00:56:49 BDT 2024

Philosopher T is removed from the table-3.

Philosopher T moved to seat 4 at the sixth table.

In table-4, Deadlock detected at Fri Sep 27 00:59:49 BDT 2024

Philosopher X is removed from the table-4.

Philosopher X moved to seat 3 at the sixth table.

In table-1, Deadlock detected at Fri Sep 27 01:02:24 BDT 2024

Philosopher I is removed from the table-1.

Philosopher I moved to seat 1 at the sixth table.

In table-0, Deadlock detected at Fri Sep 27 01:02:36 BDT 2024

Philosopher D is removed from the table-0.

Philosopher D moved to seat 0 at the sixth table.

In table-6, Deadlock detected at Fri Sep 27 01:04:32 BDT 2024

In sixth table, Deadlock detected at Fri Sep 27 01:04:32 BDT 2024

Simulation ends after 572656 milliseconds.

Last philosopher to move to the sixth table: D
```

