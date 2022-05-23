package controller;

import model.*;
import dao.DAOIncident;
import dao.DAOManager;
import dao.DAOUser;
import view.TicketsView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Controlador de FernanTickets
 * @author Alejandro Torres Castillo
 * @version 1.0
 */
public class TicketsController {

    /**
     * Constantes que necesita el controlador para funcionar
     */
    private final TicketsView tv = new TicketsView();
    private final DAOManager dao = DAOManager.getSinglenTonInstance();
    private final DAOIncident daoIncident = new DAOIncident();
    private final DAOUser daoUser = new DAOUser();

    /**
     * Lector de datos, imprime el dato solicitado y lee la siguiente linea
     * @param data String que contiene el dato solicitado
     */
    private String reader(String data){
        System.out.print(data);
        return new Scanner(System.in).nextLine();}

    /**
     * Lector de números enteros usado para los menus, devuelve un número insertado por el usuario activo
     * dentro de un rango específico (1-max)
     * @param max Última opción del rango
     * @return int dentro del rango
     */
    private int check(int max){
        int option = 0;
        while (option < 1 || option > max){
            try{
                option = Integer.parseInt(reader(tv.arrow()));
                if (option < 1 || option > max) tv.numberOutOfRange(max);
            }catch (NumberFormatException e){tv.nfe();}
        }
        return option;}

    /**
     * Función que lee los credenciales del usuario
     * @return User (Admin,Technical o FinalUser) con los credenciales indicados o null si no existe
     */
    private User login(){
        String email = reader(tv.user()), password = reader(tv.password());
        return daoUser.login(email,password,Objects.requireNonNull(dao));}

    /**
     * Genera una cadena de 5 cifras aleatorias
     * @return String con 5 cifras aleatorias
     */
    private String generateId(){
        StringBuilder id = new StringBuilder();
        id.append((int)(Math.random() * 99998) + 2);
        while(id.length() < 5) id.insert(0, "0");
        return id.toString();}

    /**
     * Genera un Identificador único de usuario
     * @return Identificador único de un nuevo User
     */
    private String newUserId(){
        String newId = null;
        while( newId == null || daoUser.usersIds(Objects.requireNonNull(dao)).contains(newId) ) newId = generateId();
        return newId;}

    /**
     * Genera un Identificador único de incidencia
     * @return Identificador único de un nuevo Incident
     */
    private String newIncidentId(){
        String newId = null;
        while( newId == null || daoIncident.incidentsIds(Objects.requireNonNull(dao)).contains(newId) ) newId = generateId();
        return newId;}

    /**
     * Muestra por pantalla todos los Incidents sin resolver
     */
    private void showAllOpenIncidents(){
        ArrayList<Incident> incidents = daoIncident.searchOpenIncidents(Objects.requireNonNull(dao));
        if (incidents.size() != 0) for (Incident inc : incidents) tv.incidentToString(inc);
        else tv.notFoundOpenIncidents();
        reader(tv.anyKey());}

    /**
     * Muestra por pantalla todos los Incidents solucionados
     */
    private void showAllClosedIncidents(){
        ArrayList<Incident> incidents = daoIncident.searchSolvedIncidents(Objects.requireNonNull(dao));
        if (incidents.size() != 0) for (Incident inc : incidents) tv.incidentToString(inc);
        else tv.notFoundSolvedIncidents();
        reader(tv.anyKey());}

    /**
     * Muestra el menu de búsqueda de los Incidents y solicita una opción por teclado
     */
    private void searchIncidents(){
        boolean flag = true;
        do{
            tv.searchIncidentMenu();
            switch (check(7)){
                case 1 -> searchIncidentById();
                case 2 -> searchIncidentsByUser();
                case 3 -> searchIncidentsByTechnical();
                case 4 -> searchIncidentsByTerm();
                case 5 -> searchIncidentsByPriority();
                case 6 -> flag = false;
            }
        }while(flag);}

    /**
     * Solicita un Identificador único de Incidents para buscar y lo muestra
     */
    private void searchIncidentById(){
        String id = reader(tv.incidentId());
        Incident inc = daoIncident.searchIncidentById(id,Objects.requireNonNull(dao));
        if (inc != null) tv.incidentToString(inc);
        else tv.notFoundIncident();
        reader(tv.anyKey());}

