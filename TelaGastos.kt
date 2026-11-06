package br.unasp.meusgastos.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.unasp.meusgastos.viewmodel.GastosViewModel

/**
 * CAMADA VIEW.
 * Este é o ÚNICO Composable do app que conhece o ViewModel.
 * Repare: não existe nenhuma regra de negócio aqui dentro. Os únicos "if"
 * apenas escolhem o que desenhar (lista ou mensagem de lista vazia).
 */
@Composable
fun TelaGastos(
    vm: GastosViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val gastosVisiveis = vm.gastosVisiveis

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Fundo)
            // Mantém o conteúdo fora das barras do sistema e acima do teclado.
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Text("Meus Gastos", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Navy)
        Spacer(Modifier.height(14.dp))
        FormularioGasto(
            descricao = vm.descricao,
            valorTexto = vm.valorTexto,
            categoria = vm.categoriaSelecionada,
            erro = vm.erro,
            aoDigitarDescricao = vm::aoDigitarDescricao,
            aoDigitarValor = vm::aoDigitarValor,
            aoSelecionarCategoria = vm::aoSelecionarCategoria,
            aoAdicionar = vm::adicionar
        )
        Spacer(Modifier.height(16.dp))
        FiltroCategorias(selecionada = vm.filtro, aoFiltrar = vm::aoFiltrar)
        Spacer(Modifier.height(12.dp))
        ResumoTotal(total = vm.total, quantidade = vm.quantidade)
        Spacer(Modifier.height(12.dp))
        if (gastosVisiveis.isEmpty()) {
            MensagemListaVazia(filtrando = vm.filtro != null)
        } else {
            // weight(1f): a lista ocupa o espaço que sobra e rola dentro dele.
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // key: o id identifica cada linha, mesmo quando a posição muda.
                items(gastosVisiveis, key = { it.id }) { gasto ->
                    LinhaGasto(gasto = gasto, aoRemover = { vm.remover(gasto.id) })
                }
            }
        }
    }
}

@Composable
private fun MensagemListaVazia(filtrando: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = if (filtrando) "Nenhum gasto nesta categoria." else "Nenhum gasto cadastrado ainda.",
        fontSize = 14.sp,
        color = Muted,
        modifier = modifier.padding(top = 8.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F6FA)
@Composable
private fun ListaVaziaPreview() {
    MensagemListaVazia(filtrando = false, modifier = Modifier.padding(16.dp))
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F6FA)
@Composable
private fun ListaVaziaFiltradaPreview() {
    MensagemListaVazia(filtrando = true, modifier = Modifier.padding(16.dp))
}
