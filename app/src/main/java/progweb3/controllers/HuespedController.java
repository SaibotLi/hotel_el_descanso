package progweb3.controllers;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import progweb3.Repositorio;
import progweb3.models.Huesped;

import java.util.List;

@Controller
@Path("/huespedes")
public class HuespedController {

    @Inject
    private Models models;

    @Inject
    private Repositorio repo;

    // LISTAR HUÉSPEDES
    @GET
    @Path("/")
    public String listar() {
        try {
            List<Huesped> huespedes = repo.listarHuespedes();
            models.put("huespedes", huespedes);
        } catch (Exception e) {
            models.put("error", "Error al cargar los huéspedes");
            e.printStackTrace();
        }
        return "huesped-list.jsp";
    }

    // MOSTRAR FORMULARIO NUEVO HUÉSPED
    @GET
    @Path("/nuevo")
    public String nuevo() {
        models.put("huesped", new Huesped());
        return "huesped-form.jsp";
    }

    // GUARDAR HUÉSPED (NUEVO O EDITADO)
    @POST
    @Path("/guardar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String guardar(
            @FormParam("id") Integer id,
            @FormParam("nombre") String nombre,
            @FormParam("telefono") String telefono,
            @FormParam("documento") String documento
    ) {
        try {
            // VALIDACIÓN DE DOCUMENTO ÚNICO (LO NUEVO)
            if (documento != null && !documento.trim().isEmpty()) {
                Huesped existente = repo.buscarPorDocumento(documento.trim(), id);
                if (existente != null) {
                    models.put("error", "Ya existe un huésped con el documento: " + documento);
                    // Recargar datos para mostrar el formulario con error
                    if (id != null && id > 0) {
                        Huesped h = repo.buscarHuesped(id);
                        models.put("huesped", h);
                    } else {
                        models.put("huesped", new Huesped());
                    }
                    return "huesped-form.jsp";
                }
            }

            Huesped h = new Huesped();
            if (id != null && id > 0) h.setId(id);

            h.setNombre(nombre);
            h.setTelefono(telefono);
            h.setDocumento(documento);

            if (id == null || id == 0) {
                repo.crearHuesped(h);
            } else {
                repo.actualizarHuesped(h);
            }

        } catch (Exception e) {
            e.printStackTrace();
            models.put("error", "Error al guardar huésped: " + e.getMessage());
            try {
                if (id != null && id > 0) {
                    Huesped h = repo.buscarHuesped(id);
                    models.put("huesped", h);
                } else {
                    models.put("huesped", new Huesped());
                }
            } catch (Exception ex) {
                e.printStackTrace();
            }
            return "huesped-form.jsp";
        }

        return "redirect:/huespedes/";
    }

    // EDITAR
    @GET
    @Path("/editar/{id}")
    public String editar(@PathParam("id") int id) {
        try {
            Huesped h = repo.buscarHuesped(id);
            models.put("huesped", h);
        } catch (Exception e) {
            models.put("error", "No se pudo cargar el huésped");
            e.printStackTrace();
        }
        return "huesped-form.jsp";
    }

    // ELIMINAR
    @GET
    @Path("/eliminar/{id}")
    public String eliminar(@PathParam("id") int id) {
        try {
            repo.eliminarHuesped(id);
        } catch (Exception e) {
            models.put("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/huespedes/";
    }
}
