# Integrador Enunciado

La empresa Aguas Vital SA es una empresa familiar que se dedica al fraccionamiento y comercialización de agua mineral en bidones y soda en sifones.

La empresa cuenta con distribuidores organizados por zonas exclusivas cubriendo todos los barrios de la ciudad de Córdoba, los cuales se encargan de efectuar el traslado de los pedidos hasta el domicilio de cada cliente. Por otra parte, se ofrece a los clientes un servicio de atención telefónica las 24 hs. del día para poder atender los pedidos correspondientes.

El procedimiento para la venta por distribución es el siguiente:

Cuando el cliente llama por teléfono, es atendido por una operadora quien le solicita sus datos personales y los productos necesarios, es decir el detalle de los productos: cantidad de bidones (de 6, 10, 12 o 20 litros) o sifones de soda.

En ese momento se verifica si quien llama es cliente de la empresa; si no lo es, se procede a registrarlo como nuevo cliente (considerando los siguientes datos: tipo y número de documento, nombre, apellido, razón social, dirección, barrio, zona y teléfono).

Luego la operadora le informa al cliente el precio de cada producto, el monto total del pedido, el día y la hora aproximada en el que el distribuidor le entregará el pedido en su domicilio.

Para estimar la fecha de entrega se debe considerar los pedidos efectuados con anterioridad y pendientes de entrega, la zona que corresponde y el distribuidor asignado a dicha zona.

Cabe aclarar que un distribuidor puede efectuar una cantidad determinada de entregas en forma diaria.

Si el cliente está de acuerdo se registra su pedido.

Todas las mañanas, a primera hora del día, la operadora genera un listado de los pedidos pendientes de entrega para cada distribuidor asignado a una determinada zona, y se lo entrega para que proceda al reparto correspondiente.

Además se genera la factura a fin de que el distribuidor pueda cobrarle al cliente.

Los distribuidores se dirigen al domicilio indicado y entregan el pedido, realizando la cobranza (que puede ser de contado o por cualquier medio de pago electrónico) y entregando la factura como comprobante de pago.

En ese momento el distribuidor hace firmar al cliente en el listado para luego poder realizar la rendición correspondiente.

Al finalizar el día el distribuidor se dirige a la empresa y rinde el trabajo realizado entregando el dinero recibido en concepto de pago, el listado con los pedidos entregados y la correspondiente firma del cliente, indicando aquellos pedidos que no fueron entregados.

El encargado administrativo procede a registrar fecha y hora de entrega de cada pedido, forma de pago y cualquier observación de ser necesario.

Además sigue dejando como pendiente aquellos pedidos que no fueron entregados, de manera tal que sea considerado en el listado de reparto del día siguiente.

Al final de cada día el encargado administrativo confecciona un resumen con los ingresos percibidos dirigido al contador de la empresa, para su correspondiente procesamiento.

Puede ocurrir que el cliente desee cancelar el pedido; frente a esta situación, es atendido por la operadora quien procede a registrar la cancelación del mismo y el motivo respectivo, y si el pedido ya está considerado en el reparto de ese día, se comunica por radio con el distribuidor para avisarle que dicho pedido no debe ser entregado, y se registra la factura correspondiente como anulada.

En forma mensual, el presidente de la empresa informa los nuevos precios del producto, si es que hubo alguna modificación, de manera de mantener actualizados los mismos.

Consideraciones
Un distribuidor tiene asignada una sola zona.
Una zona está conformada por uno o más barrios.
Un barrio se encuentra dentro de una única zona.
El domicilio de entrega del pedido es el domicilio del cliente.
Cada factura puede ser pagada con una única forma de pago.

## Requisitos de aprobación

La realización y entrega del trabajo práctico es condición de aprobación de la materia.

Las entregas del TP por parte de los alumnos es grupal y consisten en:

## Primera entrega

Un proyecto en StarUML (de tipo Rational) con los modelos y diagramas solicitados, los cuales son:

Diagrama de clases del sistema de información con métodos, atributos, relaciones y cardinalidad.
Diagrama de Casos de Uso de todo el sistema.
Descripción de 2 casos de uso principales.
Diagramas de colaboración o secuencia de los 2 casos de uso principales seleccionados en el paso anterior.
Además de la descripción de dichos casos de uso principales con plantilla utilizada en clase y en formato de documento (.odt, .doc o .docx).

## Segunda entrega

Un proyecto Maven con la implementación funcional solicitada, lo que implica:

Realizar los prototipos de interfaz de todo el sistema construidos con SceneBuilder en XML e incluidos en el proyecto Maven en el paquete correspondiente.
Implementar funcionalidad de los 2 casos de uso principales (seleccionados en la primera entrega) en lenguaje Java.
Además actualizar el proyecto en StarUML (de la primera entrega) añadiendo un diagrama de clases (de diseño) que incluya, también, los patrones de diseño encontrados y manteniendo la trazabilidad de todos los artefactos (diagramas) desarrollados durante la primera entrega.
