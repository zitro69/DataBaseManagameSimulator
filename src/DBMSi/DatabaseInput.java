package DBMSi;

import java.util.Scanner;

/**
 * Created by Albertpv on 31/03/17.
 *
 * Classe encarregada de la lectura de dades per teclat necessàries per tal de realitzar
 * certes opcions de les taules i creació de restriccions.
 */
public class DatabaseInput {

    private static final Scanner scanner = new Scanner(System.in);


    /**
     * Permet la introducció d'un valor del tipus de la columna.
     *
     * @param dataType      El tipus de la columna.
     * @param columnName    El nom de la columna.
     *
     * @return              El valor introduït per teclat.
     *
     * @throws NumberFormatException    Si es dóna un error de format amb les dades introduïdes per teclat.
     */
    public static Object readColumnValue(DataType dataType, String columnName) throws NumberFormatException {

        switch (dataType) {

            case BOOLEAN:

                System.out.print("Enter a boolean value for " + columnName + ": ");
                return Boolean.parseBoolean(scanner.nextLine()); // basura de mètode

            case INT:

                System.out.print("Enter an int value for " + columnName + ": ");
                return Integer.parseInt(scanner.nextLine());

            case LONG:

                System.out.print("Enter a long value for " + columnName + ": ");
                return Long.parseLong(scanner.nextLine());

            case FLOAT:

                System.out.print("Enter a float value for " + columnName + ": ");
                return Float.parseFloat(scanner.nextLine());

            case DOUBLE:

                System.out.print("Enter a double value for " + columnName + ": ");
                return Double.parseDouble(scanner.nextLine());

            case CHAR:

                System.out.print("Enter a char value for " + columnName + ": ");
                String text = scanner.nextLine();   // ens curem en salut amb els \n
                return text.isEmpty()? '\0' : text.charAt(0);

            case TEXT:

                System.out.print("Enter a text value for " + columnName + ": ");
                return scanner.nextLine();

                default:
                    throw new IllegalArgumentException("This data type is not supported by the System.");
        }
    }

    /**
     * S'encarrega d'obtenir el nom de la columna que serà índex de la taula.
     *
     * @return El nom de la columna índex.
     */
    public static String readIndexColumnName() {

        System.out.print("Which field?: ");
        return scanner.nextLine();
    }

    /**
     * Mostra un conjunt d'opcions per tal de demanar el tipus de restricció per un valor donat.
     *
     * @param restrictionValue  El valor a partir del qual aplicar la restricció.
     *
     * @return                  El valor enter associat al tipus de restricció.
     *
     * @throws NumberFormatException Si el format de les dades introduïdes no és enter.
     */
    public static int readRestrictionType(Object restrictionValue) throws NumberFormatException {

        System.out.println("How do you want to restrict the value?");
        System.out.println("1. Less than " + restrictionValue.toString());
        System.out.println("2. Equals to " + restrictionValue.toString());
        System.out.println("3. Greather than " + restrictionValue.toString());

        return Integer.parseInt(scanner.nextLine());
    }
}
