package br.unasp.meusgastos.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.unasp.meusgastos.model.Categoria

/**
 * Componente sem estado: mostra "Todas" e uma etiqueta por categoria.
 * [selecionada] = null significa "Todas". O clique sai por [aoFiltrar].
 */
@Composable
fun FiltroCategorias(
    selecionada: Categoria?,
    aoFiltrar: (Categoria?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Filtrar por", fontSize = 12.sp, color = Muted)
        Spacer(Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Etiqueta("Todas", ativa = selecionada == null, aoClicar = { aoFiltrar(null) })
            }
            items(Categoria.entries.toList()) { c ->
                Etiqueta(c.rotulo, ativa = selecionada == c, aoClicar = { aoFiltrar(c) })
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F6FA)
@Composable
private fun FiltroTodasPreview() {
    FiltroCategorias(selecionada = null, aoFiltrar = {}, modifier = Modifier.padding(16.dp))
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F6FA)
@Composable
private fun FiltroLazerPreview() {
    FiltroCategorias(selecionada = Categoria.LAZER, aoFiltrar = {}, modifier = Modifier.padding(16.dp))
}