    /**
     * Solicita el Identificador único de un FinalUser y muestra los Incidents que lo contengan
     */
    private void searchIncidentsByUser(){
        String id = reader(tv.userId());
        ArrayList<Incident> incidents = daoIncident.searchIncidentsByUserId(id,Objects.requireNonNull(dao));
        if (incidents.size() != 0) for (Incident inc : incidents) tv.incidentToString(inc);
        else tv.notFoundIncidentsByUserId();
        reader(tv.anyKey());}

    /**
     * Solicita el Identificador único de un Technical y muestra los Incidents que lo contengan
     */
    private void searchIncidentsByTechnical(){
        String id = reader(tv.userId());
        ArrayList<Incident> incidents = daoIncident.searchIncidentsByTechnicalId(id,Objects.requireNonNull(dao));
        if (incidents.size() != 0) for (Incident inc : incidents) tv.incidentToString(inc);
        else tv.notFoundIncidentsByUserId();
        reader(tv.anyKey());}

    /**
     * Solicita un término de búsqueda y muestra los Incidents que lo contengan
     */
    private void searchIncidentsByTerm(){
        String term = reader(tv.term());
        ArrayList<Incident> incidents = daoIncident.searchIncidentByTerm(term,Objects.requireNonNull(dao));
        if (incidents.size() != 0) for (Incident inc : incidents) tv.incidentToString(inc);
        else tv.notFoundIncidentsByTerm();
        reader(tv.anyKey());}

    /**
     * Solicita un nivel de prioridad y muestra los Incident con ese nivel de prioridad
     */
    private void searchIncidentsByPriority(){
        tv.priority();
        int priority = check(10);
        ArrayList<Incident> incidents = daoIncident.searchIncidentsByPriority(priority,Objects.requireNonNull(dao));
        if (incidents.size() != 0) for (Incident inc : incidents) tv.incidentToString(inc);
        else tv.notFoundIncidentsByPriority();
        reader(tv.anyKey());}

    /**
     * Comprueba que el objeto User que se va a registrar en la base de datos no tenga campos obligatorios vacíos,
     * que la contraseña tenga una longitud mínima de 4 caracteres y que el correo electrónico no esté registrado.
     * Después muestra una vista previa del User y solicita una confirmación antes de insertarlo en la base de datos
     * @param user User proporcionado para las comprobaciones
     */
    private boolean checkUserData(User user){
        ArrayList<String> emails = daoUser.usersEmails(Objects.requireNonNull(dao));
        if (user.getPassword().isEmpty() || user.getEmail().isEmpty() || user.getName().isEmpty()) {
            tv.emptyDataUser();
            return true;
        }
        if (user.getPassword().length()<4){
            tv.shortPassword();
            return true;
        }
        if (!emails.contains(user.getEmail())){
            tv.checkUser(user);
            switch(check(3)){
                case 1 -> {
                    if (daoUser.insertUser(user,dao)) tv.insertUser(user);
                    return false;
                }
                case 2 -> {return true;}
                case 3 -> {return false;}
            }
        }else tv.emailAlreadyRegistered();
        return true;}

    /**
     * Solicita los datos necesarios para crear un Technical
     */
    private void insertTechnical(){
        boolean flag;
        do {
            Technical technical = new Technical(newUserId(),reader(tv.name()),reader(tv.lastname()),reader(tv.user()),reader(tv.password()));
            flag = checkUserData(technical);
        }while(flag);}

    /**
     * Solicita los datos necesarios para crear un FinalUser
     */
    private void insertUser(){
        boolean flag;
        do {
            FinalUser user = new FinalUser(newUserId(),reader(tv.name()),reader(tv.lastname()),reader(tv.user()),reader(tv.password()));
            flag = checkUserData(user);
        }while(flag);}

    /**
     * Muestra por pantalla Todos los Users registrados en la base de datos
     */
    private void showAllUsers(){
        ArrayList<User> users = daoUser.getAllUsers(Objects.requireNonNull(dao));
        if (users.size() != 0) for (User user : users) tv.userToString(user);
        else tv.notFoundUsers();
        reader(tv.anyKey());}

    /**
     * Solicita el identificado único de un User para eliminarlo de la base de datos
     */
    private void deleteUser(){
        String id = reader(tv.userId());
        if (daoUser.usersIds(Objects.requireNonNull(dao)).contains(id)){
            if (daoUser.deleteUser(id,dao)) tv.deleteUser();
        } else tv.notFoundUser();
        reader(tv.anyKey());}

