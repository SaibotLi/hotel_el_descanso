package progweb3.controllers;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import progweb3.Repositorio;
import progweb3.models.Habitacion;

import java.math.BigDecimal;

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
    public String listar() {
        try {
            models.put("habitaciones", repo.listarHabitaciones());
        } catch (Exception e) {
            models.put("error", "Error listando habitaciones: " + e.getMessage());
        }
        return "habitacion-list.jsp";
    }

    // MOSTRAR FORMULARIO NUEVA HABITACION
    @GET
    @Path("/nueva")
    public String nueva() {
        return "habitacion-form.jsp";
    }

    // GUARDAR HABITACION (NUEVA O EDITADA)
    @POST
    @Path("/guardar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String guardar(
            @FormParam("id") Integer id,
            @FormParam("numero") Integer numero,
            @FormParam("tipo") String tipo,
            @FormParam("precio") String precio
    ) {
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
        } catch (Exception e) {
            models.put("error", "No se pudo cargar la habitación");
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
            models.put("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/habitaciones/";
    }
}