package progweb3.models;

/**
 * Modelo Huesped.
 * Representa una fila de la tabla "huesped".
 */
public class Huesped {

    private int id;
    private String nombre;
    private String telefono;
    private String documento;

    public Huesped() {
    }

    public Huesped(String nombre, String telefono, String documento) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.documento = documento;
    }

    // ===== Getters y Setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}