package Menu;

import DBMSi.Table;
import DBMSi.TableRow;

/**
 * Created by Javier Ortiz on 19/07/2017.
 * Interfaz para mostrar la información por pantalla.
 */
public interface Screen {

    static void mainMenu(){
        System.out.println("-- Welcome to BDMSi --");
        System.out.println("\t1. Create Table");
        System.out.println("\t2. Manage Table");
        System.out.println("\t3. Visualize tables");
        System.out.println("\t4. Visualize historical data");
        System.out.println("\t5. Shut down");
        System.out.print("Select an option: ");
    }

    static void error (String s){
        s = s.toUpperCase();
        System.out.println("Error: " + s);
    }

    static void string(String s) {
        System.out.println(s);
    }


    static void name() {
        System.out.println("Enter name for the table: ");
    }

    static void structureOptions() {
        System.out.println("Select a data structure for the table:");
        System.out.println("\t1. 2-3 tree");
        System.out.println("\t2. AVL tree");
        System.out.println("\t3. Hash table I");
        System.out.println("\t4. Hash table II");
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

    static void tableToWork() {
        System.out.println("What table do you want to work?");
    }

    static void notTableStorage() {
        System.out.println("Not tables in the storage.");
    }

    static void tablesStorage() {
        System.out.println("We have the next tables in the storage:");
    }

    static void managementOptions() {
        System.out.println("\t1. Insert");
        System.out.println("\t2. Show row by index");
        System.out.println("\t3. Select");
        System.out.println("\t4. Update row");
        System.out.println("\t5. Remove row by index");
        System.out.println("\t6. Import from .csv");
        System.out.println("\t7. Export to .csv");
        System.out.println("\t8. Main menu.");
    }

    static void repeatValue() {
        System.out.println("The row is in the table. To update the data, you must select update the row.");
    }

    static void showTR(TableRow tr) {
        System.out.println(tr.toString());
    }

    static void selectMenu(Table t) {
        System.out.println("---- SELECT FROM " + t.getName() + " ----");
        System.out.println("1. Stablish Condition");
        System.out.println("2. Execute select");
        System.out.println("3. Exit");
    }

    static void whatColumn() {
        System.out.println("Column?");
    }

    static void sureDelete() {
        System.out.println("Are you sure you want to delete this row? [Y/N]");
    }

    static void notDeleted() {
        System.out.println("The row wasn't delete.");
    }

    static void deleted() {
        System.out.println("The row deleted.");
    }

    static void importCSV(Table t) {
        System.out.println("--- CSV Import for table " + t.getName() + " ---");
    }

    static void nameCSV () {
        System.out.println("Enter the name of the CSV file:");
    }

    static void waitingCSV (){
        System.out.println("Loading file data...");
    }

    static void succesfullCSV (Table t, long newrows){
        System.out.println("Data loaded succefully. A total of " + newrows + " new rows have been inserted" +
                " into " + t.getName() + ".");
    }

    static void unsuccesfullCSV (String name){
        System.out.println("Unsuccesfull data load from " + name + " CSV file.");
    }

    static void generateCSV(Table table) {
        System.out.println("Generating CSV file for talbe " + table.getName() + "...");
    }

    static void unsuccesfullWriteCSV() {
        System.out.println("Error writting CSV file.");
    }

    static void nameTable(Table table) {
        System.out.println("\nTable " + table.getName());
    }

    static void guiones() {
        System.out.println("----------------------------------------------");

    }

    static void numberRows(Table table) {
        System.out.println("\t\nNumber of rows in the table: " + table.getSize() + "\n");
    }
}
