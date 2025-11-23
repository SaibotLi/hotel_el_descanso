<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Nueva Reserva</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  </head>
  <body class="container">
    <a href="${pageContext.request.contextPath}/pw3/" class="btn btn-primary">🏠 Inicio</a>
    <br><br>
    <h1>Nueva Reserva</h1>

    <c:if test="${not empty error}">
        <div class="error"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/pw3/reservas/guardar">
      
      <!-- Token CSRF -->
      <input type="hidden" name="csrfToken" value="<c:out value="${csrfToken}"/>">
      
      Huésped:<br />
      <select name="huespedId" required>
        <option value="">-- Seleccione un huésped --</option>
        <c:forEach var="h" items="${huespedes}">
          <option value="<c:out value="${h.id}"/>"><c:out value="${h.nombre}"/> - <c:out value="${h.documento}"/></option>
        </c:forEach>
      </select>
      <br /><br />

      Habitación:<br />
      <select name="habitacionId" required>
        <option value="">-- Seleccione una habitación --</option>
        <c:forEach var="hab" items="${habitaciones}">
          <option value="<c:out value="${hab.id}"/>">
            #<c:out value="${hab.numero}"/> - <c:out value="${hab.tipo}"/> - $<c:out value="${hab.precioPorNoche}"/>/noche
          </option>
        </c:forEach>
      </select>
      <br /><br />

      Fecha de Ingreso:<br />
      <input type="date" name="fechaIngreso" id="fechaIngreso" required 
             min="<%= java.time.LocalDate.now().toString() %>" />
      <br /><br />

      Fecha de Salida:<br />
      <input type="date" name="fechaSalida" id="fechaSalida" required />
      <br /><br />

      <button type="submit">Guardar Reserva</button>
    </form>

    <script>
      // Validar que fecha salida sea posterior a fecha ingreso
      document.getElementById('fechaIngreso').addEventListener('change', function() {
        var fechaIngreso = this.value;
        var fechaSalidaInput = document.getElementById('fechaSalida');
        if (fechaIngreso) {
          fechaSalidaInput.min = fechaIngreso;
        }
      });
    </script>

    <br />
    <a href="${pageContext.request.contextPath}/pw3/reservas/">Volver al listado</a>
  </body>
</html>
