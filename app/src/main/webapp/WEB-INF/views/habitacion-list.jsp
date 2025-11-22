<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Listado de Habitaciones</title>
  </head>
  <body>
    <h1>Habitaciones</h1>

    <a href="${pageContext.request.contextPath}/pw3/habitaciones/nueva">Nueva Habitación</a>
    <br /><br />

    <c:if test="${not empty error}">
      <div style="color: red"><c:out value="${error}"/></div>
    </c:if>
    <br>
    <form method="get" action="${pageContext.request.contextPath}/pw3/habitaciones/">
        <strong>Filtrar por tipo:</strong>
        <select name="tipo">
            <option value="">-- Todos los tipos --</option>
            <c:forEach var="t" items="${tiposDisponibles}">
                <option value="<c:out value="${t}"/>" 
                    <c:if test="${t == tipoSeleccionado}">selected</c:if> >
                    <c:out value="${t}"/>
                </option>
            </c:forEach>
        </select>
        <button type="submit">Filtrar</button>
        
        <c:if test="${not empty tipoSeleccionado}">
            <a href="${pageContext.request.contextPath}/pw3/habitaciones/">[Quitar filtro]</a>
        </c:if>
    </form>
    <br>
    <table border="1" cellpadding="10">
      <tr>
        <th>Número</th>
        <th>Tipo</th>
        <th>Precio</th>
        <th>Acciones</th>
      </tr>

      <c:forEach var="h" items="${habitaciones}">
        <tr>
          <td><c:out value="${h.numero}"/></td>
          <td><c:out value="${h.tipo}"/></td>
          <td>$<c:out value="${h.precioPorNoche}"/></td>
          <td>
            <a href="${pageContext.request.contextPath}/pw3/habitaciones/editar/${h.id}">Editar</a>
            |
            <a href="${pageContext.request.contextPath}/pw3/habitaciones/eliminar/${h.id}">Eliminar</a>
          </td>
        </tr>
      </c:forEach>
    </table>
  </body>
</html>
