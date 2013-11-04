/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

import java.sql.*;

/**
 *
 * @author gene
 */
public class DBEngine {

    /**
     * @param args the command line arguments
     */
    Connection conn = null;
    PreparedStatement pstLibraryRead, pstLibrarySearch, pstQueueRead;
    Statement stLibraryWrite, stFileRead, stQueueWrite;
    public ResultSet rsLibraryRead, rsLibrarySearch, rsFileRead, rsQueueRead;
    String dbloc="/home/aswin/music.db";
    DBEngine() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbloc);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void readLibrary() throws SQLException {
        pstLibraryRead = conn.prepareStatement("select * from music");
        rsLibraryRead = pstLibraryRead.executeQuery();

    }

    public void readQueue() throws SQLException {
        pstQueueRead = conn.prepareStatement("select * from queue");
        rsQueueRead = pstQueueRead.executeQuery();

    }

    public void searchLibrary(String q) throws SQLException {


        pstLibrarySearch = conn.prepareStatement("select * from music where title like '%"
                + q + "%' or album like '%"
                + q + "%' or artist like '%"
                + q + "%';");
        rsLibrarySearch = pstLibrarySearch.executeQuery();


    }

    public void writeLibrary(int id, String title, String album, String artist, String genre, String duration, String file) throws SQLException {
        Connection lconn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            lconn = DriverManager.getConnection("jdbc:sqlite:"+dbloc);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        stLibraryWrite = lconn.createStatement();
        String sql;

        sql = "INSERT INTO music VALUES("
                + id + ",'" + title.replace("'", "") + "','"
                + album.replace("'", "") + "','"
                + artist.replace("'", "") + "','"
                + genre.replace("'", "") + "','"
                + duration + "');";
        stLibraryWrite.executeUpdate(sql);
        sql = "INSERT INTO files VALUES("
                + id + ",'"
                + file.replace("'", "") + "');";
        stLibraryWrite.executeUpdate(sql);
        stLibraryWrite.close();
        lconn.close();

    }

    public void writeQueue(int id, String title, String album, String artist) throws SQLException {
        Connection lconn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            lconn = DriverManager.getConnection("jdbc:sqlite:"+dbloc);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        stQueueWrite = lconn.createStatement();
        String sql;
        try {
            if (!artist.equals("")) {
                sql = "INSERT INTO queue VALUES("
                        + id + ",'<html><body>"
                        + title.replace("'", "") + " <br>from <i>"
                        + album.replace("'", "") + "<br></i>by <i>"
                        + artist.replace("'", "") + "</i></body></html>');";
            } else {
                sql = "INSERT INTO queue VALUES("
                        + id + ",'<html><body>"
                        + title.replace("'", "") + " <br>from <i>"
                        + album.replace("'", "") + "<br></body></html>');";
            }

            stQueueWrite.executeUpdate(sql);
            stQueueWrite.close();
        } catch (java.sql.SQLException e) {
            System.out.println("sorry.. same thing exists");
        }
        lconn.close();


    }

    public String getFile(int id) throws SQLException {
        Connection lconn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            lconn = DriverManager.getConnection("jdbc:sqlite:"+dbloc);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        stFileRead = lconn.createStatement();
        String sql = "SELECT file from files where id=" + id + ";";
        rsFileRead = stFileRead.executeQuery(sql);
        String fname = rsFileRead.getString("file");

        stFileRead.close();
        lconn.close();
        return fname;
    }

    public String getTitle(int id) throws SQLException {
        Connection lconn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            lconn = DriverManager.getConnection("jdbc:sqlite:"+dbloc);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        stFileRead = lconn.createStatement();
        String sql = "SELECT title from music where id=" + id + ";";
        rsFileRead = stFileRead.executeQuery(sql);
        String title = rsFileRead.getString("title");

        stFileRead.close();
        lconn.close();
        return title;
    }

    public String getArtist(int id) throws SQLException {
        Connection lconn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            lconn = DriverManager.getConnection("jdbc:sqlite:"+dbloc);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        stFileRead = conn.createStatement();
        String sql = "SELECT artist from music where id=" + id + ";";
        rsFileRead = stFileRead.executeQuery(sql);
        String artist = rsFileRead.getString("artist");

        stFileRead.close();
        lconn.close();
        return artist;
    }

    public String getAlbum(int id) throws SQLException {
        Connection lconn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            lconn = DriverManager.getConnection("jdbc:sqlite:"+dbloc);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        stFileRead = lconn.createStatement();
        String sql = "SELECT album from music where id=" + id + ";";
        rsFileRead = stFileRead.executeQuery(sql);
        String album = rsFileRead.getString("album");

        stFileRead.close();
        lconn.close();
        return album;
    }

    public int getLength(int id) throws SQLException {
        Connection lconn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            lconn = DriverManager.getConnection("jdbc:sqlite:"+dbloc);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        stFileRead = lconn.createStatement();
        String sql = "SELECT duration from music where id=" + id + ";";
        rsFileRead = stFileRead.executeQuery(sql);
        String dur = rsFileRead.getString("duration");
        int time = Integer.parseInt(dur.split(":")[0]) * 60 + Integer.parseInt(dur.split(":")[1]);
        stFileRead.close();
        lconn.close();
        return time;
    }

    public void popQueue(int id) throws SQLException {
        Connection lconn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            lconn = DriverManager.getConnection("jdbc:sqlite:"+dbloc);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        stQueueWrite = lconn.createStatement();
        String sql;
        try {
            sql = "delete from queue where id=" + id + ";";
            stQueueWrite.executeUpdate(sql);
            stQueueWrite.close();
        } catch (java.sql.SQLException e) {
            System.out.println("sorry.. " + e);
        }
        lconn.close();



    }

    public void clearQueue() throws SQLException {
        Connection lconn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            lconn = DriverManager.getConnection("jdbc:sqlite:"+dbloc);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        stQueueWrite = lconn.createStatement();
        String sql;

        sql = "delete from queue;";
        stQueueWrite.executeUpdate(sql);
        stQueueWrite.close();

        lconn.close();

    }

    protected void finalize() throws SQLException, Throwable {
        try {
            pstLibraryRead.close();
            stLibraryWrite.close();
            stFileRead.close();
            stQueueWrite.close();

            pstLibrarySearch.close();

            conn.commit();
            conn.close();

        } finally {
            super.finalize();
        }

    }
}
