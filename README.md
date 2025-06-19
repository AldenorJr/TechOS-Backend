# 🛠️ Techos - Backend do Sistema de Ordem de Serviço

**Techos** é um sistema de gerenciamento de ordens de serviço voltado para controle de técnicos, clientes, contatos, orçamentos e serviços, com painel de dashboard analítico. Este repositório contém a API RESTful desenvolvida em Java utilizando o ecossistema **Spring**.

---

## 📦 Tecnologias Utilizadas

- ☕ **Java 17**
- 🌱 **Spring Boot 3**
- 🔐 **Spring Security (JWT Authentication)**
- 🧪 **JUnit 5 + Mockito**
- 🛢️ **H2 Database (ambiente de desenvolvimento/teste)**
- 📊 **Spring Data JPA**
- 📈 **Dashboard Analytics (com métricas agregadas)**

---

## 📽️ Demonstração do Projeto

https://www.youtube.com/embed/8ELGGUfj6v4

🔗 Clique na imagem acima para assistir à apresentação completa do sistema Techos no YouTube.

---

## 📁 Estrutura das Entidades Principais

- 👨‍🔧 `Tecnico` — Dados dos técnicos responsáveis pelas ordens
- 👤 `Cliente` — Informações dos clientes
- ☎️ `Contato` — Contatos associados a clientes
- 🧾 `OrdemDeServico` — Ordem de serviço com status, descrição, técnico, cliente
- 💰 `Orcamento` — Propostas de valores ligadas às ordens

---

## 🔐 Segurança

A autenticação é baseada em tokens **JWT**, com endpoints protegidos via `Spring Security`. Os usuários autenticados têm acesso controlado com base em **perfis de acesso (roles)**.

- `/auth/login` — Geração de token

---

## 📊 Dashboard Analítico

Relatórios e indicadores são expostos em endpoints específicos:

- Quantidade de ordens por status
- Número de ordens por técnico
- Total de orçamentos aprovados
- Receita estimada
- Atividades recentes

> 🔎 *Possui suporte para filtros por data, cliente e técnico.*

---

## 🚀 Executando o Projeto

### Pré-requisitos

- Java 17+
- Maven 3.8+

### Passos

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/techos-backend.git
cd techos-backend

# Rodar a aplicação
./mvnw spring-boot:run
