package RiskStuff;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;

public class FAKE extends JApplet implements MouseListener, ActionListener
{    
    ArrayList<Player> players;
    ArrayList<Territory> territories;
    ArrayList<Section> realms;
    int numPlayers;    
    String s;
    Image map;
    Territory terrInvaded=new Territory("Blank","Blank","Blank");

    Graphics bg;    
    Image buffer;

    Territory terrSelected=new Territory("Blank","Blank","Blank");//user clicks on country

    Player turnP;
    int turnIndex=0;

    int mouseX=0,mouseY=0;
    boolean canSelect=true;

    Dice attack=new Dice();
    Dice defense=new Dice();

    ArrayList<Integer> attackNum;//sort greatest to least after roll all dice in list;

    Button attackBtn=new Button("ATTACK");
    Button moveBtn=new Button("MOVE TROOPS");
    Button endTurnBtn=new Button("END TURN");
    Button nextBtn=new Button("Next");
    //Button cardBtn=new Button("CARD");
    boolean attacking=false,moving=false;
    boolean movedOnce=false;

    Font theFont=new Font("Brush Script MT",1,25);
    public void init()
    {  
        this.setLayout(null);

        attackNum=new ArrayList<Integer>();     

        attackBtn.setBounds(1240,490,110,55);
        attackBtn.addActionListener(this);        
        attackBtn.setForeground(Color.white);
        attackBtn.setFont(theFont);
        this.add(attackBtn);
        attackBtn.setVisible(false);

        moveBtn.setBounds(1240,550,110,55);
        moveBtn.addActionListener(this);        
        moveBtn.setForeground(Color.white);
        moveBtn.setFont(new Font("Brush Script MT",1,14));
        this.add(moveBtn);
        moveBtn.setVisible(false);

        endTurnBtn.setBounds(1240,610,110,55);
        endTurnBtn.addActionListener(this);        
        endTurnBtn.setForeground(Color.white);
        endTurnBtn.setFont(new Font("Brush Script MT",1,16));
        this.add(endTurnBtn);
        endTurnBtn.setVisible(false);

        nextBtn.setBounds(50,480,110,55);
        nextBtn.addActionListener(this);        
        nextBtn.setForeground(Color.white);
        nextBtn.setFont(new Font("Brush Script MT",1,20));
        this.add(nextBtn);
        nextBtn.setVisible(false);

        players=new ArrayList<Player>();
        players.add(new Player("red"));
        players.add(new Player("blue"));
        players.add(new Player("green"));
        players.add(new Player("black"));        

        realms=new ArrayList<Section>();
        realms.add(new Section("Midgard",5));//humans
        realms.add(new Section("Asgard",8));//gods
        realms.add(new Section("Alfheimr",5));//dark elves
        realms.add(new Section("Jotunheimr",4));//giants
        realms.add(new Section("Nidavellir",3));//dwarves
        realms.add(new Section("Bridge",1));

        territories=new ArrayList<Territory>();
        territories.add(new Territory("M_Left","Midgard","")); 
        territories.add(new Territory("M_Right","Midgard",""));
        territories.add(new Territory("M_Top","Midgard",""));
        territories.add(new Territory("M_Bottom","Midgard",""));
        territories.add(new Territory("M_CenterL","Midgard",""));
        territories.add(new Territory("M_CenterR","Midgard",""));
        troopSpot(0,528,348);
        troopSpot(1,799,315);
        troopSpot(2,640,200);
        troopSpot(3,633,490);
        troopSpot(4,613,336); 
        troopSpot(5,712,336); 

        territories.add(new Territory("Bridge 1","Bridge",""));
        territories.add(new Territory("Bridge 2","Bridge",""));        
        territories.add(new Territory("Bridge 3","Bridge",""));
        territories.add(new Territory("Bridge 4","Bridge",""));
        territories.add(new Territory("Bridge 5","Bridge",""));
        territories.add(new Territory("Bridge 6","Bridge",""));
        troopSpot(6,638,111);
        troopSpot(7,443,259);        
        troopSpot(8,552,595);
        troopSpot(9,849,434);
        troopSpot(10,902,330);
        troopSpot(11,191,670);

        territories.add(new Territory("N_Center","Nidavellir",""));
        territories.add(new Territory("N_Circle","Nidavellir",""));
        territories.add(new Territory("N_Border","Nidavellir",""));
        troopSpot(12,1029,492);
        troopSpot(13,946,427);
        troopSpot(14,888,494);

        territories.add(new Territory("J_Bolt","Jotunheimr",""));
        territories.add(new Territory("J_Star","Jotunheimr",""));
        territories.add(new Territory("J_Left","Jotunheimr",""));
        territories.add(new Territory("J_Right","Jotunheimr",""));
        troopSpot(15,306,506);
        troopSpot(16,395,489);
        troopSpot(17,238,578);
        troopSpot(18,350,428);

        territories.add(new Territory("Al_1C","Alfheimr","")); 
        territories.add(new Territory("Al_2","Alfheimr",""));
        territories.add(new Territory("Al_3","Alfheimr",""));
        territories.add(new Territory("Al_4","Alfheimr",""));
        territories.add(new Territory("Al_5","Alfheimr",""));
        troopSpot(19,205,169);
        troopSpot(20,166,170);
        troopSpot(21,274,170);
        troopSpot(22,108,170);
        troopSpot(23,193,304);   

        territories.add(new Territory("As_Center","Asgard",""));
        territories.add(new Territory("As_Left Border","Asgard",""));
        territories.add(new Territory("As_Bottom Border","Asgard",""));
        territories.add(new Territory("As_Top Border","Asgard",""));
        territories.add(new Territory("As_Top","Asgard",""));
        territories.add(new Territory("As_Left","Asgard",""));
        territories.add(new Territory("As_Bottom Right","Asgard",""));
        territories.add(new Territory("As_Mid Right","Asgard",""));
        troopSpot(24,1085,165);
        troopSpot(25,963,123);
        troopSpot(26,1086,315);
        troopSpot(27,1168,86);
        troopSpot(28,1064,63);
        troopSpot(29,1014,201);
        troopSpot(30,1184,233);
        troopSpot(31,1190,156);

        makeSurroundings();
        shuffle();        

        map=this.getImage(this.getCodeBase(),"map of riskie.png");       

        resize(1400,750);
        buffer=createImage(this.getWidth(),this.getHeight());
        bg=buffer.getGraphics(); 
        addMouseListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==attackBtn)
        {
            attacking=true;
            canSelect=false;

            attackBtn.setEnabled(false);
            moveBtn.setEnabled(false);
            endTurnBtn.setEnabled(false);
            nextBtn.setVisible(true);

            attackNum.add(attack.roll());
        }

