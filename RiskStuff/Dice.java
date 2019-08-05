package RiskStuff;
import java.awt.*;
import java.applet.Applet;

public class Dice
{
    private int diceNum;    
    public Dice()
    {
        diceNum=1;        
    }
    
    public int roll()
    {
        diceNum=(int)(Math.random()*6)+1;
        return diceNum;
    }
    
    public Image image(Applet app)
    {        
        return app.getImage(app.getCodeBase(),"dice"+diceNum+".png");        
    } 
}
