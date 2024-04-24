import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import StringOperations.StringService;
import StringOperations.StringServiceHelper;

public class StringClient {
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            String ior = args[0]; // Pass the IOR as a command line argument

            Object objRef = orb.string_to_object(ior);
            StringService service = StringServiceHelper.narrow(objRef);

            // Perform operations
            System.out.println("Concatenate 'Hello' and 'World': " + service.concatenate("Hello", "World"));
            System.out.println("Convert 'Hello' to upper case: " + service.toUpperCase("Hello"));
            System.out.println("Convert 'WORLD' to lower case: " + service.toLowerCase("WORLD"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
