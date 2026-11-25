[README-backend.md](https://github.com/user-attachments/files/35433069/README-backend.md)
# MPAZ PRO — Backend

Microservicios Spring Boot + **Spring Cloud Gateway MVC** + MySQL 8, orquestados con **Docker Compose**.

El cliente Android consume únicamente el **API Gateway**. El puerto externo predeterminado es `18080`.

## Servicios

| Servicio | Puerto interno Docker | Puerto del PC | Responsabilidad |
|---|---:|---:|---|
| `gateway` | 8080 | 18080 | Enrutamiento `/api/v1/**` |
| `ms-admin` | 8081 | — | Auth JWT, usuarios, cursos, matrículas, historial UTP |
| `ms-contenidos` | 8082 | — | Asignaturas, unidades, OA, materiales, recursos |
| `ms-actividades-evaluaciones` | 8083 | — | Prácticas, preguntas, prueba final |
| `ms-progreso` | 8084 | — | Asignaciones, envíos, intentos, informes |
| `mysql` | 3306 | 13307 | Bases de datos de los cuatro microservicios |

Los microservicios se comunican mediante la red interna de Docker. Solo se publican el Gateway y MySQL, en la interfaz local del PC. Docker Compose asigna automáticamente los nombres de los contenedores.

## Requisitos

- Git y Docker Desktop con Docker Compose.
- En Windows, PowerShell. No es necesario instalar Java o MySQL en el PC si se usa Docker para todo el backend.
- Usar la rama `fix` mientras las correcciones no estén integradas en `main`.

## Iniciar en Windows

1. Abrir Docker Desktop y esperar a que esté operativo.
2. Abrir PowerShell en la raíz del repositorio (donde están `docker-compose.yml` e `iniciar.ps1`).
3. Ejecutar:

```powershell
powershell -ExecutionPolicy Bypass -File .\iniciar.ps1
```

El script crea `.env` a partir de `.env.example` si no existe, construye las imágenes y espera que los servicios estén saludables. No sobrescribe el `.env` existente.

Comprobar el arranque:

```powershell
docker compose ps
Invoke-RestMethod http://127.0.0.1:18080/actuator/health
```

Respuesta esperada: `status: UP`.

### Arranque manual (Linux, macOS o Codespaces)

Desde la raíz del backend:

```bash
[ -f .env ] || cp .env.example .env
docker compose up --build -d --wait --wait-timeout 300
docker compose ps
curl http://127.0.0.1:18080/actuator/health
```

En Codespaces, reenviar el puerto `18080` si se necesita acceder desde fuera del entorno; configurar el acceso y la URL del frontend según corresponda. No publicar secretos ni exponer el entorno demo como servicio de producción.

## Configuración

El archivo `.env` no se versiona. `.env.example` contiene valores demo para desarrollo/defensa local:

- `MPAZ_MYSQL_ROOT_PASSWORD`
- `MPAZ_MYSQL_PASSWORD`
- `MPAZ_ADMIN_MYSQL_PASSWORD`
- `MPAZ_ACTIVIDADES_MYSQL_PASSWORD`
- `MPAZ_PROGRESO_MYSQL_PASSWORD`
- `MPAZ_JWT_SECRET`
- `MPAZ_GATEWAY_PORT=18080`
- `MPAZ_MYSQL_PORT=13307`

Si un puerto externo está ocupado, cambiar su valor en `.env` y volver a levantar Compose. Si se cambia el puerto del Gateway, ajustar también `mpazHostPort` en la configuración de Gradle del frontend local (sin editar Kotlin).

Si se ejecutan los microservicios mediante Maven fuera de Docker, iniciar únicamente MySQL con `docker compose up -d mysql` y usar los puertos nativos 18081–18084 y Gateway 18080. Esta modalidad no se ha verificado de extremo a extremo; el arranque recomendado es con Docker Compose.

## Detener y reiniciar

```bash
docker compose down
```

Los volúmenes conservan los datos de MySQL y los recursos subidos. No usar `docker compose down -v` salvo que se quiera eliminar deliberadamente los datos. Una instalación en otro directorio/proyecto de Compose puede usar volúmenes distintos.

Para consultar errores:

```bash
docker compose ps -a
docker compose logs --tail=100
```

## Verificar persistencia en MySQL

```bash
docker compose exec mysql mysql -u root -p
```

Usar la contraseña de `.env` y consultar la base correspondiente, por ejemplo:

```sql
USE mpaz_contenidos;
SHOW TABLES;
```

Consultar la tabla de materiales antes y después de registrar un material desde Android. Repetir el `SELECT` sobre la tabla real para verificar el nuevo registro.

## Login demo

```http
POST /api/v1/auth/demo-login
Content-Type: application/json

{"codigoUsuario":"UTP01","clave":"demo1234"}
```

Usuarios: `UTP01`, `DOC01`, `EST01` — clave `demo1234`.

## Semilla relevante

- Curso `1` *2° Básico A* + matrícula EST01 (`ms-admin` V5).
- Unidad `10` + OA **`31`** + material publicados (`ms-contenidos` V3).
- Asignación demo OA 31 (`ms-progreso`).
- Actividad/prueba del OA 31 (`ms-actividades-evaluaciones`).

## Notas de integración

- `ms-contenidos` usa `MPAZ_ADMIN_URL=http://ms-admin:8081` para registrar el historial en transiciones UTP (si apunta a `127.0.0.1` dentro de Docker → **503**).
- El gateway define las rutas en `gateway/src/main/resources/application.yml`.
- Contrato compartido del equipo: ver `CONTRATO_COMPARTIDO_MPAZ_PRO_2_0.md` en el workspace EA3.

## App Android

Ver el README del [frontend Android](https://github.com/jimm-volition/mpaz-pro) (rama `fix` mientras esté pendiente la integración).
