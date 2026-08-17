# 🚀 Spring WebFlux Reactivo + MongoDB (Atlas Cloud y Local Docker)

<br>

## 🎬 DEMOSTRACIÓN EN VIDEO (YOUTUBE)

> **Haz clic en la imagen para ver la ejecución completa del proyecto:**
>
> [![DEMO Spring WebFlux + MongoDB Atlas + Docker Local](https://img.youtube.com/vi/govap0U1zAk/0.jpg)](https://www.youtube.com/watch?v=govap0U1zAk)
>
> 🔗 **Link directo:** [https://youtu.be/govap0U1zAk](https://youtu.be/govap0U1zAk)

---

## 📋 Objetivo General del Proyecto

Implementar una **API REST Reactiva completa (CRUD)** usando:

- ✅ **Spring Boot 4.1.0 + Spring WebFlux** (arquitectura no bloqueante / reactiva)
- ✅ **Spring Data MongoDB Reactive** (Repositorios reactivos)
- ✅ **MongoDB Atlas / Cloud** — Base de datos en la nube de la UIR
- ✅ **MongoDB Local** — Mediante contenedor **Docker** oficial `mongo:8`
- ✅ **Swagger OpenAPI UI reactivo** (SpringDoc) para testeo visual
- ✅ **Postman** como alternativa de testeo de endpoints
- ✅ Arreglo de autoconfiguración Spring Boot 4.x mediante `@Bean` explícitos en `MongoConfig`

---

## 📁 Estructura del Proyecto

```
spring-weblux-nosql/
├── src/main/java/com/brando/spring_weblux_nosql/
│   ├── SpringWebluxNosqlApplication.java   # Main + Diagnóstico de conexión en consola
│   ├── config/
│   │   └── MongoConfig.java                # @Bean explícitos MongoClient (evita fallback localhost)
│   ├── controller/
│   │   └── CustomerController.java         # Endpoints REST reactivos (CRUD)
│   ├── exception/
│   │   ├── CustomerNotFoundException.java
│   │   └── GlobalExceptionHandler.java
│   ├── model/
│   │   └── Customer.java                   # Entidad @Document con firstName/lastName/email/phone
│   ├── repository/
│   │   └── CustomerRepository.java         # ReactiveMongoRepository (reactivo)
│   └── service/
│       └── CustomerService.java            # Lógica de negocio reactiva
├── src/main/resources/
│   └── application.properties              # URIs para Atlas (comentado) y Docker local (activo)
└── pom.xml                                 # Spring Boot 4.1, WebFlux, MongoDB reactivo, SpringDoc
```

---

## 🛠️ Pre-requisitos

| Herramienta | Versión usada | Descripción |
|-------------|---------------|-------------|
| ☕ Java JDK | **25 LTS** | Con `release=25` en el `pom.xml` |
| 📦 Maven | Wrapper incluido (`mvnw.cmd`) | Empaquetado y ejecución |
| 🐳 Docker Desktop | Última estable | Para correr el contenedor `mongo:8` local |
| 🏢 Cuenta DockerHub | Cualquiera | Descarga de la imagen `mongo:8` desde Docker Hub |
| ☁️ Cuenta MongoDB Atlas | Cluster `cluster0.pofa1rg.mongodb.net` | Base de datos en la nube (UIR) |
| 🧪 Postman o Swagger UI | Cualquiera | Testeo de endpoints REST |

---

## 🐳 Configuración MongoDB LOCAL (Docker)

### 1. Descarga y ejecuta el contenedor oficial (MongoDB 8 desde DockerHub)

Ejecuta este comando **en PowerShell o CMD** — crea, arranca y deja persistente el volumen:

```powershell
docker run -d `
  --name mongodb `
  -p 27017:27017 `
  -v mongodb_data:/data/db `
  -e MONGO_INITDB_ROOT_USERNAME=admin `
  -e MONGO_INITDB_ROOT_PASSWORD=admin12345 `
  mongo:8
```

| Flag | Descripción |
|------|-------------|
| `--name mongodb` | Nombre amigable del contenedor |
| `-p 27017:27017` | Expone el puerto 27017 hacia `localhost` |
| `-v mongodb_data:/data/db` | **Persistencia** — tus datos no se pierden aunque borres el contenedor |
| `-e MONGO_INITDB_ROOT_USERNAME=admin` | Crea usuario `admin` en base `admin` |
| `-e MONGO_INITDB_ROOT_PASSWORD=admin12345` | Contraseña del usuario admin |
| `mongo:8` | Imagen **oficial MongoDB versión 8** descargada desde tu cuenta Docker Hub |

### 2. Comprueba que está corriendo

```powershell
docker ps
# Busca mongo:8 y status "Up X minutes"
```

### 3. Cadena de conexión LOCAL (MongoDB Compass o application.properties)

```
mongodb://admin:admin12345@localhost:27017/?authSource=admin
```

- **Base de datos a crear:** `database_local`
- **Colección (se crea automática al insertar):** `customer`
- **authSource=admin** es **obligatorio** porque el usuario `admin` está definido en la base de sistema `admin`

---

## ☁️ Configuración MongoDB Atlas (Cloud / UIR)

1. Accede a **MongoDB Atlas** y crea (o usa) el cluster.
2. Ve a **Database Access** → asegúrate de tener un usuario con permisos `readWriteAnyDatabase`.
3. Ve a **Network Access** → agrega tu IP actual (o `0.0.0.0/0` temporalmente para pruebas).
4. Ve a **Database → Connect → Compass** y copia la URI.

### Cadena de conexión ATLAS usada en el proyecto:

```
mongodb+srv://brandoflores_db_user:e2iOpvB3lPUdvda6@cluster0.pofa1rg.mongodb.net/database_cloud?retryWrites=true&w=majority&appName=Cluster0
```

- **Base de datos:** `database_cloud`
- **Colección:** `customer`
- `retryWrites=true` y `w=majority` obligatorios en Atlas para escritura segura
- `mongodb+srv://` — protocolo DNS SRV para descubrir los 3 nodos del Replica Set

---

## 🔄 Cómo cambiar entre MongoDB Atlas y MongoDB Local (Docker)

Edita el archivo [`src/main/resources/application.properties`](src/main/resources/application.properties):

### Opción A — Usar DOCKER LOCAL (activo por defecto ahora)
```properties
# 👇 ACTIVO: Docker Local
spring.data.mongodb.uri=mongodb://admin:admin12345@localhost:27017/database_local?authSource=admin&retryWrites=true&w=majority

# 👇 COMENTADO: Atlas UIR
# spring.data.mongodb.uri=mongodb+srv://brandoflores_db_user:e2iOpvB3lPUdvda6@cluster0.pofa1rg.mongodb.net/database_cloud?retryWrites=true&w=majority&appName=Cluster0
```

### Opción B — Usar ATLAS (UIR)
Solo invierte los comentarios `#`:
```properties
# spring.data.mongodb.uri=mongodb://admin:admin12345@localhost:27017/database_local?authSource=admin&retryWrites=true&w=majority
spring.data.mongodb.uri=mongodb+srv://brandoflores_db_user:e2iOpvB3lPUdvda6@cluster0.pofa1rg.mongodb.net/database_cloud?retryWrites=true&w=majority&appName=Cluster0
```

### ⚠️ Regla de oro
Cada vez que cambies el URI: **reinicia la app** (`CTRL+C` y volver a ejecutar).

---

## ▶️ Cómo ejecutar el proyecto

### 1. Clona o navega a la raíz del proyecto
```powershell
cd C:\Users\Brando\Documents\spring-weblux-nosql
```

### 2. Limpia, compila y arranca la app (una sola línea)
```powershell
.\mvnw.cmd clean spring-boot:run
```

### 3. Observa el bloque de DIAGNÓSTICO en consola — confirma a qué MongoDB estás conectado

**Si te conectaste a ATLAS verás:**
```
[CLUSTER]  SRV host (post-SRV)   = cluster0.pofa1rg.mongodb.net
[CLUSTER]  ReplicaSet requerido  = atlas-xrqlrh-shard-0
[CLUSTER]  SSL/TLS activado      = true
>>> ✅ CONECTADO AL CLUSTER DE MONGODB ATLAS / UIR
```

**Si te conectaste a DOCKER LOCAL verás:**
```
[CLUSTER]  Hosts seed (pre-SRV)  = localhost:27017
[CLUSTER]  SRV host (post-SRV)   = (no usa SRV)
[CLUSTER]  SSL/TLS activado      = false
>>> ❌ SIGUES EN LOCALHOST/DOCKER (eso es lo correcto para pruebas locales!)
```

### 4. Swagger UI reactivo (OpenAPI) — una vez arrancada la app
Abre en tu navegador:
👉 **http://localhost:8082/swagger-ui.html**

---

## 🔌 Endpoints REST Reactivos (CRUD completo)

| Método HTTP | Endpoint | Descripción | Código respuesta |
|-------------|----------|-------------|------------------|
| 🟢 **POST** | `http://localhost:8082/api/customers` | Crea un nuevo Customer | `201 Created` |
| 🔵 **GET**  | `http://localhost:8082/api/customers` | Devuelve TODOS los customers (Flux reactivo) | `200 OK` |
| 🔵 **GET**  | `http://localhost:8082/api/customers/{id}` | Busca 1 customer por ID (Mono reactivo) | `200 OK` / `404 Not Found` |
| 🟡 **PUT**  | `http://localhost:8082/api/customers/{id}` | Actualiza un customer existente | `200 OK` / `404 Not Found` |
| 🔴 **DELETE**|`http://localhost:8082/api/customers/{id}`| Elimina un customer | `204 No Content` / `404 Not Found` |

### Modelo `Customer` — JSON para POST/PUT:
```json
{
  "firstName": "Juan",
  "lastName":  "Pérez",
  "email":     "juan.perez@correo.uir.edu.co",
  "phone":     "3101234567"
}
```

---

## 🧪 Testeo con Postman o Swagger UI

### Opción 1 — Swagger UI (✅ RECOMENDADO — no requiere instalar nada)
1. Arranca la app
2. Abre **http://localhost:8082/swagger-ui.html**
3. Despliega **Customer API**
4. Clic en `POST /api/customers` → **Try it out** → Exec
5. Luego `GET /api/customers` → verás los registros

### Opción 2 — Postman
1. Crea una colección nueva
2. Agrega las requests del CRUD
3. **Headers** en todas las requests JSON: `Content-Type: application/json`
4. Ejecuta primero un POST y luego GET para validar

---

## 📌 Diagnóstico: ¿Por qué se conectaba a localhost aunque el URI era Atlas?

Este era el bug / "feature" oculto de Spring Boot 4.x con el starter `spring-boot-starter-data-mongodb-reactive`:

1. El `Environment` leía bien el `spring.data.mongodb.uri` de `application.properties`.
2. **Pero** si el lookup DNS SRV (`mongodb+srv://`) fallaba por cualquier motivo, Spring Boot hacía **fallback silencioso a `localhost:27017`** usando su autoconfiguración por defecto — sin avisar, sin credenciales, modo SINGLE.
3. **Solución implementada en el proyecto:**
   Clase [`config/MongoConfig.java`](src/main/java/com/brando/spring_weblux_nosql/config/MongoConfig.java) con **`@Bean @Primary` explícitos** para `MongoClientSettings`, `MongoClient`, `ReactiveMongoDatabaseFactory` y `ReactiveMongoTemplate`. Esto hace que Spring Boot no tenga elección y cree el cliente desde el URI configurado (sin fallback automático).

Adicionalmente, la clase principal `SpringWebluxNosqlApplication` implementa `CommandLineRunner` y **imprime en consola los hosts reales del MongoClientSettings activo**, para que nunca tengas dudas de a qué MongoDB estás conectado realmente.

---

## 📚 Tecnologías y dependencias (pom.xml)

| Dependencia | Propósito |
|-------------|-----------|
| `spring-boot-starter-webflux` | Servidor Netty reactivo no bloqueante |
| `spring-boot-starter-data-mongodb-reactive` | Repositorios reactivos con ReactiveMongoRepository |
| `springdoc-openapi-starter-webflux-ui` | Swagger UI / OpenAPI reactivo |
| `lombok` | Reducción de boilerplate (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`) |
| `spring-boot-starter-validation` | Validación con `@Valid` |
| `spring-boot-starter-test` + `webflux-test` | Pruebas unitarias / integración reactivas |

---

## 🔗 Links rápidos

- 🎬 **Video demostrativo:** [https://youtu.be/govap0U1zAk](https://www.youtube.com/watch?v=govap0U1zAk)
- 📖 Swagger UI (app corriendo): [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
- 📘 Docs OpenAPI JSON: [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs)
- 🐳 Imagen Docker Hub MongoDB: [mongo:8](https://hub.docker.com/_/mongo)
- ☁️ MongoDB Atlas: [https://www.mongodb.com/atlas](https://www.mongodb.com/atlas)

---

**Autor:** Brando Flores  
**Curso / Tarea:** Spring WebFlux Reactivo + MongoDB Atlas + Docker Local  
**Video explicativo:** 👉 [https://youtu.be/govap0U1zAk](https://www.youtube.com/watch?v=govap0U1zAk) 👈