    /**
     * Comprueba si existen Incidents sin asignar y si existen Technical en la base de datos, Después solicita el
     * Identificador único de un Incident sin asignar y el Identificado único del Technical, muestra una vista previa
     * de la asignación y solicita confirmación antes de actualizar la base de datos
     */
    private boolean checkAssignIncident() {
        ArrayList<Incident> incidents = daoIncident.searchUnsignedIncidents(Objects.requireNonNull(dao));
        ArrayList<Technical> technicals = daoUser.getTechnicals(Objects.requireNonNull(dao));
        if (incidents.isEmpty() || technicals.isEmpty()) {
            tv.cantAssignIncidents();
            return false;
        }
        String incidentId, technicalId;
        ArrayList<String> incidentsIds = daoIncident.unsignedIncidentsIds(Objects.requireNonNull(dao));
        ArrayList<String> technicalsIds = daoUser.technicalsIds(Objects.requireNonNull(dao));
        for (Incident inc : incidents) tv.incidentToString(inc);
        do {
            incidentId = reader(tv.incidentId());
            if (!incidentsIds.contains(incidentId)) tv.notFoundIncident();
        } while (!incidentsIds.contains(incidentId));
        Incident incident = daoIncident.searchIncidentById(incidentId,Objects.requireNonNull(dao));
        for (Technical technical : technicals) tv.userToString(technical);
        do {
            technicalId = reader(tv.technicalId());
            if (!technicalsIds.contains(technicalId)) tv.notFoundTechnical();
        } while (!technicalsIds.contains(technicalId));
        incident.assignIncident(technicalId);
        tv.checkAssignIncident(daoIncident.searchIncidentById(incidentId,Objects.requireNonNull(dao)),daoUser.getTechnical(technicalId,Objects.requireNonNull(dao)));
        switch(check(3)){
            case 1 -> {
                if (daoIncident.assignIncident(incident, Objects.requireNonNull(dao))) tv.assignIncident();
                return false;
            }
            case 2 -> {return true;}
            case 3 -> {return false;}
        }
        return true;}


    /**
     * Bucle que controla la asignación de incidencias
     */
    private void assignIncident(){
        boolean flag;
        do{
            flag = checkAssignIncident();
        }while(flag);}

    /**
     * Muestra las estadísticas de la aplicación
     */
    private void dashboard(){
        ArrayList<User> users = daoUser.getAllUsers(Objects.requireNonNull(dao));
        ArrayList<Incident> incidents = daoIncident.getAllIncidents(Objects.requireNonNull(dao));
        tv.dashboard(users,incidents);
        reader(tv.anyKey());}


    /**
     * Muestra por pantalla todos los Incidents sin solucionar que estén relacionados con el User pasado como parámetro
     */
    private void showOpenIncidents(User user){
        ArrayList<Incident> incidents = daoIncident.searchUnsolvedIncidentsByUserId(user.getId(),Objects.requireNonNull(dao));
        if (incidents.size() != 0) for (Incident inc : incidents) tv.incidentToString(inc);
        else tv.notFoundOpenIncidents();
        reader(tv.anyKey());}

    /**
     * Muestra por pantalla todos los Incidents solucionados que estén relacionados con el User pasado como parámetro
     */
    private void showSolvedIncidents(User user){
        ArrayList<Incident> incidents = daoIncident.searchSolvedIncidentsByUserId(user.getId(),Objects.requireNonNull(dao));
        if (incidents.size() != 0) for (Incident inc : incidents) tv.incidentToString(inc);
        else tv.notFoundSolvedIncidents();
        reader(tv.anyKey());}

    /**
     * Actualiza la contraseña del User pasado como parámetro, solicitando antes la antigua contraseña
     */
    private void updatePassword(User user){
        String newPassword;
        if (user.getPassword().equals(reader(tv.password()))){
            boolean flag = true;
            do{
                newPassword = reader(tv.newPassword());
                if (newPassword.equals(reader(tv.checkNewPassword())) && newPassword.length()>3){
                    user.setPassword(newPassword);
                    if (daoUser.updatePassword(user,Objects.requireNonNull(dao))) tv.successPasswordUpdate();
                    flag = false;
                }
            }while(flag);

        }else{
            tv.wrongPassword();
        }
        reader(tv.anyKey());}

