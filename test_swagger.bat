@echo off
echo ========================================
echo Prueba de Configuracion de Swagger
echo ========================================
echo.

echo Probando endpoints locales...
echo.

echo 1. Probando redireccion principal...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:80/

echo 2. Probando Swagger UI...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:80/swagger-ui.html

echo 3. Probando API docs...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:80/v3/api-docs

echo.
echo ========================================
echo URLs de Acceso:
echo ========================================
echo.
echo Local:
echo - Swagger UI: http://localhost:80/swagger-ui.html
echo - API Docs:  http://localhost:80/v3/api-docs
echo - Principal: http://localhost:80/
echo.
echo AWS:
echo - Swagger UI: http://banquito-alb-1166574131.us-east-2.elb.amazonaws.com/api/vehiculos/swagger-ui.html
echo - API Docs:  http://banquito-alb-1166574131.us-east-2.elb.amazonaws.com/api/vehiculos/v3/api-docs
echo - Principal: http://banquito-alb-1166574131.us-east-2.elb.amazonaws.com/api/vehiculos/
echo.
echo ========================================
pause
