package view;

import model.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author Alejandro Torres Castillo
 * @version 1.0
 */

public class TicketsView {

    /**
     * Constantes para definir colores en consola.
     */

    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * @return Estas funciones devuelven un String para cada tipo de dato que necesita la aplicación.
     */

    public String arrow(){return "-> ";}

    public String anyKey(){return "Pulsa enter para continuar";}

    public String name(){return "nombre: ";}

    public String lastname(){return "apellido: ";}

    public String user(){return "email: ";}

    public String password(){return "contraseña: ";}

    public String newPassword(){return "nueva contraseña: ";}

    public String checkNewPassword(){return "confirma la nueva contraseña: ";}

    public String incidentId(){return "ID de la Incidencia: ";}

    public String userId(){return "ID del usuario: ";}

    public String technicalId(){return "ID del técnico: ";}

    public String term(){return "Término de búsqueda: ";}

    public String incidentDescription(){return "¿Cuál es el problema? -> ";}

    public String incidentSolution(){return "¿Como has solucionado la incidencia? -> ";}

    /**
     * Estas funciones imprimen por pantalla las diferentes interfaces (menus, errores y ToString de las clases)
     */

    public void priority(){System.out.print("Nivel de prioridad ");}

    public void notFoundIncident(){System.out.println(" --- No se encontró la incidencia ---");}

    public void notFoundOpenIncidents(){System.out.println(" --- No se encontraron incidencias abiertas ---");}

    public void notFoundSolvedIncidents(){System.out.println(" --- No se encontraron incidencias cerradas ---");}

    public void notFoundIncidentsByUserId(){System.out.println(" --- No se encontró el usuario o aun no tiene incidencias relacionadas --- ");}

    public void notFoundIncidentsByTerm(){System.out.println(" --- No hay incidencia que contengan ese término --- ");}

    public void notFoundIncidentsByPriority(){System.out.println(" --- No hay incidencias con esa prioridad --- ");}

    public void notFoundUsers(){System.out.println(" --- No hay usuarios o técnicos registrados aún --- ");}

    public void notFoundTechnical(){System.out.println(" --- No se encontró al técnico --- ");}

    public void emailAlreadyRegistered(){System.out.println(" --- El correo electrónico ya está registrado vuelve a intentarlo --- ");}

    public void cantAssignIncidents(){System.out.println(" --- No hay incidencias sin asignar o técnicos registrados ---");}

    public void emptyDataUser(){System.out.println(" --- Los campos nombre, email y contraseña son obligatorios --- ");}

    public void emptyDataIncident(){System.out.println(" --- No puedes dejar la descripción en blanco --- ");}

    public void shortPassword(){System.out.println(" --- La contraseña es demasiado corta --- ");}

    public void deleteUser(){System.out.println(" --- Usuario eliminado con éxito ---");}

    public void notFoundUser(){System.out.println(" --- No se encontró al usuario ---");}

    public void wrongPassword(){System.out.println(" --- Contraseña incorrecta ---");}

    public void successPasswordUpdate(){System.out.println(" --- Contraseña actualizada con éxito ---");}

    public void nfe(){System.out.println(" --- Tienes que introducir un número --- ");}

    public void numberOutOfRange(int max){System.out.println(" --- Las opciones válidas son las comprendidas entre 1 y " + max + " --- ");}

    public void insertUser(User user){System.out.println(((user.getClass().equals(Technical.class))?" --- Técnico":" --- Usuario") + " creado con éxito ---");}

    public void insertIncident(){System.out.println(" --- Incidencia creada con éxito --- ");}

    public void assignIncident(){System.out.println(" --- Incidencia asignada con éxito --- ");}

    public void successSolvedIncident(){System.out.println(" --- Incidencia solucionada con éxito --- ");}

    public void checkUser(User user){
        System.out.println("\nNombre: " + user.getName() +
                "\nApellidos: " + user.getLastname() +
                "\nEmail: " + user.getEmail() +
                "\nContraseña: " + user.getPassword() +
                "\n[1] Confirmar datos de " + ((user.getClass().equals(Technical.class))?"técnico":"usuario") +
                "\n[2] Reintentar" +
                "\n[3] Cancelar");}

    public void checkIncident(Incident incident){
        System.out.println();
        incidentToString(incident);
        System.out.println("""
                [1] Confirmar incidencia
                [2] Reintentar
                [3] Cancelar""");}

    public void checkAssignIncident(Incident incident, Technical technical){
        System.out.println();
        incidentToString(incident);
        System.out.println("[1] Asignar Incidencia a " + technical.getName() + "\n" +
                           "[2] Reintentar\n" +
                           "[3] Cancelar");}

    public void checkSolveIncident(Incident incident){
        System.out.println();
        incidentToString(incident);
        System.out.println("""
                [1] Confirmar incidencia solucionada
                [2] Reintentar
                [3] Cancelar""");}


    public void loginMenu(){
        System.out.print(ANSI_RESET);
        System.out.println("""
                ╔═══════════════════════════════════╗
                     MENU DE INICIO
                     Bienvenido a FernanTicket
                  [1] Acceder
                  [2] Registrarse
                ╚═══════════════════════════════════╝""");}

    public void loginFail(){
        System.out.println("""
           --- Credenciales no válidos ---
        El usuario o la contraseña no son correctos intentalo de nuevo""");}

