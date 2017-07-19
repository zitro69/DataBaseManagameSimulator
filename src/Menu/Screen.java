package Menu;

/**
 * Created by Javier Ortiz on 19/07/2017.
 * Interfaz para mostrar la información por pantalla.
 */
public interface Screen {
    /**
     * Printa el menú principal del programa por pantalla.
     */
    static void mainMenu(){
        System.out.println("-- Welcome to BDMSi --");
        System.out.println("\t1. Create Table");
        System.out.println("\t2. Manage Table");
        System.out.println("\t3. Visualize tables");
        System.out.println("\t4. Visualize historical data");
        System.out.println("\t5. Shut down");
    }

    /**
     * Remarca un error.
     * @param s     Error a mostrar por pantalla.
     */
    static void error (String s){
        s = s.toUpperCase();
        System.out.println("Error: " + s + ".");
    }
}
