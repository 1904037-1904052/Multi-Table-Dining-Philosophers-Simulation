import java.util.concurrent.ThreadLocalRandom;
import java.util.Date;
import java.util.Random;
import java.util.List;


class Table {
    private Philosopher[] philosophers;  // Array of philosophers corresponding to seats
    private int NUM_PHILOSOPHERS = 5;
    private int id;
    private Fork[] forks;
    private Philosopher lastMovedPhilosopher;

    public Table(int NUM_PHILOSOPHERS, int id) {
        this.NUM_PHILOSOPHERS = NUM_PHILOSOPHERS;
        this.id = id;

        forks = new Fork[NUM_PHILOSOPHERS];
        for(int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new Fork();
        }
    }

    public void addPhilosopher(Philosopher philosopher, int seatIndex) {
        philosophers[seatIndex] = philosopher;
        lastMovedPhilosopher = philosopher;  // Track the last moved philosopher
    }

    public synchronized void addPhilosopherToRandomSeat(Philosopher philosopher) {
        Random random = new Random();
        int seatIndex;

        // Find an empty seat
        do {
            seatIndex = random.nextInt(philosophers.length);
        } while (philosophers[seatIndex] != null);

        addPhilosopher(philosopher, seatIndex);
        System.out.println("Philosopher " + philosopher.getLabel() + " moved to seat " + seatIndex + " at the sixth table.");
    }


    public Fork getLeftFork(int philosopherIndex) {
        return forks[philosopherIndex];
    }

    public Fork getRightFork(int philosopherIndex) {
        return forks[(philosopherIndex + 1) % forks.length];
    }

    public Philosopher getLastMovedPhilosopher() {
        return lastMovedPhilosopher;
    }

    public void startDinner() {
        for (Philosopher philosopher : philosophers) {
            if(philosopher != null) {
                philosopher.run();
            }
        }

        // while (true) {
        //     if (detectDeadlock()) {
        //         handleDeadlock();
        //         break;
        //     }

        //     try {
        //         Thread.sleep(1000);  // Check for deadlock every second
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }
    }

    private void stopDinner() {
        for (Philosopher philosopher : philosophers) {
            if(philosopher == null) continue;
            philosopher.interrupt();
            try {
                philosopher.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace(); 
                System.out.println("FFFFFFFFFF stop " + philosopher.getLabel());
            }
        }
    }
    
    protected synchronized boolean detectDeadlock() {
        // Check if all philosophers are in the HUNGRY (waiting) state
        for (Philosopher philosopher : philosophers) {
            if (!philosopher.hasLeftFork() || philosopher.getPhilosopherState() != Philosopher.PhilosopherState.HUNGRY) {
                return false;  // Not all philosophers are waiting, no deadlock
            }
        }
        return true;  // All philosophers are waiting: deadlock!
    }
    
    protected Philosopher handleDeadlock() {
        System.out.println("Deadlock detected at " + new Date());

        Random random = new Random();
        int index;
        // Remove a random philosopher from the table to resolve deadlock
        do {
            index = random.nextInt(philosophers.length);
        } while (philosophers[index] == null);
        
        Philosopher philosopher = philosophers[index];
        stopDinner();
        philosophers[index] = null;  // Remove the philosopher from the current table
        System.out.println("In table-" + id + ", Philosopher " + philosopher.getLabel() + " is removed from the table.");
        return philosopher;
    }
}
