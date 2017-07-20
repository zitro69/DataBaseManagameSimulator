package Menu;

import DBMSi.Table;

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

    static void name() {
        System.out.println("Enter name for the table: ");
    }

    static void structureOptions() {
        System.out.println("Select a data structure for the table:");
        System.out.println("\t1. 2-3 tree");
        System.out.println("\t2. AVL tree");
        System.out.println("\t3. Hash table");
    }

    static void newColumn (){
        System.out.println("Would do you like add another column?[Y/N]");
    }

    static void newTable(Table t) {
        System.out.println("Table " + t.getName() + " added into the System");
    }

    static void columnName(Table t) {
        System.out.println("Enter a column name for the new table " + t.getName()+ ":");
    }

    static void dataStores() {
        System.out.println("Which kind of data stores this column?");
    }

    static void oneIndex() {
        System.out.println("At least there must be one INT or TEXT datatype to create the table.");
    }

    static void selectIndex (){
        System.out.println("Select a column from the list to use it as index:");
    }
}