        if(e.getSource()==moveBtn)
        {  
            moving=true;
            canSelect=false;

            attackBtn.setVisible(false);
            moveBtn.setEnabled(false);            
            endTurnBtn.setEnabled(false);
            nextBtn.setVisible(true);

            movedOnce=true;
        }

        if(e.getSource()==endTurnBtn)
        {
            attacking=false;
            moving=false;
            attackBtn.setVisible(true);

            movedOnce=false;
            nextTurn();            
        }

        if(e.getSource()==nextBtn)
        {
            attacking=false;
            moving=false;

            attackBtn.setEnabled(true);
            moveBtn.setEnabled(true);            
            endTurnBtn.setEnabled(true);
            nextBtn.setVisible(false);
            
            terrInvaded=new Territory("Blank","Blank","Blank");

            canSelect=true;
        }
        repaint();
    }

    public void makeSurroundings()
    {
        Territory[] M_lef={territories.get(2),territories.get(3),territories.get(4),territories.get(7)};
        territories.get(0).surroundCountries(M_lef);        

        Territory[] M_rig={territories.get(2),territories.get(3),territories.get(5),territories.get(9),territories.get(10)};
        territories.get(1).surroundCountries(M_rig);

        Territory[] M_top={territories.get(0),territories.get(1),territories.get(4),territories.get(5)};
        territories.get(2).surroundCountries(M_top);        

        Territory[] M_bot={territories.get(0),territories.get(1),territories.get(4),territories.get(5),territories.get(8)};
        territories.get(3).surroundCountries(M_bot);

        Territory[] M_cenL={territories.get(0),territories.get(2),territories.get(3),territories.get(5)};
        territories.get(4).surroundCountries(M_cenL);

        Territory[] M_cenR={territories.get(1),territories.get(2),territories.get(3),territories.get(4)};
        territories.get(5).surroundCountries(M_cenR);

        Territory[] B1={territories.get(23),territories.get(25)};
        territories.get(6).surroundCountries(B1);

        Territory[] B2={territories.get(23),territories.get(0)};
        territories.get(7).surroundCountries(B2);

        Territory[] B3={territories.get(3),territories.get(18)};
        territories.get(8).surroundCountries(B3);

        Territory[] B4={territories.get(1),territories.get(14)};
        territories.get(9).surroundCountries(B4);

        Territory[] B5={territories.get(1),territories.get(26)};
        territories.get(10).surroundCountries(B5);

        Territory[] B6={territories.get(17),territories.get(27)};
        territories.get(11).surroundCountries(B6);

        Territory[] nCen={territories.get(13)};
        territories.get(12).surroundCountries(nCen);

        Territory[] nCirc={territories.get(12),territories.get(14)};
        territories.get(13).surroundCountries(nCirc);

        Territory[] nBor={territories.get(9),territories.get(13)};
        territories.get(14).surroundCountries(nBor);

        Territory[] jBolt={territories.get(17),territories.get(18)};
        territories.get(15).surroundCountries(jBolt);

        Territory[] jStar={territories.get(18)};
        territories.get(16).surroundCountries(jStar);

        Territory[] jLeft={territories.get(11),territories.get(15)};
        territories.get(17).surroundCountries(jLeft);

        Territory[] jRig={territories.get(8),territories.get(15),territories.get(16)};
        territories.get(18).surroundCountries(jRig);

        Territory[] alC={territories.get(20)};
        territories.get(19).surroundCountries(alC);

        Territory[] al2={territories.get(19),territories.get(21)};
        territories.get(20).surroundCountries(al2);

        Territory[] al3={territories.get(20),territories.get(22)};
        territories.get(21).surroundCountries(al3);

        Territory[] al4={territories.get(21),territories.get(23)};
        territories.get(22).surroundCountries(al4);

        Territory[] al5={territories.get(22),territories.get(7),territories.get(6)};
        territories.get(23).surroundCountries(al5);

        Territory[] asCen={territories.get(28),territories.get(29),territories.get(30),territories.get(31)};
        territories.get(24).surroundCountries(asCen);

        Territory[] asLeftB={territories.get(6),territories.get(26),territories.get(28),territories.get(29)};
        territories.get(25).surroundCountries(asLeftB);

        Territory[] asBotB={territories.get(10),territories.get(25),territories.get(29),territories.get(30)};
        territories.get(26).surroundCountries(asBotB);

        Territory[] asTopB={territories.get(28),territories.get(31),territories.get(11)};
        territories.get(27).surroundCountries(asTopB);

        Territory[] asTop={territories.get(24),territories.get(25),territories.get(27)};
        territories.get(28).surroundCountries(asTop);

        Territory[] asLef={territories.get(24),territories.get(25),territories.get(26)};
        territories.get(29).surroundCountries(asLef);

        Territory[] asBotRig={territories.get(24),territories.get(26),territories.get(31)};
        territories.get(30).surroundCountries(asBotRig);

        Territory[] asMidRig={territories.get(24),territories.get(27),territories.get(30)};
        territories.get(31).surroundCountries(asMidRig);
    }

    public Color color(Territory t)
    {
        String str=t.getOwner();
        if(str.equals("red"))
            return Color.red;
        else if(str.equals("blue"))
            return Color.blue;
        else if(str.equals("black"))
            return Color.black;  
        else if(str.equals("green"))
            return Color.green;        
        return Color.pink;
    }

    public Color color(Player p)
    {
        String str=p.getColor();
        if(str.equals("red"))
            return Color.red;
        else if(str.equals("blue"))
            return Color.blue;
        else if(str.equals("green"))
            return Color.green;        
        return Color.black;
    }

    public void troopSpot(int index, int x, int y)//location of box for numbers
    {
        territories.get(index).setSpot(x,y);
    }

    public void shuffle()
    {
        getNumPlayers();

        int currentTerritory=0;
        int tPerP=(int)(territories.size()/numPlayers);
        int end=tPerP;

        //randomize territories
        for(int i=0; i<territories.size(); i++)
        {
            int randPlace = (int)(Math.random()*territories.size()); 
            Territory temp = territories.set(i, territories.get(randPlace));   // Swap cards
            territories.set(randPlace,temp);                 
        }        

        //set territories to each player
        for(int p=0;p<numPlayers;p++)
        {            
            for(int i=currentTerritory; i<end; i++)
            {
                players.get(p).addTerritory(territories.get(i));                
            }
            currentTerritory=end;
            if(end+tPerP<=territories.size())
                end+=tPerP;
        }        

        //sets random troop on spot
        for(Territory t:territories)
        {
            int val=1;
            double rand=Math.random();
            if(rand<0.4)
                val=1;
            else if(rand<0.8)
                val=3;
            else
                val=5;
            Troop test=new Troop(val);
            for(Player p:players)//set troop on territory to owner of player
            {
                if(p.getColor().equals(t.getOwner()))
                    p.addTroop(test);
            }
            t.addTroop(test);
        }        
    } 

    public ArrayList<Troop> condense(Territory t)
    {
        int troopVal=0;
        int val5=0,val3=0,val1=0;
        ArrayList<Troop> armyOnLand=new ArrayList<Troop>();
        for(int x=0;x<t.troopAmount();x++)
        {            
            troopVal+=t.getTroop(x).getSize();
        }

        int troopTest=5;
        while(troopVal>0)
        {
            if(troopVal/troopTest>0)
            {
                val5=troopVal/troopTest;
            }
            troopVal=troopVal%troopTest;   

            if(troopTest==5)
                troopTest=3;
            else
                troopTest=1;
        }

        System.out.print("5: "+val5+" 3: "+val3+" 1: "+val1);//create new Troops and set into arraylist
        /*for(int f=0;f<val5;f++)
        {
        armyOnLand.add(new Troop(t.getOwner(),t.getName(),5));
        t.addTroop(armyOnLand.get(f));
        for(Territory n:territories)
        {
        for(Player p:players)//set troop on territory to owner of player
        {
        if(p.getColor().equals(n.getOwner()))
        {
        n.removeAllTroops();
        n.addTroop(armyOnLand.get(f));  
        }
        }
        }
        }
        for(int th=0;th<val3;th++)
        {
        armyOnLand.add(new Troop(t.getOwner(),t.getName(),5));
        t.addTroop(armyOnLand.get(th));
        for(Territory t:territories)
        {
        for(Player p:players)//set troop on territory to owner of player
        {
        if(p.getColor().equals(t.getOwner()))
        {
        t.removeAllTroops();
        t.addTroop(armyOnLand.get(th));  
        }
        }
        }
        }*/

        return armyOnLand;
    }

    public void giveTroops(Player p)
    {
        int numTroops=(int)(p.territories()/3);
        if(numTroops<3)
            numTroops=3;

        while(numTroops>0)
        {
            p.addTroop(new Troop(p.getColor(),"Off Board",1));
            numTroops--;
        }

        System.out.print(p.getTroops());
    }

    public void getNumPlayers()
    {
        numPlayers=4;        
    }

    /*public void setPlayerTroops()
    {
    for(Territory t:territories)
    {
    for(Player p:players)//set troop on territory to owner of player
    {
    if(p.getColor().equals(t.getOwner()))
    p.addTroop(new Troop(p.getColor(),t.getName(),);
    }
    }
    }*/

    public void start()
    {
        // provide any code requred to run each time 
        // web page is visited
    }

    public void stop()
    {
        // provide any code that needs to be run when page
        // is replaced by another page or before JApplet is destroyed 
    }

    public void paint(Graphics g)
    { 
        turnP=players.get(turnIndex);
        bg.setColor(Color.black);
        bg.fillRect(0, 0, this.getWidth(), this.getHeight()); 
        //bg.setColor(Color.white);
        bg.drawImage(map,0, 0, this.getWidth(), this.getHeight(),this);
        /*for(int p=0;p<numPlayers;p++)
        {
        bg.drawString(players.get(p)+":       "+players.get(p).getTroops()+"   |   "+players.get(p).troopVal(),10,10+20*p);
        }*/

        //bg.drawString("N ("+mouseX+","+mouseY+")",mouseX,mouseY);
        bg.setFont(theFont);

        attackBtn.setBackground(color(turnP));
        moveBtn.setBackground(color(turnP));
        endTurnBtn.setBackground(color(turnP));
        nextBtn.setBackground(color(turnP));
        for(Territory t:territories)
        {
            bg.setColor(Color.gray);
            bg.fillRect(t.getX()-5,t.getY()-20,25,25); 
        }

        for(Territory t:territories)
        {
            if(mouseX>t.getX()-5&&mouseX<t.getX()+20&&mouseY>t.getY()-20&&mouseY<t.getY()+5)//check user territory on click
            {
                //player turn check 
                
                 /*if(moving)
                    if(color(t).equals(color(turnP)))
                        terrSelected=t;*/

                if(color(t).equals(color(turnP))&&canSelect)
                {
                    terrSelected=t; 

                    for(int i=0;i<t.numOfSurroundings();i++)
                    {                        
                        Territory surround=t.surroundingTerr(i);                                                  
                        bg.setColor(Color.white);
                        bg.fillOval(surround.getX()-6,surround.getY()-21,26,26);

                        bg.setColor(color(surround));
                        bg.drawString(surround.troopVal()+"",surround.getX(),surround.getY());
                    }
                    if(!movedOnce)
                        attackBtn.setVisible(true);
                    moveBtn.setVisible(true);
                    endTurnBtn.setVisible(true);
                }
                else if(attacking)
                {
                    if(!color(terrInvaded).equals(color(turnP)))
                    {                        
                        for(int i=0;i<t.numOfSurroundings();i++)
                        {                        
                            Territory surround=terrSelected.surroundingTerr(i);
                            if(!color(t).equals(color(turnP))&&t.equals(surround))
                            {                     
                                terrInvaded=t;
                                attacking=false;
                                bg.setColor(Color.red);
                                bg.fillOval(t.getX()-6,t.getY()-21,26,26);
                                //dice appears
                            }
                            if(!color(surround).equals(color(turnP))&&attacking)//initial attack
                            {
                                bg.setColor(Color.orange);
                                bg.fillOval(surround.getX()-6,surround.getY()-21,26,26);

                                bg.setColor(color(surround));
                                bg.drawString(surround.troopVal()+"",surround.getX(),surround.getY());
                            }

                        }
                    }                    
                }
                else if(moving)
                {
                    for(int i=0;i<t.numOfSurroundings();i++)
                    {                        
                        Territory surround=t.surroundingTerr(i);
                        if (color(surround).equals(color(turnP)))
                        {
                            bg.setColor(Color.cyan);
                            bg.fillOval(surround.getX()-6,surround.getY()-21,26,26);

                            /*bg.setColor(color(surround));
                            bg.drawString(surround.troopVal()+"",surround.getX(),surround.getY());*/
                        }
                    }
                }
                else//if wrong turn's country
                {
                    terrSelected=new Territory("Blank","Blank","Blank");
                    attackBtn.setVisible(false);
                    moveBtn.setVisible(false);
                    endTurnBtn.setVisible(false);
                }                

                /*else//if not on any country
                {
                attackBtn.setVisible(false);
                moveBtn.setVisible(false);
                endTurnBtn.setVisible(false);
                }*/
            } 
        }

        for(Territory t:territories)
        {
            if(t.equals(terrInvaded))
            {
                bg.setColor(Color.red);
                bg.fillOval(t.getX()-6,t.getY()-21,26,26);
            }
            if(t.equals(terrSelected))
            {
                bg.setColor(Color.yellow);
                bg.fillOval(t.getX()-6,t.getY()-21,26,26);
            }
            bg.setColor(color(t));
            bg.drawString(t.troopVal()+"",t.getX(),t.getY());
        }        

        if(color(turnP).equals(Color.black))//dice
            bg.setColor(Color.white);
        else
            bg.setColor(color(turnP));
        bg.fillRect(50,550,80,80);
        bg.drawImage(attack.image(this),50,550,80,80,this);

        g.drawImage(buffer,0,0,this);
    }    

    public void mouseClicked(MouseEvent e)
    {        
        mouseX=e.getX();
        mouseY=e.getY();        
        repaint();
    }

    public void mouseExited(MouseEvent e)
    {

    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mousePressed(MouseEvent e)
    {

    }

    public void mouseReleased(MouseEvent e)
    {

    }

    public void nextTurn()
    {
        if(turnIndex<3)
            turnIndex++;
        else
            turnIndex=0;
    }

    public void update(Graphics g){
        paint(g);
    }
}
