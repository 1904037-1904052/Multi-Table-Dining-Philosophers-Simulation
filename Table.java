import java.util.Random;


class Table {
    private Philosopher[] philosophers;  // Array of philosophers corresponding to seats
    private Thread[] philosopherThreads;  // Array to hold philosopher threads
    private int NUM_PHILOSOPHERS = 5;
    private int id;
    private Fork[] forks;
    private Philosopher lastMovedPhilosopher;

    public Table(int NUM_PHILOSOPHERS, int id) {
        this.NUM_PHILOSOPHERS = NUM_PHILOSOPHERS;
        this.id = id;

        philosophers = new Philosopher[NUM_PHILOSOPHERS];
        philosopherThreads = new Thread[NUM_PHILOSOPHERS];
        forks = new Fork[NUM_PHILOSOPHERS];
        for(int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new Fork();
        }
    }

    public void addPhilosopher(Philosopher philosopher, int seatIndex) {
        // System.out.println("Table " + id + " added new philosopher " + philosopher.getLabel());
        philosophers[seatIndex] = philosopher;
        philosopherThreads[seatIndex] = new Thread(philosopher);
        lastMovedPhilosopher = philosopher;  // Track the last moved philosopher
    }

    public synchronized int addPhilosopherToRandomSeat(Philosopher philosopher) {
        Random random = new Random();
        int seatIndex = 0;

        for(int i = 0; i < NUM_PHILOSOPHERS; i++) {
            if(philosophers[i] != null) continue;
            seatIndex = i; break;
        }

        addPhilosopher(philosopher, seatIndex);
        System.out.println("Philosopher " + philosopher.getLabel() + " moved to seat " + seatIndex + " at the sixth table.");
        return seatIndex;
    }

    public void startPhilosopherAtIndex(int index) {
        philosopherThreads[index].start();
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

    protected void startDinner() {
        for (Thread thread : philosopherThreads) {
            if(thread != null) {
                thread.start();

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

    protected void stopDinner() {
        int cur = -1;
        for (Thread thread : philosopherThreads) {
            cur++;
            if(thread == null) continue;
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace(); 
                System.out.println("FFFFFFFFFF stop " + philosophers[cur].getLabel());
            }
        }
    }
    
    protected boolean detectDeadlock() {
        // Check if all philosophers are in the HUNGRY (waiting) state
        int activePhiosopher = 0;

        for(int i = 0; i < NUM_PHILOSOPHERS; i++) {
            if(philosophers[i] == null) continue;
            activePhiosopher++;
            
            if (!philosophers[i].hasLeftFork() || philosophers[i].getPhilosopherState() != Philosopher.PhilosopherState.HUNGRY) {
                return false;  // Not all philosophers are waiting, no deadlock
            }
        }

        if(activePhiosopher != 5) return false;
        return true;  // All philosophers are waiting: deadlock!
    }
    
    protected Philosopher handleDeadlock() {
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
