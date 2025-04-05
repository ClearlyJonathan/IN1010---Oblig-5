import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;


public class Monitor
{
    private final Subsekvensregister register;
    private Lock lock;
    private Condition enoughFrekvenstabell;

    public Monitor()
    {
        register = new Subsekvensregister();
        lock = new ReentrantLock();
        enoughFrekvenstabell = lock.newCondition();
    }

    public static Frekvenstabell les(String filnavn)  
    {
        return Subsekvensregister.les(filnavn);
    }

    // Modified to be thread safe
    public void settInn(Frekvenstabell f)
    {
        lock.lock();
        try
        {
            register.settInn(f);
        }
        finally
        {
            lock.unlock();
        }

    }

    // Modified to be thread safe
    public Frekvenstabell taUt()
    {
        lock.lock();
        try
        {
            return register.taUt();
        }
        finally
        {
            lock.unlock();
        }
    }

    // Takes two but returns them as one after performing fletting
    public void taUtTo()
    {
        lock.lock();
        try
        {
            while (antall() < 2)
            {
                System.out.println("Waiting for threads");
                enoughFrekvenstabell.await();
            }


            Frekvenstabell t1, t2, t3;
            t1 = taUt();
            t2 = taUt();
            t3 = Frekvenstabell.flett(t1, t2);
            settInn(t3);

            if (antall() == 1)
            {
                System.out.println("Finished fletting");
            }
            if (antall() >= 2)
            {
                enoughFrekvenstabell.signal();
            }

        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            lock.unlock();
        }
        
    }

    public int antall()
    {
        lock.lock();
        try
        {
            return register.antall();
        }
        finally
        {
            lock.unlock();
        }
    }
}
