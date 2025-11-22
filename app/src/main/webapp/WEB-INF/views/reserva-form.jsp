<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Nueva Reserva</title>
  </head>
  <body>
    <h1>Nueva Reserva</h1>

    <c:if test="${not empty error}">
      <div style="color: red">${error}</div>
    </c:if>

    <form
      method="post"
      action="${pageContext.request.contextPath}/pw3/reservas/guardar"
    >
      Huésped:<br />
      <select name="huespedId" required>
        <option value="">-- Seleccione un huésped --</option>
        <c:forEach var="h" items="${huespedes}">
          <option value="${h.id}">${h.nombre} - ${h.documento}</option>
        </c:forEach>
      </select>
      <br /><br />

      Habitación:<br />
      <select name="habitacionId" required>
        <option value="">-- Seleccione una habitación --</option>
        <c:forEach var="hab" items="${habitaciones}">
          <option value="${hab.id}">
            #${hab.numero} - ${hab.tipo} - $${hab.precioPorNoche}/noche
          </option>
        </c:forEach>
      </select>
      <br /><br />

      Fecha de Ingreso:<br />
      <input type="date" name="fechaIngreso" required />
      <br /><br />

      Fecha de Salida:<br />
      <input type="date" name="fechaSalida" required />
      <br /><br />

      <button type="submit">Guardar Reserva</button>
    </form>

    <br />
    <a href="${pageContext.request.contextPath}/pw3/reservas/"
      >Volver al listado</a
    >
  </body>
</html>