    public void userMenu(User user){
        if (user.getClass().equals(Admin.class)){
            System.out.print(ANSI_YELLOW);
            System.out.println("""
                ╔═══════════════════════════════════════════════════╗
                      MENU DE ADMIN""");
            System.out.println("      Bienvenido " + user.getName());
            System.out.println("""
                  [1] Consultar todas las incidencias abiertas
                  [2] Consultar todas las incidencias cerradas
                  [3] Buscar Incidencias
                  [4] Dar de alta a un Técnico
                  [5] Consultar todos los Usuarios
                  [6] Borrar a un Usuario
                  [7] Asignar una incidencia a un técnico
                  [8] Estadísticas de la aplicación
                  [9] Cerrar sesión
                  [10] Apagar FernanTicket
                ╚═══════════════════════════════════════════════════╝""");}
        if (user.getClass().equals(Technical.class)){
            System.out.print(ANSI_GREEN);
            System.out.println("""
                    ╔═══════════════════════════════════════════════════╗
                          MENU DE TÉCNICO""");
            System.out.println("      Bienvenido " + user.getName());
            System.out.println("""
                      [1] Consultar incidencias asignadas
                      [2] Marcar una incidencia como resuelta
                      [3] Consultar mis incidencias cerradas
                      [4] Mostrar mi perfil
                      [5] Cambiar clave de acceso
                      [6] Cerrar sesión
                    ╚═══════════════════════════════════════════════════╝""");}
        if (user.getClass().equals(FinalUser.class)){
            System.out.print(ANSI_BLUE);
            System.out.println("""
                    ╔═══════════════════════════════════════════════════╗
                          MENU DE USUARIO""");
            System.out.println("      Bienvenido " + user.getName());
            System.out.println("""
                      [1] Registrar una incidencia
                      [2] Consultar mis incidencias abiertas
                      [3] Consultar mis incidencias cerradas
                      [4] Mostrar mi perfil
                      [5] Cambiar clave de acceso
                      [6] Cerrar sesión
                    ╚═══════════════════════════════════════════════════╝""");}}

    public void searchIncidentMenu(){
        System.out.println("""
                ╔═══════════════════════════════════════════════════╗
                    [1] Buscar Incidencia por ID
                    [2] Buscar Incidencias por usuario
                    [3] Buscar Incidencias por técnico
                    [4] Buscar Incidencias por término
                    [5] Buscar Incidencias por prioridad
                    [6] Salir
                ╚═══════════════════════════════════════════════════╝""");}

    public void userToString(User user){
        System.out.println("°º¤ø,¸¸,ø¤º°`°º¤ø,¸,ø¤°º¤ø,¸¸,ø¤º°`°º¤ø,¸\n" +
            "         Rango: " + ((user.getClass().equals(Admin.class))?"Administrador\n":(user.getClass().equals(Technical.class))?"Técnico\n":"Usuario\n") +
            "         Nombre: " + user.getName() + " " + (user.getLastname()!=null?user.getLastname():"") + "\n" +
            "         Email: " + user.getEmail() + "\n" +
            "         Código: " + user.getId() + "\n" +
            "¸,ø¤º°`°º¤ø,¸,ø¤°º¤ø,¸¸,ø¤º°`°º¤ø,¸¸,ø¤º°");}

    public void incidentToString(Incident inc){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("°º¤ø,¸¸,ø¤º°°º¤ø,¸,ø¤°º¤ø,¸¸,ø¤º°°º¤ø,¸¸,ø¤º°\n" +
            "   ID de Incidencia: " + inc.getId() + "\n" +
            "   ID del creador: " + inc.getIdUser() + "\n" +
            ((inc.getIdTechnical() != null)? "   ID del técnico: " + inc.getIdTechnical() + "\n":"") +
            "   Prioridad: " + inc.getPriority() + "\n" +
            "   Descripción: " + inc.getDescription() + "\n" +
            (inc.getSolution() != null ? "   Solución: " + inc.getSolution() + "\n":"") +
            "   Fecha de creación: " + sdf.format(inc.getOpenDate()) + "\n" +
            (inc.getCloseDate() != null ? "   Fecha en la que se resolvió " + sdf.format(inc.getCloseDate()) + "\n" +
                    "   Dias que tardó en resolverse: " + inc.daysToSolve() :
                    "   Dias desde que se abrió: " + inc.daysSinceOpen()) + "\n" +
            ((inc.isSolved()) ? "--- RESUELTA ---" : "--- SIN RESOLVER ---") + "\n" +
            "¸,ø¤º°°º¤ø,¸,ø¤°º¤ø,¸¸,ø¤º°°º¤ø,¸¸,ø¤º°°º¤ø,¸");}

    public void dashboard(ArrayList<User> allUsers, ArrayList<Incident> allIncidents){
        int nUsers = 0, nTechnicals = 0, nIncidents = 0, openIncidents = 0, assignedIncidents = 0, closedIncidents = 0, priority = 0;
        for (User user: allUsers){
            if (user.getClass().equals(FinalUser.class)) nUsers++;
            if (user.getClass().equals(Technical.class)) nTechnicals++;}
        for (Incident incident : allIncidents){
            nIncidents ++;
            if (!incident.isSolved() && incident.getIdTechnical() == null) openIncidents++;
            if (!incident.isSolved() && incident.getIdTechnical() != null) assignedIncidents++;
            if (incident.isSolved()) closedIncidents++;
            priority += incident.getPriority();}
        System.out.println(" --- Estadísticas de FernanTicket ---\n" +
            " Usuarios registrados:              " + nUsers + "\n" +
            " Técnicos registrados:              " + nTechnicals + "\n" +
            " Total de incidencias registradas:  " + nIncidents + "\n" +
            " Incidencias abiertas sin asignar:  " + openIncidents + "\n" +
            " Incidencias abiertas asignadas:    " + assignedIncidents + "\n" +
            " Incidencias resueltas:             " + closedIncidents + "\n" +
            " Prioridad media de las incidencias:" + (priority/allIncidents.size()) );}
}
