CREATE DATABASE db_sistema_musicas;

USE db_sistema_musicas;

CREATE TABLE tb_musicas(
	id INT PRIMARY KEY AUTO_INCREMENT,
	usuario VARCHAR(50),
    genero1 VARCHAR(20),
    genero2 VARCHAR(20),
    musica VARCHAR(50),
    nota DOUBLE
);

CREATE TABLE tb_genero(
	id INT PRIMARY KEY AUTO_INCREMENT,
	usuario VARCHAR(50),
    genero VARCHAR(20)
);

CREATE TABLE tb_genero_preferido(
	id INT PRIMARY KEY AUTO_INCREMENT,
	usuario VARCHAR(50),
    genero VARCHAR(20)
);

CREATE TABLE tb_login(
	id INT PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(50),
    senha VARCHAR(20)
);

Insert into tb_login(usuario, senha) values("admin", "admin111");

