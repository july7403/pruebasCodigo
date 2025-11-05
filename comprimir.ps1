# --- Script de PowerShell ---
# Nombre de archivo: comprimir.ps1

$outputFile = "proyecto_completo.txt"
Clear-Content -Path $outputFile -ErrorAction SilentlyContinue

# Tipos de archivo que SÍ queremos incluir (¡ajusta si es necesario!)
$include = @("*.java", "*.xml", "pom.xml", "*.md")

# Carpetas que queremos IGNORAR
$excludeFolders = @(".git", ".idea", "target", "build", "GestorCombate.java")

# Obtenemos la ruta completa de la carpeta actual
$projectRoot = $PSScriptRoot | Resolve-Path

Write-Host "Recopilando archivos del proyecto en $projectRoot..."

Get-ChildItem -Path $projectRoot -Recurse -File -Include $include | ForEach-Object {
    $isExcluded = $false
    $currentFile = $_
    
    # Revisamos si el archivo está dentro de una carpeta excluida
    foreach ($folder in $excludeFolders) {
        if ($currentFile.FullName -like "*\$folder\*") {
            $isExcluded = $true
            break
        }
    }
    
    # Si no está excluido, lo añadimos al archivo de texto
    if (-not $isExcluded) {
        Write-Host "Agregando: $($currentFile.FullName)"
        "--- INICIO: $($currentFile.FullName.Replace($projectRoot, '')) ---" | Add-Content -Path $outputFile
        Get-Content -Path $currentFile.FullName | Add-Content -Path $outputFile
        "`n--- FIN: $($currentFile.FullName.Replace($projectRoot, '')) ---`n`n" | Add-Content -Path $outputFile
    }
}

Write-Host "¡Listo! Proyecto concatenado en $outputFile"