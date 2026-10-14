##TattoManager - gestion y cotizacion para estudios de tatuaje 
-
#asignatura: programacion android 

#repositorio: https://github.com/Nacho1128/TattoManager.git

##Descripcion## 

TattoManager es una aplicacion nativa de  android (java) diseñada para optimizar
el flujo de trabajo de artistas y estudios de tatuaje. permite calcular presupuestos dinamicos basados en dimenciones y estilo tecnico, agendar citas, almacenar datos localmente con SQLite, obtener ubicacion GPS del estudio y monitorear sensores del dispositivo mediante proesamiento multihilo. 

##Caracteristicas principales##

1.-##Cotizador inteligente (`CotizarActivity\`):##

- Calculo automatico de presupuesto segun tamaño en cm y tarifa por estilo (realizmo/color vs tradicional/blackwork).
- Desglose automatico de abono (20%) y saldo pendiente a liquidar.
- Seleccion dinamica de fecha y hora mediante `DatePickerDialog` y `TimePickerDialog`.

2.-##Persistencia de datos local (`DatabaseHelper` / SQLite):##

- Implementacion de `SQLiteOpenHelper` con la tabla `cotizaciones`.
- Operaciones CRUD completas (Crear, Consultar, Actualizar y Eliminar registros).

3.-##Geolocalización GPS (`MapActivity`):##

- Verificacion de permisos dinámicos en runtime (`ACCESS_FINE_LOCATION`).
- Captura de coordenadas geográficas en tiempo real con `FusedLocationProviderClient`.

4.-##Sensores de Hardware y Multihilo (`SensorActivity`):##

- Lectura del acelerómetro en tiempo real mediante `SensorEventListener`.
- Procesamiento asíncrono en segundo plano con `ExecutorService` y actualización en interfaz UI con `Handler(Looper.getMainLooper())` para evitar bloqueos (ANR).

5.-##Diseño y Navegación Adaptable:##

- Interfaz en tema oscuro construida sobre `ConstraintLayout` con `CardView`.
- Navegación con `BottomNavigationView` e `Intents` explícitos controlando el Back Stack.