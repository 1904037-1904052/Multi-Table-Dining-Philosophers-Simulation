import java.util.concurrent.locks.ReentrantLock;

class Fork {
    private final ReentrantLock lock = new ReentrantLock();
    
    public boolean pickUp() {
        return lock.tryLock();
    }
    
    public void putDown() {
        lock.unlock();
    }
}
