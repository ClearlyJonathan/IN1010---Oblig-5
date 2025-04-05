import java.util.concurrent.CountDownLatch;

public class Lesetråd implements Runnable
{
    private final String filnavn;
    private final Monitor monitor;
    private final CountDownLatch countdown;

    public Lesetråd(String filnavn, Monitor monitor, CountDownLatch c)
    {
        this.filnavn = filnavn;
        this.monitor = monitor;
        this.countdown = c;
    }
    

    @Override
    public void run()
    {
        monitor.settInn(Monitor.les(filnavn));
        countdown.countDown();
    }
}
