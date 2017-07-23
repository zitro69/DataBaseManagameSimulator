import DBMSi.Table;
import Menu.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Javier Ortiz on 19/07/2017
 * Clase principal del programa, controla toda la información del programa.
 */
public class DataBaseManagamentSimulator {
    private Scanner sc;
    private Menu menu;
    private ArrayList<Table> dbmsi;

    //Constructor
    public DataBaseManagamentSimulator (){
        sc = new Scanner (System.in);
        menu = new Menu ();
        dbmsi = new ArrayList<>();
    }

    //Funciones

    /**
     * Inicializa el programa. Genera el menú principal y llama a las funciones utiles.
     *
     */
    public void init (){
        int value = 0;
        while (value  != 5){
            Screen.mainMenu();
            try {
                value = sc.nextInt();
            } catch (InputMismatchException ime){
                value = 0;
            }
            sc.nextLine();
            switch (value){
                case 1: //Creacion de las Tablas
                    dbmsi.add(menu.createTable(dbmsi));
                    break;
                case 2: //Tratar las Tablas
                    menu.manageTable(dbmsi);
                    break;
                case 3: //Visualizar Tablas
                    menu.visualitzeTable(dbmsi);
                    break;
                case 4: //Visualizar el historico de la información
                    menu.visualitzeHistoricalTable(dbmsi);
                    break;
                case 5: //Apagar
                    break;
                default:
                    Screen.error("Operación no válida");
                    break;
            }
        }
    }
}
