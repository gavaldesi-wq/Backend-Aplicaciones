#!/bin/bash
set -eu

mysql -u root -p"$MYSQL_ROOT_PASSWORD" <<EOSQL
CREATE DATABASE IF NOT EXISTS mpaz_actividades CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS mpaz_progreso CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'mpaz_actividades_app'@'%' IDENTIFIED BY '${MPAZ_ACTIVIDADES_MYSQL_PASSWORD}';
CREATE USER IF NOT EXISTS 'mpaz_progreso_app'@'%' IDENTIFIED BY '${MPAZ_PROGRESO_MYSQL_PASSWORD}';

GRANT ALL PRIVILEGES ON mpaz_actividades.* TO 'mpaz_actividades_app'@'%';
GRANT ALL PRIVILEGES ON mpaz_progreso.* TO 'mpaz_progreso_app'@'%';
FLUSH PRIVILEGES;
EOSQL
