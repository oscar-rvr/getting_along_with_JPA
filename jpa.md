# Getting Along with JPA

A continuación se presenta una nota acerca del ciclo de vida de una entidad en JPA, así como las observaciones trabajadas en los ejercicios de la práctica.

---

## Ciclo de vida de una entidad

| Estado    | Descripción                                                  |
|-----------|--------------------------------------------------------------|
| Transient | Entidad nueva, sin ID y no gestionada por el EntityManager.  |
| Managed   | Entidad vigilada por el EntityManager, se sincroniza sola.   |
| Detached  | Entidad fue persistida pero ya no está en el contexto.       |
| Removed   | Entidad marcada para borrado, se eliminará en el flush.      |

---

## Diferencias entre `persist()`, `merge()` y `save()`

| Método                 | ¿Para qué sirve?                                     | ¿Qué hace si ya tiene ID?              |
|------------------------|------------------------------------------------------|----------------------------------------|
| `persist()`            | Inserta entidad nueva, debe estar sin ID.            | Lanza error si ya hay ID.              |
| `merge()`              | Reconecta entidades detached o actualiza.            | Inserta o actualiza según contexto.    |
| `save()` (Spring Data) | Combina persistencia y actualización.                | Inserta o actualiza.                   |

---

## ¿Qué es `flush()`?

- Fuerza que el `EntityManager` sincronice el estado actual de las entidades con la base de datos.
- Útil cuando no se usa `@Transactional`.

---

## ¿Qué significa que una entidad esté *detached*?

- Ya no está siendo gestionada por el `EntityManager`.
- Cambios en ella no se guardan automáticamente a menos que se vuelva a conectar con `merge()`.

---

## ¿Qué es el `EntityManager`?

Es la interfaz principal de JPA que permite:

- Guardar (`persist`)
- Buscar (`find`)
- Actualizar (`merge`)
- Borrar (`remove`)
- Ejecutar queries y sincronizar (`flush`)

Actúa como un “contexto de persistencia” que vigila los objetos y sus cambios.

---

## Actividades

### [3] Save Parent without ID using repository.save(), entityManager.persist(), entityManager.merge()

- `persist()`: Inserta correctamente.
- `merge()`: Inserta una copia *managed*.
- `save()`: Inserta correctamente.
- **Aprendizaje**: `persist()` es solo para entidades nuevas. `merge()` y `save()` también insertan.

---

### [4] Save Parent with an initialized ID using repository.save(), entityManager.persist(), entityManager.merge()

- `persist()`: Lanza excepción.
- `merge()`: Inserta o actualiza.
- `save()`: Inserta o actualiza.
- **Aprendizaje**: `persist()` no permite ID; los otros dos sí.

---

### [5] Insert Parent with some ID to the database. Save another Parent with the same ID using repository.save(), entityManager.persist(), entityManager.merge()

- `persist()`: Excepción por duplicado.
- `merge()`: Puede sobrescribir sin error.
- `save()`: Sobrescribe.
- **Aprendizaje**: `save()` y `merge()` permiten reusar IDs, pero puede ser peligroso si no se controla.

---

### [6] Save Parent with Children, which are not present in the database - using the same 3 approaches

- `persist()`: Funciona si hay `cascade = PERSIST`.
- `merge()`: Inserta pero puede duplicar si no se gestionan bien los hijos.
- `save()`: Funciona si hay cascada.
- **Aprendizaje**: relaciones necesitan configuración de cascada y `mappedBy`.

---

### [7] Save Parent with Children, which are already present in the database - using the same 3 approaches

- `persist()`: Falla si los hijos tienen ID.
- `merge()`: Puede duplicar si hijos están detached.
- `save()`: Actualiza correctamente si están en BD.
- **Aprendizaje**: cuidado con estados de hijos; `merge()` tiende a duplicar si no están gestionados.

---

### [8] Save Child without Parent - using the same 3 approaches

- Todos los métodos fallan si la relación es obligatoria (violación de FK).
- **Aprendizaje**: el `Parent` debe existir o estar marcado como opcional.

---

###  [9] Save Child with Parent initialized, but not present in the database - using the same 3 approaches

- `persist()`: Funciona si hay `cascade = PERSIST` desde Child hacia Parent.
- `merge()`: Inserta ambos.
- `save()`: Inserta ambos.
- **Aprendizaje**: funciona solo si hay cascada.

---

### [10] Save Child with Parent initialized, present in the database, but detached from EntityManager/Session - using the same 3 approaches

- `persist()`: No funciona (Parent está fuera del contexto).
- `merge()`: Reconecta el Parent y guarda el Child.
- `save()`: Funciona.
- **Aprendizaje**: `merge()` y `save()` manejan reconexión de entidades detached.

---

### [11] Fetch the Parent with JpaRepository, try changing it and don’t save it explicitly. Flush the session and check whether the changes were propagated to the database

- Cambios sí se reflejan al hacer flush.
- **Aprendizaje**: las entidades *managed* se sincronizan automáticamente.

---

### [12] Start the transaction, fetch the Parent with JpaRepository, try changing it and don’t save it explicitly. Flush the session and check whether the changes were propagated to the database

- Cambios también se reflejan sin necesidad de `save()`.
- **Aprendizaje**: dentro de una transacción, los cambios se guardan si la entidad está *managed*.
