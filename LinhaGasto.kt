package br.unasp.meusgastos.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.unasp.meusgastos.model.Categoria
import br.unasp.meusgastos.model.Gasto

/**
 * Componente sem estado: mostra um gasto e avisa, por callback, quando a
 * pessoa pede para removê-lo. Quem remove de verdade é o ViewModel.
 */
@Composable
fun LinhaGasto(gasto: Gasto, aoRemover: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(gasto.descricao, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Navy)
            Text(gasto.categoria.rotulo, fontSize = 12.sp, color = Muted)
        }
        Text(formatarReal(gasto.valor), fontSize = 15.sp, color = Navy)
        // TextButton tem área de toque maior que um Text clicável.
        TextButton(
            onClick = aoRemover,
            colors = ButtonDefaults.textButtonColors(contentColor = Coral)
        ) {
            Text("Remover", fontSize = 13.sp)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F6FA)
@Composable
private fun LinhaGastoPreview() {
    LinhaGasto(
        gasto = Gasto(1, "Almoço no bandejão", 18.50, Categoria.ALIMENTACAO),
        aoRemover = {},
        modifier = Modifier.padding(16.dp)
    )
}
