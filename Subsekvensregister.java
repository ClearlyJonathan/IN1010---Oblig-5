import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Subsekvensregister 
{
    private final ArrayList<Frekvenstabell> register = new ArrayList<Frekvenstabell>(); 
    private static final int SUBSEKVENSLENGDE = 3;



    //Improve subsekvens reading
    public static Frekvenstabell les(String filnavn)
    {
        Frekvenstabell f1 = new Frekvenstabell();

        try
        {
            Scanner scanner = new Scanner(new File(filnavn));
            String line = "";
            // Read all the lines and put it in a single line
            while (scanner.hasNext())
            {
                line = scanner.nextLine().trim();

                 // We assuming that the data line is always going to be ABSHCDASKAS formatted
                String[] data = line.split("");
                String subsekvens;
                int index = 0;


                // Create subsequence as long as there's enough characters.
                while (index + SUBSEKVENSLENGDE <= data.length)
                {
                    subsekvens = data[index] + data[index + 1] + data[index + 2];
                    f1.put(subsekvens, 1);
                    index++;
                }
            }

           
            scanner.close();
            return f1;
        } catch (FileNotFoundException e)
        {
            System.out.println("File not found");
            e.printStackTrace();
        }
        System.out.println("Something went wrong in subsekvensregister");
        return null;
    }

    public void settInn(Frekvenstabell f)
    {
        if (f == null) 
        {
            System.out.println("Attempted to add a null value");  
            return;
        }
        register.add(f);
    }

    public Frekvenstabell taUt()
    {
        if (antall() == 0) 
        {
            System.out.println("No more elements can be taken out");
            return null;
        }

        Frekvenstabell f = register.remove(antall() - 1);
        if (f == null)
        {
            System.out.println("Attempted to return an empty object");
            return null;
        }
        return f;
    }

    public int antall()
    {
        return register.size();
    }
}
