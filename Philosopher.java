import java.util.concurrent.ThreadLocalRandom;

enum PhilosopherState {
    THINKING,
    HUNGRY,
    EATING
}

class Philosopher implements Runnable {
    private int id;
    private PhilosopherState state;
    private Fork leftFork, rightFork;
    private Table table;  // Reference to the table for deadlock detection
    
    public Philosopher(int id, Fork leftFork, Fork rightFork, Table table) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.state = PhilosopherState.THINKING;
        this.table = table;
    }
    
    public void run() {
        try {
            while (true) {
                think();
                getHungry();
                if (pickUpLeftFork()) {
                    Thread.sleep(4000);  // Wait 4 seconds before picking up the right fork
                    if (pickUpRightFork()) {
                        eat();
                        putDownForks();
                    } else {
                        // Couldn't pick up right fork, put down left fork
                        leftFork.putDown();
                    }
                }
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
    
    private void getHungry() throws InterruptedException {
        state = PhilosopherState.HUNGRY;
        System.out.println("Philosopher " + id + " is hungry.");
        table.checkForDeadlock();  // Notify the table to check for deadlock
    }
    
    private boolean pickUpLeftFork() {
        return leftFork.pickUp();
    }
    
    private boolean pickUpRightFork() {
        return rightFork.pickUp();
    }
    
    private void eat() throws InterruptedException {
        state = PhilosopherState.EATING;
        System.out.println("Philosopher " + id + " is eating.");
        Thread.sleep(ThreadLocalRandom.current().nextInt(0, 5000));  // Eat for 0-5 seconds
    }
    
    private void putDownForks() {
        rightFork.putDown();
        leftFork.putDown();
        state = PhilosopherState.THINKING;
    }
    
    public PhilosopherState getState() {
        return state;
    }

    public int getId() {
        return id;
    }
}
