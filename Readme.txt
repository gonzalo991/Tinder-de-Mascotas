Tinder de mascotas - Procedimientos de Despliegue

PRIMERO : 
  Se definió la conexión con la base de datos cloudclever y las demás configuraciones correspondientes
en el archivos application.properties antes de hacer "build" al proyecto.
  Se creó el archivo system.properties para el posterior despliegue del proyecto en heroku,
en el cuál se definieron la versión de maven correspondiente y la version de open sdk, no se definió
la versión de java ya que estamos usando la versión 8 y ésta es la que heroku usa por defecto.

SEGUNDO: 
