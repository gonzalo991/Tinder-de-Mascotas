Tinder de mascotas - Procedimientos de Despliegue

PRIMERO : 
  Se definió la conexión con la base de datos cloudclever y las demás configuraciones correspondientes
en el archivos application.properties antes de hacer "build" al proyecto.
  Se creó el archivo system.properties para el posterior despliegue del proyecto en heroku,
en el cuál se definieron la versión de maven correspondiente y la version de open sdk, no se definió
la versión de java ya que estamos usando la versión 8 y ésta es la que heroku usa por defecto.

SEGUNDO: 
  Se tuvo que hacer un cambio en los métodos de la configuración de seguridad, ya que el proyecto estaba
basado en la versión 2.5.4 de maven, en la versión 5.6.2 tanto de Spring Security Core y Spring Security Web
y en la versión 8.2 de NetBeans, esto se debe a que los métodos de WebConfigurerAdapter quedaron obsoletos 
en las ultimas versiones las cuales estamos utilizando : Maven 3.8.6, Spring Security Core/Web 5.7.2 y Apache NetBeans 13,
el método actualizado que se utilizo en ver de configure, fue SecurityFilterChain.

TERCERO: 
  Se agregó un Bean para la configuración de JavaMailSender, ya que al actualizarse las dependencias de pring-starter-mail y spring-context-support
la forma de inicializar el mail sender cambió ya que éste proyecto parte de una versión anterior 
tanto de NetBeans como de las dependecias usadas, y en éste momento lo estamos actualizando.