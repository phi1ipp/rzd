import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Philipp on 10/25/2014.
 */
public class Converter {

    private Connection conn;
    private PreparedStatement pstmtCountrySearch, pstmtDocTypeSearch, pstmtCustSearch, pstmtInsert, pstmtInsertCustomer;
    private String strIFileName;

    public Converter(String strInFileName, String strOutFileName) {
        // open db
        strIFileName = strInFileName;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + strOutFileName);

            pstmtCountrySearch = conn.prepareStatement("select country_key as key from countries where name=?");
            pstmtDocTypeSearch = conn.prepareStatement("select doctype_key as key from doctypes where name=?");
            pstmtCustSearch = conn.prepareStatement("select cust_key as key from customers where name=?");
            pstmtInsertCustomer = conn.prepareStatement("insert into customers(name) values(?)");

            String strInsertSQLTemplate = "insert into contacts(lastname, firstname, middlename, " +
                    "doctype_key, docnumber, doccountry_key, " +
                    "gender, birthplace, birthdate, cust_key) " + "" +
                    "values(?, ?, ?, ?, ?, ?, ?, ?, strftime(?), ?)";
            pstmtInsert = conn.prepareStatement(strInsertSQLTemplate);

        } catch (Exception e) {
            e.printStackTrace();
            conn = null;
        }
    }

    int convert() {
        InputStream ExcelFileToRead;
        try {
            ExcelFileToRead = new FileInputStream(strIFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }

        //todo Check if this block is ever needed at all
        if (strIFileName.endsWith("xls")) {
            HSSFWorkbook wb;
            try {
                wb = new HSSFWorkbook(ExcelFileToRead);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            HSSFSheet sheet=wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;

            Iterator rows = sheet.rowIterator();

            while (rows.hasNext())
            {
                row=(HSSFRow) rows.next();
                Iterator cells = row.cellIterator();

                while (cells.hasNext())
                {
                    cell=(HSSFCell) cells.next();

                    if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
                    {
                        System.out.print(cell.getStringCellValue()+" ");
                    }
                    else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
                    {
                        System.out.print(cell.getNumericCellValue()+" ");
                    }
                }
                System.out.println();
            }
        } else if (strIFileName.endsWith("xlsx")) {
            XSSFWorkbook test;
            try {
                test = new XSSFWorkbook(ExcelFileToRead);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }

            XSSFSheet sheet = test.getSheetAt(0);
            XSSFRow row;

            Iterator rows = sheet.rowIterator();
            boolean bFirst = true;
            boolean bSecond = true;
            while (rows.hasNext())
            {
                row=(XSSFRow) rows.next();

                // skip first row as it's a header
                if (bFirst) {
                    bFirst = false;
                    continue;
                }

                // if there is one row at least
                // clean current db
                if (bSecond) {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute("delete from contacts");
                        bSecond = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return -1;
                    }
                }

                String strLName = row.getCell(0).getStringCellValue();
                String strFName = row.getCell(1).getStringCellValue();
                String strMName = row.getCell(2).getStringCellValue();
                String strGender = row.getCell(3).getStringCellValue();
                String strDocType = row.getCell(4).getStringCellValue();
                String strDocNum = row.getCell(5).getRawValue();
                String strDocCountry = row.getCell(6).getStringCellValue();
                String strBDate = row.getCell(7).getStringCellValue();
                String strBPlace = row.getCell(8).getStringCellValue();

                strBDate = checkBDate(strBDate);
                if (strBDate.length() == 0) {
                    System.err.println("Error: Birth date " + strBDate + " should be in DD.MM.YYYY format");
                    continue;
                }

                // customer cell can be empty
                Cell cellCustomer = row.getCell(9, Row.RETURN_BLANK_AS_NULL);
                String strCustomer = cellCustomer == null ? null : cellCustomer.getStringCellValue();

                Integer iCustomer = 0;
                if (strCustomer != null)
                    try {
                        pstmtCustSearch.setString(1, strCustomer);
                        ResultSet rs = pstmtCustSearch.executeQuery();
                        if (rs.next()) {
                            iCustomer = rs.getInt(1);
                        }

                        // if not found -> need to insert it
                        if (iCustomer == 0) {
                            pstmtInsertCustomer.setString(1, strCustomer);
                            pstmtInsertCustomer.executeUpdate();

                            // get inserted id
                            iCustomer = pstmtInsertCustomer.getGeneratedKeys().getInt(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                else
                    iCustomer = null;

                int iDocCountry = 0;
                try {
                    pstmtCountrySearch.setString(1, strDocCountry);
                    ResultSet rs = pstmtCountrySearch.executeQuery();
                    if (rs.next()) {
                        iDocCountry = rs.getInt(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                int iDocType = 0;
                try {
                    pstmtDocTypeSearch.setString(1, strDocType);
                    ResultSet rs = pstmtDocTypeSearch.executeQuery();

                    if (rs.next()) {
                        iDocType = rs.getInt(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    pstmtInsert.setString(1, strLName);
                    pstmtInsert.setString(2, strFName);
                    pstmtInsert.setString(3, strMName);

                    pstmtInsert.setInt(4, iDocType);
                    pstmtInsert.setString(5, strDocNum);
                    pstmtInsert.setInt(6, iDocCountry);

                    pstmtInsert.setString(7, strGender);
                    pstmtInsert.setString(8, strBPlace);
                    pstmtInsert.setString(9, strBDate);

                    if (iCustomer == null)
                        pstmtInsert.setNull(10, Types.INTEGER);
                    else
                        pstmtInsert.setInt(10, iCustomer);

                    pstmtInsert.execute();
                    System.out.print(".");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }

    /**
     * Checks if a string representing a birthdate is in a right format and converts it to sqlite format
     * @param strDate
     * @return String in sqlite format, "" in case of bad input
     */
    private String checkBDate(String strDate) {
        SimpleDateFormat russian = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sqlite = new SimpleDateFormat("yyyy-MM-dd");

        try {
            String strRet = sqlite.format(russian.parse(strDate));
            return strRet;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
