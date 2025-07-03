# 🎓 Siga-Me — Sistema de Gerenciamento Acadêmico

Este projeto foi desenvolvido como parte da disciplina de **Programação Orientada a Objetos (POO)** no curso de **Análise e Desenvolvimento de Sistemas (ADS)** da **FATEC**.

O **Siga-Me** é um sistema desktop simples, feito em **JavaFX**, voltado para o gerenciamento de alunos e suas matrículas. Foi desenvolvido com o objetivo de praticar os fundamentos da **orientação a objetos**, o uso de **DAO + Model**, **JavaFX com Scene Builder**, e a conexão com banco de dados **MariaSQL** via JDBC.

---

## 📚 Descrição Geral

O sistema oferece uma interface gráfica que permite:

- Cadastro de alunos
- Matrícula de alunos
- Edição e exclusão de cadastros (CRUD)
- Edição e exclusão de matrículas (em desenvolvimento)
- Uso de **ComboBox** para seleção dinâmica de cursos, matérias e semestres
- Utilização de **collections** para tratamento dos dados

---

## 🛠️ Tecnologias Utilizadas

- **Java 23.0.1**
- **JavaFX 24.0.1**
- **Apache NetBeans 23**
- **Scene Builder 2.0**
- **MariaDB 10 (via XAMPP 8.2.12)**

---

## ⚙️ Como Executar o Projeto
- Instale o XAMPP e ative os serviços MySQL e Apache
- Importe o banco de dados (presente em database/siga_me.sql) para o phpMyAdmin
- Abra o projeto MA4_SigaMe no Apache NetBeans 23
- Verifique se o JavaFX está corretamente configurado (versão 24.0.1)
- Execute a aplicação a partir do arquivo App.java
- A interface gráfica será carregada com o menu principal

---

## 📁 Funcionalidades do Sistema:
*Menu Principal:*
- ✅ Cadastrar Aluno — Criação de novos registros de alunos
- ✅ Matricular Aluno — Associação de alunos a cursos, semestres e matérias
- ✅ Gerenciar Cadastro — Permite editar e excluir registros de alunos
- ⚠️ Gerenciar Matrículas — Funcionalidade incompleta / em desenvolvimento

### O sistema usa ComboBox para carregar dinamicamente os cursos, semestres e matérias cadastrados no banco de dados.

---

## 💡 Observações
- O banco de dados está localizado na pasta src/main/java/ por exigência do professor
- A conexão com o banco é feita via JDBC puro, sem frameworks externos
- Todo o projeto foi estruturado em camadas, com uso de DAO, Model, Controller e View
- O sistema é totalmente JavaFX desktop; o Apache é usado apenas para suporte ao MySQL via XAMPP/phpMyAdmin

---

## 🧠 Aprendizados
- Estruturação de sistemas com orientação a objetos
- Criação de interfaces gráficas com JavaFX + Scene Builder
- Manipulação de dados com Collections e componentes como ComboBox
- Criação de uma camada de acesso a dados com o padrão DAO
- Integração de sistemas Java com MariaDB via JDBC

---

## 👨‍👨‍👦 Integrantes
- Daniel Alexandre
- Fabio Nifosse
- Vicente O.

---

## 📄 Licença
Distribuído sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

---

## 🗂️ Estrutura do Projeto

```bash
📦 Siga_Me
├── MA4_SigaMe/
│   └── src/
│       └── main/
│           ├── java/
│           │   ├── br/com/fatec/
│           │   │   ├── conexao/        # Classe para a conexão com o banco de dados
│           │   │   ├── controller/     # Controladores JavaFX
│           │   │   ├── dao/        # DAOs de acesso ao banco
│           │   │   ├── exceptions/     # Exceções não utilizadas
│           │   │   ├── model       # Classes modelo (Aluno, Curso, etc.)
│           │   │   └── App.java        # Classe principal para execução
│           │   └── siga_me.sql     # Script do banco (.sql) e arquivos auxiliares    
│           └── resources/br/com/fatec/
│               ├── CSS/     # Arquivos CSS
│               ├── fonts/      # Fontes de texto não utilizadas
│               └── view/       # Telas do programa em FXML
├── N2-POO.docx             # Instruções do professor
├── README.md
├── .gitignore
└── LICENSE
