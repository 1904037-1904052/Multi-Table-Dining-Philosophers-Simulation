import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

class Fork {
    private final ReentrantLock lock = new ReentrantLock();

    public boolean pickUp() {
        try {
            return lock.tryLock(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    public void putDown() {
        lock.unlock();
    }
}
