import java.util.concurrent.CountDownLatch;

public class Flettetråd implements Runnable
{
    private final Monitor monitor;
    private final CountDownLatch countdown;

    public Flettetråd(Monitor m, CountDownLatch countdown)
    {
        monitor = m;
        this.countdown = countdown;
    }

    @Override
    public void run()
    {
        while(monitor.antall() > 1)
        {
            monitor.taUtTo();
        }
        countdown.countDown();
    }

    
}
