# Checkpoint 4 — Bug Hunt StreamFIAP

> **Professor:** Ygor Moraes Martins dos Anjos
---

## Identificação

**Grupo:** debugthebug

| Integrante | RM | Turma |
|---|---|---|
|Felipe Rodrigues Ribeiro | RM565274 | 2CCPW |
|Guilherme Ferraz de Medeiros | RM564743 | 2CCPW |
|Manoela Oliveira Bello | RM563952 | 2CCPW |
|Roberto Marques Moreira | RM564935 | 2CCPW |




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
| clean04 | Usuario.java (alugar) | No código tinha um bloco enorme de "System.out.println" simulando um recibo que uma API REST e não mostrava para o front-end. | Apagamos todos os prints, deixando só o que importa para a lógica de negócio |
| clean05 | ConteudoController.java (final do arquivo) | Tinham muitas funções obsoletas com blocos inteiros de lógica comentados . | Apagamos tudo. O histórico do que foi feito fica no Git, não em código comentado |
| clean06 | Usuario.java (debitarCreditos) | Comentário não verdadeiro sobre adicionar saldo em uma linha que estava fazendo subtração. | Deletamos o comentário que só servia para atrapalhar a leitura. |

---

## Parte 3 — Perguntas de reflexão

### 1. Injeção de dependência (Aula 13)
Pela regra geral uma interface não pode ser instanciada com um new, e o "ConteudoRepository" e uma interface, portanto nem sequer existe essa implementação.
Por isso que na classe "AluguelController.java", o uso do @Autowired em cima do "ConteudoRepository" é obrigatório, praticamente delegamos para o container de Inversão de Controle (IoC) do Spring essa responsabilidade.
O SpringData gera um proxy para que possamos implementar essa interface, acoplando a infraestrutura do Hibernate, EntityManager, conexões do DataSource e transações JDBC necessárias para conversar com o Oracle, registra esse objeto como Bean e precisa também configurar o acesso nele ao JPA do banco de dados.
Se tentássemos instanciar manualmente, além do erro de compilação por se tratar de uma interface, perderíamos todo esse gerenciamento de sessões, segurança e performance que o Spring Boot entrega pronto.

### 2. JDBC vs Spring Data JPA (Aulas 12 e 13)
No JDBC da Aula 12, precisávamos fazer tudo na mão: abrir o Connection, montar o SQL, mapear o ResultSet linha por linha e fechar recursos. 
Já no projeto StreamFIAP, o Spring Data JPA automatiza todo esse código repetitivo (boilerplate), bastando que o ConteudoRepository estenda JpaRepository<Conteudo, Long>. 
O método findByCategoria funciona sem nenhuma implementação explícita graças ao Derived Query Methods: o Spring analisa o nome do método e gera dinamicamente a query no banco de dados baseada no atributo "categoria". 
Apesar de tudo, o JDBC/DAO tradicional ainda se sai melhor em cenários específicos de altíssima performance, como batch inserts gigantes ou em consultas analíticas muito complexas, onde o mapeamento automático do Spring (ORM) poderia gerar um SQL lento.

### 3. Exceções checked vs unchecked (Aula 11)
A diferença é que classes que herdam de "Exception" são checadas, obrigando a quem estiver desenvolvendo usar try-catch ou declarar throws na assinatura do método (como ocorria em Usuario.alugar). 
Já as que herdam de "RuntimeException" são não-checadas e sobem pelas camadas do sistema sem amarrar o compilador. 
Antes, a exceção subia crua até o Spring Web, que a interpretava como uma falha fatal imprevista e gerava o erro genérico HTTP 500 (Internal Server Error). 
Para resolver isso, alteramos a exceção para unchecked e criamos um tratador no GlobalExceptionHandler com a anotação @ExceptionHandler(ClassificacaoIndicativaException.class). 
Com isso, o framework intercepta o evento e serializa um JSON amigável, entregando a mensagem da regra de negócio com o status correto de HTTP 403 (Forbidden).

### 4. Sobrescrita vs sobrecarga (Aula 7)
A sobrescrita (override) redefine um método da superclasse mantendo exatamente a mesma assinatura, enquanto a sobrecarga (overload) cria um método novo com o mesmo nome, mas parâmetros diferentes. 
Em Serie, o método foi declarado como calcularPrecoAluguel(double desconto). Como a classe mãe Conteudo não recebia argumentos, o Java interpretou isso como um overload válido e o código compilou sem erros. 
Porém, durante a chamada polimórfica no método Usuario.alugar(Conteudo c), a JVM ignorava a regra da série e executava a implementação da mãe, cobrando indevidamente R$ 9,90. 
Corrigimos removendo o parâmetro e adicionando a anotação @Override para validar a alteração de comportamento. 
Se o @Override estivesse lá desde o início, o compilador teria impedido esse bug silencioso imediatamente, alertando que não havia método com aquela assinatura na superclasse para ser sobrescrito.

### 5. Onde blindar o objeto? (Aulas 3, 4 e 13)
A blindagem estrutural deve estar na camada de domínio para garantir que o objeto nunca assuma um estado corrompido. 
O construtor garante um nascimento seguro aplicando o "Fail Fast", como fizemos ao impedir uma duração negativa em Conteudo e ao usar "super(...)" em Serie para evitar atributos nulos. 
Os setters defendem o objeto de modificações posteriores inválidas. 
Já as transições complexas pertencem aos métodos do model: a verificação de "c.isDisponivel()" e se o usuário tem saldo para o débito ocorrem exclusivamente dentro de "Usuario.alugar", pois dependem do contexto relacional da operação. 
Validar apenas em um lugar, como no "Controller", é insuficiente porque espalha a regra de negócio para fora do domínio; se a classe for instanciada por um teste unitário ou por outro fluxo interno, ela nasceria completamente desprotegida.

### 6. Abstração e interface (Aulas 8 e 9)
A classe abstrata "Conteudo" modela a identidade, ou seja, o que o objeto é, centralizando os atributos base da hierarquia (título, categoria, duração e a chave no banco). 
Já a interface Promocionavel modela um comportamento, ou seja, o que o objeto faz, permitindo acoplar uma habilidade financeira apenas às classes que precisarem, sem engessar a herança. 
Se a regra de negócio mudasse e o documentário aceitasse promoções, na alteração bastaria assinar o contrato colocando "implements Promocionavel" na classe "Documentario.java" e implementar o método "@Override public double aplicarPromocao(double preco)". 
Todo o resto — as classes Conteudo, Filme, Serie e os controllers — permaneceria 100% intacto, pois a aplicação só verifica o contrato via instanceof. 

---

## Parte 4 — Espaço livre (opcional)
- Identificamos na aba Usuario.java um possível 7° Clean Clode, porque tivemos que melhorar também o nome das variáveis "c" e "p". Trocamos o "c" por "conteudo" e o "p" por "precoAluguel". Desta forma facilita a leitura e compreensão do código.

  Todas as CPs da disciplina de POO são desafiadoras, sempre depois que finalizo toda a leitura do pdf parece que é uma tarefa impossível, por isso como regra para qualquer CP de POO temos que por obrigatoriedade revisar todas as aulas e conceitos já vistos, isso me ajuda muito a rever conceitos já esquecidos e traz uma sensação de dever cumprido quando conseguimos finalizar toda tarefa.



