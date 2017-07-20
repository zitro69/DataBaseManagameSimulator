package DBMSi;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Albertpv on 15/01/17.
 *
 * <p>Modela la fila d'una taula i facilita les comparacions entre
 *  columnes de les files.</p>
 *
 *  @author Programació Avançada i Estructura de Dades (PAED)
 *         Universitat La Salle Ramon Llull
 */
public class TableRow {

    /**
     * Conté els noms de les columnes com a clau i el valor associat com a valor.
     */
    private LinkedHashMap<String, Object> content;


    public TableRow() {

        content = new LinkedHashMap<>();
    }

    public TableRow(LinkedHashMap<String, Object> content) {

        this.content = content;
    }

    public HashMap<String, Object> getContent() {
        return content;
    }

    public void setContent(LinkedHashMap<String, Object> content) {
        this.content = content;
    }

    /**
     * Afegeix una columna amb el seu valor a la fila.
     *
     * @param column    La columna que es vol afegir.
     * @param value     El seu valor associat.
     */
    public void addColumn(String column, Object value) {
        content.put(column, value);
    }

    /**
     * Compara dos objectes entre ells que han de ser del mateix tipus.
     *
     * @param obj1  Un dels objectes a comparar.
     * @param obj2  L'altre objecte a comparar.
     *
     * @return -, 0, + en funció de si obj1 és més petit, igual o més gran respectivament.
     */
    private int getCompareResult(Object obj1, Object obj2) {
        int result = 0;

        if (obj1 instanceof Boolean)
            result = ((Boolean) obj1).compareTo((Boolean) obj2);

        else if (obj1 instanceof Integer)
            result = ((Integer) obj1).compareTo((Integer) obj2);

        else if (obj1 instanceof Long)
            result = ((Long) obj1).compareTo((Long) obj2);

        else if (obj1 instanceof Float)
            result = ((Float) obj1).compareTo((Float) obj2);

        else if (obj1 instanceof Character)
            result = ((Character) obj1).compareTo((Character) obj2);

        else if (obj1 instanceof String) {
            result = ((String) obj1).compareTo((String) obj2);
        }

        return result;
    }

    /**
     * Compara el valor d'una mateixa columna entre dues files.
     *
     * @param field         El nom de la columna per la qual es fa la comparació.
     * @param otherTable    L'altre taula amb la que comparar el valor de la columna.
     *
     * @return              -1, 0, 1 en funció de si és més petit, igual o més gran.
     */
    // Pre: el valors a comparar han de ser de mateix tipus. Si aquest mètode
    //      s'utilitza entre files de la mateixa taula mai hi haurà cap problema.
    public int compareTo(String field, TableRow otherTable) {

        System.out.println("compare of " + this.toString() + "\n"
            + "with " + otherTable.toString() + " by field: " + field);
        if (!content.containsKey(field))
            throw new UnsupportedOperationException("Aquest objecte no conté el camp [" + field + "].");

        Object thisObject = content.get(field);
        Object other = otherTable.content.get(field);

        int res =  getCompareResult(thisObject, other);

        System.out.println("compareTo result: " + res);
        return res;
    }

    /**
     * Compara el valor que hi ha en la columna de la fila amb un altre valor.
     *
     * @param field         El nom de la columna per la qual es fa la comparació.
     * @param other         El valor amb el que es vol comparar la columna de la fila.
     *
     * @return              -1, 0, 1 en funció de si és més petit, igual o més gran.
     */
    // Pre: el valors a comparar han de ser de mateix tipus. Si aquest mètode
    //      s'utilitza entre files de la mateixa taula mai hi haurà cap problema.
    public int compareTo(String field, Object other) {

        if (!content.containsKey(field))
            throw new UnsupportedOperationException("Aquest objecte no conté el camp " + field);

        Object thisObject = content.get(field);

        return getCompareResult(thisObject, other);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Object> entry : content.entrySet()) {

            Object value = entry.getValue();

            if (value instanceof String)
                sb.append(String.format("%-20s ", value));

            else sb.append(String.format("%-10s ", value));
        }

        return sb.toString();
    }
}
