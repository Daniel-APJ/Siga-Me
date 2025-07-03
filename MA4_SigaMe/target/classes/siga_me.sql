-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 12/06/2025 às 15:51
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `siga_me`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `alunos`
--

CREATE DATABASE `siga_me`;
USE `siga_me`;

CREATE TABLE `alunos` (
  `ra` int(11) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `cpf` varchar(20) NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `nascimento` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `alunos`
--

INSERT INTO `alunos` (`ra`, `nome`, `cpf`, `telefone`, `email`, `nascimento`) VALUES
(1001, 'Daniel Alexandre', '111.222.333-4', '11 98765-4321', 'dapj@fatec.com', '2005-02-22'),
(1002, 'Abeia Meloso', '333.666.999-01', '11 4002-8922', 'honey@gmail.com', '2015-02-28');

-- --------------------------------------------------------

--
-- Estrutura para tabela `cursos`
--

CREATE TABLE `cursos` (
  `id_curso` varchar(5) NOT NULL,
  `nome_curso` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `cursos`
--

INSERT INTO `cursos` (`id_curso`, `nome_curso`) VALUES
('ADS', 'Análise e Desenvolvimento de Sistemas'),
('BDN', 'Big Data para Negócios'),
('GCom', 'Gestão Comercial'),
('GRH', 'Gestão de Recursos Humanos'),
('JOGD', 'Jogos Digitais'),
('SEGI', 'Segurança da Informação');

-- --------------------------------------------------------

--
-- Estrutura para tabela `materias`
--

CREATE TABLE `materias` (
  `id_materia` varchar(10) NOT NULL,
  `nome_materia` varchar(100) NOT NULL,
  `curso` varchar(5) DEFAULT NULL,
  `semestre` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `materias`
--

INSERT INTO `materias` (`id_materia`, `nome_materia`, `curso`, `semestre`) VALUES
('IBD002', 'Banco de Dados', 'ADS', 4),
('IIA006', 'Inteligência Artificial', 'ADS', 6),
('IJD019', 'Jogos Digitais para Web', 'JOGD', 5),
('ILP204', 'Ferramentas para Desenvolvimento Web', 'JOGD', 5),
('ISI002', 'Sistemas da Informação', 'ADS', 2),
('ISO001', 'Sistema Operacional', 'SEGI', 2),
('ITI200', 'Fundamentos da Tecnologia da Informação', 'JOGD', 1),
('LIN400', 'Língua Inglesa Nível 4', 'ADS', 4);

-- --------------------------------------------------------

--
-- Estrutura para tabela `matriculas`
--

CREATE TABLE `matriculas` (
  `id_matr` int(11) NOT NULL,
  `ra` int(11) DEFAULT NULL,
  `curso` varchar(5) DEFAULT NULL,
  `semestre` int(11) NOT NULL,
  `data_matricula` date NOT NULL,
  `status` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `matriculas`
--

INSERT INTO `matriculas` (`id_matr`, `ra`, `curso`, `semestre`, `data_matricula`, `status`) VALUES
(1, 1001, 'ADS', 4, '2023-08-01', 1),
(2, 1002, 'BDN', 2, '2024-02-29', 0);

-- --------------------------------------------------------

--
-- Estrutura para tabela `matricula_has_materias`
--

CREATE TABLE `matricula_has_materias` (
  `id_matricula_fk` int(11) DEFAULT NULL,
  `id_materia_fk` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `alunos`
--
ALTER TABLE `alunos`
  ADD PRIMARY KEY (`ra`),
  ADD UNIQUE KEY `cpf` (`cpf`);

--
-- Índices de tabela `cursos`
--
ALTER TABLE `cursos`
  ADD PRIMARY KEY (`id_curso`),
  ADD UNIQUE KEY `nome_curso` (`nome_curso`);

--
-- Índices de tabela `materias`
--
ALTER TABLE `materias`
  ADD PRIMARY KEY (`id_materia`),
  ADD KEY `curso` (`curso`);

--
-- Índices de tabela `matriculas`
--
ALTER TABLE `matriculas`
  ADD PRIMARY KEY (`id_matr`),
  ADD KEY `ra` (`ra`),
  ADD KEY `fk_curso` (`curso`);

--
-- Índices de tabela `matricula_has_materias`
--
ALTER TABLE `matricula_has_materias`
  ADD KEY `Matricula_idfk_2` (`id_matricula_fk`),
  ADD KEY `Materia_idfk_2` (`id_materia_fk`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `alunos`
--
ALTER TABLE `alunos`
  MODIFY `ra` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1004;

--
-- AUTO_INCREMENT de tabela `matriculas`
--
ALTER TABLE `matriculas`
  MODIFY `id_matr` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `materias`
--
ALTER TABLE `materias`
  ADD CONSTRAINT `Materias_idfk_1` FOREIGN KEY (`curso`) REFERENCES `cursos` (`id_curso`);

--
-- Restrições para tabelas `matriculas`
--
ALTER TABLE `matriculas`
  ADD CONSTRAINT `Matriculas_idfk_1` FOREIGN KEY (`ra`) REFERENCES `alunos` (`ra`),
  ADD CONSTRAINT `fk_curso` FOREIGN KEY (`curso`) REFERENCES `cursos` (`id_curso`);

--
-- Restrições para tabelas `matricula_has_materias`
--
ALTER TABLE `matricula_has_materias`
  ADD CONSTRAINT `Materia_idfk_2` FOREIGN KEY (`id_materia_fk`) REFERENCES `materias` (`id_materia`),
  ADD CONSTRAINT `Matricula_idfk_2` FOREIGN KEY (`id_matricula_fk`) REFERENCES `matriculas` (`id_matr`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
