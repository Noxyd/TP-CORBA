import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.swing.JFrame;
import org.omg.CosNaming.NamingContext;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class ClientTalk {
	
	public static talk.client t_alk;
	public static void main(String args[]) {     
            
            try {

                // Intialisation de l'orb
                org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args,null);
                
                // Recuperation du POA
                POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

                // Creation du servant
                //*********************
                clientImpl cli = new clientImpl();

                // Activer le servant au sein du POA et recuperer son ID
                byte[] monTalkId = rootPOA.activate_object(cli);

                // Activer le POA manager
                rootPOA.the_POAManager().activate();


                // Enregistrement dans le service de nommage
                //*******************************************
                // Recuperation du naming service
                NamingContext nameRoot=org.omg.CosNaming.NamingContextHelper.narrow(orb.resolve_initial_references("NameService"));

                // Construction du nom a enregistrer
                org.omg.CosNaming.NameComponent[] nameToRegister = new org.omg.CosNaming.NameComponent[1];
                System.out.println("Sous quel nom voulez-vous enregistrer l'objet Corba ?");
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String nomObj = in.readLine();
                
                nameToRegister[0] = new org.omg.CosNaming.NameComponent(nomObj,"");

                // Enregistrement de l'objet CORBA dans le service de noms
                nameRoot.rebind(nameToRegister,rootPOA.servant_to_reference(cli));
                System.out.println("==> Nom '"+ nomObj + "' est enregistre dans le service de noms.");

                String IORServant = orb.object_to_string(rootPOA.servant_to_reference(cli));
                System.out.println("L'objet possede la reference suivante :");
                System.out.println(IORServant);                
                
                ServerThread s_th = new ServerThread(orb);
                Thread t = new Thread(s_th);

                t.start();
                

                // Saisie du nom de l'objet (si utilisation du service de nommage)
                System.out.println("Quel objet Corba voulez-vous contacter ?");
                String idObj = in.readLine();

                // Construction du nom a rechercher
                org.omg.CosNaming.NameComponent[] nameToFind = new org.omg.CosNaming.NameComponent[1];
                nameToFind[0] = new org.omg.CosNaming.NameComponent(idObj,"");

                // Recherche aupres du naming service
                org.omg.CORBA.Object distantTalk = nameRoot.resolve(nameToFind);
                System.out.println("Objet '" + idObj + "' trouve aupres du service de noms. IOR de l'objet :");
                System.out.println(orb.object_to_string(distantTalk));

                // Utilisation directe de l'IOR (SAUF utilisation du service de nommage)
                //org.omg.CORBA.Object distantEuro = orb.string_to_object("IOR:000000000000001b49444c3a436f6e766572746973736575722f4575726f3a312e300000000000010000000000000086000102000000000d3139322e3136382e35362e310000dae900000031afabcb0000000020499e8ee200000001000000000000000100000008526f6f74504f410000000008000000010000000014000000000000020000000100000020000000000001000100000002050100010001002000010109000000010001010000000026000000020002");

                t_alk = talk.clientHelper.narrow(distantTalk);

                // A compléter
                String m = new String();
                Scanner sc = new Scanner(in);
                while(true) {
                        System.out.print("Saisir un message : ");

                        m = sc.nextLine();

                        t_alk.afficherMessage(m);

                }

            }
            catch (Exception e) {
                    e.printStackTrace();
            }

    } // main
}
