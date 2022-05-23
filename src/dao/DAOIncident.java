package dao;

import model.Incident;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Clase para modificar y consultar la tabla Incidents del la base de datos
 * @author Alejandro Torres Castillo
 * @version 1.0
 */
public class DAOIncident {

    /**
     * Función privada que se ejecuta para actualizar datos de la base de datos
     * @param sql String con la ejecución del script para insertar, modificar o borrar incidencias
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
     * @param sql String con la ejecución de un select para consultar incidencias
     * @param dao DAOManager que contiene la conexión a la base de datos
     * @return Devuelve un ArrayList de Incidents resultantes de hacer una consulta en la base de datos
     */
    private ArrayList<Incident> executeQuery(String sql, DAOManager dao){
        ArrayList<Incident> incidents = new ArrayList<>();
        try{
            PreparedStatement ps = dao.getConn().prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    incidents.add(new Incident(rs.getInt("PRIORITY"),rs.getString("INC_DESCRIPTION")
                            ,rs.getString("INC_SOLUTION"),rs.getString("ID")
                            ,rs.getString("ID_USER"),rs.getString("ID_TECHNICAL")
                            ,rs.getBoolean("SOLVED"),rs.getDate("OPEN_DATE")
                            ,rs.getDate("CLOSE_DATE")));
                }
            }
        }catch (SQLException ex) {ex.printStackTrace();}
        return incidents;}

    /**
     * Función privada que devuelve un ArrayList de Strings (los Ids de las incidencias)
     * @param sql Scrip sql para seleccionar los Ids específicos según requisitos
     * @param dao DAOManager que contiene la conexión a la base de datos
     * @return Devuelve un ArrayList de Strings(Ids) resultantes de hacer una consulta en la base de datos
     */
    private ArrayList<String> executeQueryStrings(String sql, DAOManager dao){
        ArrayList<String> incidentsIds = new ArrayList<>();
        try{
            PreparedStatement ps = dao.getConn().prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    incidentsIds.add(rs.getString("ID"));
                }
            }
        }catch (SQLException ex) {ex.printStackTrace();}
        return incidentsIds;}

    /**
     * Función que devuelve un Incident a partir de Id
     * @param id Identificador de la incidencia
     * @param dao DAOManager que contiene la conexión a la base de datos
     * @return Incidencia asociada al Id especificado o null si no existe el Id
     */
    public Incident searchIncidentById(String id, DAOManager dao) {
        Incident incident = null;
        String sql = "SELECT * FROM INCIDENTS WHERE ID = '" + id + "';";
        try{
            PreparedStatement ps = dao.getConn().prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    incident = new Incident(rs.getInt("PRIORITY"),rs.getString("INC_DESCRIPTION")
                            ,rs.getString("INC_SOLUTION"),rs.getString("ID")
                            ,rs.getString("ID_USER"),rs.getString("ID_TECHNICAL")
                            ,rs.getBoolean("SOLVED"),rs.getDate("OPEN_DATE")
                            ,rs.getDate("CLOSE_DATE"));
                }
            }
        }catch (SQLException ex) {ex.printStackTrace();}
        return incident;}

    /**
     * Inserta un Incident en la base da datos
     * @param incident Incidencia que se inserta en la tabla Incidents de la bse de datos
     */
    public boolean insertIncidents(Incident incident, DAOManager dao) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String sql = "INSERT INTO INCIDENTS(ID, PRIORITY, INC_DESCRIPTION, ID_USER, OPEN_DATE) "
                + "VALUES ('" + incident.getId() + "','" + incident.getPriority() + "','"
                + incident.getDescription() + "','" + incident.getIdUser() + "','" + sdf.format(incident.getOpenDate()) + "');";
        return executeUpdate(sql, dao);}

    /**
     * Asocia un Incident con un Technical
     * @param incident Incidencia que contiene Identificador único del técnico
     */
    public boolean assignIncident(Incident incident, DAOManager dao) {
        String sql = "UPDATE INCIDENTS SET ID_TECHNICAL = '" + incident.getIdTechnical()
                + "' WHERE ID = '" + incident.getId() + "';";
        return executeUpdate(sql, dao);}

    /**
     * Marca un objeto Incident como solucionado
     * @param incident Incidencia con la solución
     */
    public boolean solveIncident(Incident incident, DAOManager dao) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String sql = "UPDATE INCIDENTS SET INC_SOLUTION = '" + incident.getSolution() + ""
                + "', CLOSE_DATE = '" + sdf.format(incident.getCloseDate()) + "', SOLVED = true WHERE ID = '" + incident.getId() + "';";
        return executeUpdate(sql, dao);}

    /**
     * Elimina el Identificador único del usuario FinalUser del todos los objetos Incidents
     * @param userId Identificador único del usuario
     */
    public boolean deleteUserId(String userId, DAOManager dao) {
        String sql = "UPDATE INCIDENTS SET ID_USER = NULL WHERE ID_USER = '" + userId + "';";
        return executeUpdate(sql, dao);}

     /**
     * Elimina el Identificador único del usuario Technical de todos los objetos Incident
     * @param technicalId Identificado único del técnico
     */
    public boolean deleteTechnicalId(String technicalId, DAOManager dao) {
        String sql = "UPDATE INCIDENTS SET ID_TECHNICAL = NULL WHERE ID_TECHNICAL = '" + technicalId + "';";
        return executeUpdate(sql, dao);}

     /**
     * Devuelve una colección de Incidents que contengan el Identificador único de algún FinalUser
     * @param id_user Identificador único del usuario
     */
    public ArrayList<Incident> searchIncidentsByUserId(String id_user, DAOManager dao) {
        String sql = "SELECT * FROM INCIDENTS WHERE ID_USER = '" + id_user + "' ORDER BY ;";
        return executeQuery(sql,dao);}

     /**
     * Devuelve una colección de Incidents que contengan el Identificado único de algún Technical
     * @param id_technical Identificador único del técnico
     */
    public ArrayList<Incident> searchIncidentsByTechnicalId(String id_technical, DAOManager dao) {
        String sql = "SELECT * FROM INCIDENTS WHERE ID_TECHNICAL = '" + id_technical + "';";
        return executeQuery(sql,dao);}

     /**
     * Devuelve una colección de Incidents que contengan el término pasado como parámetro en la descripción o la solución
     * @param term Término de búsqueda
     */
    public ArrayList<Incident> searchIncidentByTerm(String term, DAOManager dao) {
        String sql = "SELECT * FROM INCIDENTS WHERE INC_DESCRIPTION LIKE '%" + term + "%' OR INC_SOLUTION LIKE '%" + term + "%';";
        return executeQuery(sql,dao);}

     /**
     * Devuelve una colección de Incidents que sean de la prioridad indicada
     * @param priority Número entero comprendido entre 1 y 10
     */
    public ArrayList<Incident> searchIncidentsByPriority(int priority, DAOManager dao){
        String sql = "SELECT * FROM INCIDENTS WHERE PRIORITY = " + priority + ";";
        return executeQuery(sql,dao);}

     /**
     * Devuelve una colección de Incidents sin resolver que aún no han sido asignadas a ningún Technical
     */
    public ArrayList<Incident> searchUnsignedIncidents(DAOManager dao){
        String sql = "SELECT * FROM INCIDENTS WHERE ID_TECHNICAL IS NULL AND SOLVED = FALSE;";
        return executeQuery(sql,dao);}

     /**
     * Devuelve una colección de todos los Incidents sin resolver
     */
    public ArrayList<Incident> searchOpenIncidents(DAOManager dao){
        String sql = "SELECT * FROM INCIDENTS WHERE SOLVED = FALSE;";
        return executeQuery(sql,dao);}

     /**
     * Devuelve una colección de todos los Incidents registrados
     */
    public ArrayList<Incident> getAllIncidents(DAOManager dao){
        String sql = "SELECT * FROM INCIDENTS;";
        return executeQuery(sql,dao);}

     /**
     * Devuelve una colección de todos los Incidents solucionados
     */
    public ArrayList<Incident> searchSolvedIncidents(DAOManager dao){
        String sql = "SELECT * FROM INCIDENTS WHERE SOLVED = TRUE;";
        return executeQuery(sql,dao);}

     /**
     * Devuelve una colección de Incidents solucionados que contengan el identificador único de un FinalUser o un Technical
     * @param userId Identificador único del usuario/técnico
     */
    public ArrayList<Incident> searchSolvedIncidentsByUserId(String userId, DAOManager dao){
        String sql = "SELECT * FROM INCIDENTS WHERE (ID_USER = '" + userId + "' OR ID_TECHNICAL = '" + userId + "') AND SOLVED = TRUE;";
        return executeQuery(sql,dao);}

     /**
     * Devuelve una colección de Incidents sin resolver que contengan el identificador único de un FinalUser o un Technical
     * @param userId Identificador único del usuario/técnico
     */
    public ArrayList<Incident> searchUnsolvedIncidentsByUserId(String userId, DAOManager dao){
        String sql = "SELECT * FROM INCIDENTS WHERE (ID_USER = '" + userId + "' OR ID_TECHNICAL = '" + userId + "') AND SOLVED = FALSE;";
        return executeQuery(sql,dao);}

     /**
     * Devuelve una colección de Identificadores únicos de todos los Incidents
     */
    public ArrayList<String> incidentsIds(DAOManager dao){
        String sql = "SELECT ID FROM INCIDENTS;";
        return executeQueryStrings(sql,dao);}

     /**
     * Devuelve una colección de Identificadores únicos de lo Incidents sin asignar
     */
    public ArrayList<String> unsignedIncidentsIds(DAOManager dao){
        String sql = "SELECT ID FROM INCIDENTS WHERE ID_TECHNICAL IS NULL AND SOLVED = FALSE;";
        return executeQueryStrings(sql,dao);}

     /**
     * Devuelve una colección de Incidents sin resolver que contengan el Identificador único de un Technical
     * @param technicalId Identificador único del técnico
     */
    public ArrayList<String> openIncidentsIds(String technicalId, DAOManager dao){
        String sql = "SELECT ID FROM INCIDENTS WHERE ID_TECHNICAL = '" + technicalId + "' AND SOLVED = FALSE;";
        return executeQueryStrings(sql,dao);}
}