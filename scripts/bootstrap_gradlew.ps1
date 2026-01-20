Param([string]$GradleVersion = "8.7")

$ErrorActionPreference = "Stop"
$RootDir = Resolve-Path (Join-Path $PSScriptRoot "..")
$TmpDir = Join-Path $RootDir ".tmp_gradle_bootstrap"
$DistUrl = "https://services.gradle.org/distributions/gradle-$GradleVersion-bin.zip"
$ZipPath = Join-Path $TmpDir "gradle.zip"

New-Item -ItemType Directory -Force -Path $TmpDir | Out-Null
Write-Host "Downloading Gradle $GradleVersion..."
Invoke-WebRequest -Uri $DistUrl -OutFile $ZipPath

Write-Host "Unzipping..."
Expand-Archive -Path $ZipPath -DestinationPath $TmpDir -Force

$GradleBat = Join-Path $TmpDir "gradle-$GradleVersion\bin\gradle.bat"

Write-Host "Generating wrapper..."
Push-Location $RootDir
& $GradleBat wrapper --gradle-version $GradleVersion
Pop-Location

Remove-Item -Recurse -Force $TmpDir
Write-Host "Done: .\gradlew.bat is ready."
