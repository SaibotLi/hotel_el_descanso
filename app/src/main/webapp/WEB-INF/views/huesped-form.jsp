<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Huésped</title>
  </head>
  <body>
    <h1>
      <c:choose>
        <c:when test="${huesped != null && huesped.id > 0}">
          Editar Huésped
        </c:when>
        <c:otherwise> Nuevo Huésped </c:otherwise>
      </c:choose>
    </h1>

    <c:if test="${not empty error}">
      <div style="color: red"><c:out value="${error}" /></div>
    </c:if>

    <form
      method="post"
      action="${pageContext.request.contextPath}/pw3/huespedes/guardar"
    >
      <c:if test="${huesped != null && huesped.id > 0}">
        <input type="hidden" name="id" value="<c:out value="${huesped.id}" />">
      </c:if>

      Nombre:<br />
      <input type="text" name="nombre" value="<c:out
        value="${huesped.nombre}"
      />" required> <br /><br />

      Teléfono:<br />
      <input type="tel" name="telefono" value="<c:out
        value="${huesped.telefono}"
      />" pattern="[0-9]*" inputmode="numeric"> <br /><br />

      Documento:<br />
      <input type="number" name="documento" value="<c:out
        value="${huesped.documento}"
      />" required> <br /><br />

      <button type="submit">Guardar</button>
    </form>

    <br />
    <a href="${pageContext.request.contextPath}/pw3/huespedes/"
      >Volver al listado</a
    >
  </body>
</html>
