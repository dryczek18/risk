//Daniel Ryczek
//Player class
package RiskStuff;
import java.util.*;
import java.util.ArrayList;
public class Player
{
    private String color;
    private ArrayList<Troop> army; 
    private ArrayList<Territory> fiefs;
    
    public Player(String inColor)
    {
        color=inColor;
        army= new ArrayList<Troop>();
        fiefs= new ArrayList<Territory>();
    }
    
    public String getColor()
    {
        return color;
    }
    
    public int troopVal()
    {
        int troopVal=0;
        for(int x=0;x<army.size();x++)
        {            
            troopVal+=army.get(x).getSize();
        }
        return troopVal;
    }
    
    public void addTroop(Troop t)
    {
        t.setTeam(color);
        army.add(t);
    }
    
    public Troop getTroop(int i)
    {
        return army.get(i);
    }
    //Decide to use object or int
    public boolean removeTroop(Troop t)
    {
        return army.remove(t);
    }
    
    public Troop removeTroop(int i)
    {
        return army.remove(i);
    }   
    
    public Territory getTerritory(int i)
    {
        return fiefs.get(i);
    }
    
    public String getTerritories()
    {
        String land="";
        for(Territory t:fiefs)
        {
            land+=t.getName()+" "+army;
        }
        return land;
    }
    
    public String getTroops()
    {
        String troops="";
        for(Troop t:army)
        {
            troops+=t+"  |  ";
        }
        return troops;
    }
    
    public void addTerritory(Territory t)
    {
        t.setOwner(color);
        fiefs.add(t);
    }
    
    public int territories()
    {
        return fiefs.size();
    }
    
    public boolean removeTerritory(Territory t)
    {
        return fiefs.remove(t);
    }
    
    public String toString()
    {
        return ("Player: "+color+" Territory#: "+fiefs.size());
    }
}

