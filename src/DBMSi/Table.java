package DBMSi;

import java.util.*;

/**
 * Created by Albertpv on 15/01/17.
 *
 * <p>Aquesta classe modela una Taula d'un sistema de BBDD.</p>
 *
 * <p>Està dissenyada per tal que es pugui fer el següent</p>
 *
 * <ul>
 *     <li>Afegir columnes de diferents tipus de dades (INT, FLOAT, TEXT...)</li>
 *     <li>Establir un índex per tal d'optimitzar l'estructura interna.</li>
 *     <li>Visualitzar tot el contingut.</li>
 *     <li>Aplicar filtres en la visualització.</li>
 *     <li>Cercar contingut a partir d'un camp.</li>
 *     <li>Eliminar contingut a partir de l'índex.</li>
 * </ul>
 *
 *
 * @author Programació Avançada i Estructura de Dades (PAED)
 *         Universitat La Salle Ramon Llull
 *
 * @see DataType            Tipus de dades disponibles per emmagatzemar.
 * @see TableRow            Model de les files de la taula.
 * @see TableDataStructure  Contracte d'estructura de dades.
 */
public class Table {

    /**
     * El nom de la taula.
     */
    private String name;

    /**
     * Informació de les columnes, el seu <b>nom</b> i <b>DataType</b> respectivament.
     */
    private LinkedHashMap<String, Object> columns;

    /**
     * L'estructura de dades que implementa aquesta taula.
     */
    private TableDataStructure dataStructure;

    /**
     * Constructor.
     *
     * @param name              The name of the table.
     * @param dataStructure     The data structure that will store the values of the table.
     */
    public Table (String name, TableDataStructure dataStructure) {

        this.name = name;
        this.dataStructure = dataStructure;
        columns = new LinkedHashMap<>();
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getIndex() {

        return dataStructure.getIndex();
    }

    /**
     * Estableix una de les columnes de la taula com a índex.
     *
     * @param column    La columna per la qual s'indexarà la taula.
     *
     * @throws IllegalArgumentException Si l'índex ja es trobava definit per una taula amb dades existents,
     *                                  si la columna no existeix o bé el tipus d'aquesta no és ni INT ni TEXT.
     */
    public void setIndex(String column) throws IllegalArgumentException {
        String index = dataStructure.getIndex();

        if (!columns.containsKey(column))
            throw new IllegalArgumentException("The column " + column + " does not exist in table " + name);

        if (index != null && !index.isEmpty() && dataStructure.size() > 0)
            throw new IllegalArgumentException("Table " + name + " already has an index defined: " + index
                    + ". Clear all the existing rows before setting a new index.");

        DataType type = (DataType) columns.get(column);
        if (type != DataType.INT && type != DataType.TEXT)
            throw new IllegalArgumentException("The index column must be INT or TEXT");

        dataStructure.setIndex(column); // si passem les verificacions, establim la columna índex
    }

    /**
     * @return Una llista amb els noms de totes les columnes.
     */
    public List<String> getColumnNames() {
        List<String> columnsNames = new ArrayList<>();

        for (Map.Entry<String, Object> entry : columns.entrySet())
            columnsNames.add(entry.getKey());

        return columnsNames;
    }

    /**
     * @return Una llista amb el tipus de cada columna de la taula.
     */
    public List<DataType> getColumnTypes() {
        List<DataType> dataTypes = new ArrayList<>();

        for (Map.Entry<String, Object> entry : columns.entrySet())
            dataTypes.add((DataType) entry.getValue());

        return dataTypes;
    }

    /**
     * @param columnName Nom de la columna de la qual es vol saber el seu tipus de dades que guarda.
     *
     * @return El tipus de dada de la columna introduïda o null si és un nom que no existeix.
     */
    public DataType getColumnType(String columnName) {

        return (DataType) columns.get(columnName);
    }

    public boolean hasIndex() {

        return dataStructure.getIndex() != null && !dataStructure.getIndex().isEmpty();
    }

    /**
     * Afegeix una nova columna a la taula.
     *
     * @param columnName    El nom de la nova columna.
     * @param valueType     El valor que guarda la nova columna.
     *
     * @return true si s'ha pogut afegir, false si ja existia la columna.
     */
    public boolean addColumn(String columnName, DataType valueType) {
        boolean added = false;

        if (!columns.containsKey(columnName)) {

            columns.put(columnName, valueType);
            added = true;
        }

        return added;
    }

    /**
     * Afegeix una nova fila a la taula.
     *
     * @param row   La nova fila que es vol afegir.
     *
     * @return      true si s'ha pogut afegir, false en cas contrari.
     */
    public boolean addRow(TableRow row) {

        if (row.getContent().size() != columns.size())
            throw new IllegalArgumentException("El nombre de columnes de la nova fila no coincideix amb les de la taula");

        for (String column : getColumnNames())
            if (!row.getContent().containsKey(column))
                throw new IllegalArgumentException("La nova fila no conté la columna: " + column);

        return dataStructure.add(row);
    }

    /**
     * Visualitza totes les files de la taula o bé aquelles que passin les restriccions
     * si és que hi ha.
     *
     * @param restrictions Les restriccions a aplicar en la visualització de les files.
     *
     */
    public void selectRows(TableRowRestriction restrictions) {

        dataStructure.select(restrictions);
    }

    public boolean updateRow(TableRow row) {

        return dataStructure.update(dataStructure.getIndex(), row);
    }


    /**
     * Elimina una fila de la taula a partir d'un valor que pot tenir la columna
     * índex d'aquesta. A més, actualitza la data de l'última eliminació en la taula.
     *
     * @param value El valor pel qual si es troba s'elimina la fila.
     *
     * @return true si s'ha eliminat, false en cas contrari.
     */
    // Pre: l'índex ha d'haver estar definit prèviament
    public boolean removeRow(Object value) {

        return dataStructure.remove(dataStructure.getIndex(), value);
    }

    /**
     * @return El nombre total de files existents en la taula.
     */
    public long getRowsNumber() {

        return dataStructure.size();
    }

    @Override
    public String toString() {

        // TODO: Per l'alumne
        return "";
    }

}
