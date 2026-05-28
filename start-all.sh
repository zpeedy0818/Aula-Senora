#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
LOGS_DIR="$ROOT_DIR/logs"
mkdir -p "$LOGS_DIR"

SERVICES=(
  "microservicios/user-service|8081"
  "microservicios/file-service|8082"
  "microservicios/aula-service|8083"
  "microservicios/messaging-service|8084"
  "microservicios/calendar-service|8085"
  "microservicios/admin-service|8086"
)
PIDS=()

cleanup() {
  echo ""
  echo "[stop] Deteniendo todos los servicios..."
  for pid in "${PIDS[@]}"; do
    if kill -0 "$pid" 2>/dev/null; then
      kill "$pid" 2>/dev/null || true
    fi
  done
  wait 2>/dev/null || true
  echo "[stop] Todos detenidos."
}
trap cleanup EXIT INT TERM

wait_for_port() {
  local port=$1
  local service=$2
  local max_attempts=60
  local attempt=0
  while ! (echo >/dev/tcp/localhost/"$port") 2>/dev/null && [ $attempt -lt $max_attempts ]; do
    sleep 2
    attempt=$((attempt + 1))
  done
  if [ $attempt -lt $max_attempts ]; then
    echo "  -> $service listo en puerto $port (${attempt}s)"
  else
    echo "  -> $service NO responde en puerto $port (timeout)"
  fi
}

start_service() {
  local path="$1"
  local port="$2"
  local name
  name="$(basename "$path")"
  local log="$LOGS_DIR/$name.log"

  echo "[start] $name (puerto $port)"
  mvn spring-boot:run -f "$ROOT_DIR/$path/pom.xml" -q > "$log" 2>&1 &
  PIDS+=("$!")
}

# Build everything first (fast path)
echo "[build] Compilando todo..."
mvn compile -f "$ROOT_DIR/pom.xml" -q 2>&1 | tail -2
for entry in "${SERVICES[@]}"; do
  path="${entry%%|*}"
  mvn compile -f "$ROOT_DIR/$path/pom.xml" -q 2>&1 | tail -2
done
echo "[build] Compilacion OK"

# Start all microservices in parallel
for entry in "${SERVICES[@]}"; do
  IFS='|' read -r path port <<< "$entry"
  start_service "$path" "$port"
done

# Wait for each microservice to be ready
for entry in "${SERVICES[@]}"; do
  IFS='|' read -r path port <<< "$entry"
  wait_for_port "$port" "$(basename "$path")"
done

# Start main app last
echo "[start] main-app (puerto 8080)"
mvn spring-boot:run -f "$ROOT_DIR/pom.xml" -q > "$LOGS_DIR/main-app.log" 2>&1 &
PIDS+=("$!")

echo ""
echo "[ready] Todos los servicios en ejecucion"
echo "[logs]  $LOGS_DIR/"
echo "[use]   Abre http://localhost:8080 en tu navegador"
echo "[stop]  Ctrl+C para detener todo"
echo ""

wait
