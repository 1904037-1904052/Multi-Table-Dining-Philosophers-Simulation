import java.util.concurrent.ThreadLocalRandom;
import java.util.Date;
import java.util.Random;
import java.util.List;


class Table {
    private List<Philosopher> philosophers;
    private int NUM_PHILOSOPHERS = 5;
    private int id;

    public Table(int NUM_PHILOSOPHERS, int id) {
        this.NUM_PHILOSOPHERS = NUM_PHILOSOPHERS;
        this.id = id;
    }

    public void addPhilosopher(Philosopher philosopher) {
        philosophers.add(philosopher);
    }

    public void startDinner() {
        for (Philosopher philosopher : philosophers) {
            philosopher.run();
        }

        while (true) {
            if (detectDeadlock()) {
                handleDeadlock();
                break;
            }

            try {
                Thread.sleep(1000);  // Check for deadlock every second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopDinner() {
        for (Philosopher philosopher : philosophers) {
            philosopher.interrupt();
            try {
                philosopher.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace(); 
                System.out.println("FFFFFFFFFF" + philosopher.getId());
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

        // Remove a random philosopher from the table to resolve deadlock
        int philosopherToRemove = new Random().nextInt(NUM_PHILOSOPHERS);
        Philosopher philosopher = philosophers.get(philosopherToRemove);
        philosopher.interrupt();
        System.out.println("In table-" + id + ", Philosopher " + philosopherToRemove + " is removed from the table.");
        return philosopher;
    }
}
