# Checkpoint 4 — Bug Hunt StreamFIAP

> **Professor:** Ygor Moraes Martins dos Anjos
---

## Identificação

**Grupo:** debugthebug

| Integrante | RM | Turma |
|---|---|---|
|Manoela Oliveira Bello | RM563952 | 2CCPW |
|Roberto Marques Moreira | RM564935 | 2CCPW |
|Guilherme Ferraz de Medeiros | RM564743 | 2CCPW |
|Felipe Rodrigues Ribeiro | RM565274 | 2CCPW |


| Campo | |
|---|---|
| **Total de bugs corrigidos** | 12 / 12 |
| **Total de ajustes de Clean Code** | 6 / 6 |

---

## Parte 1 — Bugs encontrados

> Uma linha por bug, na ordem em que você os encontrou. Use a numeração dos seus
> commits (`fix: bug01 ...`). Preencha TODAS as colunas — metade da nota está aqui.

| # | Sintoma observado (o que fiz/vi) | Causa raiz (arquivo e linha aproximada) | Correção aplicada | Conceito da disciplina |
|---|---|---|---|---|
| bug01 | Cadastro de usuário não gerava ID automaticamente no banco. | Usuario.java: Na linha ~12 faltava configurar o autoincremento no atributo id. | Adicionada a anotação @GeneratedValue(strategy = GenerationType.IDENTITY). | Spring Data JPA / Mapeamento Banco-Objeto |
| bug02 | O nome do usuário ficava null no banco de dados. | Usuario.java: O construtor estava salvando a variável nela mesma (nome = nome) na linha ~21 em vez de no atributo da classe. | Colocado o ponteiro this para referenciar o atributo: this.nome = nome;. | Escopo e uso do this |
| bug03 | Buscar um ID que não existe retornava tela em branco (HTTP 200) em vez de erro 404. | ConteudoController.java: Tinha um bloco try-catch vazio engolindo o erro nas linhas ~30-36. | Apaguei o try-catch para a exceção subir corretamente para o GlobalExceptionHandler. | Tratamento de Exceções RESTe como vimos em aula nunca usar CATCH vazio |
| bug04 | A busca por categoria (ex: FICCAO) não trazia nada. | ConteudoController.java: Estava comparando String com == em vez de usar .equals(), além de fazer o filtro na mão na linha ~43. | Substituído pelo método automático findByCategoria do Spring Data. | Comparação de Strings / Spring Data |
| bug05 | O preço promocional estava cobrando 20% a mais, e não a menos. | Filme.java: A matemática estava multiplicando o preço por 1.2. na linha ~27 | Alterado para preco * 0.8 (aplicando o desconto de 20%). | Raciocínio Lógico |
| bug06 | Séries eram salvas sem título e duração no banco. | Serie.java: O construtor não passava os parâmetros obrigatórios para a classe mãe (Conteudo). Nas linhas ~15-18 o construtor recebia dados da superclasse mas não invocava super | Inclusão de super(titulo, categoria, duracaoMinutos, classificacaoEtaria, true), repassando todos os dados básicos. | Herança e Construtores |
| bug07 | Aluguel de série cobrava o valor fixo de R$ 9,90 igual ao filme. | Serie.java: O método de preço estava com um parâmetro extra (sobrecarga), então o Java ignorava ele e usava o da classe mãe. Na linha ~21 o método estava declarado como "calcularPrecoAluguel(double desconto)", gerando overload  | Removido o parâmetro e adicionada a anotação @Override. | Polimorfismo / Sobrescrita |
| bug08 | Documentário estava cobrando R$ 9,90 em vez de ser gratuito. | Documentario.java: Faltou sobrescrever a regra de preço herdada na linha ~10. | Criado o método com @Override retornando 0.0. | POO (Polimorfismo / Especialização) |
| bug09 | Permitia alugar sem saldo e barrava quem tinha limite, ou seja o usuário com saldo 0 conseguia alugar, e usuário com R$ 100 tinha aluguel recusado  | Usuario.java: A lógica do if estava invertida (preco >= creditos) na linha ~26 | Corrigida a lógica para this.creditos >= preco | Lógica Condicional / Operadores |
| bug10 | Sistema permitia alugar um filme que já constava como indisponível. | Usuario.java: O método cobrava o usuário antes de checar se o título estava livre | Inserida a validação if (!conteudo.isDisponivel()) lançando exceção | Regras de Negócio (Fail-Fast) |
| bug11 | Usuário menor de idade tomava erro HTTP 500 genérico no servidor. | GlobalExceptionHandler.java: O sistema não sabia o que fazer com a ClassificacaoIndicativaException | Criado o @ExceptionHandler retornando o status HTTP 403 (Forbidden) | Spring Boot / ControllerAdvice |
| bug12 | Dava para cadastrar filmes com duração negativa (-10 min). | Conteudo.java: Faltou checar os dados recebidos antes de instanciar o objeto entre as linhas linhas ~24-30 | Inserido um if no construtor barrando valor zero ou negativo | Validação de Dados / Defesa de Domínio |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 | ConteudoController.java (buscarPorId) | Retorno muito verboso e redundante. | Simplifiquei o código para retornar direto o ResponseEntity.ok(conteudo). |
| clean02 | ConteudoController.java (listarPorCategoria) | Má performance: o código puxava tudo do banco (findAll) para filtrar com um for na memória do Java. | Apaguei o loop e passei a usar a query direta do repositório (findByCategoria). |
| clean03 | Conteudo.java e ConteudoController.java | Quebra do Encapsulamento: a variável duracaoMinutos estava pública e solta. | Mudei para private e passei a acessar via .getDuracaoMinutos() no controller. |
| clean04 | Usuario.java (alugar) | No código tinha um bloco enorme de "System.out.println" simulando um recibo que uma API REST e não mostrava para o front-end. | Apagamos todos os prints, deixando só o que importa para a lógica de negócio. Melhoramos também o nome das variáveis c e p |
| clean05 | ConteudoController.java (final do arquivo) | Tinham muitas funções obsoletas com blocos inteiros de lógica comentados . | Apagamos tudo. O histórico do que foi feito fica no Git, não em código comentado |
| clean06 | Usuario.java (debitarCreditos) | Comentário não verdadeiro sobre adicionar saldo em uma linha que estava fazendo subtração. | Deletamos o comentário que só servia para atrapalhar a leitura. |

