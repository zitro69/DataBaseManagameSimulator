package DBMSi;

import java.util.*;

/**
 * Created by Albertpv on 22/03/17.
 *
 *
 * <p>Aquesta classe permet la implementació de la comanda
 * "select" en el nostre sistema.</p>
 *
 * <p>Per fer-ho, es crea una llista de restriccions <b>per cadascuna de les
 * columnes</b> d'una taula. Dins d'aquesta llista, s'afegeixen tantes
 * restriccions com l'usuari desitgi. Un cop es filtrin els resultats, <b>només que
 * una d'aquestes</b> restriccions de <b>cada llista</b> es compleixi, la fila es visualitzarà.</p>
 *
 * <p>Per tant, si tenim una fila amb les columnes c1, c2 i c3 podríem crear el següent select:</p>
 *
 * <p><i>SELECT FROM taula WHERE (c1 LESS THAN x OR c1 EQUALS TO y) AND (c2 GREATER THAN z)</i></p>
 *
 * @author Programació Avançada i Estructura de Dades (PAED)
 *         Universitat La Salle Ramon Llull
 */
public class TableRowRestriction {

    /**
     * <p>La clau consisteix en el nom d'una columna, on el valor
     * correspon a la llista de restriccions associada a aquesta.</p>
     *
     * <p>És a dir, si per exemple tinguéssim "columna 1" la qual
     * emmagatzema valors enters, podríem tenir una restricció que fos
     * LESS_THAN 50 i una altre GREATER_THAN 90. Per tant, per aquesta
     * columna tindriem:</p>
     *
     * <p><i>WHERE "columna 1".valor LESS THAN 50 OR "columna 1".valor GREATER THAN 90</i></p>
     */
    private HashMap<String, List<Restriction>> restrictions;

    public static final int RESTRICTION_LESS    = 1;
    public static final int RESTRICTION_EQUALS  = 2;
    public static final int RESTRICTION_GREATER = 3;

    public TableRowRestriction() {

        restrictions = new HashMap<>();
    }

    /**
     * Afegeix una restriccó per a una columna d'una taula, internament
     * el mètode demanarà si es vol restringir per valors més petits, iguals
     * o majors al introduït.
     *
     * @param field     El nom de la columna.
     * @param value     El valor pel qual es restringeix.
     * @param condition Tipus de condició: [RESTRICTION_LESS, RESTRICTION_EQUALS, RESTRICTION_GREATER]
     */
    public void addRestriction(String field, Object value, int condition) {

        try {
            Restriction restriction = new Restriction(field, value);

            switch (condition) {

                case RESTRICTION_LESS:
                    restriction.restrictionType = RestrictionType.LESS_THAN;
                    break;

                case RESTRICTION_EQUALS:
                    restriction.restrictionType = RestrictionType.EQUALS;
                    break;

                case RESTRICTION_GREATER:
                    restriction.restrictionType = RestrictionType.GREATER_THAN;
                    break;

                default:
                    System.out.println("Opció incorrecte.");
                    return;
            }

            // Si ja existien restriccions, afegim una de nova
            if (restrictions.containsKey(field)) {

                List<Restriction> existingRestrictions = restrictions.get(field);
                existingRestrictions.add(restriction);
                restrictions.put(field, existingRestrictions);
            }
            else { // sinó, creem una primera restricció per la columna

                List<Restriction> restrictionsForField = new ArrayList<>();
                restrictionsForField.add(restriction);
                restrictions.put(field, restrictionsForField);
            }

        } catch (NumberFormatException e) {

            System.err.println("Opció incorrecte.");
        }
    }

    /**
     * Per una columna, comprova que el seu valor compleixi <b>una</b> de les restriccions indicades
     * per aquest camp. És a dir, si per exemple hi ha 3 restriccions per aquesta columna, si compleix
     * una d'elles, el valor serà vàlid.
     *
     * @param tableRow              La fila de la taula d'on comprovar el valor.
     * @param restrictionsForField  La llista de restriccions associades a la columna que es valida.
     *
     * @return                      true si passa les restriccions, false en cas contrari.
     */
    private boolean checkFieldRestrictions(TableRow tableRow, List<Restriction> restrictionsForField) {
        int compareResult;
        boolean pass = false;

        for (Restriction res : restrictionsForField) {

            compareResult = tableRow.compareTo(res.field, res.value);

            switch (res.restrictionType) {

                case LESS_THAN:

                    if (compareResult < 0) pass = true;
                    break;

                case EQUALS:

                    if (compareResult == 0) pass = true;
                    break;

                case GREATER_THAN:

                    if (compareResult > 0) pass = true;
                    break;
            }
        }

        return pass;
    }

    /**
     * Comprova que es compleixin totes les restriccions establertes.
     *
     * @param tableRow  La fila de la taula que es vol validar.
     *
     * @return          true en cas que passi les restriccions, false en cas contrari
     */
    public boolean test(TableRow tableRow) {

        for (Map.Entry<String, List<Restriction>> restriction : restrictions.entrySet()) {

            List<Restriction> res = restriction.getValue();

            // si no passa una de les restriccions, sortim
            if (!checkFieldRestrictions(tableRow, res)) return false;
        }

        return true;
    }


    private enum RestrictionType {

        LESS_THAN, EQUALS, GREATER_THAN
    }

    private class Restriction {

        String field;
        RestrictionType restrictionType;
        Object value;

        Restriction(String field, Object value) {

            this.field = field;
            this.value = value;
        }
    }
}
