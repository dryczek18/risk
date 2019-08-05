package RiskStuff; 
public class RiskTest
{
   public static void main(String[]args)
   {
       Territory test=new Territory("Country","Bigger Land","red");
       test.addTroop(new Troop(1));
       test.addTroop(new Troop(3));
       System.out.print(test);
   }
}
