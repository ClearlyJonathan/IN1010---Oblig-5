import java.util.TreeMap;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Frekvenstabell extends TreeMap<String, Integer>
// String represents the subsekvens
// Integer represents the subsekvens
{

    public static Frekvenstabell flett(Frekvenstabell f1, Frekvenstabell f2)
    {
        Frekvenstabell flettet = new Frekvenstabell();
        flettet.putAll(f1);
        for (String key : f2.keySet())
        {
            // Adds +1 count in the value
            if (flettet.containsKey(key))
            {
                flettet.put(key, flettet.get(key) + 1);
            }
            else
            {
                flettet.put(key, 1);
            }
        }
        return flettet;
    }

    // Writes to file
    public void skrivTilFil(String filnavn)
    {
        File file = new File(filnavn);
        try
        {
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            if (!file.canWrite()) 
            {
                System.out.println("Unable to write file");
                fw.close();
                return;
            }
            fw.write(this.toString());
            fw.close();

        } catch (IOException e)
        {
            e.getCause();
        }

    }


    @Override
    public String toString()
    {
        String result = "";
        for (String key : this.keySet())
        {
            result += String.format("%s %d\n", key, get(key));

        }
        return result;
    }

    
}
