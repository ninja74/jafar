package Database.Structure;


public class tb_JafarStructure {

    public static final String tableName = "tb_Jafar";

    public static final String colPK_Jafar = "PK_Jafar";
    public static final String colCompanyName = "CompanyName";
    public static final String colproductName = "productName";
    public static final String colUrlImg = "UrlImg";
    public static final String colYear = "Year";
    public static final String colMonth = "Month";
    public static final String colDays = "Days";
    public static final String colPayCash = "PayCash";
    public static final String colBaqi = "Baqi";
    public static final String colCheckNumber = "CheckNumber";
    public static final String colCheckYear = "CheckYear";
    public static final String colCheckMonth = "CheckMonth";
    public static final String colCheckDays = "CheckDays";
    public static final String colKeywords = "Keywords";


    public static String createTableQuery = "create table " + tableName + "(" +
            colPK_Jafar + " integer primary key autoincrement," +
            colCompanyName + " text, " +
            colproductName + " text, " +
            colUrlImg + " text, " +
            colYear + " text, " +
            colMonth + " text, " +
            colDays + " text, " +
            colPayCash + " text, " +
            colBaqi + " text, " +
            colCheckNumber + " text, " +
            colCheckYear + " text, " +
            colCheckMonth + " text, " +
            colCheckDays + " text, " +
            colKeywords + " text" +
            ")";
}
