package RiskStuff;
import java.util.ArrayList;

public class Section
{
    String name;
    ArrayList<Territory> territories;
    int value;
    public Section(String s, int v)
    {
        name=s;
        value=v;
        territories=new ArrayList<Territory>();
    }

    public void addTerritory(Territory t)
    {
        territories.add(t);
    }
    
    public String toString()
    {
        return name+": "+value;
    }
    
    public int getVal()
    {
        return value;
    }
    
    public String tList()
    {
        return territories.toString();
    }
}
