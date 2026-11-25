# MPAZ PRO — Backend

Microservicios Spring Boot + **Spring Cloud Gateway MVC** + MySQL 8, orquestados con **Docker Compose**.

El cliente Android solo consume el **gateway en `:8080`**.

## Servicios

| Contenedor | Puerto | Responsabilidad |
|---|---|---|
| `mpaz-gateway` | 8080 | Enrutamiento `/api/v1/**` |
| `mpaz-ms-admin` | 8081 | Auth JWT, usuarios, cursos, matrículas, historial UTP |
| `mpaz-ms-contenidos` | 8082 | Asignaturas, unidades, OA, materiales, recursos |
| `mpaz-ms-actividades` | 8083 | Prácticas, preguntas, prueba final |
| `mpaz-ms-progreso` | 8084 | Asignaciones, envíos, intentos, informes |
| `mpaz-mysql` | 127.0.0.1:3307 | Bases: `mpaz_admin`, `mpaz_contenidos`, `mpaz_actividades`, `mpaz_progreso` |

## Requisitos

- Docker Desktop
- Archivo `.env` en esta carpeta (no se versiona)

### Claves / secretos (compañeros)

El `.env` está en `.gitignore`, por eso al clonar las claves “vienen vacías”.  
Usá el ejemplo con valores **demo del equipo**:

```bash
# Windows (PowerShell / cmd)
copy .env.example .env

# macOS / Linux
cp .env.example .env
```

Variables incluidas (demo local / defensa):

- `MPAZ_MYSQL_ROOT_PASSWORD`
- `MPAZ_MYSQL_PASSWORD` (contenidos)
- `MPAZ_ADMIN_MYSQL_PASSWORD`
- `MPAZ_ACTIVIDADES_MYSQL_PASSWORD`
- `MPAZ_PROGRESO_MYSQL_PASSWORD`
- `MPAZ_JWT_SECRET`

**No versionar** el `.env`. Si MySQL ya tenía un volumen creado con otras claves, o bien alineás el `.env` a esas claves, o borrás el volumen y levantás de cero:

```bash
docker compose down -v
docker compose up -d --build
```

(`-v` borra datos de MySQL; solo en entorno local.)

## Levantar

```bash
copy .env.example .env   # solo la primera vez
docker compose up -d --build
docker compose ps
curl http://127.0.0.1:8080/actuator/health
```

Respuesta esperada: `"status":"UP"`.

Parar:

```bash
docker compose down
```

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

Ver README en `../../frontend/mpaz-pro` (APK, `adb reverse`, guion de defensa).
