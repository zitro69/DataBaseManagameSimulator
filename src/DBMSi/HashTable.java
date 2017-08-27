package DBMSi;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Tabla de hash de redispersion que implementa el almacenamiento de
 * los diferentes registros de una tabla
 * Created by avoge on 20/07/2017.
 *
 */
public class HashTable extends TableDataStructure {

    /**
     * Tabla a la que pertenecen las filas almacenadas por esta tabla de hash
     */
    private Table table;

    /**
     * Capacidad inicial de almacenamiento de la tabla
     * (Numero primo)
     */
    private static final int INITIAL_CAPACITY = 112358;

    /**
     * Capacidad actual de la tabla
     */
    private int capacity;

    /**
     * Numero de filas almacenadas en la tabla
     */
    private int size;

    /**
     * Version de la funcion de hash a utilizar
     */
    private int hashVersion;

    public static final int HASH_VERSION_1 = 1;

    public static final int HASH_VERSION_2 = 2;

    /**
     * Registros de la tabla
     */
    private ArrayList<TableRow> rows;

    private ArrayList<UpdateNode> updates;

    /**
     * Tipo de dato de la columna índice de la tabla
     */
    private DataType indexType;

    /**
     * Construye una tabla de hash para registros de una tabla
     * @param hashVersion Version de funcion de hash a utilizar
     */
    public HashTable(int hashVersion){
        this.hashVersion = hashVersion;
        this.size = 0;

        rows = new ArrayList<>(INITIAL_CAPACITY);
        updates = new ArrayList<>(INITIAL_CAPACITY);
        for(int i = 0; i < INITIAL_CAPACITY; i++){
            rows.add(i, null);
            updates.add(i, null);
        }

        capacity = INITIAL_CAPACITY;
    }

    /**
     * Metodo que indica a la tabla de hash a qué Tabla de la base de datos pertenece.
     * Solo podra asignarse una vez.
     * @param table Tabla a la que pertenece esta estructura de datos
     */
    public void setTable(Table table){
        this.table = this.table == null? table : this.table;
    }

    @Override
    protected boolean add(TableRow tableRow) {
        int i = 0;

        if(indexType == null){
            indexType = table.getColumnType(index);
        }

        // Comprobacion de que el indice no exista ya en la tabla
        if(indexExists(tableRow.getContent().get(index))){
            System.err.println("No se puede inserir. Ya existe una fila con el indice "+tableRow.getContent().get(index).toString());
            return false;
        }


        // Insercion de la fila en la tabla
        if(indexType == DataType.INT){
            int key = (int) tableRow.getContent().get(index);
            int position = hashi(hashVersion, key, i);
            if(position == -1) return false;
            while(rows.get(position) != null && i < capacity){
                i++;
                position = hashi(hashVersion, key, i);
                if(position == -1)
                    return false;
            }
            if(i == capacity-1 && rows.get(position) != null)
                return false;

            rows.add(position, tableRow);
            checkCapacity();

        } else if(indexType == DataType.TEXT){
            String key = (String) tableRow.getContent().get(index);
            int position = hashi(hashVersion, key, i);
            if(position == -1) return false;
            while(rows.get(position) != null && i < capacity){
                i++;
                position = hashi(hashVersion, key, i);
                if(position == -1)
                    return false;
            }
            if(i == capacity-1 && rows.get(position) != null)
                return false;

            rows.add(position, tableRow);
            checkCapacity();

        }else{
            return false;
        }

        size++;
        return true;
    }

    @Override
    protected void select(TableRowRestriction restrictions) {

        System.out.println(table.toString());

        for(TableRow row : rows){
            if(row != null && (restrictions == null || restrictions.test(row))){
                System.out.println(row.toString());
            }
        }


    }

    @Override
    protected TableRow getTableRow (TableRowRestriction restrictions){
        for(TableRow row : rows){
            if(row != null && (restrictions == null || restrictions.test(row))){
                return row;
            }
        }
        return null;
    }

