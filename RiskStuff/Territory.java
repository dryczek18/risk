package RiskStuff;
import java.util.ArrayList;

public class Territory
{
    private String name;
    private String section;
    private String owner;
    private ArrayList<Territory> surroundings;    
    private ArrayList<Troop> troops;
    private int X,Y;
    public Territory(String n,String s,String p)
    {
        setName(n);
        setSec(s);
        setOwner(p);
        troops=new ArrayList<Troop>();
        surroundings=new ArrayList<Territory>();
        X=50;
        Y=50;
    }
    
    public boolean equals(Territory t)
    {
        if(name.equals(t.getName()))
        return true;
        return false;
    }
    
    public String surroundings()
    {
        return surroundings.toString();
    }
    
    public int numOfSurroundings()
    {
        return surroundings.size();
    }
    
    public Territory surroundingTerr(int i)
    {
        return surroundings.get(i);
    }
    
    public void surroundCountries(Territory[] tList)
    {        
        for(Territory t:tList)
        surroundings.add(t);
    }
    
    public void setSpot(int inX,int inY)
    {
        X=inX;
        Y=inY;
    }
    
    public int getX(){return X;}
    public int getY(){return Y;}
    
    public String getOwner()
    {
        if(owner==null)
        return "Empty";
        return owner;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String toString()
    {
        return name;
    }
    
    public String allInfo()
    {
        return "Name: "+name+" | Section: "+section+" | Owner: "+owner+" | Troops: "+troops;
    }
    
    public void setName(String n)
    {
        name=n;
    }
    
    public void setOwner(String p)
    {
        String teamColors="red blue green black magenta";// yellow brown
        if(teamColors.indexOf(p)>-1&&p.length()>2)
            owner=p;
        else
            owner="Empty";
        }
    
    public void setSec(String s)
    {
        section=s;
    }
    
    public String getSec()
    {
        return section;
    }
    
    public void setTroops(ArrayList<Troop> army)
    {
        for(int i=0;i<army.size();i++)
        troops.add(army.get(i));
    }
    
    public Troop getTroop(int i)
    {
        return troops.get(i);
    }
    
    public boolean addTroop(Troop t)
    {
        t.setLoc(name);
        t.setTeam(owner);
        troops.add(t);
        
        return true;
    }
    
    public boolean removeTroop(Troop t)
    {
        troops.remove(t);
        return true;
    }
    
    public Troop removeTroop(int i)
    {
        return troops.remove(i);
    }
    
    public void removeAllTroops()
    {
        for(int i=0;i<troops.size();i++)
        troops.remove(i);
    }
    
    public Troop setTroop(int index,int size)
    {
        Troop holder=troops.get(index);
        troops.set(index,new Troop(size));
        return holder;
    }
    
    public int troopAmount()
    {
        return troops.size();
    }
    
    public int troopVal()
    {
        int troopVal=0;
        for(int x=0;x<troopAmount();x++)
        {            
            troopVal+=troops.get(x).getSize();
        }
        return troopVal;
    }
    
    public boolean noTroops()
    {
        return troops.isEmpty();
    }
}


