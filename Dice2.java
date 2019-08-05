package RiskStuff;
import java.awt.*;
import javax.swing.*;

public class Dice2
{
    private int diceNum;    
    public Dice2()
    {
        diceNum=1;        
    }
    
    public int roll()
    {
        diceNum=(int)(Math.random()*6)+1;
        return diceNum;
    }
    
    public Image image()
    {        
        return new ImageIcon("dice"+diceNum+".png").getImage();        
    } 
}
