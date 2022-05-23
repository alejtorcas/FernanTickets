package model;
import java.io.Serializable;

/**
 * Clase de la que heredan las clases (Admin, Technical y FinalUser)
 * @see model.Admin
 * @see model.Technical
 * @see model.FinalUser
 * @author Alejandro Torres Castillo
 * @version 1.0
 */
public abstract class User implements Serializable {

    /**
     * Atributos de la clase User
     */
    private String name,lastname,password,email,id;

    /**
     * getters y setters de la clase User
     */
    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getLastname() {return lastname;}

    public void setLastname(String lastname) {this.lastname = lastname;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    /**
     * Constructo de la clase User
     * @param id Identificador único de usuario
     * @param name Nombre del usuario
     * @param lastname Apellido del usuario
     * @param email Email del usuario
     * @param password Contraseña deñ usuario
     */
    public User(String id, String name, String lastname, String email, String password){
        setId(id);
        setName(name);
        setLastname(lastname);
        setEmail(email);
        setPassword(password);}
}