    @Override
    protected boolean update(String field, TableRow row, Table t) {

        if(size == 0){
            System.err.println("Tabla \""+table.getName()+"\" vacía. No puede actualizarse ninguna fila.");
            return false;
        }

        if(field.equals(index)){
            int i;
            if(indexType == DataType.INT){
                int key = (int) row.getContent().get(field);
                int position = -1;

                for(i = 0; i < capacity; i++){
                    position = hashi(hashVersion, key, i);
                    if(position == -1) return false;
                    if(rows.get(position) == null) continue;
                    if(rows.get(position).getContent().get(field) == row.getContent().get(field)){
                        break;
                    }
                }

                if(i == capacity-1 && rows.get(position).getContent().get(field) != row.getContent().get(field)) {
                    return false;
                }

                if(updates.get(position) == null) {
                    updates.set(position, new UpdateNode());
                    updates.get(position).oldData = new ArrayList<>();
                }

                /*for (int j = 0; j < t.getColumnNames().size(); j++){
                    Object o = null;
                    if (row.compareTo(t.getColumnNames().get(j), o) == 0){
                        row.addColumn(t.getColumnNames().get(j), rows.get(position).getContent().get(t.getColumnNames().get(j)));
                    }
                }*/
                updates.get(position).oldData.add(rows.get(position));
                rows.set(position, row);
                hasUpdates = true;
                updateCount++;
                return true;
            }else if(indexType == DataType.TEXT){

                String key = (String) row.getContent().get(field);
                int position = -1;

                for(i = 0; i < capacity; i++){
                    position = hashi(hashVersion, key, i);
                    if(position == -1) return false;
                    if(rows.get(position) == null) continue;
                    if(rows.get(position).getContent().get(field).equals(row.getContent().get(field))){
                        break;
                    }
                }

                if(i == capacity-1 && !rows.get(position).getContent().get(field).equals(row.getContent().get(field))) {
                    return false;
                }

                if(updates.get(position) == null) {
                    updates.set(position, new UpdateNode());
                    updates.get(position).oldData = new ArrayList<>();
                }

                /*for (int j = 0; j < t.getColumnNames().size(); j++){
                    Object o = null;
                    if (row.compareTo(t.getColumnNames().get(j), o) == 0){
                        row.addColumn(t.getColumnNames().get(j), rows.get(position).getContent().get(t.getColumnNames().get(j)));
                    }
                }*/
                updates.get(position).oldData.add(rows.get(position));
                rows.set(position, row);
                return true;
            }else{
                return false;
            }
        }else{
            int position;
            for(TableRow e : rows){

                if(e == null) continue;

                if(e.getContent().get(field).equals(row.getContent().get(field))){
                    position = rows.indexOf(e);

                    if(updates.get(position) == null) {
                        updates.set(position, new UpdateNode());
                        updates.get(position).oldData = new ArrayList<>();
                    }

                    /*for (int j = 0; j < t.getColumnNames().size(); j++){
                        Object o = null;
                        if (row.compareTo(t.getColumnNames().get(j), o) == 0){
                            row.addColumn(t.getColumnNames().get(j), rows.get(position).getContent().get(t.getColumnNames().get(j)));
                        }
                    }*/

                    updates.get(position).oldData.add(e);
                    rows.set(position, row);
                    return true;
                }

            }
        }

        return true;
    }

    /**
     * @param tr Fila con el valor del índice de la fila cuyo historial de cambios se desea recuperar
     * @return null si no se encuentra la fila. Coleccion de filas en caso contrario.
     */
    public  ArrayList<TableRow> getHistoricalRow(TableRow tr){

        ArrayList<TableRow> historic, auxOldData;

        if(size == 0){
            System.err.println("Tabla \""+table.getName()+"\" vacía. No hay datos históricos para la fila.");
            return null;
        }

        int i, position = -1;

        if(indexType == DataType.INT){
            int key = (int) tr.getContent().get(index);

            for(i = 0; i < capacity; i++){
                position = hashi(hashVersion, key, i);
                if(position == -1) return null;
                if(rows.get(position) == null) continue;
                if(rows.get(position).getContent().get(index) == tr.getContent().get(index)){
                    break;
                }
            }

            if(i == capacity-1 && rows.get(position).getContent().get(index) != tr.getContent().get(index)) {
                return null;
            }

        }else if(indexType == DataType.TEXT){
            String key = (String) tr.getContent().get(index);

            for(i = 0; i < capacity; i++){
                position = hashi(hashVersion, key, i);
                if(position == -1) return null;
                if(rows.get(position) == null) continue;
                if(rows.get(position).getContent().get(index).equals(tr.getContent().get(index))){
                    break;
                }
            }

            if(i == capacity-1 && !rows.get(position).getContent().get(index).equals(tr.getContent().get(index))) {
                return null;
            }
        }else{
            return null;
        }

        if(position != -1){
            historic = new ArrayList<>();
            historic.add(rows.get(position));

            if(updates.get(position) != null && updates.get(position).oldData != null && updates.get(position).oldData.size() > 0) {
                auxOldData = new ArrayList<>(updates.get(position).oldData);
                Collections.reverse(auxOldData);
                historic.addAll(auxOldData);
            }
            return historic;
        }

        return null;
    }

    @Override
    protected boolean remove(String field, Object value) {

        if(size == 0){
            System.err.println("Tabla \""+table.getName()+"\" vacía. No puede eliminarse ningun elemento.");
            return false;
        }

        if(field.equals(index)){ //Columna field es el indice de la tabla
            int i;

            if(indexType == DataType.INT){
                int key = (int) value;
                int position = -1;

                for(i = 0; i < capacity; i++){
                    position = hashi(hashVersion, key, i);
                    if(position == -1) return false;
                    if(rows.get(position) == null) continue;
                    if(rows.get(position).getContent().get(field) == value){
                        break;
                    }
                }

                if(i == capacity-1 && rows.get(position).getContent().get(field) != value) {
                    return false;
                }

                rows.set(position, null);
                size--;
                return true;
            }else{

                String key = (String) value;
                int position = -1;

                for(i = 0; i < capacity; i++){
                    position = hashi(hashVersion, key, i);
                    if(position == -1) return false;
                    if(rows.get(position) == null) continue;
                    if(rows.get(position).getContent().get(field).equals(value)){
                        break;
                    }
                }

                if(i == capacity-1 && !rows.get(position).getContent().get(field).equals(value)) {
                    return false;
                }

                rows.set(position, null);
                size--;
                return true;
            }

        }else{ // La columna field no es el indice de la tabla
            for(TableRow row : rows){

                if(row == null) continue;

                if(row.getContent().get(field).equals(value)){
                    rows.set(rows.indexOf(row), null);
                    size--;
                    return true;
                }

            }
        }

        return false;
    }

