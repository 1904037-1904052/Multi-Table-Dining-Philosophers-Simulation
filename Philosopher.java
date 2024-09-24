import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;

class Philosopher extends Thread {
    enum PhilosopherState {
        THINKING,
        HUNGRY,
        EATING
    }
    
    private String label;
    private int id;
    private PhilosopherState state;
    private Fork leftFork, rightFork;
    private boolean hasLeftFork;
    private Table table;  // Reference to the table for deadlock detection
    private Random random;
    
    public Philosopher(int id, Fork leftFork, Fork rightFork, Table table) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.state = PhilosopherState.THINKING;
        this.table = table;
    }
    
    public void run() {
        try {
            while (!Thread.interrupted()) {
                think();

                state = PhilosopherState.HUNGRY;
                System.out.println("Philosopher " + id + " is hungry, trying to pick up left chopstick.");

                // Try to acquire the left chopstick
                while (!leftFork.pickUp()) {
                    System.out.println("Philosopher " + id + " is waiting for the left chopstick.");
                }

                hasLeftFork = true;

                try {
                    System.out.println("Philosopher " + id + " picked up left chopstick, waiting for 4 seconds.");
                    Thread.sleep(4000); 

                    // Try to acquire the right chopstick
                    while (!rightFork.pickUp()) {
                        System.out.println("Philosopher " + id + " is waiting for the right chopstick.");
                    }

                    try {
                        eat();  // Successfully picked up both chopsticks, now eat
                    } finally {
                        rightFork.putDown();
                    }
                } finally {
                    leftFork.putDown();  
                    hasLeftFork = false;
                }

                // Slow down to avoid instant locking all the time
                Thread.sleep(random.nextInt(1000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void think() throws InterruptedException {
        state = PhilosopherState.THINKING;
        System.out.println("Philosopher " + id + " is thinking.");
        Thread.sleep(ThreadLocalRandom.current().nextInt(0, 10000));  // Think for 0-10 seconds
    }
    
    private void eat() throws InterruptedException {
        state = PhilosopherState.EATING;
        System.out.println("Philosopher " + id + " is eating.");
        Thread.sleep(ThreadLocalRandom.current().nextInt(0, 5000));  // Eat for 0-5 seconds
    }

    public boolean hasLeftFork() {
        return hasLeftFork;
    }
    
    public PhilosopherState getPhilosopherState() {
        return state;
    }

    public int getPhilosopherId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
