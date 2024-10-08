import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;

class Philosopher extends Thread {
    enum PhilosopherState {
        THINKING,
        HUNGRY,
        EATING
    }
    
    private char label;
    private int id;
    private PhilosopherState state;
    private Fork leftFork, rightFork;
    private boolean hasLeftFork;
    private Random random = new Random();
    
    public Philosopher(int id, Fork leftFork, Fork rightFork, Table table, char label) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.state = PhilosopherState.THINKING;
        this.label = label;
    }
    
    public void run() {
        try {
            while (!Thread.interrupted()) {
                think();

                state = PhilosopherState.HUNGRY;
                // System.out.println("Philosopher " + label + " is hungry, trying to pick up left chopstick.");

                // Try to acquire the left chopstick
                while (!leftFork.pickUp(label, 0)) {
                    // System.out.println("Philosopher " + label + " is waiting for the left chopstick.");
                    Thread.sleep(500);  
                }

                hasLeftFork = true;

                try {
                    // System.out.println("Philosopher " + label + " picked up left chopstick, waiting for 4 seconds.");
                    Thread.sleep(4000); 

                    // Try to acquire the right chopstick
                    while (!rightFork.pickUp(label, 1)) {
                        // System.out.println("Philosopher " + label + " is waiting for the right chopstick.");
                        Thread.sleep(500);  
                    }

                    try {
                        eat();  
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
        // System.out.println("Philosopher " + label + " is thinking.");
        Thread.sleep(ThreadLocalRandom.current().nextInt(0, 10000));  // Think for 0-10 seconds
    }
    
    private void eat() throws InterruptedException {
        state = PhilosopherState.EATING;
        // System.out.println("Philosopher " + id + " is eating.");
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

    public char getLabel() {
        return label;
    }
}
