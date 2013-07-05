package de.Keyle.MyPet.util;

import de.Keyle.MyPet.entity.types.InactiveMyPet;
import de.Keyle.MyPet.util.logger.DebugLogger;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyPetMySQL {

    static final String WRITE_OBJECT_SQL = "INSERT INTO InactiveMyPets(owner, inactivemypet) VALUES (?, ?)";

    static final String UPDATE_SQL_OBJECT = "UPDATE InactiveMyPets SET inactivemypet = ? WHERE owner = ?";

    static final String READ_OBJECT_SQL = "SELECT inactivemypet FROM InactiveMyPets WHERE owner = ?";

    static final String READ_ALL_INACTIVEMYPETS = "SELECT * FROM InactiveMyPets";

    public static Connection getConnection() throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + MyPetConfiguration.MYSQL_DATABASE;
        String username = MyPetConfiguration.MYSQL_USER;
        String password = MyPetConfiguration.MYSQL_PASSWORD;
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static boolean writeInactiveMyPet(String owner, Object object) throws Exception {

        if(!(object instanceof InactiveMyPet)) {
            return false;
        }

        InactiveMyPet inactiveMyPet = (InactiveMyPet)object;

        PreparedStatement pstmt = getConnection().prepareStatement(READ_OBJECT_SQL);
        pstmt.setString(1,owner);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next())
        {
            PreparedStatement updateStatement = getConnection().prepareStatement(UPDATE_SQL_OBJECT);
            updateStatement.setString(2,owner);
            updateStatement.setObject(1,inactiveMyPet);
            updateStatement.executeUpdate();
            updateStatement.close();
            rs.close();
            pstmt.close();
            return true;
        }
        rs.close();
        pstmt.close();
        pstmt = getConnection().prepareStatement(WRITE_OBJECT_SQL);
        // set input parameters
        pstmt.setString(1, owner);
        pstmt.setObject(2, inactiveMyPet);
        pstmt.executeUpdate();

        pstmt.close();
        System.out.println("writeJavaObject: wrote object to SQL for owner: " + owner);
        return true;
    }

    public static InactiveMyPet readInactiveMyPet(String owner) throws Exception {
        PreparedStatement pstmt = getConnection().prepareStatement(READ_OBJECT_SQL);
        pstmt.setString(1, owner);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        Object object = rs.getObject(1);

        rs.close();
        pstmt.close();
        DebugLogger.info("readJavaObject: read pet from SQL for owner : " + owner);
        return (InactiveMyPet)object;
    }

    public static List<InactiveMyPet> readAllInactiveMyPets() throws Exception
    {
        List<InactiveMyPet> tmpList = new ArrayList<InactiveMyPet>();
        PreparedStatement pstmt = getConnection().prepareStatement(READ_ALL_INACTIVEMYPETS);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next())
        {
            ByteArrayInputStream bis = new ByteArrayInputStream((byte[])rs.getObject("inactivemypet"));
            ObjectInputStream ois = new ObjectInputStream(bis);
            tmpList.add((InactiveMyPet)ois.readObject());
        }

        rs.close();
        pstmt.close();

        return tmpList;
    }

    public static void setupMySQL() {

        String inactiveMyPet_Table_Create = "CREATE TABLE IF NOT EXISTS InactiveMyPets " +
                "(owner VARCHAR(16) NOT NULL, " +
                " inactivemypet BLOB, " +
                " PRIMARY KEY ( owner ))";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.executeUpdate(inactiveMyPet_Table_Create);
            statement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
