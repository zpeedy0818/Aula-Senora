# Script para cargar variables de entorno y ejecutar la aplicación
if (Test-Path .env) {
    Get-Content .env | ForEach-Object {
        if ($_ -match "^(?<name>[^=]+)=(?<value>.*)$") {
            $name = $Matches['name'].Trim()
            $value = $Matches['value'].Trim()
            [System.Environment]::SetEnvironmentVariable($name, $value, "Process")
            Write-Host "Set $name"
        }
    }
} else {
    Write-Error ".env file not found!"
    exit 1
}

Write-Host "Iniciando aplicación..." -ForegroundColor Green
./mvnw spring-boot:run
