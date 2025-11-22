<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Habitación</title>
</head>
<body>

<h1>
    <c:choose>
        <c:when test="${habitacion != null}">
            Editar Habitación
        </c:when>
        <c:otherwise>
            Nueva Habitación
        </c:otherwise>
    </c:choose>
</h1>

<c:if test="${not empty error}">
    <div style="color:red">${error}</div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/pw3/habitaciones/guardar">
    
    <!-- Campo oculto para saber si es edición -->
    <c:if test="${habitacion != null}">
        <input type="hidden" name="id" value="${habitacion.id}">
    </c:if>

    Número:<br>
    <input type="number" name="numero" value="${habitacion.numero}">
    <br><br>

    Tipo:<br>
    <input type="text" name="tipo" value="${habitacion.tipo}">
    <br><br>

    Precio por noche:<br>
    <input type="text" name="precio" value="${habitacion.precioPorNoche}">
    <br><br>

    <button type="submit">Guardar</button>
</form>

<br>
<a href="${pageContext.request.contextPath}/pw3/habitaciones/">Volver al listado</a>

</body>
</html>