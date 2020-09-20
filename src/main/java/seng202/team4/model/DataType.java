package seng202.team4.model;


import java.util.ArrayList;

/**
 * Describes the required implementation for Airline, Airport, Route and FlightPath classes.
 */
public abstract class DataType implements Validate {

    /**
     * The string required between attributes of the insert statement.
     */
    public static final String BETWEEN = "', '";

    /**
     * Gets the database insert statement.
     * @param setID the ID of the set the that will be inserted into.
     * @return the insert statement.
     */
    public abstract String getInsertStatement(int setID);

    // TODO: will be removed once getValid is implemented for uploading

    /**
     * Gets the name of data type.
     * @return name of data type.
     */
    public abstract String getTypeName();

    /**
     * Gets the name of data type set.
     * @return name of data type set.
     */
    public abstract String getSetName();

    /**
     * Gets a valid data type from the array of strings, or returns null with
     * reasons for being invalid in the error message.
     * @param record        array of strings constituting the record.
     * @param errorMessage  arrayList where the error messages will be stored.
     * @return a valid new dataType, 'null' if invalid.
     */
    public abstract DataType getValid(String[] record, ArrayList<String> errorMessage);

    /**
     * Gets a valid data type from the array of strings, or returns null with
     * reasons for being invalid in the error message.
     * @param record string constituting the record.
     * @param errorMessage arrayList where the error messages will be stored.
     * @return a valid new dataType, 'null' if invalid.
     */
    public abstract DataType getValid(String record, ArrayList<String> errorMessage);

}
