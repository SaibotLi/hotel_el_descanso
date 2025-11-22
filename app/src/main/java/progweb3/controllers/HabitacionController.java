package progweb3.controllers;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import progweb3.Repositorio;
import progweb3.models.Habitacion;
import progweb3.util.CsrfTokenUtil;

import java.math.BigDecimal;
import java.util.List;

@Controller
@Path("/habitaciones")
public class HabitacionController {

    @Inject
    private Models models;

    @Inject
    private Repositorio repo;

    // LISTAR HABITACIONES
    @GET
    @Path("/")
    public String listar(@QueryParam("tipo") String tipoFiltro) {
        try {
            List<Habitacion> habitaciones;
            
            if (tipoFiltro != null && !tipoFiltro.trim().isEmpty()) {
                habitaciones = repo.listarPorTipo("%" + tipoFiltro.trim() + "%"); // opcional: con % para búsqueda parcial
                models.put("tipoSeleccionado", tipoFiltro.trim());
            } else {
                habitaciones = repo.listarHabitaciones();
            }
            
            models.put("habitaciones", habitaciones);
            models.put("tiposDisponibles", repo.obtenerTiposDisponibles());
            
        } catch (Exception e) {
            models.put("error", "Error al cargar las habitaciones");
            e.printStackTrace();
        }
        return "habitacion-list.jsp";
    }

    // MOSTRAR FORMULARIO NUEVA HABITACION
    @GET
    @Path("/nueva")
    public String nueva() {
        try {
            models.put("habitacion", new Habitacion());
            models.put("tiposDisponibles", repo.obtenerTiposDisponibles());
            models.put("csrfToken", CsrfTokenUtil.generateToken());
        } catch (Exception e) {
            models.put("error", "Error al cargar tipos de habitación");
            e.printStackTrace();
        }
        return "habitacion-form.jsp";
    }

    // GUARDAR HABITACION (NUEVA O EDITADA)
    @POST
    @Path("/guardar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String guardar(
            @FormParam("csrfToken") String csrfToken,
            @FormParam("id") Integer id,
            @FormParam("numero") Integer numero,
            @FormParam("tipo") String tipo,
            @FormParam("precio") String precio
    ) {
        // Validar CSRF token
        if (!CsrfTokenUtil.validateToken(csrfToken)) {
            models.put("error", "Token de seguridad inválido. Por favor, intente nuevamente.");
            try {
                models.put("habitacion", new Habitacion());
                models.put("tiposDisponibles", repo.obtenerTiposDisponibles());
                models.put("csrfToken", CsrfTokenUtil.generateToken());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "habitacion-form.jsp";
        }
        
        try {
            Habitacion h = new Habitacion();
            if (id != null && id > 0) h.setId(id);

            h.setNumero(numero);
            h.setTipo(tipo);
            h.setPrecioPorNoche(new BigDecimal(precio));

            if (id == null || id == 0) {
                repo.crearHabitacion(h);
            } else {
                repo.actualizarHabitacion(h);
            }

        } catch (Exception e) {
            e.printStackTrace();
            models.put("error", "Error al guardar habitación: " + e.getMessage());
            try {
                // Recargar tipos en caso de error para que el formulario funcione
                models.put("habitacion", new Habitacion());
                models.put("tiposDisponibles", repo.obtenerTiposDisponibles());
                models.put("csrfToken", CsrfTokenUtil.generateToken());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "habitacion-form.jsp";
        }

        return "redirect:/habitaciones/";
    }

    // EDITAR
    @GET
    @Path("/editar/{id}")
    public String editar(@PathParam("id") int id) {
        try {
            Habitacion h = repo.buscarHabitacion(id);
            models.put("habitacion", h);
            models.put("tiposDisponibles", repo.obtenerTiposDisponibles());
            models.put("csrfToken", CsrfTokenUtil.generateToken());
        } catch (Exception e) {
            models.put("error", "No se pudo cargar la habitación");
            e.printStackTrace();
        }
        return "habitacion-form.jsp";
    }

    // ELIMINAR
    @GET
    @Path("/eliminar/{id}")
    public String eliminar(@PathParam("id") int id) {
        try {
            repo.eliminarHabitacion(id);
        } catch (Exception e) {
            // El mensaje de error ya viene del repositorio con información clara
            models.put("error", e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/habitaciones/";
    }

}