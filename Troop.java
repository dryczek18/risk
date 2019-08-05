package RiskStuff;
public class Troop
{
    private String team,location;
    private int size;
    public Troop(String t, String loc, int s)
    {
        setTeam(t);
        setLoc(loc);
        setSize(s);
    }

    public Troop(int s)
    {
        setSize(s);
    }

    public int getSize()
    {
        return size;
    }

    public void setTeam(String col)
    {
        String teamColors="red blue green black magenta";// yellow brown
        if(teamColors.indexOf(col)>-1&&col.length()>2)
            team=col;
        else
            team="None";
    }

    public void setSize(int s)
    {
        size=s;
    }

    public void setLoc(String loc)//check with real locations
    {
        location=loc;
    }

    public String toString()
    {
        return "Team: "+team+" | Location: "+location+" | Size: "+size;
    }

    public String team()
    {
        return team;
    }

    public int size()
    {
        return size;
    }

    public String loc()
    {
        return location;
    }
}