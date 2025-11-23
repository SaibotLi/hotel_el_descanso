<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Habitación</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="container">

<a href="${pageContext.request.contextPath}/pw3/" class="btn btn-primary">🏠 Inicio</a>
<br><br>

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
    <div class="error"><c:out value="${error}"/></div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/pw3/habitaciones/guardar">
    
    <!-- Token CSRF -->
    <input type="hidden" name="csrfToken" value="<c:out value="${csrfToken}"/>">
    
    <!-- Campo oculto para saber si es edición -->
    <c:if test="${habitacion != null}">
        <input type="hidden" name="id" value="<c:out value="${habitacion.id}"/>">
    </c:if>

    Número:<br>
    <input type="number" name="numero" value="<c:out value="${habitacion.numero}"/>">
    <br><br>

    Tipo:<br>
    <select name="tipo">
        <option value="">-- Seleccione un tipo --</option>
        <c:forEach var="tipo" items="${tiposDisponibles}">
            <option value="<c:out value="${tipo}"/>" 
                <c:if test="${habitacion.tipo == tipo}">selected</c:if> >
                <c:out value="${tipo}"/>
            </option>
        </c:forEach>
    </select>
    <br><br>

    Precio por noche:<br>
    <input type="number" name="precio" value="<c:out value="${habitacion.precioPorNoche}"/>" min="0" required>
    <br><br>

    <button type="submit">Guardar</button>
</form>

<br>
<a href="${pageContext.request.contextPath}/pw3/habitaciones/">Volver al listado</a>

</body>
</html>