import java.util.concurrent.ThreadLocalRandom;


class Table {
    private Philosopher[] philosophers;
    
    public Table(Philosopher[] philosophers) {
        this.philosophers = philosophers;
    }
    
    public synchronized void checkForDeadlock() {
        boolean allHungry = true;
        
        // Check if all philosophers are in the HUNGRY state
        for (Philosopher philosopher : philosophers) {
            if (philosopher.getState() != PhilosopherState.HUNGRY) {
                allHungry = false;
                break;
            }
        }
        
        // If all are hungry, check if none of them are eating (implying deadlock)
        if (allHungry) {
            System.out.println("Deadlock detected at this table!");
            // Handle deadlock resolution (e.g., move a philosopher to the 6th table)
            resolveDeadlock();
        }
    }
    
    private void resolveDeadlock() {
        // Pick a random philosopher to move to the 6th table
        int randomIndex = ThreadLocalRandom.current().nextInt(0, philosophers.length);
        Philosopher philosopherToMove = philosophers[randomIndex];
        
        System.out.println("Philosopher " + philosopherToMove.getId() + " is moving to the 6th table.");
        
        // Move the philosopher to the 6th table and reassign them accordingly
        // ...
    }
}
