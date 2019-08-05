package RiskStuff;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.image.*;
public class RiskV2 extends JFrame implements Runnable,MouseListener, ActionListener
{    
    ArrayList<Player> players;
    ArrayList<Territory> territories;
    ArrayList<Section> realms;
    String s;
    Image map,instr,textBox;
    Territory terrInvaded=new Territory("Blank","Blank","Blank");
    Territory terrMovingTo=new Territory("Blank","Blank","Blank");
    Territory terrSelected=new Territory("Blank","Blank","Blank");//user clicks on country

    Player turnP;
    int turnIndex=0;
    int turnCt=1;
    int mouseX=0,mouseY=0;
    boolean canSelect=true;

    Dice2 attack=new Dice2();
    Dice2 defense=new Dice2();

    Button done=new Button("DONE");
    Button addBtn=new Button("ADD");
    Button removeBtn=new Button("REMOVE");
    Button revoltBtn=new Button("OFF");
    Button attackBtn=new Button("ATTACK");
    Button moveBtn=new Button("MOVE TROOPS");
    Button endTurnBtn=new Button("END TURN");
    Button nextBtn=new Button("NEXT");
    Button instructionsBtn= new Button("INSTRUCTONS");
    Button toValBtn=new Button("VALHALA");/**initialize*/
    boolean attacking=false,moving=false,win=false,instructions=false;
    boolean movedOnce=false,fight=false,moveAfterFight=false,movedAttack=false;
    String out="",rMessage="";

    int troopsToPlace=3,playerCt=4,revModeCt=0;

    Font theFont=new Font("Brush Script MT",1,25);
    Thread main=new Thread(this);
    boolean inValhala=true,rightClick=false,leftClick=false,revoltWin=false, start=false,revoltMode=false, revoltModeHard=false;
    public static void main(String args[])
    {  
        new RiskV2().setVisible(true);
    }

    public void revolt()
    {
        ArrayList<Player> powerList=new ArrayList<Player>();//least to greatest of terr num
        int strength=0;
        for(int i=0;i<players.size();i++) 
            if(players.get(i).territories()>0)
                powerList.add(players.get(i));

        Player p,temp;        
        for(int i=0;i<powerList.size()-1;i++)
        {
            for(int j=i+1;j<powerList.size();j++)
            {
                p=powerList.get(i);
                temp=powerList.get(j);
                if(p.territories()>temp.territories())
                {
                    powerList.set(j,p);
                    powerList.set(i,temp);
                }
            }
        }

        int i=powerList.indexOf(turnP);
        if(Math.random()<=.10+i*.05)
        {     
            if(i>powerList.size()-3)
                strength=3;
            else
                strength=2;
            Territory terrRevolt=turnP.getTerritory((int)(Math.random())*turnP.territories());
            terrRevolt.setTroop(0,terrRevolt.troopVal()-strength);
            rMessage="REVOLT: "+revoltReasons()+terrRevolt.getSec();
            if(terrRevolt.troopVal()<1)
            {
                person(terrRevolt.getOwner()).removeTerritory(terrRevolt);
                terrRevolt.setOwner("pink");
                terrRevolt.setTroop(0,terrRevolt.troopVal()*-1);
            }
        }
        else
            rMessage="";
    }

    public void revoltHard()
    {
        ArrayList<Player> powerList=new ArrayList<Player>();//least to greatest of terr num
        int strength=0;
        for(int i=0;i<players.size();i++) 
            if(players.get(i).territories()>0)
                powerList.add(players.get(i));

        Player p,temp;        
        for(int i=0;i<powerList.size()-1;i++)
        {
            for(int j=i+1;j<powerList.size();j++)
            {
                p=powerList.get(i);
                temp=powerList.get(j);
                if(p.territories()>temp.territories())
                {
                    powerList.set(j,p);
                    powerList.set(i,temp);
                }
            }
        }

        int i=powerList.indexOf(turnP);
        if(Math.random()<=.35+i*.05)
        {            
            if(i>powerList.size()-3)
                strength=6;
            else
                strength=5;
            Territory terrRevolt=turnP.getTerritory((int)(Math.random())*turnP.territories());
            terrRevolt.setTroop(0,terrRevolt.troopVal()-strength);
            rMessage="REVOLT: "+revoltReasons()+terrRevolt.getSec();
            if(terrRevolt.troopVal()<1)
            {
                person(terrRevolt.getOwner()).removeTerritory(terrRevolt);
                terrRevolt.setOwner("pink");
                terrRevolt.setTroop(0,terrRevolt.troopVal()*-1);
            }
        }
        else
            rMessage="";
    }

