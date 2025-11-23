package progweb3.controllers;

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import progweb3.Repositorio;
import progweb3.models.Huesped;
import progweb3.util.CsrfTokenUtil; 

import java.util.List;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    // LISTAR HUÉSPEDES ACTUALES
    @GET
    @Path("/actuales")
    public String listarActuales() {
        try {
            List<Huesped> huespedes = repo.listarHuespedesActuales();
            models.put("huespedes", huespedes);
            models.put("titulo", "Huéspedes Actuales");
        } catch (Exception e) {
            models.put("error", "Error al cargar los huéspedes actuales");
            e.printStackTrace();
        }
        return "huesped-list.jsp";
    }

    // MOSTRAR FORMULARIO NUEVO HUÉSPED
    @GET
    @Path("/nuevo")
    public String nuevo() {
        models.put("huesped", new Huesped());
        models.put("csrfToken", CsrfTokenUtil.generateToken()); // AGREGAR ESTA LÍNEA
        return "huesped-form.jsp";
    }

    // GUARDAR HUÉSPED (NUEVO O EDITADO)
    @POST
    @Path("/guardar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String guardar(
            @FormParam("csrfToken") String csrfToken, 
            @FormParam("id") Integer id,
            @FormParam("nombre") String nombre,
            @FormParam("telefono") String telefono,
            @FormParam("documento") String documento
    ) {
        // Validar CSRF token
        if (!CsrfTokenUtil.validateToken(csrfToken)) {
            models.put("error", "Token de seguridad inválido. Por favor, intente nuevamente.");
            models.put("huesped", new Huesped());
            models.put("csrfToken", CsrfTokenUtil.generateToken());
            return "huesped-form.jsp";
        }
        
        try {
            // VALIDACIÓN DE DOCUMENTO ÚNICO
            if (documento != null && !documento.trim().isEmpty()) {
                Huesped existente = repo.buscarPorDocumento(documento.trim(), id);
                if (existente != null) {
                    models.put("error", "Ya existe un huésped con el documento: " + documento + ". Por favor, verifique los datos.");
                    if (id != null && id > 0) {
                        Huesped h = repo.buscarHuesped(id);
                        models.put("huesped", h);
                    } else {
                        models.put("huesped", new Huesped());
                    }
                    models.put("csrfToken", CsrfTokenUtil.generateToken());
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
                models.put("csrfToken", CsrfTokenUtil.generateToken());
            } catch (Exception ex) {
                ex.printStackTrace();
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
            models.put("csrfToken", CsrfTokenUtil.generateToken());
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
            // El mensaje de error ya viene del repositorio con información clara
            models.put("error", e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/huespedes/";
    }
}