    /**
     * Comprueba que la descripción del Incident pasada como parámetro no este vacío, muestra una vista previa
     * y solicita confirmación antes de insertarla en la base de datos
     * @param incident Incidencia proporcionada para las comprobaciones
     */
    private boolean checkIncidentData(Incident incident, DAOManager dao){
        if (incident.getDescription().isEmpty()){
            tv.emptyDataIncident();
            return true;
        }else{
            tv.checkIncident(incident);
            switch (check(3)){
                case 1 ->{
                    if (daoIncident.insertIncidents(incident,Objects.requireNonNull(dao))) tv.insertIncident();
                    return false;
                }
                case 2 -> {return true;}
                case 3 -> {return false;}
            }
        }
        return true;}

    /**
     * Solicita los datos necesarios para crear un Incident
     */
    private void insertIncident(User user){
        boolean flag;
        do{
            tv.priority();
            Incident incident = new Incident(newIncidentId(),check(10),user.getId(),reader(tv.incidentDescription()));
            flag = checkIncidentData(incident, dao);
        }while(flag);}

    /**
     * Solicita la solución de una incidencia sin resolver y la guarda en la base de datos
     */
    private void solveIncident(User user){
        ArrayList<Incident> incidents = daoIncident.searchUnsolvedIncidentsByUserId(user.getId(),Objects.requireNonNull(dao));
        ArrayList<String> incidentsIds = daoIncident.openIncidentsIds(user.getId(),Objects.requireNonNull(dao));
        boolean flag = true;
        do{
            for (Incident incident : incidents) tv.incidentToString(incident);
            String incidentId;
            do{
                incidentId = reader(tv.incidentId());
                if (!incidentsIds.contains(incidentId)) tv.notFoundIncident();
            }while(!incidentsIds.contains(incidentId));
            Incident incident = daoIncident.searchIncidentById(incidentId,Objects.requireNonNull(dao));
            do{
                incident.solveIncident(reader(tv.incidentSolution()));
                if(incident.getSolution().isEmpty()) tv.emptyDataIncident();
            }while(incident.getSolution().isEmpty());
            tv.checkSolveIncident(incident);
            switch (check(3)){
                case 1 -> {
                    if (daoIncident.solveIncident(incident,Objects.requireNonNull(dao))) tv.successSolvedIncident();
                    flag = false;
                }
                case 3 -> flag = false;
            }
        }while(flag);}

    /**
     * Muestra el perfil del usuario pasado como parámetro
     * @param user User que se muestra por pantalla
     */
    private void showProfile(User user){
        tv.userToString(user);
        reader(tv.anyKey());}

    /**
     * Estructura básica de FernanTicket, donde se muestran los menús básicos de la aplicación
     * y se llaman a las funciones del controlador
     */
    public void on() {
        boolean appOn = true;
        do{
            try {
                Objects.requireNonNull(dao).open();
            }catch (Exception e){
                e.printStackTrace();
            }
            tv.loginMenu();
            switch (check(2)){
                case 1 -> {
                    User activeUser = login();
                    if (activeUser != null){
                        boolean login = true;
                        do{
                            tv.userMenu(activeUser);
                            if(activeUser.getClass().equals(Admin.class)){
                                switch (check(11)){
                                    case 1 -> showAllOpenIncidents();
                                    case 2 -> showAllClosedIncidents();
                                    case 3 -> searchIncidents();
                                    case 4 -> insertTechnical();
                                    case 5 -> showAllUsers();
                                    case 6 -> deleteUser();
                                    case 7 -> assignIncident();
                                    case 8 -> dashboard();
                                    case 9 -> login = false;
                                    case 10 ->{ appOn = false; login = false; }
                                }
                            }
                            if(activeUser.getClass().equals(Technical.class)){
                                switch (check(6)){
                                    case 1 -> showOpenIncidents(activeUser);
                                    case 2 -> solveIncident(activeUser);
                                    case 3 -> showSolvedIncidents(activeUser);
                                    case 4 -> showProfile(activeUser);
                                    case 5 -> updatePassword(activeUser);
                                    case 6 -> login = false;
                                }
                            }
                            if(activeUser.getClass().equals(FinalUser.class)){
                                switch (check(6)){
                                    case 1 -> insertIncident(activeUser);
                                    case 2 -> showOpenIncidents(activeUser);
                                    case 3 -> showSolvedIncidents(activeUser);
                                    case 4 -> showProfile(activeUser);
                                    case 5 -> updatePassword(activeUser);
                                    case 6 -> login = false;
                                }
                            }
                        }while(login);
                    }else tv.loginFail();
                }
                case 2 -> insertUser();
            }
            try{
                Objects.requireNonNull(dao).close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }while(appOn);
    }
}