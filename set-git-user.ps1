# Script para cambiar la identidad de Git en este proyecto
# Uso: ./set-git-user.ps1 "Tu Nombre" "tu-correo@ejemplo.com"

param (
    [Parameter(Mandatory=$true)]
    [string]$Name,

    [Parameter(Mandatory=$true)]
    [string]$Email
)

Write-Host "Configurando Git local para Aula Señora..." -ForegroundColor Cyan

git config user.name "$Name"
git config user.email "$Email"

Write-Host "¡Listo!" -ForegroundColor Green
Write-Host "Nombre: $(git config user.name)"
Write-Host "Email:  $(git config user.email)"
Write-Host ""
Write-Host "IMPORTANTE: Para subir los cambios (push), Windows podría pedirte credenciales." -ForegroundColor Yellow
Write-Host "Si eso pasa, usa tu nombre de usuario y un Personal Access Token (PAT) como contraseña." -ForegroundColor Yellow
