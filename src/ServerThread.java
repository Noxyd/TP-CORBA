import org.omg.CORBA.ORB;
/**
 *
 * @author Samuel
 */
public class ServerThread implements Runnable{
    
    private ORB orba;
    
    ServerThread(ORB orb){
        this.orba = orb;
    }

    @Override
    public void run() {
        try {
            // Lancement de l'ORB et mise en attente de requete
            //**************************************************
            orba.run();
        }
        catch (Exception e) {
        }
    }
    
}
