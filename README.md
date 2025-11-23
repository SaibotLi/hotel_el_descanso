Parados en la raíz del proyecto (Carpeta: hotel_el_descanso):

PASO 1
Ejecutar:
gradle clean build

PASO 2:
En:
payara6/glassfish/domains/domain1/lib
Se debe alojar:
postgresql-42.7.3.jar
El cuál se puede instalar en:
Link oficial: https://jdbc.postgresql.org/download

PASO 3:
En payara6/bin ejecutar:
asadmin start-domain

PASO 4:
asadmin deploy build/libs/hotel-el-descanso.war

Deberia poder visualizarse en:
http://localhost:8080/hotel-el-descanso/pw3/habitaciones/

VERSIONES
PostgreSQL 18
Payara 6 
Postgresql-42.7.3.jar
