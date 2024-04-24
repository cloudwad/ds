import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import StringOperations.StringServicePOA;

class StringServiceImpl extends StringServicePOA {
    @Override
    public String concatenate(String s1, String s2) {
        return s1 + s2;
    }

    @Override
    public String toUpperCase(String s) {
        return s.toUpperCase();
    }

    @Override
    public String toLowerCase(String s) {
        return s.toLowerCase();
    }
}

public class StringServer {
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            StringServiceImpl serviceImpl = new StringServiceImpl();
            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(serviceImpl);
            String ior = orb.object_to_string(ref);

            System.out.println("StringService IOR: " + ior);

            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
