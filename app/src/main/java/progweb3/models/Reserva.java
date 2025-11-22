package progweb3.models;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo Reserva (JavaBean simple).
 * Representa una fila de la tabla "reserva".
 */
public class Reserva {

    private int id;
    private int habitacionId;
    private int huespedId;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private BigDecimal precioTotal;

    public Reserva() {
    }

    public Reserva(int habitacionId, int huespedId, LocalDate fechaIngreso, LocalDate fechaSalida, BigDecimal precioTotal) {
        this.habitacionId = habitacionId;
        this.huespedId = huespedId;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.precioTotal = precioTotal;
    }

    // ===== Getters y Setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHabitacionId() {
        return habitacionId;
    }

    public void setHabitacionId(int habitacionId) {
        this.habitacionId = habitacionId;
    }

    public int getHuespedId() {
        return huespedId;
    }

    public void setHuespedId(int huespedId) {
        this.huespedId = huespedId;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }
}