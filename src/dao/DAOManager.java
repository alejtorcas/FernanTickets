package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOManager {

    // Atributos
    private Connection conn;
    private final String URL;
    private final String USER;
    private final String PASS;
    private static DAOManager singleTon; // Atributo estático que guarda una referencia al DAO

    // Constructor PRIVADO para que no se pueda utilizar desde el exterior
    private DAOManager() {
        this.conn = null;
        this.URL = "jdbc:mysql://127.0.0.1:1433/TICKETS_FINAL"; // Dirección del servidor y de BD a usar
        this.USER = "root"; // Usuario de la base de datos
        this.PASS = "";} // Clave de la base de datos

    // "Constructor" PÚBLICO. Comprueba si el atributo singleTon ya tiene valor.
    // Si no lo tiene, crea la conexión y el objeto.
    // Si lo tiene, devuelve null para que no se creen más objetos de la clase DAOManager.
    public static DAOManager getSinglenTonInstance(){
        if (singleTon == null) {
            singleTon = new DAOManager();
            return singleTon;
        }else return null;}

    // Abre la conexión con BD y la guarda en conn
    public void open() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver"); //Cargo el driver de conexión JDBC
        conn = DriverManager.getConnection(URL, USER, PASS);} //Uso la clase DriverManager para crear la conexión

    // Devuelve la conexión con la BD
    public Connection getConn() {return conn;}

    // Cierra la conexión con BD
    public void close() throws SQLException {if(this.conn!=null) this.conn.close();}
}