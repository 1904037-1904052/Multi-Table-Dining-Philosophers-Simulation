// import java.util.ArrayList;
// import java.util.List;
// import java.util.Random;
// import java.util.concurrent.locks.Lock;
// import java.util.concurrent.locks.ReentrantLock;
// import java.util.concurrent.atomic.AtomicReference;

// class Fork {
//     private final Lock lock = new ReentrantLock();

//     public boolean pickUp(long timeout) {
//         try {
//             return lock.tryLock(timeout, java.util.concurrent.TimeUnit.MILLISECONDS);
//         } catch (InterruptedException e) {
//             return false;
//         }
//     }

//     public void putDown() {
//         if (lock.tryLock()) {
//             lock.unlock();
//         }
//     }
// }

// class Philosopher implements Runnable {
//     private final char id;
//     private Fork leftFork;
//     private Fork rightFork;
//     private final Table table;
//     private int eatingCount = 0;
//     private volatile boolean isHungry = false;
//     private final Random random = new Random();

//     public Philosopher(char id, Fork leftFork, Fork rightFork, Table table) {
//         this.id = id;
//         this.leftFork = leftFork;
//         this.rightFork = rightFork;
//         this.table = table;
//     }

//     @Override
//     public void run() {
//         try {
//             while (!Thread.currentThread().isInterrupted() && !table.isDeadlocked()) {
//                 think();
//                 if (getForks()) {
//                     eat();
//                     putForks();
//                 }
//             }
//         } catch (InterruptedException e) {
//             Thread.currentThread().interrupt();
//         }
//     }

//     private void think() throws InterruptedException {
//         Thread.sleep(random.nextInt(1000)); // Think for up to 1 second
//     }

//     private boolean getForks() throws InterruptedException {
//         isHungry = true;
//         boolean gotLeftFork = false;
//         boolean gotRightFork = false;

//         try {
//             gotLeftFork = leftFork.pickUp(4000);
//             if (gotLeftFork) {
//                 gotRightFork = rightFork.pickUp(4000);
//             }
//             return gotLeftFork && gotRightFork;
//         } finally {
//             if (!(gotLeftFork && gotRightFork)) {
//                 if (gotLeftFork) {
//                     leftFork.putDown();
//                 }
//                 isHungry = false;
//             }
//         }
//     }

//     private void eat() throws InterruptedException {
//         eatingCount++;
//         Thread.sleep(random.nextInt(50000)); // Eat for up to 0.5 seconds
//     }

//     private void putForks() {
//         leftFork.putDown();
//         rightFork.putDown();
//         isHungry = false;
//     }

//     public char getId() {
//         return id;
//     }

//     public boolean isHungry() {
//         return isHungry;
//     }

//     public void setLeftFork(Fork leftFork) {
//         this.leftFork = leftFork;
//     }

//     public void setRightFork(Fork rightFork) {
//         this.rightFork = rightFork;
//     }
// }

// class Table {
//     private final int id;
//     private final List<Fork> forks;
//     private final List<Philosopher> philosophers;
//     private volatile boolean deadlocked = false;

//     public Table(int id, int numPhilosophers) {
//         this.id = id;
//         this.forks = new ArrayList<>();
//         this.philosophers = new ArrayList<>();
//         for (int i = 0; i < numPhilosophers; i++) {
//             forks.add(new Fork());
//         }
//     }

//     public void addPhilosopher(Philosopher philosopher) {
//         philosophers.add(philosopher);
//     }

//     public boolean isDeadlocked() {
//         return deadlocked;
//     }

//     public void setDeadlocked(boolean deadlocked) {
//         this.deadlocked = deadlocked;
//     }

//     public boolean checkDeadlock() {
//         return philosophers.stream().allMatch(Philosopher::isHungry);
//     }

//     public List<Philosopher> getPhilosophers() {
//         return philosophers;
//     }

//     public List<Fork> getForks() {
//         return forks;
//     }
// }
// public class DiningPhilosophersSimulation {
//     private static final int NUM_TABLES = 6;
//     private static final int NUM_PHILOSOPHERS_PER_TABLE = 5;
//     private static final int TOTAL_PHILOSOPHERS = NUM_TABLES * NUM_PHILOSOPHERS_PER_TABLE;

//     public static void main(String[] args) throws InterruptedException {
//         List<Table> tables = new ArrayList<>();
//         List<Thread> threads = new ArrayList<>();
//         AtomicReference<Philosopher> lastMovedPhilosopher = new AtomicReference<>(null);

//         for (int i = 0; i < NUM_TABLES; i++) {
//             tables.add(new Table(i, NUM_PHILOSOPHERS_PER_TABLE));
//         }

//         for (int i = 0; i < TOTAL_PHILOSOPHERS; i++) {
//             int tableId = i / NUM_PHILOSOPHERS_PER_TABLE;
//             Table table = tables.get(tableId);
//             Fork leftFork = table.getForks().get(i % NUM_PHILOSOPHERS_PER_TABLE);
//             Fork rightFork = table.getForks().get((i + 1) % NUM_PHILOSOPHERS_PER_TABLE);
//             Philosopher philosopher = new Philosopher((char) ('A' + i), leftFork, rightFork, table);
//             table.addPhilosopher(philosopher);
//             threads.add(new Thread(philosopher));
//         }

//         long startTime = System.currentTimeMillis();
//         threads.forEach(Thread::start);

//         Table sixthTable = tables.get(NUM_TABLES - 1);

//         while (!sixthTable.isDeadlocked()) {
//             for (int i = 0; i < NUM_TABLES - 1; i++) {
//                 Table table = tables.get(i);
//                 if (table.checkDeadlock()) {
//                     table.setDeadlocked(true);
//                     Philosopher movingPhilosopher = table.getPhilosophers().get(new Random().nextInt(table.getPhilosophers().size()));
//                     table.getPhilosophers().remove(movingPhilosopher);
//                     sixthTable.addPhilosopher(movingPhilosopher);
//                     movingPhilosopher.setLeftFork(sixthTable.getForks().get(sixthTable.getPhilosophers().size() - 1));
//                     movingPhilosopher.setRightFork(sixthTable.getForks().get(sixthTable.getPhilosophers().size() % NUM_PHILOSOPHERS_PER_TABLE));
//                     lastMovedPhilosopher.set(movingPhilosopher);
//                     System.out.println("Philosopher " + movingPhilosopher.getId() + " moved to the sixth table");
//                     table.setDeadlocked(false);
//                     break;
//                 }
//             }

//             if (sixthTable.getPhilosophers().size() == NUM_PHILOSOPHERS_PER_TABLE) {
//                 if (sixthTable.checkDeadlock()) {
//                     sixthTable.setDeadlocked(true);
//                 }
//             }

//             Thread.sleep(100); // Small delay to prevent busy-waiting
//         }

//         long endTime = System.currentTimeMillis();
//         long simulationTime = endTime - startTime;

//         System.out.println("Simulation ended. Time taken: " + simulationTime / 1000.0 + " seconds");
        
//         Philosopher lastMoved = lastMovedPhilosopher.get();
//         if (lastMoved != null) {
//             System.out.println("Last philosopher to move to the sixth table: " + lastMoved.getId());
//         } else {
//             System.out.println("No philosopher moved to the sixth table before deadlock occurred.");
//         }

//         threads.forEach(Thread::interrupt);
//         for (Thread thread : threads) {
//             thread.join();
//         }
//     }
// }