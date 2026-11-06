# MUDANCAS.md — Melhorias aplicadas ao Meus Gastos

Nada mudou na arquitetura: Model, ViewModel e View continuam separados, os dados continuam só em memória e a troca `GastoRepositoryMemoria` → `GastoRepositoryDemo` continua sendo uma linha no `GastosViewModel`.

**Estas alterações não foram compiladas nem executadas.** Veja a lista de testes no final.

| # | O que mudou | Por que | Como funciona | Benefício | Arquivo |
|---|---|---|---|---|---|
| 1 | Novo ramo `!valor.isFinite()` na validação | `"NaN".toDoubleOrNull()` devolve `NaN`, e `NaN <= 0.0` é falso: o valor passava | `isFinite()` é falso para NaN e Infinity, e o erro "O valor precisa ser um número" aparece | Não entra mais gasto inválido nem "R$ NaN" no total | `GastosViewModel.kt` |
| 2 | Removido o `valor!!` | O `!!` quebra o app se a premissa falhar | `if (erro != null \|\| valor == null) return`: depois dessa linha o compilador sabe que `valor` não é nulo (smart cast) | Código mais seguro, sem asserção | `GastosViewModel.kt` |
| 3 | `singleLine` nos campos e teclado `Decimal` no Valor | O campo aceitava qualquer texto e quebras de linha | `KeyboardOptions(keyboardType = KeyboardType.Decimal)` pede teclado numérico | Menos digitação inválida; o ViewModel continua validando | `FormularioGasto.kt` |
| 4 | "remover" virou `TextButton` "Remover" | Texto de 12sp era difícil de tocar | `TextButton` tem área de toque padrão do Material | Mais fácil de usar, mesmo callback `aoRemover` | `LinhaGasto.kt` |
| 5 | `key = { it.id }` e `weight(1f)` no `LazyColumn` | Sem chave o Compose identifica linhas pela posição; sem `weight` a lista não tem tamanho definido | A chave liga cada linha ao `id` do gasto; o `weight` dá à lista o espaço que sobra | Remoção correta de itens e rolagem dentro da área certa | `TelaGastos.kt` |
| 5 | `safeDrawingPadding()` na tela | Em Android 15+ o conteúdo pode ficar sob a barra de status | Aplica margem das barras do sistema e do teclado | Título e campos não ficam cobertos | `TelaGastos.kt` |
| 5 | Rótulos "Categoria" e "Filtrar por" | As duas fileiras de chips eram idênticas | Dois `Text` pequenos acima de cada fileira | Interface mais clara | `FormularioGasto.kt`, `FiltroCategorias.kt` |
| 5 | Mensagem de lista vazia | Tela em branco não explica nada | `if (gastosVisiveis.isEmpty())` escolhe entre a mensagem e a lista (só decide o que desenhar, não é regra de negócio) | Feedback ao usuário | `TelaGastos.kt` |
| 6 | `formatarReal` com `NumberFormat` e locale `pt-BR`, em `Formatacao.kt` | Dependia do idioma do aparelho, sem milhar, e estava no arquivo de cores | `NumberFormat.getCurrencyInstance(pt-BR)` | "R$ 1.234,56" em qualquer aparelho; cada arquivo com uma responsabilidade | `Formatacao.kt`, `Cores.kt` |
| 6 | Plural: "1 gasto" / "N gastos" | "gasto(s)" era um atalho | `if (quantidade == 1)` | Texto natural | `ResumoTotal.kt` |
| 6 | Cor `Card` renomeada para `CorCard` | Confundia com o componente `Card` do Material3 | Renomeada em `Cores.kt` e `Etiqueta.kt` | Leitura sem ambiguidade | `Cores.kt`, `Etiqueta.kt` |
| 7 | `Coral` `F3704E` → `C8441F`; `Muted` `5A7A96` → `4A6782` | Contraste calculado: branco/coral 2,9:1 e Muted/CorCard 3,9:1 | Cores mais escuras: agora 4,88:1 e 5,12:1 | Texto mais legível; **muda um pouco a identidade visual** | `Cores.kt` |
| 8 | `FiltroCategorias` movido para `ui/FiltroCategorias.kt` | Um componente por arquivo, como os outros | Mesmo código, arquivo próprio, com dois Previews no padrão dos demais | Organização e manutenção | `FiltroCategorias.kt`, `TelaGastos.kt` |
| 8 | KDoc em `LinhaGasto`, `ResumoTotal`, `FiltroCategorias` | Só alguns componentes tinham explicação | Comentários curtos de responsabilidade | Mais fácil de entender | arquivos `ui/` |

## Não aplicado
- **`isError` nos campos:** o ViewModel expõe uma única mensagem, sem dizer qual campo errou. Marcar os dois em vermelho enganaria; marcar o certo exigiria novo estado no ViewModel. A mensagem em vermelho abaixo do formulário foi mantida.
- **Deduplicar Memoria e Demo:** mudaria as classes que o professor mostra, sem ganho para a atividade.

## O que testar no Android Studio
1. Make Project sem erros (imports e smart cast da validação).
2. Digitar `NaN` e `Infinity` no Valor: deve mostrar erro e não criar gasto.
3. Descrição vazia, `abc` e `0`: erros corretos. `12,50` e `12.50`: aceitos.
4. Título "Meus Gastos" visível sob a barra de status, em emulador com Android 15+.
5. Remover um gasto no meio da lista; filtrar; remover o último (aparece a mensagem de vazio).
6. Total e quantidade acompanham o filtro; "1 gasto" no singular.
7. Valores acima de mil com ponto de milhar ("R$ 1.234,56").
8. Os Previews de todos os arquivos `ui/` renderizam.
9. Altura e alinhamento da linha com o botão "Remover".
10. Trocar para `GastoRepositoryDemo()` sem editar nenhum arquivo de `ui/`.
