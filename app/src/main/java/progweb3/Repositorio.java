package progweb3;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

            // Relleno los parámetros del INSERT
            ps.setInt(1, h.getNumero());
            ps.setString(2, h.getTipo());
            ps.setBigDecimal(3, h.getPrecioPorNoche());

            // Ejecuto el INSERT
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
     * Lanza excepción si la habitación tiene reservas asociadas.
     */
    public void eliminarHabitacion(int id) throws Exception {
        // Validar que no tenga reservas
        if (tieneReservasHabitacion(id)) {
            throw new Exception("No se puede eliminar la habitación porque tiene reservas asociadas");
        }
        
        String sql = "DELETE FROM habitacion WHERE id=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Verifica si una habitación tiene reservas asociadas.
     * Retorna true si tiene reservas, false si no tiene.
     */
    public boolean tieneReservasHabitacion(int habitacionId) throws Exception {
        String sql = "SELECT COUNT(*) FROM reserva WHERE habitacion_id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, habitacionId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Verifica si un huésped tiene reservas asociadas.
     * Retorna true si tiene reservas, false si no tiene.
     */
    public boolean tieneReservasHuesped(int huespedId) throws Exception {
        String sql = "SELECT COUNT(*) FROM reserva WHERE huesped_id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, huespedId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
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
     * Lanza excepción si el huésped tiene reservas asociadas.
     */
    public void eliminarHuesped(int id) throws Exception {
        // Validar que no tenga reservas
        if (tieneReservasHuesped(id)) {
            throw new Exception("No se puede eliminar el huésped porque tiene reservas asociadas");
        }
        
        String sql = "DELETE FROM huesped WHERE id=?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

        // ============================================================
    //                     CRUD RESERVA
    // ============================================================

    /**
     * Verifica si una habitación está disponible en un rango de fechas.
     * Retorna true si está disponible, false si está ocupada.
     */
    public boolean verificarDisponibilidad(int habitacionId, LocalDate fechaIngreso, LocalDate fechaSalida, Integer idReservaExcluir) throws Exception {
        String sql;
        if (idReservaExcluir != null) {
            sql = "SELECT COUNT(*) FROM reserva " +
                  "WHERE habitacion_id = ? " +
                  "AND fecha_salida > ? " +
                  "AND fecha_ingreso < ? " +
                  "AND id != ?";
        } else {
            sql = "SELECT COUNT(*) FROM reserva " +
                  "WHERE habitacion_id = ? " +
                  "AND fecha_salida > ? " +
                  "AND fecha_ingreso < ?";
        }
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, habitacionId);
            ps.setDate(2, Date.valueOf(fechaIngreso));
            ps.setDate(3, Date.valueOf(fechaSalida));
            if (idReservaExcluir != null) {
                ps.setInt(4, idReservaExcluir);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Si count = 0, está disponible
                }
            }
        }
        return true;
    }

    /**
     * Calcula el precio total de una reserva según cantidad de noches y precio de la habitación.
     */
    public BigDecimal calcularPrecioTotal(int habitacionId, LocalDate fechaIngreso, LocalDate fechaSalida) throws Exception {
        // Obtener precio de la habitación
        Habitacion h = buscarHabitacion(habitacionId);
        if (h == null) {
            throw new Exception("Habitación no encontrada");
        }
        
        // Calcular días (fechaSalida - fechaIngreso)
        long dias = java.time.temporal.ChronoUnit.DAYS.between(fechaIngreso, fechaSalida);
        if (dias <= 0) {
            throw new Exception("La fecha de salida debe ser posterior a la de ingreso");
        }
        
        // Precio total = días * precio por noche
        BigDecimal precioPorNoche = h.getPrecioPorNoche();
        return precioPorNoche.multiply(new BigDecimal(dias));
    }

    /**
     * Crea una nueva reserva. Verifica disponibilidad antes de insertar.
     */
    public void crearReserva(Reserva r) throws Exception {
        // Verificar disponibilidad
        if (!verificarDisponibilidad(r.getHabitacionId(), r.getFechaIngreso(), r.getFechaSalida(), null)) {
            throw new Exception("La habitación no está disponible en las fechas seleccionadas");
        }
        
        // Calcular precio total
        BigDecimal precioTotal = calcularPrecioTotal(r.getHabitacionId(), r.getFechaIngreso(), r.getFechaSalida());
        r.setPrecioTotal(precioTotal);
        
        String sql = "INSERT INTO reserva (habitacion_id, huesped_id, fecha_ingreso, fecha_salida, precio_total) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, r.getHabitacionId());
            ps.setInt(2, r.getHuespedId());
            ps.setDate(3, Date.valueOf(r.getFechaIngreso()));
            ps.setDate(4, Date.valueOf(r.getFechaSalida()));
            ps.setBigDecimal(5, r.getPrecioTotal());
            
            ps.executeUpdate();
        }
    }

    /**
     * Lista todas las reservas con información completa de habitación y huésped.
     * Retorna lista de ReservaInfoCompleta.
     */
    public List<ReservaInfoCompleta> listarReservas() throws Exception {
        List<ReservaInfoCompleta> lista = new ArrayList<>();
        String sql = "SELECT r.*, h.numero as habitacion_numero, h.tipo as habitacion_tipo, " +
                     "hu.nombre as huesped_nombre, hu.documento as huesped_documento " +
                     "FROM reserva r " +
                     "JOIN habitacion h ON r.habitacion_id = h.id " +
                     "JOIN huesped hu ON r.huesped_id = hu.id " +
                     "ORDER BY r.fecha_ingreso DESC";
        
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                ReservaInfoCompleta info = new ReservaInfoCompleta();
                info.setId(rs.getInt("id"));
                info.setHabitacionId(rs.getInt("habitacion_id"));
                info.setHabitacionNumero(rs.getInt("habitacion_numero"));
                info.setHabitacionTipo(rs.getString("habitacion_tipo"));
                info.setHuespedId(rs.getInt("huesped_id"));
                info.setHuespedNombre(rs.getString("huesped_nombre"));
                info.setHuespedDocumento(rs.getString("huesped_documento"));
                info.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                info.setFechaSalida(rs.getDate("fecha_salida").toLocalDate());
                info.setPrecioTotal(rs.getBigDecimal("precio_total"));
                lista.add(info);
            }
        }
        return lista;
    }

    /**
     * Lista reservas con filtros, retornando información completa.
     */
    public List<ReservaInfoCompleta> listarReservasConFiltros(LocalDate fechaDesde, LocalDate fechaHasta, String tipoHabitacion) throws Exception {
        List<ReservaInfoCompleta> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT r.*, h.numero as habitacion_numero, h.tipo as habitacion_tipo, " +
            "hu.nombre as huesped_nombre, hu.documento as huesped_documento " +
            "FROM reserva r " +
            "JOIN habitacion h ON r.habitacion_id = h.id " +
            "JOIN huesped hu ON r.huesped_id = hu.id " +
            "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        
        if (fechaDesde != null) {
            sql.append(" AND r.fecha_ingreso >= ?");
            params.add(Date.valueOf(fechaDesde));
        }
        if (fechaHasta != null) {
            sql.append(" AND r.fecha_salida <= ?");
            params.add(Date.valueOf(fechaHasta));
        }
        if (tipoHabitacion != null && !tipoHabitacion.trim().isEmpty()) {
            sql.append(" AND h.tipo = ?");
            params.add(tipoHabitacion.trim());
        }
        
        sql.append(" ORDER BY r.fecha_ingreso DESC");
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof Date) {
                    ps.setDate(i + 1, (Date) params.get(i));
                } else {
                    ps.setString(i + 1, (String) params.get(i));
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReservaInfoCompleta info = new ReservaInfoCompleta();
                    info.setId(rs.getInt("id"));
                    info.setHabitacionId(rs.getInt("habitacion_id"));
                    info.setHabitacionNumero(rs.getInt("habitacion_numero"));
                    info.setHabitacionTipo(rs.getString("habitacion_tipo"));
                    info.setHuespedId(rs.getInt("huesped_id"));
                    info.setHuespedNombre(rs.getString("huesped_nombre"));
                    info.setHuespedDocumento(rs.getString("huesped_documento"));
                    info.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                    info.setFechaSalida(rs.getDate("fecha_salida").toLocalDate());
                    info.setPrecioTotal(rs.getBigDecimal("precio_total"));
                    lista.add(info);
                }
            }
        }
        return lista;
    }

    /**
     * Busca una reserva por ID.
     */
    public Reserva buscarReserva(int id) throws Exception {
        String sql = "SELECT * FROM reserva WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reserva r = new Reserva();
                    r.setId(rs.getInt("id"));
                    r.setHabitacionId(rs.getInt("habitacion_id"));
                    r.setHuespedId(rs.getInt("huesped_id"));
                    r.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                    r.setFechaSalida(rs.getDate("fecha_salida").toLocalDate());
                    r.setPrecioTotal(rs.getBigDecimal("precio_total"));
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * Elimina (cancela) una reserva por ID.
     */
    public void eliminarReserva(int id) throws Exception {
        String sql = "DELETE FROM reserva WHERE id=?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Obtiene información completa de una reserva con nombres de habitación y huésped.
     */
    public ReservaInfoCompleta obtenerReservaCompleta(int id) throws Exception {
        String sql = "SELECT r.*, h.numero as habitacion_numero, h.tipo as habitacion_tipo, " +
                     "hu.nombre as huesped_nombre, hu.documento as huesped_documento " +
                     "FROM reserva r " +
                     "JOIN habitacion h ON r.habitacion_id = h.id " +
                     "JOIN huesped hu ON r.huesped_id = hu.id " +
                     "WHERE r.id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReservaInfoCompleta info = new ReservaInfoCompleta();
                    info.setId(rs.getInt("id"));
                    info.setHabitacionId(rs.getInt("habitacion_id"));
                    info.setHabitacionNumero(rs.getInt("habitacion_numero"));
                    info.setHabitacionTipo(rs.getString("habitacion_tipo"));
                    info.setHuespedId(rs.getInt("huesped_id"));
                    info.setHuespedNombre(rs.getString("huesped_nombre"));
                    info.setHuespedDocumento(rs.getString("huesped_documento"));
                    info.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                    info.setFechaSalida(rs.getDate("fecha_salida").toLocalDate());
                    info.setPrecioTotal(rs.getBigDecimal("precio_total"));
                    return info;
                }
            }
        }
        return null;
    }

    // Clase auxiliar para información completa de reserva
    public static class ReservaInfoCompleta {
        private int id;
        private int habitacionId;
        private int habitacionNumero;
        private String habitacionTipo;
        private int huespedId;
        private String huespedNombre;
        private String huespedDocumento;
        private LocalDate fechaIngreso;
        private LocalDate fechaSalida;
        private BigDecimal precioTotal;
        
        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getHabitacionId() { return habitacionId; }
        public void setHabitacionId(int habitacionId) { this.habitacionId = habitacionId; }
        public int getHabitacionNumero() { return habitacionNumero; }
        public void setHabitacionNumero(int habitacionNumero) { this.habitacionNumero = habitacionNumero; }
        public String getHabitacionTipo() { return habitacionTipo; }
        public void setHabitacionTipo(String habitacionTipo) { this.habitacionTipo = habitacionTipo; }
        public int getHuespedId() { return huespedId; }
        public void setHuespedId(int huespedId) { this.huespedId = huespedId; }
        public String getHuespedNombre() { return huespedNombre; }
        public void setHuespedNombre(String huespedNombre) { this.huespedNombre = huespedNombre; }
        public String getHuespedDocumento() { return huespedDocumento; }
        public void setHuespedDocumento(String huespedDocumento) { this.huespedDocumento = huespedDocumento; }
        public LocalDate getFechaIngreso() { return fechaIngreso; }
        public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
        public LocalDate getFechaSalida() { return fechaSalida; }
        public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }
        public BigDecimal getPrecioTotal() { return precioTotal; }
        public void setPrecioTotal(BigDecimal precioTotal) { this.precioTotal = precioTotal; }
    }

    /**
     * Lista huéspedes que tienen reservas activas (fecha actual entre ingreso y salida).
     * Estos son los "huéspedes actuales" del hotel.
     */
    public List<Huesped> listarHuespedesActuales() throws Exception {
        List<Huesped> lista = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        
        String sql = "SELECT DISTINCT hu.* " +
                     "FROM huesped hu " +
                     "JOIN reserva r ON hu.id = r.huesped_id " +
                     "WHERE ? >= r.fecha_ingreso AND ? <= r.fecha_salida " +
                     "ORDER BY hu.nombre";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, Date.valueOf(hoy));
            ps.setDate(2, Date.valueOf(hoy));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Huesped h = new Huesped();
                    h.setId(rs.getInt("id"));
                    h.setNombre(rs.getString("nombre"));
                    h.setTelefono(rs.getString("telefono"));
                    h.setDocumento(rs.getString("documento"));
                    lista.add(h);
                }
            }
        }
        return lista;
    }
}