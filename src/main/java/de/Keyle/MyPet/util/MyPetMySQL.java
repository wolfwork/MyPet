package de.Keyle.MyPet.util;

import de.Keyle.MyPet.entity.types.InactiveMyPet;
import de.Keyle.MyPet.util.logger.DebugLogger;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyPetMySQL {

    static final String WRITE_PET_SQL = "INSERT INTO InactiveMyPets(owner, inactivemypet) VALUES (?, ?)";

    static final String UPDATE_SQL_PET = "UPDATE InactiveMyPets SET inactivemypet = ? WHERE owner = ?";

    static final String READ_PET_SQL = "SELECT inactivemypet FROM InactiveMyPets WHERE owner = ?";

    static final String READ_ALL_INACTIVEMYPETS = "SELECT * FROM InactiveMyPets";

    static final String READ_ALL_MYPETPLAYERS = "SELECT * FROM MyPetPlayers";

    static final String WRITE_PLAYER_SQL = "INSERT INTO MyPetPlayers(player, mypetplayer) VALUES (?, ?)";

    static final String UPDATE_SQL_PLAYER = "UPDATE MyPetPlayers SET mypetplayer = ? WHERE player = ?";

    static final String READ_PLAYER_SQL = "SELECT mypetplayer FROM MyPetPlayers WHERE player = ?";

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

        PreparedStatement pstmt = getConnection().prepareStatement(READ_PET_SQL);
        pstmt.setString(1,owner);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next())
        {
            PreparedStatement updateStatement = getConnection().prepareStatement(UPDATE_SQL_PET);
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
        pstmt = getConnection().prepareStatement(WRITE_PET_SQL);
        // set input parameters
        pstmt.setString(1, owner);
        pstmt.setObject(2, inactiveMyPet);
        pstmt.executeUpdate();

        pstmt.close();
        System.out.println("writeJavaObject: wrote object to SQL for owner: " + owner);
        return true;
    }

    public static InactiveMyPet readInactiveMyPet(String owner) throws Exception {
        PreparedStatement pstmt = getConnection().prepareStatement(READ_PET_SQL);
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

    public static int readAllMyPetPlayers() throws Exception
    {
        int playerCount = 0;
        PreparedStatement pstmt = getConnection().prepareStatement(READ_ALL_MYPETPLAYERS);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next())
        {
            ByteArrayInputStream bis = new ByteArrayInputStream((byte[])rs.getObject("mypetplayer"));
            ObjectInputStream ois = new ObjectInputStream(bis);
            MyPetPlayer tmpPlayer = ((MyPetPlayer) ois.readObject());
            MyPetPlayer.insertMyPetPlayer(tmpPlayer);
            playerCount++;

            DebugLogger.info("Loaded player " + tmpPlayer + "("+playerCount+")");
            System.out.println(tmpPlayer.getName());
            for (InactiveMyPet myPet : tmpPlayer.getInactiveMyPets())
            {
                System.out.println(myPet.getUUID() + myPet.getPetName());
            }

        }

        rs.close();
        pstmt.close();

        return playerCount;
    }

    public static void writeAllMyPetPlayers() throws Exception{

        for(MyPetPlayer myPetPlayer : MyPetPlayer.getMyPetPlayers())
        {
            PreparedStatement pstmt = getConnection().prepareStatement(READ_PLAYER_SQL);
            pstmt.setString(1,myPetPlayer.getName());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
                PreparedStatement updateStatement = getConnection().prepareStatement(UPDATE_SQL_PLAYER);
                updateStatement.setString(2,myPetPlayer.getName());
                updateStatement.setObject(1,myPetPlayer);
                updateStatement.executeUpdate();
                updateStatement.close();
                rs.close();
                pstmt.close();
                return;
            }
            rs.close();
            pstmt.close();
            pstmt = getConnection().prepareStatement(WRITE_PLAYER_SQL);
            // set input parameters
            pstmt.setString(1, myPetPlayer.getName());
            pstmt.setObject(2, myPetPlayer);
            pstmt.executeUpdate();

            pstmt.close();
            System.out.println("writeJavaObject: wrote object to SQL for player: " + myPetPlayer.getName());
        }

    }

    public static void setupMySQL() {

        String inactiveMyPet_Table_Create = "CREATE TABLE IF NOT EXISTS InactiveMyPets " +
                "(owner VARCHAR(16) NOT NULL, " +
                " inactivemypet BLOB, " +
                " PRIMARY KEY ( owner ))";

        String myPetPlayer_Table_Create = "CREATE TABLE IF NOT EXISTS MyPetPlayers " +
                "(player VARCHAR(16) NOT NULL, " +
                " mypetplayer BLOB, " +
                " PRIMARY KEY ( player ))";

        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            statement.executeUpdate(inactiveMyPet_Table_Create);
            statement.executeUpdate(myPetPlayer_Table_Create);
            statement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
