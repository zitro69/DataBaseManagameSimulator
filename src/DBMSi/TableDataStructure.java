package DBMSi;

import java.util.ArrayList;

/**
 * Created by Albertpv on 15/01/17.
 *
 * <p>Defineix la base de l'estructura de qualsevol taula del sistema.</p>
 *
 * @author Programació Avançada i Estructura de Dades (PAED)
 *         Universitat La Salle Ramon Llull
 *
 * @see Table
 */
public abstract class TableDataStructure {

    /**
     * Representa el nom de la columna que serà utilitzada per organitzar l'estructura.
     */
    protected String index;

    /**
     * Indica si se han actualizado filas de esta tabla
     */
    protected boolean hasUpdates;

    /**
     * Numero de actualizaciones que s ehan hecho sobre esta tabla
     */
    protected int updateCount;

    /**
     * @return Numero de actualizaciones que s ehan hecho sobre esta tabla
     */
    protected int getUpdateCount(){return updateCount;}

    /**
     * @return true si se han actualizado filas de esta tabla
     */
    protected boolean hasUpdates(){return hasUpdates;}

    /**
     * @return El nom de l'índex.
     */
    protected String getIndex() {
        return index;
    }

    /**
     * Estableix el nom del camp pel qual s'organitza l'estructura.
     *
     * @param field El nom del camp que serà l'índex.
     */
    protected void setIndex(String field) {
        this.index = field;
    }

    /**
     * Afegeix una nova fila dins de l'estructura utilitzada per una taula.
     *
     * @param tableRow La nova fila a afegir.
     *
     * @return true si s'ha pogut afegir, false en cas contrari.
     */
    protected abstract boolean add(TableRow tableRow);

    /**
     * Visualitza el contingut de l'estructura de dades.
     *
     * @param restrictions  Restriccions per tal de filtrar files en la visualització.
     */
    protected abstract void select(TableRowRestriction restrictions);

    /**
     * Permet actualitzar una fila de l'estructura de dades.
     *
     * @param field El camp pel qual cercar la fila existent.
     * @param row   El contingut actualitzat de la fila.
     *
     * @return      true si s'ha actualitzat, false si no s'ha trobat el valor previ de la fila en l'estructura.
     */
    protected abstract boolean update(String field, TableRow row);

    /**
     * Si existeix el valor en l'estructura, en la columna especificada, llavors
     * elimina la primera coincidència de l'estructura. És a dir, la fila sencera.
     *
     * @param field El nom del camp o columna.
     * @param value El valor que ha de tenir el camp.
     *
     * @return true si s'ha pogut eliminar la fila, false en cas contrari.
     */
    protected abstract boolean remove(String field, Object value);

    /**
     * @return El total d'elements que hi ha guardats en l'estructura.
     */
    protected abstract long size();

    protected abstract ArrayList<TableRow> getData();

    public abstract ArrayList<TableRow> getHistoricalRow(TableRow tr);
}