    @Override
    protected long size() {
        return size;
    }

    @Override
    public ArrayList<TableRow> getData () {
        ArrayList<TableRow> data = new ArrayList<>();
        for (TableRow row : rows) {
            if (row == null) continue;
            data.add(row);
        }
        return data;
    }

    /**
     * Comprueba si la tabla contiene una fila con el indice especificado
     * @param index Indice a buscar
     * @return true si existe
     */
    private boolean indexExists(Object index){
        TableRowRestriction restriction = new TableRowRestriction();
        restriction.addRestriction(this.index, index, TableRowRestriction.RESTRICTION_EQUALS);

        for(TableRow row : rows){
            if(row != null && restriction.test(row)){
                return true;
            }
        }

        return false;
    }

    /**
     * Funcion de hash de redispersion para enteros
     * @param version Version de funcion de hash
     * @param key Clave a buscar mediante el hash
     * @param i Iterador de redispersion
     * @return posicion en el vector
     */
    public int hashi(int version, int key, int i){
        return version == HASH_VERSION_1? hashi1(key, i)
                : version == HASH_VERSION_2? hashi2(key, i)
                : -1;
    }

    /**
     * Funcion de hash de redispersion para Strings
     * @param version Version de funcion de hash
     * @param key Clave a buscar demdiante el hash
     * @param i Iterador de redispersion
     * @return posicion en el vector
     */
    public int hashi(int version, String key, int i){
        return version == HASH_VERSION_1? hashi1(key, i)
                : version == HASH_VERSION_2? hashi2(key, i)
                : -1;
    }

    /**
     * Primera funcion de hash para enteros
     * @param key Clave a buscar mediante el hash
     * @param i Iterador de redispersion
     * @return posicion en el vector
     */
    private int hashi1(int key, int i){
        return (key + (int)Math.pow(i, 2))%capacity;
    }

    /**
     * Segunda funcion de hash para enteros
     * @param key Clave a buscar mediante el hash
     * @param i Iterador de redispersion
     * @return posicion en el vector
     */
    private int hashi2(int key, int i){
        return (key +(i*(8681-(key/8681))))%capacity;
    }

    /**
     * Primera funcion de hash para Strings
     * @param key Clave a buscar mediante el hash
     * @param i Iterador de redispersion
     * @return posicion en el vector
     */
    private int hashi1(String key, int i){
        int hash = 0;
        int length = key.length();

        for(int j = length-3; j < length; j++)
            hash += (int)key.charAt(j) * Math.pow(27, j);

        return (hash * (int)Math.pow(i, 2))%capacity;
    }

    /**
     * Segunda funcion de hash para Strings
     * @param key Clave a buscar mediante el hash
     * @param i Iterador de redispersion
     * @return posicion en el vector
     */
    private int hashi2(String key, int i){
        int hash = 0;
        int length = key.length();

        for(int j = length-3; j < length; j++)
            hash += (int)key.charAt(j) * Math.pow(27, j);

        return (hash +(i*(8681-(hash/8681))))%capacity;
    }

    /**
     * Comprueba la cantidad de registros almacenados en la estructura
     * y redimensiona el vector en caso de ser necesario
     */
    private void checkCapacity(){
        int qRows = 0;

        for(int i = 0; i < rows.size(); i++)
            qRows += rows.get(i) != null ? 1 : 0;

        if(qRows/capacity > 0.6){
            int newCapacity = capacity+8693;
            ArrayList<TableRow> auxRows = new ArrayList<>(newCapacity);
            ArrayList<UpdateNode> auxUpdates = new ArrayList<>(newCapacity);

            for(int i = 0; i < capacity; i++){
                auxRows.set(i, rows.get(i));
                auxUpdates.set(i, updates.get(i));
            }
            for(int i = capacity; i < newCapacity; i++){
                auxRows.add(i, null);
                auxUpdates.add(i, null);
            }

            rows = auxRows;
            updates = auxUpdates;
            capacity = newCapacity;
        }
    }

    private static class UpdateNode{
        ArrayList<TableRow> oldData;
    }

    public static void main(String[] args) {
        HashTable table = new HashTable(1);
        Table people = new Table("People", table);
        table.setTable(people);

        people.addColumn("id", DataType.INT);
        people.addColumn("name", DataType.TEXT);
        people.addColumn("online", DataType.BOOLEAN);

        people.setIndex("name");

        System.out.println();
        System.out.println();

        people.addColumn("test", DataType.TEXT);

        people.importCSV("People.csv");

        people.selectRows(null);
    }
}
