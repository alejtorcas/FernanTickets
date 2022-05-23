package model;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Clase Incident, los objetos de esta clase los crean los FinalUsers
 * @author Alejandro Torres Castillo
 * @version 1.0
 */
public class Incident implements Serializable {

    /**
     * Atributos de la clase Incident
     */
    private int priority ;
    private String description, solution, id, idUser, idTechnical;
    private boolean solved;
    private Date openDate, closeDate;

    /**
     * Getters y setters de la clase Incident
     */
    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public int getPriority() {return priority;}

    public void setPriority(int priority) {this.priority = priority;}

    public String getIdUser() {return idUser;}

    public void setIdUser(String idUser) {this.idUser = idUser;}

    public String getIdTechnical() {return idTechnical;}

    public void setIdTechnical(String idTechnical) {this.idTechnical = idTechnical;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public String getSolution() {return solution;}

    public void setSolution(String solution) {this.solution = solution;}

    public boolean isSolved() {return solved;}

    public void setSolved(boolean solved) {this.solved = solved;}

    public Date getOpenDate() {return openDate;}

    public void setOpenDate(Date openDate) {this.openDate = openDate;}

    public Date getCloseDate() {return closeDate;}

    public void setCloseDate(Date closeDate) {this.closeDate = closeDate;}

    /**
     * Constructor de la clase Incident
     * @param id Identificador único de la incidencia
     * @param priority Prioridad de la incidencia
     * @param description Descripción de la incidencia
     * @param solution Solución de la incidencia
     * @param idUser Identificador del usuario que ha creado la incidencia
     * @param idTechnical Identificador del técnico al que se le asigna la incidencia
     * @param solved Estado de la incidencia (true - resuelta, false - sin resolver)
     * @param openDate Fecha en la que se creó la incidencia
     * @param closeDate Fecha en la que se solucionó la incidencia
     */
    public Incident(int priority, String description, String solution, String id, String idUser, String idTechnical, boolean solved, Date openDate, Date closeDate) {
        setId(id);
        setPriority(priority);
        setDescription(description);
        setSolution(solution);
        setIdUser(idUser);
        setIdTechnical(idTechnical);
        setSolved(solved);
        setOpenDate(openDate);
        setCloseDate(closeDate);}

    public Incident(String id, int priority, String idUser, String description){
        setId(id);
        setPriority(priority);
        setDescription(description);
        setSolution(null);
        setIdUser(idUser);
        setIdTechnical(null);
        setSolved(false);
        setOpenDate(new Date());
        setCloseDate(null);}

    public void assignIncident(String idTechnical){setIdTechnical(idTechnical);}

    public void solveIncident(String solution){
        setSolved(true);
        setSolution(solution);
        setCloseDate(new Date());}

    public int daysSinceOpen(){
        Date today = new Date();
        return (int) TimeUnit.DAYS.convert(today.getTime() - getOpenDate().getTime(), TimeUnit.MILLISECONDS);}

    public int daysToSolve(){
        if (getCloseDate()!=null)
            return (int) TimeUnit.DAYS.convert( getCloseDate().getTime() - getOpenDate().getTime(), TimeUnit.MILLISECONDS);
        else return 0;}
}