# Configuración de Swagger/OpenAPI - Gestión de Vehículos

## Descripción
Esta aplicación tiene configurado Swagger/OpenAPI para documentar y probar la API REST de Gestión de Vehículos del Banco del Pacífico.

## URLs de Acceso

### Producción (AWS)
- **Interfaz de Swagger**: `http://banquito-alb-1166574131.us-east-2.elb.amazonaws.com/api/vehiculos/swagger-ui.html`
- **Documentación JSON**: `http://banquito-alb-1166574131.us-east-2.elb.amazonaws.com/api/vehiculos/v3/api-docs`
- **Redirección automática**: `http://banquito-alb-1166574131.us-east-2.elb.amazonaws.com/api/vehiculos/`

### Local
- **Interfaz de Swagger**: `http://localhost:80/swagger-ui.html`
- **Documentación JSON**: `http://localhost:80/v3/api-docs`
- **Redirección automática**: `http://localhost:80/`

## Características de la Configuración

### Servidores Configurados
1. **Servidor de Producción**: AWS Application Load Balancer
2. **Servidor Local**: Desarrollo local

### Configuraciones de Swagger UI
- **Tema**: Feeling Blue
- **Ordenamiento**: Por método HTTP
- **Expansión**: Colapsada por defecto
- **Duración de requests**: Visible
- **Try it out**: Habilitado
- **Filtros**: Habilitados
- **Deep linking**: Habilitado
- **Syntax highlighting**: Tema Monokai

### Endpoints Documentados

#### Autenticación (`/api/concesionarios/v1/auth`)
- Login de usuarios
- Gestión de usuarios (CRUD)
- Perfil de usuario actual

#### Concesionarios (`/api/concesionarios/v1`)
- Gestión de concesionarios (CRUD)
- Búsqueda por RUC, estado, razón social, email
- Gestión de vendedores asociados
- Gestión de vehículos asociados

## Seguridad

### Endpoints Públicos (Sin Autenticación)
- `/swagger-ui/**` - Interfaz de Swagger
- `/v3/api-docs/**` - Documentación de la API
- `/swagger-ui.html` - Página principal de Swagger
- `/actuator/**` - Métricas y health checks
- `/` - Redirección a Swagger

### Endpoints Protegidos
- `/api/concesionarios/**` - Requiere autenticación
- Otros endpoints requieren autenticación

## CORS Configurado
La aplicación permite acceso desde:
- Localhost (varios puertos)
- Servidor AWS de producción
- Ambos protocolos HTTP y HTTPS

## Uso

1. **Acceder a la interfaz**: Navega a cualquiera de las URLs de Swagger
2. **Explorar endpoints**: Revisa la documentación de cada operación
3. **Probar API**: Usa el botón "Try it out" para ejecutar requests
4. **Autenticación**: Para endpoints protegidos, usa el endpoint de login primero

## Solución de Problemas

### Si Swagger se carga pero no muestra endpoints:

1. **Verificar logs**: Revisa los logs de la aplicación para errores
2. **Probar endpoints de prueba**: Accede a `/api/test/hello` para verificar que la API funcione
3. **Verificar contexto**: Asegúrate de que el context-path esté configurado correctamente
4. **Reiniciar aplicación**: A veces es necesario reiniciar después de cambios de configuración

### Endpoints de prueba disponibles:
- `GET /api/test/hello` - Mensaje de prueba
- `GET /api/test/status` - Estado del sistema

### Configuraciones de perfil:
- **Desarrollo**: Usa `application-dev.properties` para logging detallado
- **AWS**: Usa `application-aws.properties` para producción

## Dependencias
- `springdoc-openapi-starter-webmvc-ui` (v2.5.0)
- `swagger-annotations` (v2.2.15)

## Configuración Técnica
- **Puerto**: 80
- **Context Path**: `/api/vehiculos`
- **Versión de Spring Boot**: 3.3.0
- **Java**: 21
