package DBMSi;

/**
 * Created by Albertpv on 15/01/17.
 *
 *
 * <p>Possibles valors que poden tenir les columnes
 * de qualsevol taula del sistema.</p>
 *
 * @author Programació Avançada i Estructura de Dades (PAED)
 *         Universitat La Salle Ramon Llull
 */
public enum DataType {

    BOOLEAN,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    CHAR,
    TEXT;


    /**
     * Transforma una string a formato DataType.
     * @param s     String la cual se transforma.
     * @return      Devuelve un dato en formato DataType. En caso de que no se
     *              pueda transformar devuelve un null.
     */
    public static DataType toDataType (String s){
        s = s.toUpperCase();
        switch (s){
            case "BOOLEAN":
                return BOOLEAN;
            case "INT":
                return INT;
            case "LONG":
                return LONG;
            case "FLOAT":
                return FLOAT;
            case "DOUBLE":
                return DOUBLE;
            case "CHAR":
                return CHAR;
            case "TEXT":
                return TEXT;
            default:
                return null;
        }
    }

    /**
     * Comprueba que la string introducida sea del datatype que se ha introducido.
     * @param dt    Tipo de datatype que debe ser la string.
     * @param s     Valor a comprobar.
     * @return      true si el datatype concide con el valor introducido.
     *              false si el datatype no coincide con el valor introducido.
     */
    public static boolean correctValue (DataType dt, String s){
        switch (dt){
            case BOOLEAN:
                boolean auxboolean = Boolean.parseBoolean(s);
                s = s.toLowerCase();
                return (auxboolean || s.equals("false"));
            case CHAR:
                return s.length() == 1;
            case DOUBLE:
                try {
                    double auxdouble = Double.parseDouble(s);
                } catch (NumberFormatException nfe){
                    return false;
                }
                return true;
            case FLOAT:
                try {
                    float auxfloat = Float.parseFloat(s);
                } catch (NumberFormatException nfe){
                    return false;
                }
                return true;
            case INT:
                try {
                    int auxint = Integer.parseInt(s);
                } catch (NumberFormatException nfe){
                    return false;
                }
                return !(s.contains(".") || s.contains(","));
            case LONG:
                try {
                    long auxfloat = Long.parseLong(s);
                } catch (NumberFormatException nfe){
                    return false;
                }
                return true;
            case TEXT:
                return true;
            default:
                return false;
        }
    }


    @Override
    public String toString() {
        switch (this){
            case BOOLEAN:
                return "boolean";
            case CHAR:
                return "char";
            case DOUBLE:
                return "double";
            case FLOAT:
                return "float";
            case INT:
                return "int";
            case LONG:
                return "long";
            case TEXT:
                return "text";
            default:
                return "null";
        }
    }
}
