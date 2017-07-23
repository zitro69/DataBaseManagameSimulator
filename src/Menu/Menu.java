package Menu;

import DBMSi.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Javier Ortiz on 19/07/2017.
 * Trata la información.
 */
public class Menu {
    private Scanner sc;

    //Constructor
    public Menu (){
        sc = new Scanner (System.in);
    }

    //Funciones

        //Funciones necesaria para la opcion create table
    /**
     * Crea una tabla, comprobando todas las restricciones impuestas. No se deben repetir nombres entre distintas
     * tablas, y debe crearse un indice válido (debe ser formato INT o TEXT).
     * @param dbmsi     ArrayList que contiene todas las tablas, para comprubar los nombres.
     * @return          La table creada.
     */
    public Table createTable(ArrayList<Table> dbmsi) {
        TableDataStructure tds = null;
        String nom = null;
        //Introducción del nombre
        while (checkName(dbmsi, nom)){
            Screen.name();
            nom = sc.nextLine();
        }
        //Introducción de daatos en la estructura
        int value = 0;
        while (value < 1 || value > 4){
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
            case 3://HASH_1
                tds = new HashTable(1);
                break;
            case 4://HASH_2
                tds = new HashTable(2);
                break;
        }
        //Creación de la tabla
        Table t = new Table (nom, tds);
        if(tds instanceof AVLTree ){
            ((AVLTree)tds).setTable(t);
        }
        if(tds instanceof HashTable){
            ((HashTable)tds).setTable(t);
        }
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
                newColumn(t, names, indexs);
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
            System.out.println("added");
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
                if (dbmsi.get(i).getName().equals(nom)) return true;
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
                    Screen.string(indexs.get(i));
                }
                String column = DatabaseInput.readIndexColumnName();
                for (int i = 0; i < indexs.size(); i++) {
                    if (column.equals(indexs.get(i))) {
                        found = true;
                        System.out.println(indexs.get(i));
                        t.setIndex(indexs.get(i));
                        break;
                    }
                }
            }
        }
    }

        //Funciones necesaria para la opcion manage table

    /**
     * Hace seleccionar al usuario una tabla y la trata.
     * @param dbmsi     Array con las tablas del programa
     */
    public void manageTable(ArrayList<Table> dbmsi) {
        //Selecionar la tabla con la que se trabajara
        Table table;
        if (listTables(dbmsi)){
            Screen.tableToWork();
            while ((table = checkTables(sc.nextLine(), dbmsi)) == null){
                Screen.tableToWork();
            }
            //Mostramos el menú del manage table
            int option = 0;
            while (option != 8){
                Screen.managementOptions();
                try {
                    option = sc.nextInt();
                } catch (InputMismatchException ime){
                    option = 0;
                }
                sc.nextLine();
                switch (option){
                    case 1: //Insert
                        if (!insert(table)) Screen.error("Can't insert the value in the table");
                        break;
                    case 2: //Show by index
                        showRow(table);
                        break;
                    case 3: //Select
                        int value = 0;
                        TableRowRestriction trr = new TableRowRestriction();
                        while (value != 3){
                            value = selectMenu(table);
                            switch (value){
                                case 1:
                                    newCondition (trr, table);
                                    break;
                                case 2:
                                    table.toString();
                                    table.selectRows(trr);
                                    break;
                                case 3:
                                    break;
                            }
                        }
                        break;
                    case 4: //Update row
                        updateRow(table);
                        break;
                    case 5: //Remove row by index
                        Object o = showRow(table);
                        boolean correct = true;
                        while (correct) {
                            Screen.sureDelete();
                            String conf = sc.nextLine();
                            conf = conf.toUpperCase();
                            if (conf.equals("Y")) {
                                correct = false;
                                if (table.removeRow(o)) {
                                    Screen.deleted();
                                } else {
                                    break;
                                }
                            } else if (conf.equals("N")) {
                                correct = false;
                                Screen.notDeleted();
                            } else {
                                Screen.error("Incorrect value");
                            }
                        }
                        break;
                    case 6:
                        Screen.importCSV(table);
                        sc.nextLine();
                        Screen.nameCSV();
                        String archivo = sc.nextLine();
                        importData(table, archivo);
                        break;
                    case 7:
                        exportData(table);
                        break;
                    case 8: //Main menu
                        break;
                    default:
                        Screen.error("Not valid option.");
                        break;
                }
            }
        }
    }

    /**
     * Actualiza un fila de una table.
     * @param table     table a la que pertenece la casilla.
     */
    private void updateRow(Table table) {
        TableRow tr = new TableRow();
        Object o = DatabaseInput.readColumnValue(table.getColumnType(table.getIndex()),
                table.getIndex());
        tr.addColumn(table.getIndex(), o);
        for (int i = 0; i < table.getColumnNames().size(); i++) {
            if (!(table.getIndex().equals(table.getColumnNames().get(i)))){
                Screen.modify(table.getColumnNames().get(i));
                boolean next = false;
                while (!next) {
                    String comf = sc.nextLine();
                    comf = comf.toUpperCase();
                    if (comf.equals("Y")) {
                        o = DatabaseInput.readColumnValue(table.getColumnType(table.getColumnNames().get(i)),
                                table.getColumnNames().get(i));
                        tr.addColumn(table.getColumnNames().get(i), o);
                        next = true;
                    } else if (comf.equals("N")) {
                        Screen.string("Value don't changed.");
                        next = true;
                    } else {
                        Screen.error("Incorrect option.");
                        Screen.string("Enter a new option [Y/N].");
                        next = false;
                    }
                }
            }
        }
        if (table.updateRow(tr)){
            Screen.rowModified(table);
        } else {
            Screen.error("The index doesn't exist. We can't update the row.");
        }

    }

    /**
     * Lista las tablas con las que se podría trabajar.
     * @param dbmsi     ArrayList con todas las tablas
     * @return          true: si exiten tablas
     *                  false: si no exiten tablas
     */
    private boolean listTables(ArrayList<Table> dbmsi) {
        //Ordena la tabla por orden alfabetico
        Collections.sort(dbmsi, (Table t1, Table t2) -> {
            char a = Character.toLowerCase(t1.getName().charAt(0));
            char b = Character.toLowerCase(t2.getName().charAt(0));
            return a-b;
        });
        if (dbmsi.size() == 0){
            Screen.notTableStorage();
            return false;
        } else {
            Screen.tablesStorage();
            for (int i = 0; i < dbmsi.size(); i++){
                Screen.string("\t" + dbmsi.get(i).getName());
            }
            return true;
        }
    }

    /**
     * Comprueba si la tabla existe en la base de datos.
     * @param name      Nombre de la tabla a comprobar.
     * @param dbmsi     ArrayList con todas las tablas.
     * @return          Tabla con el nombre introduccido. Si la tabla no existe en la base de datos devuelve un NULL.
     */
    private Table checkTables (String name, ArrayList<Table> dbmsi){
        Table t = null;
        for (int i = 0;i < dbmsi.size(); i++){
            if (name.equals(dbmsi.get(i).getName())) t = dbmsi.get(i);
        }
        if (t == null){
           Screen.error("The table doesn't exist.");
            return null;
        } else {
            return t;
        }
    }

    /**
     * Permite al usuario insertar un valor en la talba
     * @param t         Tabla en la que se inserta el valor
     * @return          true: si se ha insertado correctamente;
     */
    private boolean insert (Table t){
        TableRow tr = new TableRow();
        setInfo(t, tr);
        return t.addRow(tr);
    }

    /**
     * Completa la infromación de una fila segun los parametro de la tabla.
     * @param t         Tabla de donde se saca la información.
     * @param tr        Fila a completar.
     */
    private void setInfo(Table t, TableRow tr) {
        for (int i = 0; i < t.getColumnNames().size(); i++){
            tr.addColumn(t.getColumnNames().get(i), DatabaseInput.readColumnValue(t.getColumnType(
                    t.getColumnNames().get(i)), t.getColumnNames().get(i)
            ));
        }
    }

    /**
     * Muestra por pantalla el valor de una casiila.
     * @param t         Tabla a la que pertenece la casilla.
     * @return          El objeto indice que se ha mostrado.
     */
    private Object showRow (Table t){
        TableRowRestriction trr = new TableRowRestriction();
        Object o = DatabaseInput.readColumnValue(t.getColumnType(t.getIndex()), t.getIndex());
        trr.addRestriction(t.getIndex(), o, 2);
        System.out.println(t.toString());
        t.selectRows(trr);
        return o;
    }

    /**
     * Muestra y selección el menú de selec de una tabla.
     * @param t         tabla
     * @return          opción del menú
     */
    private int selectMenu (Table t){
        Screen.selectMenu(t);
        int value = sc.nextInt();
        sc.nextLine();
        return value;

    }

    /**
     * Pone una restricción a la tabla.
     * @param trr       Restricciones
     * @param t         tabla
     */
    private void newCondition(TableRowRestriction trr, Table t) {
        Screen.whatColumn();
        String column = sc.nextLine();
        boolean trobat = false;
        for (int i = 0; i < t.getColumnNames().size(); i++){
            if (t.getColumnNames().get(i).equals(column)){
                trobat = true;
            }
        }
        if (trobat){
            Object a = DatabaseInput.readColumnValue(t.getColumnType(column), column);
            trr.addRestriction(column, a,DatabaseInput.readRestrictionType(a));
        } else {
            Screen.error("The column introducced don't exist.");
        }
    }

    /**
     * Importa los datos de una archivo a la tabla
     * @param table     tabla que recibira los datos
     * @param archivo   path del archivo
     */
    private void importData(Table table, String archivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String columnnames[] = br.readLine().split(",");
            long contador = 0;
            for (int i = 0; i < columnnames.length; i++){
                if (table.getColumnType(columnnames[i]) == null){
                    br.close();
                    Screen.unsuccesfullCSV(archivo);
                    return;
                }
            }
            String tr;
            while ((tr = br.readLine()) != null){
                String value[] = tr.split(",");
                TableRow newvalue = new TableRow();
                for (int i = 0; i < columnnames.length; i++){
                    newvalue.addColumn(columnnames[i], DatabaseInput.readValue(table.getColumnType(columnnames[i]),
                            value[i]));
                }
                table.addRow(newvalue);
                contador++;
            }
            Screen.succesfullCSV(table, contador);
            br.close();
        } catch (FileNotFoundException fnfe){
            Screen.unsuccesfullCSV(archivo);
            Screen.error("The file don't exists.");
        } catch (IOException ioe){
            Screen.unsuccesfullCSV(archivo);
            Screen.error("Invalid data in the file: " + archivo);
        }
    }

    /**
     * Exporta los datos de una tabla a un archivo.
     * @param table     tabla a exportar.
     */
    private void exportData (Table table){
        Screen.generateCSV(table);
        ArrayList<TableRow> data = table.getData();
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(table.getName()+".csv"));
            for (int j = 0; j < table.getColumnNames().size(); j++){
                bw.write(table.getColumnNames().get(j) + ",");
            }
            bw.write("\n");
            for (int i = 0; i < data.size(); i++){
                for (int j = 0; j < table.getColumnNames().size(); j++){
                    bw.write(data.get(i).getContent().get(table.getColumnNames().get(j)) + ",");
                }
                bw.write("\n");
            }
            bw.close();
        } catch (IOException ioe){
            Screen.unsuccesfullWriteCSV();
        }
    }

    /**
     * Visualiza las distintas tablas de Database
     * @param dbmsi     database
     */
    public void visualitzeTable(ArrayList<Table> dbmsi) {
        if (dbmsi.size() == 0){
            Screen.error("No tables in the program.");
            return;
        }
        for (int i = 0; i < dbmsi.size(); i++){
            Screen.nameTable(dbmsi.get(i));
            Screen.guiones();
            Screen.string(String.format("%10s %10s", "Column", "DataType"));
            Screen.guiones();
            for (int j = 0; j < dbmsi.get(i).getColumnNames().size(); j++){
                Screen.string(String.format("%10s %10s", dbmsi.get(i).getColumnNames().get(j),
                        (dbmsi.get(i).getColumnType(dbmsi.get(i).getColumnNames().get(j)))));
            }
            Screen.guiones();
            Screen.numberRows(dbmsi.get(i));
        }
    }

    /**
     * Visualiza el historico de una tabla del database
     * @param dbmsi     database con las diversas tablas
     */
    public void visualitzeHistoricalTable(ArrayList<Table> dbmsi) {
        if (dbmsi.size() == 0){
            Screen.error("No tables in the program.");
            return;
        }
        showTables(dbmsi);
        ArrayList<TableRow> values = new ArrayList<>();
        Screen.string("Select one table:");
        int table = (sc.nextInt()-1);
        sc.nextLine();
        if (table >= 0 && table < dbmsi.size()){
            Screen.string("What historial row do you want see?\nWrite the index:");
            String row = sc.nextLine();
            TableRow tr = new TableRow();
            tr.addColumn(dbmsi.get(table).getIndex(), DatabaseInput.readValue(dbmsi.get(table).getColumnType(
                    dbmsi.get(table).getIndex()), row));
           values = dbmsi.get(table).getHistoricalRow(tr);
           if (values == null){
               Screen.error("the row with this index don't exist");
               return;
           }
           Screen.guiones();
           for (int i = 0; i < dbmsi.get(table).getColumnNames().size(); i++){
               System.out.print(dbmsi.get(table).getColumnNames().get(i) + "\t");
           }
           System.out.println();
           Screen.guiones();
           for (int i = values.size()-1; i >= 0; i--){
               for (int j = 0; j < dbmsi.get(table).getColumnNames().size(); j++){
                   System.out.print(values.get(i).getContent().get(dbmsi.get(table).getColumnNames().get(j)) + "\t");
               }
               System.out.println();
           }
            Screen.guiones();
        } else {
            return;
        }
    }

    /**
     * Ordena las tablas del database por orden alfabetico y las muestra por pantalla.
     * @param dbmsi     database con las tablas
     */
    private void showTables (ArrayList<Table> dbmsi){
        Collections.sort(dbmsi, (Table t1, Table t2)->{
            if (t1.getName().charAt(0) > t2.getName().charAt(0)) return 1;
            else if (t1.getName().charAt(0) < t2.getName().charAt(0)) return -1;
            return 0;
        });
        for (int i = 0; i < dbmsi.size(); i++){
            Screen.guiones();
            Screen.string((i+1) + ". \tTable:" + dbmsi.get(i).getName());
            Screen.string("\tRows:" +dbmsi.get(i).getRowsNumber());
            Screen.guiones();
        }
    }


}
