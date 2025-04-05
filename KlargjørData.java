import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;



public class KlargjørData
{

    private final static int ANTALL_TRÅDER = 8;
    public static void main(String[] args)
    {

        if (args.length == 0)
        {
            System.out.println("You've forgot to input an argument 'Data/metadata.csv'");
            System.exit(-1);
        }

        File metadata = new File(args[0]);
        File directory = metadata.getParentFile();

        if (directory == null)
        {
            System.out.println("Invalid directory, expected TestData/metadata.csv");
        }

        // Monitor creation

        Monitor smittMedCMV = new Monitor();
        Monitor ikkeSmittMedCMV = new Monitor();

        System.out.println("Creating reading threads...");
        createLeseTråd(metadata, directory, smittMedCMV, ikkeSmittMedCMV);

        System.out.println("Creating fletting threads");
        createFletteTråd(smittMedCMV, ikkeSmittMedCMV);

        Frekvenstabell smittMedCMVFrekvens = smittMedCMV.taUt();
        Frekvenstabell ikkeSmittMedCMVFrekvens = ikkeSmittMedCMV.taUt();

        smittMedCMVFrekvens.skrivTilFil("smittet");
        ikkeSmittMedCMVFrekvens.skrivTilFil("ikke_smittet");
        

        

        
        


        

    }

    static void createFletteTråd(Monitor smittMedCMV, Monitor ikkeSmittMedCMV)
    {
        Monitor[] monitors = {smittMedCMV, ikkeSmittMedCMV};
        CountDownLatch awaitForAllThreadsToBeProcessed = new CountDownLatch(ANTALL_TRÅDER);
        try
        {
            for (Monitor m : monitors)
            {
                for (int i = 0; i < ANTALL_TRÅDER; i++)
                {
                    new Thread(new Flettetråd(m, awaitForAllThreadsToBeProcessed)).start();
                }
            }
            System.out.println("Awaiting for data to be processed");
            awaitForAllThreadsToBeProcessed.await();
            System.out.println("All data has been processed");
        } catch (InterruptedException i)
        {
            i.printStackTrace();
        }
        
    }
    

    static void createLeseTråd(File metadata, File directory, Monitor smittMedCMV, Monitor ikkeSmittMedCMV)
    {
        int threadCount = 1;
        try
        {
            Scanner scanner = new Scanner(metadata);
            String f = String.format("%s/%s", directory.getName(), metadata.getName());
            CountDownLatch awaitForAllThreadsToBeRead = new CountDownLatch(getThreadCount(f));
            


            String fileName;
            String booleanValue;
    
            while (scanner.hasNext())
            {
                String line = scanner.nextLine().trim();
                fileName = line.split(",")[0].trim();
               
                String trueFilePath = String.format("%s/%s", directory, fileName);
                booleanValue = line.split(",")[1];
                threadCount++;
                if (isInfected(booleanValue))
                {
                    new Thread(new Lesetråd(trueFilePath, smittMedCMV, awaitForAllThreadsToBeRead)).start();
                }
                else
                {
                    new Thread(new Lesetråd(trueFilePath, ikkeSmittMedCMV, awaitForAllThreadsToBeRead)).start();
                }
                
            }

            

            System.out.println("Awaiting for data to be completely read");
            awaitForAllThreadsToBeRead.await();
            System.out.println("All data has been read");
            
        assert Thread.activeCount() == threadCount : "Expected " + Thread.activeCount() + " but got " + threadCount + " threads";

            scanner.close();
        } catch (FileNotFoundException f)
        {
            f.printStackTrace();
        } catch (InterruptedException i)
        {
            i.printStackTrace();
        }
    }

    static boolean isInfected(String s)
    {
        return (s.equals("True"));
    }

    static int getThreadCount(String f) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File(f));
        int index = 0;
        while (scanner.hasNext())
        {
            scanner.nextLine();
            index++;
        }
        
        scanner.close();
        return index;
    }

}