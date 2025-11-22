<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Listado de Huéspedes</title>
  </head>
  <body>
    <h1>Huéspedes</h1>

    <a href="${pageContext.request.contextPath}/pw3/huespedes/nuevo"
      >Nuevo Huésped</a
    >
    <br /><br />

    <c:if test="${not empty error}">
      <div style="color: red">${error}</div>
    </c:if>
    <br />

    <table border="1" cellpadding="10">
      <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Teléfono</th>
        <th>Documento</th>
        <th>Acciones</th>
      </tr>

      <c:forEach var="h" items="${huespedes}">
        <tr>
          <td>${h.id}</td>
          <td>${h.nombre}</td>
          <td>${h.telefono}</td>
          <td>${h.documento}</td>
          <td>
            <a
              href="${pageContext.request.contextPath}/pw3/huespedes/editar/${h.id}"
              >Editar</a
            >
            |
            <a
              href="${pageContext.request.contextPath}/pw3/huespedes/eliminar/${h.id}"
              >Eliminar</a
            >
          </td>
        </tr>
      </c:forEach>
    </table>
  </body>
</html>
