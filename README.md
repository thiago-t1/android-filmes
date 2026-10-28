<div align="center">

# 🎬 Gerenciador de Filmes e Séries

### Aplicativo Android em Java com persistência em tempo real no ☁️ Cloud Firestore

**AC2 — Fabrício Tonetto Londero**

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/Language-Java-f89820?logo=openjdk&logoColor=white)
![Firebase](https://img.shields.io/badge/Database-Cloud%20Firestore-FFCA28?logo=firebase&logoColor=black)
![Build](https://img.shields.io/badge/Build-Gradle%20KTS-02303A?logo=gradle&logoColor=white)
![Status](https://img.shields.io/badge/Status-Concluído-success)

</div>

---

## 📖 Sobre o projeto

Aplicativo mobile para **cadastrar, listar, editar, excluir e filtrar** filmes e séries assistidos (ou que o usuário ainda deseja assistir). Todos os dados são persistidos em **tempo real** no Cloud Firestore — nada fica apenas em memória: ao fechar e reabrir o app, os registros continuam lá. 🔥

## ✨ Funcionalidades

| Recurso | Descrição |
|---|---|
| ➕ **Cadastro** | Nome, Tipo (Spinner), Gênero (Spinner), Ano de lançamento, Nota pessoal e status "Já assistiu?" |
| 📋 **Listagem em tempo real** | `addSnapshotListener` atualiza a lista automaticamente a cada mudança no Firestore |
| ✏️ **Edição** | Clique curto no item carrega os dados no formulário |
| 🗑️ **Exclusão** | Clique longo no item + diálogo de confirmação antes de excluir |
| 🔎 **Busca por nome** | Filtro dinâmico enquanto o usuário digita |
| 🧭 **Filtro por tipo** | Spinner dedicado (Todos / Filme / Série / Documentário / Animação) |
| ✅ **Validações** | Nome, tipo, gênero, ano e nota são obrigatórios; usuário é avisado com mensagens claras |

## 🗂️ Estrutura do projeto

```
ac2-filmes-mobile/
├── app/
│   ├── build.gradle.kts                  # Dependências (Firebase BoM, Firestore, RecyclerView...)
│   ├── google-services.json.example      # Modelo — copie e renomeie para google-services.json
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/gerenciadorfilmes/
│       │   ├── Filme.java                # 📦 Modelo de dados
│       │   ├── FilmeAdapter.java         # 🧩 Adapter do RecyclerView (clique curto/longo)
│       │   └── MainActivity.java         # 🧠 CRUD completo no Firestore + validações + filtros
│       └── res/
│           ├── layout/
│           │   ├── activity_main.xml     # Formulário, busca, filtro e lista
│           │   └── item_filme.xml        # CardView de cada item da lista
│           └── values/arrays.xml         # Opções dos Spinners (tipo e gênero)
├── build.gradle.kts                      # Configuração raiz + plugin do Firebase
├── settings.gradle.kts
└── README.md
```

## 🧱 Modelo de dados (`Filme`)

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `String` | ID do documento no Firestore |
| `nome` | `String` | Nome do filme/série |
| `tipo` | `String` | Filme, Série, Documentário ou Animação |
| `genero` | `String` | Ação, Comédia, Drama, Terror, Ficção Científica... |
| `anoLancamento` | `int` | Ano de lançamento |
| `notaPessoal` | `float` | Nota dada pelo usuário (0 a 10) |
| `assistido` | `boolean` | Se já foi assistido |

## 🔥 Como o Firebase é usado

- `filmesRef.addSnapshotListener(...)` → escuta a coleção `filmes` em tempo real e atualiza a UI automaticamente.
- `filmesRef.add(filme)` → cria um novo registro.
- `filmesRef.document(id).set(filme)` → atualiza um registro existente.
- `filmesRef.document(id).delete()` → remove um registro (com confirmação via `AlertDialog`).

## 🚀 Como rodar

1. **Clone o repositório**
   ```bash
   git clone https://github.com/thiago-t1/android-filmes.git
   ```

2. **Crie um projeto no [Firebase Console](https://console.firebase.google.com/)** e registre um app Android com o package `com.example.gerenciadorfilmes`.

3. **Baixe o `google-services.json`** gerado pelo Firebase e coloque em:
   ```
   app/google-services.json
   ```
   > 💡 Use o `app/google-services.json.example` como referência do formato esperado. Esse arquivo é ignorado pelo Git (`.gitignore`) por boas práticas.

4. **Ative o Cloud Firestore** no console (modo de teste é suficiente para desenvolvimento/apresentação).

5. **Abra no Android Studio**, aguarde o Gradle sincronizar e rode em um emulador ou dispositivo físico. ▶️

## 🎯 Requisitos atendidos (extras escolhidos)

- ✅ Busca por nome
- ✅ Diálogo de confirmação antes de excluir

## 🛠️ Tecnologias

- ☕ Java
- 🤖 Android SDK (RecyclerView, CardView, Spinner, AlertDialog)
- 🔥 Firebase Cloud Firestore
- ⚙️ Gradle (Kotlin DSL)

---

<div align="center">

Feito com 🍿 para a disciplina de Desenvolvimento Mobile

</div>
