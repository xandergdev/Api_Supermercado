# API Supermercado

API REST para gestionar categorías, proveedores, productos, clientes y ventas de un supermercado.
Hecha con Spring Boot 3, Spring Data JPA, Validation y Lombok.

## Requisitos
- JDK 17 o superior
- Maven
- PostgreSQL o MySQL
- Postman

## Configuración
1. Crea la base de datos con `schema.sql` (`CREATE DATABASE supermercado;`).
2. En `src/main/resources/application.properties` pon tu usuario y contraseña
   (hay una sección para PostgreSQL y otra para MySQL).
3. Las tablas se crean solas al iniciar la aplicación.

## Ejecución
```
mvn spring-boot:run
```
La API queda en `http://localhost:8080`.

## Endpoints
| Recurso | Endpoints |
|---|---|
| Categorías | `POST/GET /api/categorias`, `GET/PUT/DELETE /api/categorias/{id}` |
| Proveedores | `POST/GET /api/proveedores`, `GET/PUT/DELETE /api/proveedores/{id}` |
| Productos | `POST/GET /api/productos`, `GET/PUT/DELETE /api/productos/{id}`, `GET /api/productos/categoria/{categoriaId}`, `GET /api/productos/buscar?nombre=`, `GET /api/productos/stock-bajo?limite=10` |
| Clientes | `POST/GET /api/clientes`, `GET/PUT/DELETE /api/clientes/{id}`, `GET /api/clientes/documento/{documento}` |
| Ventas | `POST/GET /api/ventas`, `GET/DELETE /api/ventas/{id}`, `GET /api/ventas/cliente/{clienteId}`, `GET /api/ventas/fecha?inicio=AAAA-MM-DD&fin=AAAA-MM-DD` |

## Reglas de negocio
- Al registrar una venta se valida el stock, se toma el precio actual del producto, se calculan subtotales y total, y se descuenta el stock.
- Al eliminar una venta se restituye el stock.
- No se eliminan categorías, proveedores, clientes ni productos con datos relacionados (409).
- Código de barras y documento de cliente son únicos.

## Códigos HTTP
200 consultas y actualizaciones · 201 creación · 204 eliminación · 400 datos inválidos · 404 no encontrado · 409 conflicto

## Pruebas
Importa `Supermercado.postman_collection.json` en Postman y ejecuta en este orden:
categoría, proveedor, producto, cliente, venta.
