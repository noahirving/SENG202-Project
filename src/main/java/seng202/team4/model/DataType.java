package seng202.team4.model;


import java.util.ArrayList;

public abstract class DataType implements Validate {

    public static final String BETWEEN = "', '";
    public abstract String getInsertStatement(int setID);
    public abstract DataType newDataType(String line);
    public abstract String getTypeName();
    public abstract String getSetName();
    public abstract DataType getValid(String[] record, ArrayList<String> errorMessage);
}
