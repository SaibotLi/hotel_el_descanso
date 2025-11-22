package progweb3;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import progweb3.models.Habitacion;
import progweb3.models.Huesped;
import progweb3.models.Reserva;

/**
 * Este Repositorio se encarga de TODAS las operaciones con la base de datos.
 * Es @ApplicationScoped porque solo necesitamos UNA instancia para toda la app.
 */
@ApplicationScoped
public class Repositorio {

    /**
     * Inyectamos el DataSource usando el recurso creado en Payara:
     *   jdbc/hotelDS
     * Este DataSource ya contiene la URL, usuario y contraseña configurados en el panel de Payara.
     */
    @Resource(lookup = "jdbc/hotelDS")
    private DataSource dataSource;


    // ============================================================
    //                     CRUD HABITACION
    // ============================================================

    /**
     * Inserta una habitación en la tabla "habitacion".
     */
    public void crearHabitacion(Habitacion h) throws Exception {

        // INSERT con parámetros, para evitar SQL Injection
        String sql = "INSERT INTO habitacion (numero, tipo, precio_por_noche) VALUES (?, ?, ?)";

        // "try-with-resources": Java cierra la conexión automáticamente
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Rellenamos los parámetros del INSERT
            ps.setInt(1, h.getNumero());
            ps.setString(2, h.getTipo());
            ps.setBigDecimal(3, h.getPrecioPorNoche());

            // Ejecutamos el INSERT
            ps.executeUpdate();
        }
    }

    /**
     * Devuelve una lista de todas las habitaciones.
     */
    public List<Habitacion> listarHabitaciones() throws Exception {

        List<Habitacion> lista = new ArrayList<>();

        // SELECT simple
        String sql = "SELECT * FROM habitacion ORDER BY numero";

        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // Recorremos cada fila del resultado
            while (rs.next()) {
                Habitacion h = new Habitacion();

                // Obtenemos cada columna y se la pasamos al objeto
                h.setId(rs.getInt("id"));
                h.setNumero(rs.getInt("numero"));
                h.setTipo(rs.getString("tipo"));
                h.setPrecioPorNoche(rs.getBigDecimal("precio_por_noche"));

                lista.add(h);
            }
        }

        return lista;
    }

// Filtrar habitaciones por tipo (case-insensitive)
    public List<Habitacion> listarPorTipo(String tipo) throws Exception {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM habitacion WHERE tipo ILIKE ? ORDER BY numero";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Habitacion h = new Habitacion();
                    h.setId(rs.getInt("id"));
                    h.setNumero(rs.getInt("numero"));
                    h.setTipo(rs.getString("tipo"));
                    h.setPrecioPorNoche(rs.getBigDecimal("precio_por_noche"));
                    lista.add(h);
                }
            }
        }
        return lista;
    }

    // Obtiene todos los tipos disponibles desde la tabla tipo_habitacion
    public List<String> obtenerTiposDisponibles() throws Exception {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT nombre FROM tipo_habitacion ORDER BY nombre";
        
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
             
            while (rs.next()) {
                tipos.add(rs.getString("nombre"));
            }
        }
        return tipos;
    }

    /**
     * Busca una habitación por ID.
     */
    public Habitacion buscarHabitacion(int id) throws Exception {

        String sql = "SELECT * FROM habitacion WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Seteamos el parámetro ? del WHERE
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                // Si encontró una fila, la devolvemos
                if (rs.next()) {
                    Habitacion h = new Habitacion();
                    h.setId(rs.getInt("id"));
                    h.setNumero(rs.getInt("numero"));
                    h.setTipo(rs.getString("tipo"));
                    h.setPrecioPorNoche(rs.getBigDecimal("precio_por_noche"));
                    return h;
                }
            }
        }

        // Si no encontró nada devolvemos null
        return null;
    }

    /**
     * Actualiza una habitación existente.
     */
    public void actualizarHabitacion(Habitacion h) throws Exception {

        String sql = "UPDATE habitacion SET numero=?, tipo=?, precio_por_noche=? WHERE id=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, h.getNumero());
            ps.setString(2, h.getTipo());
            ps.setBigDecimal(3, h.getPrecioPorNoche());
            ps.setInt(4, h.getId());

            ps.executeUpdate();
        }
    }

    /**
     * Elimina una habitación por ID.
     */
    public void eliminarHabitacion(int id) throws Exception {

        String sql = "DELETE FROM habitacion WHERE id=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ============================================================
    //                     CRUD HUESPED
    // ============================================================

    /**
     * Inserta un huésped en la tabla "huesped".
     */
    public void crearHuesped(Huesped h) throws Exception {
        String sql = "INSERT INTO huesped (nombre, telefono, documento) VALUES (?, ?, ?)";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, h.getNombre());
            ps.setString(2, h.getTelefono());
            ps.setString(3, h.getDocumento());
            
            ps.executeUpdate();
        }
    }

    /**
     * Devuelve una lista de todos los huéspedes.
     */
    public List<Huesped> listarHuespedes() throws Exception {
        List<Huesped> lista = new ArrayList<>();
        String sql = "SELECT * FROM huesped ORDER BY nombre";
        
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Huesped h = new Huesped();
                h.setId(rs.getInt("id"));
                h.setNombre(rs.getString("nombre"));
                h.setTelefono(rs.getString("telefono"));
                h.setDocumento(rs.getString("documento"));
                lista.add(h);
            }
        }
        return lista;
    }

    /**
     * Busca un huésped por ID.
     */
    public Huesped buscarHuesped(int id) throws Exception {
        String sql = "SELECT * FROM huesped WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Huesped h = new Huesped();
                    h.setId(rs.getInt("id"));
                    h.setNombre(rs.getString("nombre"));
                    h.setTelefono(rs.getString("telefono"));
                    h.setDocumento(rs.getString("documento"));
                    return h;
                }
            }
        }
        return null;
    }

    /**
     * Busca un huésped por documento (para validar documento único).
     * Retorna el huésped si existe, null si no existe.
     * Si se pasa un id, excluye ese id de la búsqueda.
     */
    public Huesped buscarPorDocumento(String documento, Integer idExcluir) throws Exception {
        String sql;
        if (idExcluir != null) {
            sql = "SELECT * FROM huesped WHERE documento = ? AND id != ?";
        } else {
            sql = "SELECT * FROM huesped WHERE documento = ?";
        }
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, documento);
            if (idExcluir != null) {
                ps.setInt(2, idExcluir);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Huesped h = new Huesped();
                    h.setId(rs.getInt("id"));
                    h.setNombre(rs.getString("nombre"));
                    h.setTelefono(rs.getString("telefono"));
                    h.setDocumento(rs.getString("documento"));
                    return h;
                }
            }
        }
        return null;
    }

    /**
     * Actualiza un huésped existente.
     */
    public void actualizarHuesped(Huesped h) throws Exception {
        String sql = "UPDATE huesped SET nombre=?, telefono=?, documento=? WHERE id=?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, h.getNombre());
            ps.setString(2, h.getTelefono());
            ps.setString(3, h.getDocumento());
            ps.setInt(4, h.getId());
            
            ps.executeUpdate();
        }
    }

    /**
     * Elimina un huésped por ID.
     */
    public void eliminarHuesped(int id) throws Exception {
        String sql = "DELETE FROM huesped WHERE id=?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    // ============================================================
    // AQUI LUEGO IRÁN:
    //    CRUD HUESPED
    //    CRUD RESERVA
    //    LOGICA DE FECHAS / DISPONIBILIDAD
    // ============================================================

}