---

## Parte 3 — Perguntas de reflexão

> Responda com suas palavras, 5 a 10 linhas cada, **usando o código real do projeto
> como exemplo**. Respostas genéricas de tutorial não pontuam.

### 1. Injeção de dependência (Aula 13)
Os controllers recebem os repositories via `@Autowired` (ex.: `ConteudoController`
usa `ConteudoRepository`). Explique por que o Spring precisa gerenciar esses objetos
em vez de criarmos com `new ConteudoRepository()`. O que exatamente o Spring faz ao
injetar um bean, e por que isso não funcionaria com um `new` comum?

### 2. JDBC vs Spring Data JPA (Aulas 12 e 13)
Na Aula 12 escrevemos um `ProdutoDAO` na mão com `Connection`, `PreparedStatement` e
`ResultSet`. Aqui o `ConteudoRepository` tem 2 linhas e faz CRUD completo. Compare as
duas abordagens: o que o Spring Data JPA automatiza, o que o JDBC/DAO ainda resolve
melhor, e como o `findByCategoria` consegue funcionar sem implementação.

### 3. Exceções checked vs unchecked (Aula 11)
A `ClassificacaoIndicativaException` estourava como um erro genérico do servidor,
sem mensagem útil para o cliente. Explique a diferença entre `extends Exception` e
`extends RuntimeException` no contexto desse bug, e como você fez a mensagem da
regra (classificação indicativa) chegar de forma clara ao cliente da API.

### 4. Sobrescrita vs sobrecarga (Aula 7)
Um dos bugs compilava sem nenhum erro: o método da `Serie` parecia sobrescrever
`calcularPrecoAluguel`, mas na verdade sobrecarregava. Explique a diferença entre
override e overload nesse caso e por que a anotação `@Override` teria impedido o bug.

### 5. Onde blindar o objeto? (Aulas 3, 4 e 13)
Vimos bugs de dados inválidos aceitos (duração negativa, créditos negativos, campos
nulos). Em quais lugares (construtor, setter, método do model) cada tipo de validação
deve ficar? Justifique usando os bugs que você encontrou e explique por que validar só
em um lugar não foi suficiente.

### 6. Abstração e interface (Aulas 8 e 9)
`Conteudo` é abstrata e `Promocionavel` é uma interface. Explique a diferença de
propósito entre as duas nesse projeto e o que mudaria no código se o Documentário
passasse a ter promoções — quais classes/linhas seriam tocadas e quais ficariam
intactas? O que isso diz sobre o design do sistema?

---

## Parte 4 — Espaço livre (opcional)

Alguma dificuldade, dúvida ou comentário sobre o checkpoint?

```

```
