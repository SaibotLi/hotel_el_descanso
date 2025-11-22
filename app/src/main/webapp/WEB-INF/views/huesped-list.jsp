<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Listado de Huéspedes</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css"
    />
  </head>
  <body class="container">
    <a href="${pageContext.request.contextPath}/pw3/" class="btn btn-primary"
      >🏠 Inicio</a
    >
    <br /><br />

    <h1>Huéspedes</h1>

    <a href="${pageContext.request.contextPath}/pw3/huespedes/nuevo"
      >Nuevo Huésped</a
    >
    <br /><br />

    <c:if test="${not empty error}">
      <div class="error"><c:out value="${error}" /></div>
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
          <td><c:out value="${h.id}" /></td>
          <td><c:out value="${h.nombre}" /></td>
          <td><c:out value="${h.telefono}" /></td>
          <td><c:out value="${h.documento}" /></td>
          <td>
            <a
              href="${pageContext.request.contextPath}/pw3/huespedes/editar/${h.id}"
              >Editar</a
            >
            |
            <a
              href="${pageContext.request.contextPath}/pw3/huespedes/eliminar/${h.id}"
              onclick="return confirm('¿Está seguro de que desea eliminar este huésped?');"
              >Eliminar</a
            >
          </td>
        </tr>
      </c:forEach>
    </table>
  </body>
</html>
