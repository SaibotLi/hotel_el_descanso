<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Hotel El Descanso - Sistema de Gestión</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css"
    />
  </head>
  <body class="container">
    <header>
      <h1>🏨 Hotel El Descanso</h1>
      <p class="subtitle">Sistema de Gestión de Reservas</p>
    </header>

    <main>
      <div class="menu-grid">
        <div class="menu-card">
          <h2>Habitaciones</h2>
          <p>Gestionar habitaciones del hotel</p>
          <div class="menu-actions">
            <a
              href="${pageContext.request.contextPath}/pw3/habitaciones/"
              class="btn btn-primary"
              >Ver Habitaciones</a
            >
            <a
              href="${pageContext.request.contextPath}/pw3/habitaciones/nueva"
              class="btn btn-secondary"
              >Nueva Habitación</a
            >
          </div>
        </div>

        <div class="menu-card">
          <h2>Huéspedes</h2>
          <p>Gestionar huéspedes registrados</p>
          <div class="menu-actions">
            <a
              href="${pageContext.request.contextPath}/pw3/huespedes/"
              class="btn btn-primary"
              >Ver Huéspedes</a
            >
            <a
              href="${pageContext.request.contextPath}/pw3/huespedes/nuevo"
              class="btn btn-secondary"
              >Nuevo Huésped</a
            >
            <a
              href="${pageContext.request.contextPath}/pw3/huespedes/actuales"
              class="btn btn-info"
              >Huéspedes Actuales</a
            >
          </div>
        </div>

        <div class="menu-card">
          <h2>Reservas</h2>
          <p>Gestionar reservas y consultas</p>
          <div class="menu-actions">
            <a
              href="${pageContext.request.contextPath}/pw3/reservas/"
              class="btn btn-primary"
              >Ver Reservas</a
            >
            <a
              href="${pageContext.request.contextPath}/pw3/reservas/nueva"
              class="btn btn-secondary"
              >Nueva Reserva</a
            >
          </div>
        </div>
      </div>
    </main>
  </body>
</html>
