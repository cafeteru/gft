# GFT Test

## Requisitos

- **Docker** y **Docker Compose** instalados en el sistema.

## Cómo iniciar el proyecto

Para ejecutar el proyecto completo (aplicación y base de datos PostgreSQL) en un entorno Docker,
sigue estos pasos:

1. Ejecuta el siguiente comando para construir y ejecutar los contenedores con Docker Compose:
   ```bash
   docker-compose up --build
    ```
   Este comando hará lo siguiente:
    - Construirá la imagen de la aplicación Java usando Maven (ejecutando mvn clean install en el
      proceso).
    - Iniciará un contenedor de PostgreSQL con los datos de conexión definidos en el archivo
      docker-compose.yml.
    - Iniciará la aplicación Spring Boot y la conectará a PostgreSQL.

2. Accede a la aplicación en tu navegador web en la dirección `http://localhost:8080` para poder
   usar Swagger.
   
