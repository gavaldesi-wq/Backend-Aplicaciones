$ErrorActionPreference = "Stop"

Set-Location $PSScriptRoot

if (-not (Test-Path ".env")) {
    Copy-Item ".env.example" ".env"
}

docker info *> $null

if ($LASTEXITCODE -ne 0) {
    throw "Abre Docker Desktop antes de iniciar."
}

docker compose up --build -d --wait --wait-timeout 300

if ($LASTEXITCODE -ne 0) {
    docker compose ps -a
    throw "Error al iniciar el backend."
}

docker compose ps

Write-Host "Backend iniciado correctamente." -ForegroundColor Green