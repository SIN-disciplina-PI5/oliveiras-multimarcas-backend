# 🚗 Oliveira's Multimarcas - Backend

![Status do Projeto](https://img.shields.io/badge/status-em_desenvolvimento-yellow)

Repositório dedicado ao desenvolvimento do **backend** do sistema da loja "Oliveira's Multimarcas".

## 📝 Sobre o Projeto

Este projeto é a contraparte de *backend* (servidor) para o sistema completo da concessionária Oliveira's Multimarcas. Ele será responsável por fornecer uma API segura e eficiente para as aplicações do cliente (site) e do painel administrativo.

Este repositório faz parte da entrega do **Projeto Integrador 5** [Nome da Instituição/Curso].

### Repositórios Relacionados
* **Frontend (Cliente):** `[Link para o repositório frontend, se houver]`
* **Frontend (Admin):** `[Link para o repositório admin, se houver]`

---

## ✨ Funcionalidades Planejadas

O backend dará suporte às seguintes funcionalidades:

* **Autenticação & Autorização:**
    * [ ] Login/Registro de Clientes.
    * [ ] Login de Administradores (com permissões distintas).
    * [ ] Rotas protegidas com JWT (JSON Web Token).
* **Gerenciamento de Veículos (CRUD):**
    * [ ] Cadastro, leitura, atualização e exclusão de carros.
    * [ ] Upload de múltiplas imagens por veículo.
    * [ ] Filtros avançados (marca, modelo, ano, preço, etc.).
* **Painel Administrativo:**
    * [ ] Gerenciamento de todo o estoque de veículos.
    * [ ] Visualização de propostas de clientes.
    * [ ] Gerenciamento de usuários.
* **Interação do Cliente:**
    * [ ] Envio de propostas/interesse em um veículo.
    * [ ] Visualização do catálogo de veículos.

---

## 🛠️ Tecnologias Utilizadas

Este projeto será construído utilizando as seguintes tecnologias:

* **Linguagem:** `[Ex: Node.js (TypeScript/JavaScript)]`
* **Framework:** `[Ex: Express.js, Nest.js, Fastify]`
* **Banco de Dados:** `[Ex: PostgreSQL, MySQL, MongoDB]`
* **ORM / Query Builder:** `[Ex: Prisma, TypeORM, Knex.js]`
* **Autenticação:** `[Ex: Passport.js, JWT]`
* **Testes:** `[Ex: Jest, Vitest]`
* **Containerização:** `[Ex: Docker]`

---

## 🚀 Como Executar o Projeto

Para rodar este projeto localmente, siga os passos abaixo:

### Pré-requisitos

* `[Node.js v18+]`
* `[npm ou yarn]`
* `[Docker (para o banco de dados)]`

### Instalação

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/oliveiras-multimarcas-backend.git](https://github.com/seu-usuario/oliveiras-multimarcas-backend.git)
    cd oliveiras-multimarcas-backend
    ```

2.  **Instale as dependências:**
    ```bash
    npm install
    # ou
    yarn install
    ```

3.  **Configure as Variáveis de Ambiente:**
    * Renomeie o arquivo `.env.example` para `.env`.
    * Preencha as variáveis necessárias (como credenciais do banco de dados, segredo do JWT, etc.).
    ```bash
    # Exemplo de .env
    DATABASE_URL="postgresql://user:password@localhost:5432/db_name"
    JWT_SECRET="seu-segredo-super-secreto"
    ```

4.  **Execute as Migrações do Banco:** (Se aplicável)
    ```bash
    npm run migrate
    # ou comando específico do seu ORM
    ```

5.  **Inicie o servidor de desenvolvimento:**
    ```bash
    npm run dev
    ```

O servidor estará rodando em `http://localhost:3333` (ou a porta definida no seu `.env`).

---

## 📚 Documentação da API

A documentação dos *endpoints* da API será mantida utilizando `[Swagger / Postman]` e estará disponível em:

`[Link da documentação ou "Em construção"]`

---

## 👥 Equipe e Colaboradores

Este projeto está sendo desenvolvido por:

* **[Seu Nome]** - `[Sua Função, ex: Dev. Backend]` - [@seu-github](https://github.com/seu-github)
* **[Nome Colega 1]** - `[Função, ex: Dev. Frontend]` - [@github-colega1](https://github.com/github-colega1)
* **[Nome Colega 2]** - `[Função, ex: QA/Tester]` - [@github-colega2](https://github.com/github-colega2)
