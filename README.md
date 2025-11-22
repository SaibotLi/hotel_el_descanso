En la carpeta hotel_el_descanso:

gradle clean build

En:
payara6/glassfish/domains/domain1/lib
Se debe alojar:
postgresql-42.7.3.jar
El cuál se puede instalar en:
Link oficial: https://jdbc.postgresql.org/download

En payara6/bin ejecutar:
asadmin start-domain

asadmin deploy build/libs/hotel-el-descanso.war

Deberia poder visualizarse en:
http://localhost:8080/hotel-el-descanso/pw3/habitaciones/

VERSIONES
PostgreSQL 18
Payara 6 
Postgresql-42.7.3.jar
