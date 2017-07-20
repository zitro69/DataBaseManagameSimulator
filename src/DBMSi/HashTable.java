package DBMSi;

import java.util.ArrayList;

/**
 * Tabla de hash de redispersion que implementa el almacenamiento de
 * los diferentes registros de una tabla
 * Created by avoge on 20/07/2017.
 *
 */
public class HashTable extends TableDataStructure {

    /**
     * Capacidad inicial de almacenamiento de la tabla
     * (Numero primo)
     */
    private static final int INITIAL_CAPACITY = 8689;

    /**
     * Capacidad actual de la tabla
     */
    private int capacity;

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
     * @param index Nombre de la columna indice
     * @param indexType Tipo de la columna indice
     */
    public HashTable(String index, DataType indexType, int hashVersion){
        this.index = index;
        this.indexType = indexType;
        this.hashVersion = hashVersion;

        rows = new ArrayList<>(INITIAL_CAPACITY);
        updates = new ArrayList<>(INITIAL_CAPACITY);
        capacity = INITIAL_CAPACITY;
    }

    @Override
    protected boolean add(TableRow tableRow) {
        int i = 0;

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

        return true;
    }

    @Override
    protected void select(TableRowRestriction restrictions) {

    }

    @Override
    protected boolean update(String field, TableRow row) {

        if(field.equals(index)){
            int i = 0;
            if(indexType == DataType.INT){
                int key = (int) row.getContent().get(field);
                int position = hashi(hashVersion, key, i);
                if(position == -1) return false;
                while(rows.get(position).getContent().get(field) != row.getContent().get(field)
                        && i < capacity){
                    i++;
                    position = hashi(hashVersion, key, i);
                    if(position == -1) return false;
                }

                if(i == capacity-1
                        && rows.get(position).getContent().get(field) != row.getContent().get(field)) {
                    return false;
                }

                if(updates.get(position).oldData == null) updates.get(position).oldData = new ArrayList<>();
                updates.get(position).oldData.add(rows.get(position));
                rows.set(position, row);
                return true;
            }else{

                String key = (String) row.getContent().get(field);
                int position = hashi(hashVersion, key, i);
                if(position == -1) return false;
                while(!rows.get(position).getContent().get(field).equals(row.getContent().get(field))
                        && i < capacity){
                    i++;
                    position = hashi(hashVersion, key, i);
                    if(position == -1) return false;
                }

                if(i == capacity-1
                        && !rows.get(position)
                        .getContent().get(field).equals(row.getContent().get(field))) {
                    return false;
                }

                if(updates.get(position).oldData == null) updates.get(position).oldData = new ArrayList<>();
                updates.get(position).oldData.add(rows.get(position));
                rows.set(position, row);
                return true;
            }
        }else{
            for(int i = 0; i< capacity; i++){

                if(rows.get(i) == null) continue;

                if(rows.get(i).getContent().get(field).equals(row.getContent().get(field))){
                    if(updates.get(i).oldData == null) updates.get(i).oldData = new ArrayList<>();
                    updates.get(i).oldData.add(rows.get(i));
                    rows.set(i, row);
                    return true;
                }

            }
        }

        return true;
    }

    @Override
    protected boolean remove(String field, Object value) {

        if(field.equals(index)){ //Columna field es el indice de la tabla
            int i = 0;
            if(indexType == DataType.INT){
                int key = (int) value;
                int position = hashi(hashVersion, key, i);
                if(position == -1) return false;
                while(rows.get(position).getContent().get(field) != value
                        && i < capacity){
                    i++;
                    position = hashi(hashVersion, key, i);
                    if(position == -1) return false;
                }

                if(i == capacity-1
                        && rows.get(position)
                            .getContent().get(field) != value) {
                    return false;
                }

                rows.set(position, null);
                return true;
            }else{

                String key = (String) value;
                int position = hashi(hashVersion, key, i);
                if(position == -1) return false;
                while(!rows.get(position).getContent().get(field).equals(value)
                        && i < capacity){
                    i++;
                    position = hashi(hashVersion, key, i);
                    if(position == -1) return false;
                }

                if(i == capacity-1
                        && rows.get(position)
                        .getContent().get(field).equals(value)) {
                    return false;
                }

                rows.set(position, null);
                return true;
            }

        }else{ // La columna field no es el indice de la tabla
            for(int i = 0; i< capacity; i++){

                if(rows.get(i) == null) continue;

                if(rows.get(i).getContent().get(field).equals(value)){
                    rows.set(i, null);
                    return true;
                }

            }
        }

        return false;
    }

    @Override
    protected long size() {
        return rows.size();
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

        return 0;
    }

    /**
     * Segunda funcion de hash para enteros
     * @param key Clave a buscar mediante el hash
     * @param i Iterador de redispersion
     * @return posicion en el vector
     */
    private int hashi2(int key, int i){

        return 0;
    }

    /**
     * Primera funcion de hash para Strings
     * @param key Clave a buscar mediante el hash
     * @param i Iterador de redispersion
     * @return posicion en el vector
     */
    private int hashi1(String key, int i){

        return 0;
    }

    /**
     * Segunda funcion de hash para Strings
     * @param key Clave a buscar mediante el hash
     * @param i Iterador de redispersion
     * @return posicion en el vector
     */
    private int hashi2(String key, int i){

        return 0;
    }

    /**
     * Comprueba la cantidad de registros almacenados en la estructura
     * y redimensiona el vector en caso de ser necesario
     */
    private void checkCapacity(){
        if(rows.size()/capacity > 0.6){
            ArrayList<TableRow> auxRows = new ArrayList<>(capacity+8693);
            ArrayList<UpdateNode> auxUpdates = new ArrayList<>(capacity+8693);

            for(int i = 0; i < capacity; i++){
                auxRows.set(i, rows.get(i));
                auxUpdates.set(i, updates.get(i));
            }

            rows = auxRows;
            updates = auxUpdates;
        }
    }

    private static class UpdateNode{
        ArrayList<TableRow> oldData;
    }
}
