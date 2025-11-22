<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Listado de Reservas</title>
</head>
<body>
    <h1>Reservas</h1>

    <a href="${pageContext.request.contextPath}/pw3/reservas/nueva">Nueva Reserva</a>
    <br><br>

    <c:if test="${not empty error}">
        <div style="color:red"><c:out value="${error}"/></div>
    </c:if>
    <br>

    <!-- Formulario de filtros -->
    <form method="get" action="${pageContext.request.contextPath}/pw3/reservas/">
        <strong>Filtros:</strong><br>
        
        Fecha desde:
        <input type="date" name="fechaDesde" value="<c:out value="${fechaDesdeSeleccionada}"/>">
        
        Fecha hasta:
        <input type="date" name="fechaHasta" value="<c:out value="${fechaHastaSeleccionada}"/>">
        
        Tipo de habitación:
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
        
        <c:if test="${not empty fechaDesdeSeleccionada || not empty fechaHastaSeleccionada || not empty tipoSeleccionado}">
            <a href="${pageContext.request.contextPath}/pw3/reservas/">[Quitar filtros]</a>
        </c:if>
    </form>
    <br>

    <table border="1" cellpadding="10">
        <tr>
            <th>ID</th>
            <th>Huésped</th>
            <th>Habitación</th>
            <th>Fecha Ingreso</th>
            <th>Fecha Salida</th>
            <th>Precio Total</th>
            <th>Acciones</th>
        </tr>

        <c:forEach var="r" items="${reservas}">
            <tr>
                <td><c:out value="${r.id}"/></td>
                <td><c:out value="${r.huespedNombre}"/> (<c:out value="${r.huespedDocumento}"/>)</td>
                <td>#<c:out value="${r.habitacionNumero}"/> - <c:out value="${r.habitacionTipo}"/></td>
                <td><c:out value="${r.fechaIngreso}"/></td>
                <td><c:out value="${r.fechaSalida}"/></td>
                <td>$<c:out value="${r.precioTotal}"/></td>
                <td>
                    <a href="${pageContext.request.contextPath}/pw3/reservas/cancelar/${r.id}">Cancelar</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
