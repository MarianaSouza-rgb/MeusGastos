# ARQUITETURA.md — Meus Gastos (MVVM)

## 1. Se a fonte de dados mudar de memória para outra implementação, o que permanece igual e o que pode precisar de adaptação?

**Permanece igual:** toda a pasta `ui/` (TelaGastos, FormularioGasto, LinhaGasto, ResumoTotal, Etiqueta), as regras de validação do `GastosViewModel`, o estado derivado (`gastosVisiveis`, `total`, `quantidade`) e as classes `Gasto` e `Categoria`. Isso acontece porque o ViewModel depende da interface `GastoRepository` (o contrato: `listar`, `adicionar`, `remover`) e não de uma classe concreta. No experimento da seção 7, trocar `GastoRepositoryMemoria()` por `GastoRepositoryDemo()` exigiu mudar uma única linha, o valor padrão do parâmetro.

**Pode precisar de adaptação:** uma troca para o Room não seria só trocar a classe.
- Operações de banco são assíncronas e não devem rodar na thread principal. O contrato (`fun listar()`) teria de virar `suspend` ou retornar `Flow`, e o ViewModel passaria a usar `viewModelScope`.
- A função `sincronizar()` poderia deixar de existir se o repositório devolvesse um fluxo observável.
- Seria preciso configurar o banco (entidade, DAO, dependências do Gradle) e decidir como o repositório é criado (injeção de dependência ou fábrica de ViewModel), já que o valor padrão no construtor deixaria de bastar.

Ou seja: o contrato protege a tela e as regras, mas só se for projetado para isso. Se a interface precisar mudar, o ViewModel muda junto.

## 2. Por que `total` e `quantidade` são calculados a partir da lista visível? Que erro surgiria se fossem atualizados manualmente?

São estado **derivado**: o total e a quantidade são consequência de `gastos` e `filtro`. Calculados em `get()`, eles são recalculados sempre que uma dessas fontes muda, então nunca ficam desatualizados e existe uma única fonte de verdade.

Se fossem variáveis guardadas, cada ação (adicionar, remover, trocar filtro) teria de lembrar de atualizá-las. Um erro concreto: o usuário filtra por "Lazer", e o total fica mostrando a soma de *todas* as categorias porque `aoFiltrar` não recalculou o `total`. Outro: o usuário remove um gasto de R$ 142,00 e o `remover()` esquece de subtrair; a lista mostra um item a menos, mas o total continua o mesmo e o resumo diz "4 gasto(s)" para 3 itens na tela.

## 3. O que os componentes sem estado conseguem fazer nos previews que seria menos simples com dependência direta de um ViewModel?

Como recebem apenas valores e callbacks, os componentes podem ser desenhados no `@Preview` com dados fixos e lambdas vazias (`aoClicar = {}`). Dá para ver, por exemplo, o `FormularioGasto` já com a mensagem de erro `"O valor precisa ser um número"`, sem digitar nada, sem acionar a validação e sem executar o app. Também dá para ver o `ResumoTotal` vazio e preenchido, ou a `Etiqueta` ativa e inativa, lado a lado.

Um componente que dependesse de um `GastosViewModel` real exigiria criar uma instância (e possivelmente um repositório) só para desenhar, e para mostrar o estado de erro seria preciso colocar o ViewModel nesse estado, o que dá mais trabalho e acopla o preview à lógica. Os componentes sem estado também são mais fáceis de reutilizar: a `Etiqueta` serve tanto para a categoria do formulário quanto para o filtro da lista.
