package seng202.team4.model;



public abstract class DataType {

    public static final String between = "', '";
    public abstract String getInsertStatement();
    public abstract DataType newDataType(String line);




    /*


    */
}