    public Player person(String s)
    {
        for(Player p:players)
        {
            if(p.getColor().equals(s))
                return p;
        }
        return new Player("IT Broke");
    }

    public void run()
    {
        int delay=20;
        while(!win)
        {
            repaint();
            try
            {
                main.sleep(40);
            } 
            catch(Exception e){}
            if(start)
            {
                delay--;
                if(fight&&delay<0)
                {
                    delay=5;//20

                    if(terrSelected.troopVal()>1&&terrInvaded.troopVal()>0)
                    {                    
                        int a=attack.roll();
                        int d=defense.roll();
                        if(a>d)
                            terrInvaded.setTroop(0,terrInvaded.getTroop(0).getSize()-1);
                        else if(d>a)
                            terrSelected.setTroop(0,terrSelected.getTroop(0).getSize()-1);
                    }       
                    else
                    {
                        if(terrInvaded.troopAmount()>0&&terrInvaded.troopVal()==0)
                        {
                            person(terrInvaded.getOwner()).removeTerritory(terrInvaded);
                            //terrInvaded.setOwner(terrSelected.getOwner());
                            person(terrSelected.getOwner()).addTerritory(terrInvaded);

                            int newTroop=terrSelected.troopVal()-1;
                            terrSelected.setTroop(0,1);
                            if(terrInvaded.getTroop(0)==null)
                                terrInvaded.addTroop(new Troop(newTroop));                            
                            else
                                terrInvaded.setTroop(0,newTroop);
                        }
                        fight=false;
                        moveAfterFight=true;
                    }               
                }      
            }
        }
    }

    public String revoltReasons()
    {
        String[]response={"There was not enough food in ",
                "A tornado devastated people in ",
                "The laws seemed unjust in ",
                "Chickens went berserk in ",
                "There weren't enough jobs in ",
                "The Gods were displeased in ",
                "They didn't get good beard trims in ",
                "The people want action in ",
                "Too many poisonous plants in ",
                "Disease outbreak in "
            };

        int rand=(int)(Math.random()*response.length);
        return response[rand];
    }

    private RiskV2()
    {  
        super("RISK!");
        setSize(1400,750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null); 

        done.setBounds(540,510,110,55);
        done.addActionListener(this);        
        done.setForeground(Color.black);
        done.setFont(theFont);
        this.add(done);

        revoltBtn.setBounds(510,390,160,55);
        revoltBtn.addActionListener(this);        
        revoltBtn.setForeground(Color.black);
        revoltBtn.setFont(theFont);
        this.add(revoltBtn);

        addBtn.setBounds(690,300,110,55);
        addBtn.addActionListener(this);        
        addBtn.setForeground(Color.black);
        addBtn.setFont(theFont);
        this.add(addBtn);

        removeBtn.setBounds(390,300,110,55);
        removeBtn.addActionListener(this);        
        removeBtn.setForeground(Color.black);
        removeBtn.setFont(theFont);
        this.add(removeBtn);

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

        nextBtn.setBounds(1240,490,110,55);
        nextBtn.addActionListener(this);        
        nextBtn.setForeground(Color.white);
        nextBtn.setFont(new Font("Brush Script MT",1,20));
        this.add(nextBtn);
        nextBtn.setVisible(false);

        instructionsBtn.setBounds(0,0,110,55);
        instructionsBtn.addActionListener(this);        
        instructionsBtn.setForeground(Color.white);
        instructionsBtn.setBackground(Color.blue);
        instructionsBtn.setFont(new Font("Brush Script MT",1,14));
        this.add(instructionsBtn);
        instructionsBtn.setVisible(false);

        realms=new ArrayList<Section>();
        realms.add(new Section("Midgard",5));//humans
        realms.add(new Section("Asgard",7));//gods
        realms.add(new Section("Alfheimr",4));//dark elves
        realms.add(new Section("Jotunheimr",4));//giants
        realms.add(new Section("Nidavellir",3));//dwarves
        realms.add(new Section("Bridge",10));

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

        map=new ImageIcon("map of riskie.png").getImage();
        instr=new ImageIcon("instr.png").getImage();
        textBox=new ImageIcon("textbox of riskie 2.png").getImage();

        addMouseListener(this);
        main.start();
    }

