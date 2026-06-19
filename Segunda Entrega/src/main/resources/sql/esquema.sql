CREATE TABLE IF NOT EXISTS zona (
    id_zona TEXT PRIMARY KEY,
    nombre TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS barrio (
    id_barrio TEXT PRIMARY KEY,
    nombre TEXT NOT NULL,
    id_zona TEXT NOT NULL,
    FOREIGN KEY (id_zona) REFERENCES zona(id_zona)
);

CREATE TABLE IF NOT EXISTS envase (
    tipo_envase TEXT NOT NULL,
    capacidad INTEGER NOT NULL,
    PRIMARY KEY (tipo_envase, capacidad)
);

CREATE TABLE IF NOT EXISTS producto (
    codigo TEXT PRIMARY KEY,
    nombre TEXT NOT NULL,
    tipo_envase TEXT NOT NULL,
    capacidad INTEGER NOT NULL,
    FOREIGN KEY (tipo_envase, capacidad) REFERENCES envase(tipo_envase, capacidad)
);

CREATE TABLE IF NOT EXISTS precio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cod_producto TEXT NOT NULL,
    valor REAL NOT NULL,
    fecha_desde TEXT NOT NULL,
    fecha_hasta TEXT,
    FOREIGN KEY (cod_producto) REFERENCES producto(codigo)
);

CREATE TABLE IF NOT EXISTS cliente (
    nro_doc TEXT PRIMARY KEY,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    razon_social TEXT DEFAULT '-',
    direccion TEXT NOT NULL,
    telefono TEXT DEFAULT '',
    id_barrio TEXT NOT NULL,
    FOREIGN KEY (id_barrio) REFERENCES barrio(id_barrio)
);

