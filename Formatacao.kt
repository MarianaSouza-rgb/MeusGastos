package br.unasp.meusgastos.ui

import java.text.NumberFormat
import java.util.Locale

private val localeBrasil: Locale = Locale.forLanguageTag("pt-BR")

/**
 * Formata um valor em reais, sempre no padrão brasileiro (ex.: R$ 1.234,56),
 * independente do idioma configurado no aparelho.
 */
fun formatarReal(valor: Double): String =
    NumberFormat.getCurrencyInstance(localeBrasil).format(valor)
