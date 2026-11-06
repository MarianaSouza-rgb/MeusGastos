# ESTRAGOS.md — Experimentos de depuração

> Como usar: as colunas "Minha previsão", "O que explica o resultado" e "Como restaurei o código" já estão preenchidas.
> **"O que observei" precisa ser preenchido por quem executar o experimento no emulador.** Não foi possível rodar o app ao preparar este arquivo, então deixei o espaço em branco em vez de inventar observações. Confirme ou corrija as previsões com o que aparecer na tela.

---

## Experimento 1 — Construir o ViewModel na tela

**Mudança:** em `TelaGastos`, trocar `vm: GastosViewModel = viewModel()` por `vm: GastosViewModel = GastosViewModel()`.

**Minha previsão:** a tela abre normalmente, mas o estado fica instável. Cada vez que `TelaGastos` recompõe, uma instância nova pode ser criada, e os campos digitados ou os itens cadastrados podem sumir. Ao girar a tela, os dados devem ser perdidos com certeza.

**O que observei:** _(preencher após executar: cadastre alguns itens, digite no campo e gire o emulador)_

**O que explica o resultado:** `viewModel()` obtém a instância guardada no `ViewModelStore`, associada ao ciclo de vida da tela, e a devolve de novo após recomposições e mudanças de configuração. Construir manualmente cria um objeto sem esse gerenciamento. Se a composição o recriar, o estado volta ao inicial. O resultado exato depende de como a composição e o estado interagem, por isso o comportamento ao digitar deve ser observado, não presumido.

**Como restaurei o código:** voltei para `vm: GastosViewModel = viewModel()` e confirmei o import `androidx.lifecycle.viewmodel.compose.viewModel`.

---

## Experimento 2 — Lista não observável

**Mudança:** em `GastosViewModel`, trocar `mutableStateListOf<Gasto>()` por `mutableListOf<Gasto>()`.

**Minha previsão:** ao remover um item, a lista na tela não muda (o item continua aparecendo), porque nada avisa o Compose. Ao adicionar, a lista pode até aparecer atualizada, porque `descricao` e `valorTexto` são limpos e isso dispara recomposição da tela.

**O que observei:** _(preencher após executar)_

**O que explica o resultado:** o Compose só recompõe quando lê um estado observável que mudou. `mutableListOf` não notifica ninguém quando recebe `clear()` ou `addAll()`. Mudanças nos campos `mutableStateOf` ainda disparam recomposição, e nessa recomposição a lista é lida de novo. Por isso a atualização pode parecer funcionar em alguns casos e falhar em outros.

**Como restaurei o código:** voltei para `mutableStateListOf<Gasto>()`.

---

## Experimento 3 — Total manual

**Mudança:** guardar `var total` e `var quantidade` no ViewModel e atualizá-los somente em `adicionar()`.

**Minha previsão:** o total ficará correto ao adicionar, mas errado ao remover um item e ao trocar o filtro (continua mostrando a soma de tudo).

**O que observei:** _(preencher após executar: adicione itens, remova um, depois filtre por categoria)_

**O que explica o resultado:** o total deixou de ser calculado a partir de `gastosVisiveis` e passou a ser um dado independente que precisa ser mantido sincronizado em cada ação. `remover()` e `aoFiltrar()` não o atualizam, então ele diverge da lista exibida. Com estado derivado, essa classe de erro não existe.

**Como restaurei o código:** apaguei as variáveis guardadas e voltei aos getters `val total: Double get() = gastosVisiveis.sumOf { it.valor }` e `val quantidade: Int get() = gastosVisiveis.size`.

---

## Experimento 4 — Acoplamento à classe

**Mudança:** declarar `private val repositorio = GastoRepositoryMemoria()` dentro do ViewModel, sem receber `GastoRepository` pelo construtor.

**Minha previsão:** o app funciona igual, mas para usar `GastoRepositoryDemo` agora preciso editar o corpo do ViewModel, e a troca deixa de ser uma mudança em uma linha do parâmetro.

**O que observei:** _(preencher após executar: tente repetir o experimento da seção 7)_

**O que explica o resultado:** o ViewModel passou a depender de uma implementação concreta em vez do contrato. A interface é a fronteira que permitia a troca de implementação sem mexer na lógica da tela.

**Como restaurei o código:** voltei ao construtor `class GastosViewModel(private val repositorio: GastoRepository = GastoRepositoryMemoria())`.

---

## Experimento 5 — Regra na tela

**Mudança:** validar a descrição vazia dentro do `onClick` do botão "Adicionar", na própria tela.

**Minha previsão:** o app continua funcionando, mas a regra passa a existir em dois lugares (tela e ViewModel), ou só na tela. Qualquer outra tela que adicione gastos teria de copiar a validação, e não haveria como testá-la sem a interface.

**O que observei:** _(preencher após executar)_

**O que explica o resultado:** validar é regra de negócio e pertence ao ViewModel. A View deve apenas encaminhar o evento e mostrar a mensagem de erro que recebe. Com a regra no ViewModel, ela é reutilizável por outras telas e testável sem desenhar nada.

**Como restaurei o código:** removi a validação do `onClick` e deixei `aoAdicionar = vm::adicionar`.
