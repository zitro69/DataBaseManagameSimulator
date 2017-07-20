package Menu;

import DBMSi.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Javier Ortiz on 19/07/2017.
 * Trata la información.
 */
public class Menu {

    //Constructor
    public Menu (){}

    /**
     * Crea una tabla, comprobando todas las restricciones impuestas. No se deben repetir nombres entre distintas
     * tablas, y debe crearse un indice válido (debe ser formato INT o TEXT).
     * @param dbmsi     ArrayList que contiene todas las tablas, para comprubar los nombres.
     * @return          La table creada.
     */
    public Table createTable(ArrayList<Table> dbmsi) {
        Scanner sc = new Scanner(System.in);
        TableDataStructure tds = null;
        String nom = null;
        //Introducción del nombre
        while (checkName(dbmsi, nom)){
            Screen.name();
            nom = sc.nextLine();
        }
        //Introducción de daatos en la estructura
        int value = 0;
        while (value <= 1 || value >= 3){
            Screen.structureOptions();
            try {
                value = sc.nextInt();
            } catch (InputMismatchException ime){
                value = 0;
                Screen.error("value not valid");
            }
            sc.nextLine();
        }
        switch (value){
            case 1:
                tds = new TwoThreeTree();
                break;
            case 2:
                tds = new AVLTree();
                break;
            case 3:
                //TODO: clase de tabla de hash
        }
        //Creación de la tabla
        Table t = new Table (nom, tds);
        //Introducción de la celdas de la tabla
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> indexs = new ArrayList<>();
        boolean finish = false;
        newColumn(t, names, indexs);
        while (!finish){
            String yn;
            Screen.newColumn();
            yn = sc.nextLine();
            yn = yn.toLowerCase();
            if (yn.equals("y")){
                newColumn(t, indexs, names);
            } else if (yn.equals("n")){
                finish = true;
            } else {
                Screen.error("Invalid option.");
            }
        }
        //Selección del index
        selectIndex(t, indexs, names);
        Screen.newTable(t);

        return t;
    }

    /**
     * Añade una columna a la tabla. Se comprueba que el nombre de la columna no este repetido y en caso de que
     * sea INT o TEXT lo mete en el array de indexs.
     * @param t         Tabla a la que se la añade de la columna.
     * @param names     Array donde se almacenan los nombres de la columnas.
     * @param indexs    Array donde se guardan los indicenes.
     */
    private void newColumn(Table t, ArrayList<String> names, ArrayList<String> indexs) {
        Scanner sc = new Scanner (System.in);
        boolean correct = false;
        String name = "";
        while (!correct) {
            Screen.columnName(t);
            name = sc.nextLine();
            for (int i = 0; i < names.size(); i++){
                correct = true;
                if (name.equals(names.get(i))){
                    correct = false;
                    break;
                }
            }
            if (names.size() == 0){
                correct = true;
            }
        }
        names.add(name);
        String datatype = "";
        while (DataType.toDataType(datatype) == null) {
            Screen.dataStores();
            datatype = sc.nextLine();
        }
        t.addColumn(name, DataType.toDataType(datatype));
        if (DataType.toDataType(datatype) == DataType.INT || DataType.toDataType(datatype) == DataType.TEXT){
            indexs.add(name);
        }
    }

    /**
     * Comprueba que el nombre de la nueva tabla no exista en la base de datos.
     * @param dbmsi     Array donde se almacena las tablas.
     * @param nom       Nombre que se debe de comprobar
     * @return          true: en caso de que el nombre exista
     *                  false: en caso de que el nombre no exista
     */
    private boolean checkName(ArrayList<Table> dbmsi, String nom) {
        if (nom == null){
            return true;
        } else {
            for (int i = 0; i < dbmsi.size(); i++){
                if (dbmsi.get(i).getName() == nom) return true;
            }
        }
        return false;
    }

    /**
     * Selecciona el indice de la tabla.
     * @param t         Tabla a la que se le selecciona el indice
     * @param indexs    Array de los indices
     * @param names     Array de las columnas
     */
    private void selectIndex (Table t, ArrayList<String> indexs, ArrayList<String> names){
        boolean found = false;
        while (!found){
            if (indexs.size() == 0){
                Screen.error("Not found column with INT or TEXT datatype.");
                Screen.oneIndex();
                newColumn (t, indexs, names);
            } else {
                Screen.selectIndex();
                for (int i = 0; i < indexs.size(); i++) {
                    System.out.println(indexs.get(i));
                }
                String column = DatabaseInput.readIndexColumnName();
                for (int i = 0; i < indexs.size(); i++) {
                    if (column.equals(indexs.get(i))) {
                        found = true;
                        t.setIndex(indexs.get(i));
                        break;
                    }
                }
            }
        }
    }
}