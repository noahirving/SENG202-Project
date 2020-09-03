package seng202.team4.model;



public abstract class DataType {

    public static final String between = "', '";
    public abstract String getInsertStatement(int setID);
    public abstract DataType newDataType(String line);
    public abstract String getTypeName();
    public abstract String getSetName();




    /*


    */
}
