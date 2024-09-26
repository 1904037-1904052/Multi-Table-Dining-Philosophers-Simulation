import java.util.concurrent.locks.ReentrantLock;

class Fork {
    private final ReentrantLock lock = new ReentrantLock();

    public boolean pickUp(char c, int temp) {
        return lock.tryLock();
    }
    
    public void putDown() {
        if (lock.isHeldByCurrentThread()) {  // Ensure that the lock is held by the current thread before unlocking
            lock.unlock();
        }
    }
}