    public void startGame()
    {
        players=new ArrayList<Player>();
        players.add(new Player("red"));
        players.add(new Player("blue"));
        if(playerCt>2)
            players.add(new Player("green"));
        if(playerCt>3)
            players.add(new Player("black"));
        if(playerCt>4)
            players.add(new Player("magneta"));

        makeSurroundings();
        shuffle();    

        turnP=players.get(turnIndex);
        start=true;
    }

    public void paint(Graphics bg)
    { 
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) 
        {
            createBufferStrategy(3);
            return;
        }
        bg = bs.getDrawGraphics();

        bg.setColor(Color.blue);
        bg.fillRect(0, 0, this.getWidth(), this.getHeight()); 

        if(!win&&start)
        {
            bg.drawImage(map,0, 0, this.getWidth(), this.getHeight(),this);
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
                    if(color(t).equals(color(turnP))&&canSelect)
                    {
                        terrSelected=t;
                        if(troopsToPlace==0)
                        {
                            for(int i=0;i<t.numOfSurroundings();i++)
                            {                        
                                Territory surround=t.surroundingTerr(i);                                                  
                                bg.setColor(Color.white);
                                bg.fillOval(surround.getX()-6,surround.getY()-21,26,26);

                                bg.setColor(color(surround));
                                bg.drawString(surround.troopVal()+"",surround.getX(),surround.getY());
                            }
                            if(!movedOnce)
                            {
                                attackBtn.setVisible(true);        
                                moveBtn.setVisible(true);
                            }
                        }
                        else
                        {
                            if(leftClick)
                            {
                                terrSelected.setTroop(0,terrSelected.troopVal()+1);
                                terrSelected=null;
                                mouseX=1000;
                                troopsToPlace--;
                            }
                            else if(rightClick)
                            {
                                terrSelected.setTroop(0,terrSelected.troopVal()+troopsToPlace);
                                terrSelected=null;
                                mouseX=1000;
                                troopsToPlace=0;
                            }
                        }
                    }
                    else if(attacking)
                    {
                        if(color(terrInvaded)!=null&& !color(terrInvaded).equals(color(turnP)))
                        {                        
                            for(int i=0;i<terrSelected.numOfSurroundings();i++)
                            {                        
                                Territory surround=terrSelected.surroundingTerr(i);
                                if(!color(surround).equals(color(turnP))&&attacking)//initial attack
                                {
                                    bg.setColor(Color.orange);
                                    bg.fillOval(surround.getX()-6,surround.getY()-21,26,26);

                                    bg.setColor(color(surround));
                                    bg.drawString(surround.troopVal()+"",surround.getX(),surround.getY());
                                }
                                if(!color(t).equals(color(turnP))&&t.equals(surround))
                                {                     
                                    terrInvaded=t; 
                                    fight=true;
                                    attacking=false;
                                }
                            }
                        }                    
                    }
                    else if(moving)
                    {                
                        for(int i=0;i<terrSelected.numOfSurroundings();i++)
                        {                        
                            Territory surround=terrSelected.surroundingTerr(i);
                            if (color(surround).equals(color(turnP)))
                            {
                                bg.setColor(Color.cyan);
                                bg.fillOval(surround.getX()-6,surround.getY()-21,26,26);
                            }
                            if (color(t).equals(color(turnP))&&t.equals(surround))
                            {
                                terrMovingTo=t;
                                moving=false;
                            }
                        }
                    }
                    else//if wrong turn's country
                    {                    
                        attackBtn.setVisible(false);
                        moveBtn.setVisible(false);
                    }
                    if(movedOnce&&t.equals(terrMovingTo)&&terrSelected.troopVal()>1)
                    {
                        if(leftClick)
                        {
                            terrMovingTo.setTroop(0,terrMovingTo.troopVal()+1);
                            terrSelected.setTroop(0,terrSelected.troopVal()-1);
                            mouseX=2000;
                        }
                        if(rightClick)
                        {
                            terrMovingTo.setTroop(0,terrMovingTo.troopVal()+terrSelected.troopVal()-1);
                            terrSelected.setTroop(0,1);
                            mouseX=2000;
                        }
                    }
                    if(movedOnce&&t.equals(terrSelected)&&terrMovingTo.troopVal()>1)
                    {
                        if(leftClick)
                        {
                            terrMovingTo.setTroop(0,terrMovingTo.troopVal()-1);
                            terrSelected.setTroop(0,terrSelected.troopVal()+1);
                            mouseX=2000;
                        }
                        if(rightClick)
                        {                            
                            terrSelected.setTroop(0,terrSelected.troopVal()+terrMovingTo.troopVal()-1);
                            terrMovingTo.setTroop(0,1);
                            mouseX=2000;
                        }
                    }

                    if(moveAfterFight&&terrInvaded.getOwner().equals(terrSelected.getOwner())&&t.equals(terrInvaded)&&terrSelected.troopVal()>1)
                    {
                        if(leftClick)
                        {
                            terrInvaded.setTroop(0,terrInvaded.troopVal()+1);
                            terrSelected.setTroop(0,terrSelected.troopVal()-1);
                            mouseX=2000;
                        }
                        if(rightClick)
                        {
                            terrInvaded.setTroop(0,terrInvaded.troopVal()+terrSelected.troopVal()-1);
                            terrSelected.setTroop(0,1);
                            mouseX=2000;
                        }
                    }
                    if(moveAfterFight&&terrInvaded.getOwner().equals(terrSelected.getOwner())&&t.equals(terrSelected)&&terrInvaded.troopVal()>1)
                    {
                        if(leftClick)
                        {
                            terrInvaded.setTroop(0,terrInvaded.troopVal()-1);
                            terrSelected.setTroop(0,terrSelected.troopVal()+1);
                            mouseX=2000;
                        }
                        if(rightClick)
                        {                            
                            terrSelected.setTroop(0,terrSelected.troopVal()+terrInvaded.troopVal()-1);
                            terrInvaded.setTroop(0,1);
                            mouseX=2000;
                        }
                    }
                } 
            }

            for(Territory t:territories)
            {
                if(t.equals(terrInvaded))
                {
                    bg.setColor(new Color(130,0,0));
                    bg.fillOval(t.getX()-6,t.getY()-21,26,26);
                }
                if(terrSelected!=null && t.equals(terrSelected))
                {
                    bg.setColor(Color.pink);
                    bg.fillOval(t.getX()-6,t.getY()-21,26,26);
                }
                if(t.equals(terrMovingTo))
                {
                    bg.setColor(new Color(0,128,192));
                    bg.fillOval(t.getX()-6,t.getY()-21,26,26);
                }
                bg.setColor(color(t));
                bg.drawString(t.troopVal()+"",t.getX(),t.getY());
            }        

            if(color(turnP).equals(Color.black))//Dice2
                bg.setColor(Color.white);
            else
                bg.setColor(color(turnP));
            bg.fillRect(1250,300,80,80);
            bg.drawImage(attack.image(),1250,300,80,80,this);

            if(color(terrInvaded).equals(Color.black))//Dice2
                bg.setColor(Color.white);
            else
                bg.setColor(color(terrInvaded));
            if(!color(terrInvaded).equals(Color.pink))
            {
                bg.fillRect(1250,400,80,80);
                bg.drawImage(defense.image(),1250,400,80,80,this);            
            }

            bg.setColor(Color.white);
            bg.drawString(out,50,50);

            bg.drawImage(textBox,0,370,230,220,this);
            bg.drawString("Troops left: "+troopsToPlace,20,400);  
            bg.drawString("Number of territories:",20,460);
            for(int i=0;i<players.size();i++)
                bg.drawString(players.get(i).getColor()+": "+players.get(i).territories(),30,480+i*20);

            if(rMessage.length()>0)
            {
                bg.drawImage(textBox,310,130,760,50,this);
                bg.drawString(rMessage,355,160);     
            }

            if(instructions)
                bg.drawImage(instr,385,0,630,750,this);
        }
        else if(revoltWin)
        {
            bg.setFont(new Font("Brush Script MT",1,26));
            bg.setColor(Color.pink);
            bg.fillRect(0, 0, this.getWidth(), this.getHeight());
            bg.setColor(Color.white);
            bg.drawString("The Revolution has plunged the realms into chaos.  Turns: "+turnCt,450,350);
        }
        else if(start)
        {
            if(!revoltModeHard)
            {
                bg.setFont(new Font("Brush Script MT",1,26));
                bg.setColor(color(turnP));
                bg.fillRect(0, 0, this.getWidth(), this.getHeight());
                bg.setColor(Color.white);
                bg.drawString(turnP.getColor()+" WINS!!!!  Turns: "+turnCt,450,350);
            }
            else
            {
                bg.setFont(new Font("Brush Script MT",1,26));
                bg.setColor(color(turnP));
                bg.fillRect(0, 0, this.getWidth(), this.getHeight());
                bg.setColor(Color.white);
                bg.drawString("You have survived the blood-thirsty revolt! Turns: "+turnCt,450,350);
            }
        }

        if(!start)
        {
            bg.setFont(theFont);
            bg.setColor(Color.white);
            bg.drawString("Players: "+playerCt,550,350);
            bg.drawString("Revolt Mode: ",540,400);
        }

        bs.show();
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
        else if(str.equals("magneta"))
            return Color.magenta;        
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
        else if(str.equals("magneta"))
            return Color.magenta;      
        return Color.black;
    }

    public void troopSpot(int index, int x, int y)//location of box for numbers
    {
        territories.get(index).setSpot(x,y);
    }

    public void shuffle()
    {
        int currentTerritory=0;
        int tPerP=(int)(territories.size()/playerCt);
        int end=tPerP;

        //randomize territories
        for(int i=0; i<territories.size(); i++)
        {
            int randPlace = (int)(Math.random()*territories.size()); 
            Territory temp = territories.set(i, territories.get(randPlace));   // Swap cards
            territories.set(randPlace,temp);                 
        }        

        //set territories to each player
        for(int p=0;p<playerCt;p++)
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
            for(Player p:players)//set troop on territory to owner of player
            {
                if(p.getColor().equals(t.getOwner()))
                    p.addTroop(new Troop(1));
            }
            t.addTroop(new Troop(1));
        }        

        for (Player p:players)
        {            
            int pool=p.territories()*2;
            while(pool>0)
            {
                for(int n=0;n<p.territories();n++)
                {                
                    if(pool>0)
                    {
                        int val=1;
                        /**double rand=Math.random();
                        if(pool<3||rand<0.4)
                        val=0;
                        else if(pool<5||rand<0.8)
                        val=2;
                        else
                        val=4;*/
                        if(Math.random()>.5)
                        {
                            int size=p.getTerritory(n).getTroop(0).getSize();                        
                            p.getTerritory(n).setTroop(0,size+val);
                            pool-=val;
                        }
                    }
                }
                //System.out.println("Pool: "+pool);
            }
        }        
    } 

    public void giveTroops(Player p)
    {
        troopsToPlace=(int)(p.territories()/3);
        if(troopsToPlace<3)
            troopsToPlace=3;
        boolean bonus=true,bridgeBonus=true,asB=true,alfB=true,jotB=true,nidB=true;;
        for(Territory t:territories)
        {            
            if(t.getSec().equals("Midgard"))
            {
                if(!turnP.getColor().equals(t.getOwner()))
                {
                    bonus=false;
                }
            }
            if(t.getSec().equals("Bridge"))
            {
                if(!turnP.getColor().equals(t.getOwner()))
                {
                    bridgeBonus=false;
                }
            }
            if(t.getSec().equals("Asgard"))
            {
                if(!turnP.getColor().equals(t.getOwner()))
                {
                    asB=false;
                }
            }
            if(t.getSec().equals("Alfheimr"))
            {
                if(!turnP.getColor().equals(t.getOwner()))
                {
                    alfB=false;
                }
            }
            if(t.getSec().equals("Jotunheimr"))
            {
                if(!turnP.getColor().equals(t.getOwner()))
                {
                    jotB=false;
                }
            }
            if(t.getSec().equals("Nidavellir"))
            {
                if(!turnP.getColor().equals(t.getOwner()))
                {
                    nidB=false;
                }
            }
        }
        if(bonus)
            troopsToPlace+=5;
        if(bridgeBonus)
            troopsToPlace+=10;
        if(asB)
            troopsToPlace+=6;
        if(alfB)
            troopsToPlace+=3;
        if(jotB)
            troopsToPlace+=3;
        if(nidB)
            troopsToPlace+=2;
    }

    public void mouseClicked(MouseEvent e)
    {        
        mouseX=e.getX();
        mouseY=e.getY(); 

        if(e.getButton()==MouseEvent.BUTTON1)
            leftClick=true;
        if(e.getButton()==MouseEvent.BUTTON3)
            rightClick=true;
        if(moveAfterFight)
            movedAttack=true;  

        rMessage="";
    }

    public void actionPerformed(ActionEvent e)
    {
        if(start)
        {
            if(e.getSource()==attackBtn)
            {
                attacking=true;
                canSelect=false;

                attackBtn.setVisible(false);
                moveBtn.setVisible(false);

                nextBtn.setVisible(true);            
            }

            if(e.getSource()==moveBtn)
            {  
                moving=true;
                canSelect=false;

                attackBtn.setVisible(false);
                moveBtn.setVisible(false);            

                nextBtn.setVisible(true);

                movedOnce=true;
            }

            if(e.getSource()==endTurnBtn)
            {
                attacking=false;
                moving=false;            
                moveAfterFight=false;
                canSelect=true;
                movedAttack=false;
                mouseX=2000;
                terrSelected=new Territory("Blank","Blank","Blank");
                terrInvaded=new Territory("Blank","Blank","Blank");
                terrMovingTo=new Territory("Blank","Blank","Blank");            

                movedOnce=false;

                attackBtn.setVisible(false);
                moveBtn.setVisible(false); 
                nextBtn.setVisible(false);

                turnCt++;

                nextTurn();            
            }

            if(e.getSource()==nextBtn)
            {            
                attacking=false;
                moving=false;
                fight=false;

                if(!movedOnce)
                {
                    attackBtn.setVisible(true); 
                }
                else
                {
                    attackBtn.setVisible(false);
                }

                nextBtn.setVisible(false);
                moveBtn.setVisible(true);

                if(!movedOnce&&!movedAttack&&terrInvaded.getOwner().equals(turnP.getColor()))
                {        
                    terrSelected=terrInvaded;
                    mouseX=terrSelected.getX();
                    mouseY=terrSelected.getY();
                }
                else if(movedOnce)
                {
                    terrSelected=terrMovingTo;            
                    mouseX=terrSelected.getX();
                    mouseY=terrSelected.getY();
                }
                else
                {
                    mouseX=terrSelected.getX();
                    mouseY=terrSelected.getY();
                }

                terrInvaded=new Territory("Blank","Blank","Blank");

                terrMovingTo=new Territory("Blank","Blank","Blank");

                canSelect=true;
                moveAfterFight=false;
                movedAttack=false;
            }

            if(e.getSource()==instructionsBtn)
            {
                instructions=!instructions;
            }
        }

        if(e.getSource()==done)
        {
            startGame();
            addBtn.setVisible(false);
            done.setVisible(false);
            removeBtn.setVisible(false);
            revoltBtn.setVisible(false);
            endTurnBtn.setVisible(true);
            instructionsBtn.setVisible(true);
        }
        if(e.getSource()==addBtn&&playerCt<5)
        {
            playerCt++;
        }
        if(e.getSource()==removeBtn&&playerCt>2)
        {
            playerCt--;
        }
        if(e.getSource()==revoltBtn)
        {
            if(revModeCt<2)
                revModeCt++;
            else
                revModeCt=0;
            if(revModeCt==1)
            {
                revoltMode=true;
                revoltModeHard=false;
                revoltBtn.setLabel("NORMAL");
            }
            else if(revModeCt==2)
            {
                revoltMode=false;
                revoltModeHard=true;
                revoltBtn.setLabel("IMPOSSIBLE");
            }
            else
            {
                revoltMode=false;
                revoltModeHard=false;
                revoltBtn.setLabel("OFF");
            }           
        }
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
        leftClick=false;
        rightClick=false;
    }

    public void nextTurn()
    {
        boolean skipTurn=false;
        if(turnIndex<players.size()-1)
            turnIndex++;
        else
            turnIndex=0;
        turnP=players.get(turnIndex);
        revoltWin=true;
        for(Player p:players)
            if(p.territories()!=0)
                revoltWin=false;

        if(turnCt>89&&revoltModeHard)
            win=true;

        if(turnP.territories()==0&&!revoltWin)
            skipTurn=true;

        if(!skipTurn)
        {
            if(turnP.territories()==32||revoltWin)
            {            
                win=true;
                endTurnBtn.setVisible(false);
                instructionsBtn.setVisible(false);
                repaint();
            }
            if(turnCt>0&&revoltMode&&!revoltWin)
                revolt();
            if(turnCt>0&&revoltModeHard&&!revoltWin)
                revoltHard();

            giveTroops(turnP);
        }
        else  
            nextTurn();
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
}