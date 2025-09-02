# PowerShell script to run backend and frontend in development concurrently
# Usage: ./dev.ps1

# Ensure we are at the repo root
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

# Start backend dev
Write-Host "Starting Quarkus backend (dev mode)..." -ForegroundColor Cyan
$backend = Start-Process -FilePath "mvnw.cmd" -ArgumentList "-f","backend/pom.xml","quarkus:dev" -NoNewWindow -PassThru

# Start frontend dev
Write-Host "Starting Astro frontend (dev server)..." -ForegroundColor Cyan
$frontend = Start-Process -FilePath "npm.cmd" -ArgumentList "run","dev" -WorkingDirectory "frontend" -NoNewWindow -PassThru

# Trap Ctrl+C to stop both
$stopped = $false
Register-EngineEvent PowerShell.Exiting -Action {
  if (-not $stopped) {
    Write-Host "Stopping processes..." -ForegroundColor Yellow
    if ($backend -and !$backend.HasExited) { Stop-Process -Id $backend.Id -Force }
    if ($frontend -and !$frontend.HasExited) { Stop-Process -Id $frontend.Id -Force }
  }
}

Write-Host "Both dev servers launched. Press Ctrl+C to stop." -ForegroundColor Green

# Wait until both exit
while (($backend -and -not $backend.HasExited) -or ($frontend -and -not $frontend.HasExited)) {
  Start-Sleep -Seconds 1
}

$stopped = $true
