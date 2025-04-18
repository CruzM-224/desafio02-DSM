# Desafio Practico 2 - DSM
Cruz Enrique Moreno Lozano - ML210800

## Planeamiento del problema
Desarrolla una aplicación móvil en Android que permita a los usuarios registrarse e iniciar sesión
utilizando Firebase Authentication. Una vez autenticados, los usuarios podrán gestionar una To-Do List
(lista de tareas pendientes), almacenando y recuperando sus tareas mediante Firebase Realtime.

## Requisitos
1. Autenticación de Usuarios
   - Implementar el registro y el inicio de sesión mediante Firebase Authentication utilizando
   correo electrónico y contraseña.
   - Validar los datos ingresados por el usuario antes de enviarlos a Firebase.
   - Mostrar mensajes de error en caso de credenciales incorrectas o fallos en la autenticación.
2. Gestión de Tareas (To-Do List)
   - Cada usuario autenticado podrá agregar, editar, eliminar y marcar tareas como
   completadas.
   - Las tareas deben almacenarse en Firebase Realtime Database o SQLite, según la elección
   del estudiante.
   - Cada tarea debe contener al menos:
     - Un título (obligatorio).
     - Una descripción (opcional).
     - Un estado (pendiente o completada).
     - Una fecha de creación.
## Captura de la base
![image](images/registros.png)