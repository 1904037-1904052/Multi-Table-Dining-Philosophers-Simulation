import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.concurrent.*;


class DiningSimulation {
    private static final int NUM_PHILOSOPHERS_PER_TABLE = 5;
    private static final int NUM_TABLES = 6;
    private List<Table> tables;
    private Table sixthTable;
    private ExecutorService deadlockChecker;
    private char currentPhilosopherLabel = 'A';  // Label starting from 'A'
    
    public DiningSimulation() {
        tables = new ArrayList<>();

        for (int i = 0; i < NUM_TABLES - 1; i++) {
            tables.add(createTable(i));
        }

        sixthTable = new Table(NUM_PHILOSOPHERS_PER_TABLE, 6);
        tables.add(sixthTable);

        deadlockChecker = Executors.newFixedThreadPool(NUM_TABLES);
    }

    private Table createTable(int tableNum) {
        Table table = new Table(NUM_PHILOSOPHERS_PER_TABLE, tableNum);

        for (int i = 0; i < NUM_PHILOSOPHERS_PER_TABLE; i++) {
            Fork leftFork = table.getLeftFork(i);
            Fork rightFork = table.getRightFork(i);
            Philosopher philosopher = new Philosopher(i, leftFork, rightFork, table, currentPhilosopherLabel);
            table.addPhilosopher(philosopher, i);
            currentPhilosopherLabel++;
        }
        return table;
    }

    public void start() {
        for (Table table : tables) {
            table.startDinner();
        }

        // Start concurrent deadlock detection for all tables
        for (int i = 0; i < tables.size() - 1; i++) {
            final int tableIndex = i;
            deadlockChecker.submit(() -> monitorTableForDeadlock(tables.get(tableIndex)));
        }

        // Monitor the sixth table for deadlock
        deadlockChecker.submit(this::monitorSixthTableForDeadlock);

        // while (true) {
        //     for (int i = 0; i < tables.size() - 1; i++) {
        //         Table table = tables.get(i);
        //         if (table.detectDeadlock()) {
        //             Philosopher philosopher = table.handleDeadlock();
        //             sixthTable.addPhilosopher(philosopher);
        //             if (sixthTable.detectDeadlock()) {
        //                 System.out.println("Deadlock at the sixth table! Last philosopher to move: " + philosopher.getLabel());
        //                 return;
        //             }
        //         }
        //     }
        //     try {
        //         Thread.sleep(1000);  // Check for deadlocks every second
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }
    }

    private void monitorTableForDeadlock(Table table) {
        while (true) {
            if (table.detectDeadlock()) {
                Philosopher movedPhilosopher = table.handleDeadlock();
                
                // Find a random available seat at the sixth table
                Table sixthTable = tables.get(NUM_TABLES - 1);
                synchronized (sixthTable) {
                    sixthTable.addPhilosopherToRandomSeat(movedPhilosopher);
                    movedPhilosopher.run();
                }
                break;
            }

            try {
                Thread.sleep(1000);  // Check for deadlocks every second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void monitorSixthTableForDeadlock() {
        Table sixthTable = tables.get(NUM_TABLES - 1);
        while (true) {
            if (sixthTable.detectDeadlock()) {
                System.out.println("Sixth table is deadlocked!");
                // Output the philosopher who last moved to the sixth table
                Philosopher lastPhilosopher = sixthTable.getLastMovedPhilosopher();
                System.out.println("Last philosopher to move to the sixth table: " + lastPhilosopher.getLabel());
                sixthTable.startDinner(); // stop all philosopher 
                break;  // Stop the simulation
            }
            try {
                Thread.sleep(1000);  // Check for deadlock every second
            } catch (InterruptedException e) {
                break;
            }
        }
        shutdown();
    }

    private void shutdown() {
        deadlockChecker.shutdownNow();
    }

}