CREATE TABLE IF NOT EXISTS empleado (
    nro_doc TEXT PRIMARY KEY,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    tipo_doc TEXT DEFAULT 'DNI',
    razon_social TEXT DEFAULT '-',
    cargo TEXT NOT NULL,
    capacidad_maxima INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS pedido (
    nro_pedido INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_emision TEXT NOT NULL,
    fecha_entrega TEXT NOT NULL,
    estado TEXT NOT NULL DEFAULT 'PENDIENTE',
    total REAL NOT NULL DEFAULT 0,
    nro_doc_cliente TEXT NOT NULL,
    metodo_pago TEXT,
    pago_procesado INTEGER DEFAULT 0,
    observaciones TEXT DEFAULT '',
    FOREIGN KEY (nro_doc_cliente) REFERENCES cliente(nro_doc)
);

CREATE TABLE IF NOT EXISTS detalle_pedido (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nro_pedido INTEGER NOT NULL,
    cod_producto TEXT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario REAL NOT NULL,
    FOREIGN KEY (nro_pedido) REFERENCES pedido(nro_pedido),
    FOREIGN KEY (cod_producto) REFERENCES producto(codigo)
);

INSERT OR IGNORE INTO zona (id_zona, nombre) VALUES ('Z1', 'Zona Norte');
INSERT OR IGNORE INTO zona (id_zona, nombre) VALUES ('Z2', 'Zona Centro');
INSERT OR IGNORE INTO zona (id_zona, nombre) VALUES ('Z3', 'Zona Sur');

INSERT OR IGNORE INTO barrio (id_barrio, nombre, id_zona) VALUES ('B1', 'Alberdi', 'Z2');
INSERT OR IGNORE INTO barrio (id_barrio, nombre, id_zona) VALUES ('B2', 'Centro', 'Z2');
INSERT OR IGNORE INTO barrio (id_barrio, nombre, id_zona) VALUES ('B3', 'Alta Cordoba', 'Z1');
INSERT OR IGNORE INTO barrio (id_barrio, nombre, id_zona) VALUES ('B4', 'Nueva Cordoba', 'Z3');

INSERT OR IGNORE INTO envase (tipo_envase, capacidad) VALUES ('Bidon', 6);
INSERT OR IGNORE INTO envase (tipo_envase, capacidad) VALUES ('Bidon', 10);
INSERT OR IGNORE INTO envase (tipo_envase, capacidad) VALUES ('Bidon', 12);
INSERT OR IGNORE INTO envase (tipo_envase, capacidad) VALUES ('Bidon', 20);
INSERT OR IGNORE INTO envase (tipo_envase, capacidad) VALUES ('Sifon', 2);

INSERT OR IGNORE INTO producto (codigo, nombre, tipo_envase, capacidad) VALUES ('P1', 'Bidon de agua 6L', 'Bidon', 6);
INSERT OR IGNORE INTO producto (codigo, nombre, tipo_envase, capacidad) VALUES ('P2', 'Bidon de agua 10L', 'Bidon', 10);
INSERT OR IGNORE INTO producto (codigo, nombre, tipo_envase, capacidad) VALUES ('P3', 'Bidon de agua 12L', 'Bidon', 12);
INSERT OR IGNORE INTO producto (codigo, nombre, tipo_envase, capacidad) VALUES ('P4', 'Bidon de agua 20L', 'Bidon', 20);
INSERT OR IGNORE INTO producto (codigo, nombre, tipo_envase, capacidad) VALUES ('P5', 'Sifon de soda', 'Sifon', 2);

INSERT OR IGNORE INTO precio (cod_producto, valor, fecha_desde) VALUES ('P1', 2500, date('now', '-1 year'));
INSERT OR IGNORE INTO precio (cod_producto, valor, fecha_desde) VALUES ('P2', 3500, date('now', '-1 year'));
INSERT OR IGNORE INTO precio (cod_producto, valor, fecha_desde) VALUES ('P3', 4200, date('now', '-1 year'));
INSERT OR IGNORE INTO precio (cod_producto, valor, fecha_desde) VALUES ('P4', 5500, date('now', '-1 year'));
INSERT OR IGNORE INTO precio (cod_producto, valor, fecha_desde) VALUES ('P5', 1800, date('now', '-1 year'));

INSERT OR IGNORE INTO cliente (nro_doc, nombre, apellido, razon_social, direccion, telefono, id_barrio)
VALUES ('123', 'Juan', 'Perez', 'Juan Perez SRL', 'Av. Colon 1200', '351123456', 'B1');
INSERT OR IGNORE INTO cliente (nro_doc, nombre, apellido, razon_social, direccion, telefono, id_barrio)
VALUES ('456', 'Maria', 'Gomez', '-', 'Belgrano 450', '351987654', 'B2');
INSERT OR IGNORE INTO cliente (nro_doc, nombre, apellido, razon_social, direccion, telefono, id_barrio)
VALUES ('789', 'Lucia', 'Diaz', 'Comercio Diaz', 'Sucre 80', '351555222', 'B3');

INSERT OR IGNORE INTO empleado (nro_doc, nombre, apellido, tipo_doc, cargo, capacidad_maxima)
VALUES ('201', 'Omar', 'Ruiz', 'DNI', 'OPERADORA', 0);
INSERT OR IGNORE INTO empleado (nro_doc, nombre, apellido, tipo_doc, cargo, capacidad_maxima)
VALUES ('202', 'Ana', 'Lopez', 'DNI', 'ENCARGADO_ADMINISTRATIVO', 0);
INSERT OR IGNORE INTO empleado (nro_doc, nombre, apellido, tipo_doc, cargo, capacidad_maxima)
VALUES ('203', 'Diego', 'Sosa', 'DNI', 'DISTRIBUIDOR', 20);
INSERT OR IGNORE INTO empleado (nro_doc, nombre, apellido, tipo_doc, cargo, capacidad_maxima)
VALUES ('204', 'Clara', 'Vega', 'DNI', 'PRESIDENTE', 0);

INSERT OR IGNORE INTO pedido (nro_pedido, fecha_emision, fecha_entrega, estado, total, nro_doc_cliente, metodo_pago, pago_procesado, observaciones)
VALUES (1001, date('now', 'localtime'), date('now', 'localtime'), 'PENDIENTE', 10000, '123', NULL, 0, '');
INSERT OR IGNORE INTO pedido (nro_pedido, fecha_emision, fecha_entrega, estado, total, nro_doc_cliente, metodo_pago, pago_procesado, observaciones)
VALUES (1002, date('now', 'localtime'), date('now', 'localtime'), 'PENDIENTE', 3800, '456', NULL, 0, '');
INSERT OR IGNORE INTO pedido (nro_pedido, fecha_emision, fecha_entrega, estado, total, nro_doc_cliente, metodo_pago, pago_procesado, observaciones)
VALUES (1003, date('now', 'localtime'), date('now', 'localtime'), 'ENTREGADO', 5400, '123', 'Efectivo $10000', 1, '');
INSERT OR IGNORE INTO pedido (nro_pedido, fecha_emision, fecha_entrega, estado, total, nro_doc_cliente, metodo_pago, pago_procesado, observaciones)
VALUES (1004, date('now', 'localtime'), date('now', 'localtime'), 'ENTREGADO', 5000, '789', 'Mercado Pago', 1, '');
INSERT OR IGNORE INTO pedido (nro_pedido, fecha_emision, fecha_entrega, estado, total, nro_doc_cliente, metodo_pago, pago_procesado, observaciones)
VALUES (1005, date('now', 'localtime'), date('now', 'localtime'), 'NO_ENTREGADO', 7600, '456', NULL, 0, 'Cliente ausente.');
INSERT OR IGNORE INTO pedido (nro_pedido, fecha_emision, fecha_entrega, estado, total, nro_doc_cliente, metodo_pago, pago_procesado, observaciones)
VALUES (1006, date('now', 'localtime', '-1 day'), date('now', 'localtime', '-1 day'), 'ENTREGADO', 10000, '789', 'Efectivo $10000', 1, '');

INSERT OR IGNORE INTO detalle_pedido (nro_pedido, cod_producto, cantidad, precio_unitario) VALUES (1001, 'P1', 2, 5000);
INSERT OR IGNORE INTO detalle_pedido (nro_pedido, cod_producto, cantidad, precio_unitario) VALUES (1002, 'P2', 1, 3800);
INSERT OR IGNORE INTO detalle_pedido (nro_pedido, cod_producto, cantidad, precio_unitario) VALUES (1003, 'P3', 3, 1800);
INSERT OR IGNORE INTO detalle_pedido (nro_pedido, cod_producto, cantidad, precio_unitario) VALUES (1004, 'P1', 1, 5000);
INSERT OR IGNORE INTO detalle_pedido (nro_pedido, cod_producto, cantidad, precio_unitario) VALUES (1005, 'P2', 2, 3800);
INSERT OR IGNORE INTO detalle_pedido (nro_pedido, cod_producto, cantidad, precio_unitario) VALUES (1006, 'P1', 2, 5000);
