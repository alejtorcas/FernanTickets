package dao;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Clase para modificar y consultar la tabla Users del la base de datos
 * @author Alejandro Torres Castillo
 * @version 1.0
 */
public class DAOUser {

    /**
     * Función privada que se ejecuta para actualizar datos de la base de datos
     * @param sql String con la ejecución del script para insertar, modificar o borrar usuarios
     * @param dao DAOManager que contiene la conexión a la base de datos
     * @return devuelve true si la ejecución de script funciona y false si no se ejecuta correctamente
     */
    private boolean executeUpdate(String sql, DAOManager dao){
        try (Statement stmt = dao.getConn().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException ex) {return false;}}

    /**
     * Función privada que devuelve un conjunto de registros de la base de datos
     * @param sql String con la ejecución de un select para consultar usuarios
     * @param dao DAOManager que contiene la conexión a la base de datos
     * @return Devuelve un ArrayList de Users resultantes de hacer una consulta en la base de datos
     */
    private ArrayList<String> executeQuery(String sql, String label, DAOManager dao){
        ArrayList<String> data = new ArrayList<>();
        try{
            PreparedStatement ps = dao.getConn().prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) data.add(rs.getString(label));
            }
        }catch (SQLException ex) {ex.printStackTrace();}
        return data;}

    /**
     * Inserta un User en la base de datos
     * @param user Usuario que se inserta en la tabla Users de la base de datos
     */
    public boolean insertUser(User user, DAOManager dao) {
        String sql = "INSERT INTO USERS "
                + "VALUES ('" + user.getId() + "','" + user.getName() + "','"
                + user.getLastname() + "','" + user.getPassword() + "','" + user.getEmail() + "','"
                + (user.getClass().equals(Admin.class)?"admin":(user.getClass().equals(Technical.class)?"technical":"user")) + "');";
        return executeUpdate(sql,dao);}

    /**
     * Actualiza la contraseña de un User existente
     * @param user Usuario con la contraseña actualizada
     */
    public boolean updatePassword(User user, DAOManager dao){
        String sql = "UPDATE USERS SET USER_PASSWORD = '" + user.getPassword() + "' WHERE ID = '" + user.getId() + "';";
        return executeUpdate(sql,dao);}

    /**
     * Elimina un User de la base de datos a partir de su Identificador único
     * @param id Identificador único del usuario a eliminar
     */
    public boolean deleteUser(String id, DAOManager dao){
        String sql = "DELETE FROM USERS WHERE ID = '" + id + "';";
        DAOIncident daoIncident = new DAOIncident();
        try(Statement stmt = dao.getConn().createStatement()){
            if (daoIncident.deleteUserId(id,dao) || daoIncident.deleteTechnicalId(id,dao)) {
                stmt.executeUpdate(sql);
                return true;
            } else return false;
        }catch(SQLException ex){return false;}}

    /**
     * Devuelve una colección de Identificadores únicos de todos los Users registrados en la base de datos
     */
    public ArrayList<String> usersIds(DAOManager dao){
        String sql = "SELECT ID FROM USERS;";
        return executeQuery(sql,"ID",dao);}

    /**
     * Devuelve una colección de Identificadores únicos de todos lo Technicals registrados en la base de datos
     */
    public ArrayList<String> technicalsIds(DAOManager dao){
        String sql = "SELECT ID FROM USERS WHERE USER_STATUS = 'technical';";
        return executeQuery(sql,"ID",dao);}

    /**
     * Devuelve una colección de todos los correos electrónicos de los Users registrados en la base de datos
     */
    public ArrayList<String> usersEmails(DAOManager dao){
        String sql = "SELECT EMAIL FROM USERS;";
        return executeQuery(sql,"EMAIL",dao);}

    /**
     * Devuelve un User con el que trabajar si coinciden el correo electrónico y la contraseña
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     */
    public User login(String email, String password, DAOManager dao){
        User user = null;
        String sql = "SELECT * FROM USERS WHERE EMAIL = '" + email + "' AND USER_PASSWORD = '" + password + "';";
        try{
            PreparedStatement ps = dao.getConn().prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    if (rs.getString("USER_STATUS").equals("admin")){
                        user = new Admin(rs.getString("ID"),rs.getString("USER_NAME")
                                ,rs.getString("LASTNAME"),rs.getString("EMAIL")
                                ,rs.getString("USER_PASSWORD"));}
                    if (rs.getString("USER_STATUS").equals("technical")){
                        user = new Technical(rs.getString("ID"),rs.getString("USER_NAME")
                                ,rs.getString("LASTNAME"),rs.getString("EMAIL"),
                                rs.getString("USER_PASSWORD"));}
                    if (rs.getString("USER_STATUS").equals("user")){
                        user = new FinalUser(rs.getString("ID"),rs.getString("USER_NAME")
                                ,rs.getString("LASTNAME"),rs.getString("EMAIL"),
                                rs.getString("USER_PASSWORD"));}
                }
            }
        }catch (SQLException ex) {ex.printStackTrace();}
        return user;}

    /**
     * Devuelve un Technical a partir de su Identificador único
     * @param id Identificador único del técnico
     */
    public Technical getTechnical(String id, DAOManager dao){
        Technical technical = null;
        String sql = "SELECT * FROM USERS WHERE ID = '" + id + "';";
        try{
            PreparedStatement ps = dao.getConn().prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    technical = new Technical(rs.getString("ID"),rs.getString("USER_NAME")
                            ,rs.getString("LASTNAME"),rs.getString("EMAIL")
                            ,rs.getString("USER_PASSWORD"));
                }
            }
        }catch (SQLException ex) {ex.printStackTrace();}
        return technical;}

    /**
     * Devuelve una colección de todos los Technical registrados en la base de datos
     */
    public ArrayList<Technical> getTechnicals(DAOManager dao){
        ArrayList<Technical> technicals = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE USER_STATUS = 'technical'";
        try{
            PreparedStatement ps = dao.getConn().prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    technicals.add(new Technical(rs.getString("ID"),rs.getString("USER_NAME")
                            ,rs.getString("LASTNAME"),rs.getString("EMAIL")
                            ,rs.getString("USER_PASSWORD")));
                }
            }
        }catch (SQLException ex) {ex.printStackTrace();}
        return technicals;}

    /**
     * Devuelve una colección de todos los FinalUsers registrados en la base de datos
     */
    public ArrayList<User> getAllUsers(DAOManager dao){
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS;";
        try{
            PreparedStatement ps = dao.getConn().prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    if (rs.getString("USER_STATUS").equals("admin")){
                        users.add(new Admin(rs.getString("ID"),rs.getString("USER_NAME")
                                ,rs.getString("LASTNAME"),rs.getString("EMAIL")
                                ,rs.getString("USER_PASSWORD")));}
                    if (rs.getString("USER_STATUS").equals("technical")){
                        users.add(new Technical(rs.getString("ID"),rs.getString("USER_NAME")
                                ,rs.getString("LASTNAME"),rs.getString("EMAIL")
                                ,rs.getString("USER_PASSWORD")));}
                    if (rs.getString("USER_STATUS").equals("user")){
                        users.add(new FinalUser(rs.getString("ID"),rs.getString("USER_NAME")
                                ,rs.getString("LASTNAME"),rs.getString("EMAIL")
                                ,rs.getString("USER_PASSWORD")));}
                }
            }
        }catch (SQLException ex) {ex.printStackTrace();}
        return users;}
}
