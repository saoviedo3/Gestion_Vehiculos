@echo off
echo ========================================
echo Diagnostico de Swagger
echo ========================================
echo.

echo 1. Verificando endpoints de prueba...
echo.
curl -s -w "HTTP Status: %%{http_code}\n" http://localhost:80/api/test/hello
curl -s -w "HTTP Status: %%{http_code}\n" http://localhost:80/api/test/status

echo.
echo 2. Verificando documentacion de la API...
echo.
curl -s -w "HTTP Status: %%{http_code}\n" http://localhost:80/v3/api-docs

echo.
echo 3. Verificando configuracion de Swagger...
echo.
curl -s -w "HTTP Status: %%{http_code}\n" http://localhost:80/v3/api-docs/swagger-config

echo.
echo 4. Verificando Swagger UI...
echo.
curl -s -w "HTTP Status: %%{http_code}\n" http://localhost:80/swagger-ui.html

echo.
echo ========================================
echo URLs de Diagnostico:
echo ========================================
echo.
echo Endpoints de prueba:
echo - Test Hello: http://localhost:80/api/test/hello
echo - Test Status: http://localhost:80/api/test/status
echo.
echo Swagger:
echo - UI: http://localhost:80/swagger-ui.html
echo - API Docs: http://localhost:80/v3/api-docs
echo - Config: http://localhost:80/v3/api-docs/swagger-config
echo.
echo ========================================
echo Si los endpoints de prueba funcionan pero Swagger no muestra endpoints:
echo 1. Verifica los logs de la aplicacion
echo 2. Asegurate de que el context-path este configurado
echo 3. Reinicia la aplicacion
echo ========================================
pause
