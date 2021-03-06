DROP DATABASE IF EXISTS TICKETS_FINAL;
CREATE DATABASE TICKETS_FINAL;
USE TICKETS_FINAL;

CREATE TABLE USERS(
	ID VARCHAR(5) PRIMARY KEY,
    USER_NAME VARCHAR(20) NOT NULL,
    LASTNAME VARCHAR(50) NULL,
    USER_PASSWORD VARCHAR(20) NOT NULL,
    EMAIL VARCHAR(50) NOT NULL,
    USER_STATUS VARCHAR(10) NOT NULL 
);

CREATE TABLE INCIDENTS(
	ID VARCHAR(5) PRIMARY KEY,
    PRIORITY INTEGER NOT NULL,
    INC_DESCRIPTION VARCHAR(200) NOT NULL,
    INC_SOLUTION VARCHAR(200) NULL,
    ID_USER VARCHAR(5) NOT NULL,
    ID_TECHNICAL VARCHAR(5) NULL,
    SOLVED BOOLEAN DEFAULT FALSE,
    OPEN_DATE DATE NOT NULL,
    CLOSE_DATE DATE NULL,
    FOREIGN KEY (ID_USER) REFERENCES USERS(ID),
    FOREIGN KEY (ID_TECHNICAL) REFERENCES USERS(ID)
);

INSERT INTO USERS VALUES ('00001','admin',Null,'adm1n','admin@f3m.com','admin');
INSERT INTO USERS VALUES ('00002','pepe','lopez','1234','pepe@f3m.com','user');
INSERT INTO USERS VALUES ('00003','paco','perez','1234','paco@f3m.com','technical');

INSERT INTO INCIDENTS(ID, PRIORITY, INC_DESCRIPTION, ID_USER, ID_TECHNICAL, OPEN_DATE) VALUES ('01010',7,'No funciona el dhcp','00002','00003', 20220102);
INSERT INTO INCIDENTS VALUES ('05678',9,'No funciona la red','Router configurado de nuevo','00002','00003',true,20220301,20220304);
INSERT INTO INCIDENTS(ID, PRIORITY, INC_DESCRIPTION, ID_USER, OPEN_DATE) VALUES ('03450',3,'No funciona la maquina de café','00002', 20220117);
INSERT INTO INCIDENTS(ID, PRIORITY, INC_DESCRIPTION, ID_USER, ID_TECHNICAL, OPEN_DATE) VALUES ('01230',5,'No funciona la impresora','00002','00003', 20220130);
INSERT INTO INCIDENTS(ID, PRIORITY, INC_DESCRIPTION, ID_USER, OPEN_DATE) VALUES ('01450',2,'No funciona el ascensor','00002', 20220218);
INSERT INTO INCIDENTS VALUES ('45230',10,'Se ha caido el servidor','Servidor reestablecido','00002','00003',true,20211209,20211212);*/

