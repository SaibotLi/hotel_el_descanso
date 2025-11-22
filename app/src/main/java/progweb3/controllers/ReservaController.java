package progweb3.controllers;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import progweb3.Repositorio;
import progweb3.models.Reserva;
import progweb3.util.CsrfTokenUtil; // AGREGAR ESTE IMPORT

import java.time.LocalDate;
import java.util.List;

@Controller
@Path("/reservas")
public class ReservaController {

    @Inject
    private Models models;

    @Inject
    private Repositorio repo;

    // LISTAR RESERVAS (con filtros opcionales)
    @GET
    @Path("/")
    public String listar(
            @QueryParam("fechaDesde") String fechaDesdeStr,
            @QueryParam("fechaHasta") String fechaHastaStr,
            @QueryParam("tipo") String tipoHabitacion
    ) {
        try {
            List<Repositorio.ReservaInfoCompleta> reservas;
            LocalDate fechaDesde = null;
            LocalDate fechaHasta = null;

            // Convertir strings a LocalDate si vienen
            if (fechaDesdeStr != null && !fechaDesdeStr.trim().isEmpty()) {
                fechaDesde = LocalDate.parse(fechaDesdeStr);
            }
            if (fechaHastaStr != null && !fechaHastaStr.trim().isEmpty()) {
                fechaHasta = LocalDate.parse(fechaHastaStr);
            }

            // Si hay filtros, usar método con filtros, sino listar todas
            if (fechaDesde != null || fechaHasta != null || (tipoHabitacion != null && !tipoHabitacion.trim().isEmpty())) {
                reservas = repo.listarReservasConFiltros(fechaDesde, fechaHasta, tipoHabitacion);
            } else {
                reservas = repo.listarReservas();
            }

            models.put("reservas", reservas);
            models.put("tiposDisponibles", repo.obtenerTiposDisponibles());
            
            // Mantener filtros seleccionados para el formulario
            if (fechaDesde != null) {
                models.put("fechaDesdeSeleccionada", fechaDesdeStr);
            }
            if (fechaHasta != null) {
                models.put("fechaHastaSeleccionada", fechaHastaStr);
            }
            if (tipoHabitacion != null && !tipoHabitacion.trim().isEmpty()) {
                models.put("tipoSeleccionado", tipoHabitacion);
            }

        } catch (Exception e) {
            models.put("error", "Error al cargar las reservas: " + e.getMessage());
            e.printStackTrace();
        }
        return "reserva-list.jsp";
    }

    // MOSTRAR FORMULARIO NUEVA RESERVA
    @GET
    @Path("/nueva")
    public String nueva() {
        try {
            models.put("reserva", new Reserva());
            models.put("habitaciones", repo.listarHabitaciones());
            models.put("huespedes", repo.listarHuespedes());
            models.put("csrfToken", CsrfTokenUtil.generateToken()); 
        } catch (Exception e) {
            models.put("error", "Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
        }
        return "reserva-form.jsp";
    }

    // GUARDAR RESERVA
    @POST
    @Path("/guardar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String guardar(
            @FormParam("csrfToken") String csrfToken, // AGREGAR ESTE PARÁMETRO
            @FormParam("habitacionId") Integer habitacionId,
            @FormParam("huespedId") Integer huespedId,
            @FormParam("fechaIngreso") String fechaIngresoStr,
            @FormParam("fechaSalida") String fechaSalidaStr
    ) {
        // Validar CSRF token
        if (!CsrfTokenUtil.validateToken(csrfToken)) {
            models.put("error", "Token de seguridad inválido. Por favor, intente nuevamente.");
            try {
                models.put("reserva", new Reserva());
                models.put("habitaciones", repo.listarHabitaciones());
                models.put("huespedes", repo.listarHuespedes());
                models.put("csrfToken", CsrfTokenUtil.generateToken());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "reserva-form.jsp";
        }
        
        try {
            // Validar campos requeridos
            if (habitacionId == null || huespedId == null || 
                fechaIngresoStr == null || fechaIngresoStr.trim().isEmpty() ||
                fechaSalidaStr == null || fechaSalidaStr.trim().isEmpty()) {
                models.put("error", "Todos los campos son obligatorios");
                models.put("reserva", new Reserva());
                models.put("habitaciones", repo.listarHabitaciones());
                models.put("huespedes", repo.listarHuespedes());
                models.put("csrfToken", CsrfTokenUtil.generateToken());
                return "reserva-form.jsp";
            }

            // Convertir fechas
            LocalDate fechaIngreso = LocalDate.parse(fechaIngresoStr);
            LocalDate fechaSalida = LocalDate.parse(fechaSalidaStr);
            LocalDate hoy = LocalDate.now();

            // Validar que fecha ingreso no sea en el pasado
            if (fechaIngreso.isBefore(hoy)) {
                models.put("error", "La fecha de ingreso no puede ser en el pasado");
                models.put("reserva", new Reserva());
                models.put("habitaciones", repo.listarHabitaciones());
                models.put("huespedes", repo.listarHuespedes());
                models.put("csrfToken", CsrfTokenUtil.generateToken());
                return "reserva-form.jsp";
            }

            // Validar que fecha salida sea posterior a fecha ingreso
            if (!fechaSalida.isAfter(fechaIngreso)) {
                models.put("error", "La fecha de salida debe ser posterior a la fecha de ingreso");
                models.put("reserva", new Reserva());
                models.put("habitaciones", repo.listarHabitaciones());
                models.put("huespedes", repo.listarHuespedes());
                models.put("csrfToken", CsrfTokenUtil.generateToken());
                return "reserva-form.jsp";
            }

            // Crear reserva
            Reserva r = new Reserva();
            r.setHabitacionId(habitacionId);
            r.setHuespedId(huespedId);
            r.setFechaIngreso(fechaIngreso);
            r.setFechaSalida(fechaSalida);

            // El repositorio calculará el precio y verificará disponibilidad
            repo.crearReserva(r);

        } catch (Exception e) {
            e.printStackTrace();
            models.put("error", "Error al guardar reserva: " + e.getMessage());
            try {
                models.put("reserva", new Reserva());
                models.put("habitaciones", repo.listarHabitaciones());
                models.put("huespedes", repo.listarHuespedes());
                models.put("csrfToken", CsrfTokenUtil.generateToken());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "reserva-form.jsp";
        }

        return "redirect:/reservas/";
    }

    // CANCELAR RESERVA (eliminar)
    @GET
    @Path("/cancelar/{id}")
    public String cancelar(@PathParam("id") int id) {
        try {
            repo.eliminarReserva(id);
        } catch (Exception e) {
            models.put("error", "Error al cancelar reserva: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/reservas/";
    }
}
