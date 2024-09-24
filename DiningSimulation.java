import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

class DiningSimulation {
    private static final int NUM_PHILOSOPHERS_PER_TABLE = 5;
    private static final int NUM_TABLES = 6;
    private List<Table> tables;
    private Table sixthTable;
    private List<String> labels;
    
    public DiningSimulation() {
        tables = new ArrayList<>();
        labels = new ArrayList<>();
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            labels.add(String.valueOf(ch));
        }

        for (int i = 0; i < NUM_TABLES - 1; i++) {
            tables.add(createTable(i));
        }

        sixthTable = new Table(NUM_PHILOSOPHERS_PER_TABLE, 6);
        tables.add(sixthTable);
    }

    private Table createTable(int tableNum) {
        Table table = new Table(NUM_PHILOSOPHERS_PER_TABLE, tableNum);
        for (int i = 0; i < NUM_PHILOSOPHERS_PER_TABLE; i++) {
            Fork leftFork = new Fork();
            Fork rightFork = new Fork();
            Philosopher philosopher = new Philosopher(i, leftFork, rightFork, table);
            table.addPhilosopher(philosopher);
        }
        return table;
    }

    public void start() {
        for (Table table : tables) {
            table.startDinner();
        }

        while (true) {
            for (int i = 0; i < tables.size() - 1; i++) {
                Table table = tables.get(i);
                if (table.detectDeadlock()) {
                    Philosopher philosopher = table.handleDeadlock();
                    sixthTable.addPhilosopher(philosopher);
                    if (sixthTable.detectDeadlock()) {
                        System.out.println("Deadlock at the sixth table! Last philosopher to move: " + philosopher.getLabel());
                        return;
                    }
                }
            }
            try {
                Thread.sleep(1000);  // Check for deadlocks every second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
