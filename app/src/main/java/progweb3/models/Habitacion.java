package progweb3.models;

import java.math.BigDecimal;

/**
 * Modelo Habitacion (JavaBean simple)
 * Representa una fila de la tabla "habitacion".
 */
public class Habitacion {

    private int id;
    private int numero;
    private String tipo;
    private BigDecimal precioPorNoche;

    // Constructor vacío obligatorio para JavaBean
    public Habitacion() {
    }

    // Constructor opcional para comodidad
    public Habitacion(int numero, String tipo, BigDecimal precioPorNoche) {
        this.numero = numero;
        this.tipo = tipo;
        this.precioPorNoche = precioPorNoche;
    }

    // ===== Getters y Setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecioPorNoche() {
        return precioPorNoche;
    }

    public void setPrecioPorNoche(BigDecimal precioPorNoche) {
        this.precioPorNoche = precioPorNoche;
    }

}