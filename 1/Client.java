import java.rmi.*;
import java.util.Scanner;

public class Client
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		
		try
		{
			String serverURL = "rmi://localhost/Server";
			ServerIntf serverIntf = (ServerIntf)Naming.lookup(serverURL);
			
			System.out.println("Enter no");
			double num1 = sc.nextDouble();
			
			System.out.println("Enter 2 no");
			double num2 = sc.nextDouble();
			
			System.out.println("Addition is" +serverIntf.Addition(num1,num2));
			
		}
		catch(Exception e)
		{
			System.out.println("Exception Occured" +e.getMessage());
		}
		
	}
}
