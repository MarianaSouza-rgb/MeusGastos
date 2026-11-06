# Meus Gastos (MVVM com Kotlin e Jetpack Compose)

Código-fonte dos arquivos do apêndice do tutorial, nos pacotes corretos (`app/src/main/java/br/unasp/meusgastos`).

Este ZIP contém os arquivos Kotlin, não um projeto Gradle completo. Para usar:

1. No Android Studio, crie um projeto **Empty Activity**: nome `MeusGastos`, package `br.unasp.meusgastos`, Kotlin DSL, minSdk 24.
2. Em `gradle/libs.versions.toml`, acrescente:
   - em `[versions]`: `lifecycleViewmodelCompose = "2.8.7"`
   - em `[libraries]`: `androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleViewmodelCompose" }`
3. Em `app/build.gradle.kts`, dentro de `dependencies`: `implementation(libs.androidx.lifecycle.viewmodel.compose)`. Faça o Sync.
4. Copie a pasta `model/`, `viewmodel/`, `ui/` e o `MainActivity.kt` deste ZIP para `app/src/main/java/br/unasp/meusgastos` (substituindo o MainActivity existente).
5. Build > Make Project e Run.

Arquivos novos em `ui/` em relação ao gabarito: `FiltroCategorias.kt` (antes dentro de `TelaGastos.kt`) e `Formatacao.kt` (antes `formatarReal` dentro de `Cores.kt`).

Na raiz: `ARQUITETURA.md`, `ESTRAGOS.md` e `MUDANCAS.md` (o que mudou, por quê, como funciona, benefício e o que testar).

Aviso: este código não foi compilado nem executado.
