package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StartupGuideScreen(
    onGoToEvaluator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("startup_guide_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Guía de Validación Rápida",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Aprende a transformar ideas en negocios reales con metodologías ágiles comprobadas.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Framework 1: The Mom Test
        GuideMethodCard(
            title = "1. Regla de Oro: The Mom Test",
            subtitle = "Cómo validar sin que te mientan por cortesía",
            icon = Icons.Default.Psychology,
            iconTint = Color(0xFF7C3AED),
            points = listOf(
                "No preguntes: '¿Comprarías una app o producto así?' (La gente siempre dice sí por amabilidad).",
                "Pregunta sobre hechos pasados: '¿Cuándo fue la última vez que tuviste este problema y cómo lo resolviste?'.",
                "Indaga en el dolor real: '¿Cuánto tiempo o dinero te costó resolverlo?'. Si nunca han intentado resolverlo, el dolor no es suficiente."
            )
        )

        // Framework 2: El Producto Mínimo Viable (MVP)
        GuideMethodCard(
            title = "2. El MVP Verdadero",
            subtitle = "No construyas el auto entero; empieza con la patineta",
            icon = Icons.Default.Build,
            iconTint = Color(0xFF2563EB),
            points = listOf(
                "MVP de Mago de Oz: El usuario ve una interfaz automatizada, pero los procesos tras bambalinas los haces manualmente al inicio.",
                "MVP de Conserje: Presta el servicio tú mismo personalmente a 5 clientes para entender cada fricción antes de automatizar.",
                "Landing Page de humo: Publica una página web sencilla con tu propuesta de valor y un botón de preventa antes de comprar inventario."
            )
        )

        // Framework 3: Validación de Demanda y Dinero
        GuideMethodCard(
            title = "3. Validación con Dinero Real",
            subtitle = "La única validación definitiva es la disposición de pago",
            icon = Icons.Default.Payments,
            iconTint = Color(0xFF00897B),
            points = listOf(
                "Los likes y comentarios positivos no pagan nóminas ni costos operativos.",
                "Consigue 3 a 10 cartas de intención de compra (LOI) o depósitos anticipados con descuento por cliente fundador.",
                "Si la gente duda en pagar un precio de prueba justo, ajusta la propuesta de valor antes de seguir invirtiendo."
            )
        )

        // Framework 4: El Ciclo Lean Startup
        GuideMethodCard(
            title = "4. Ciclo Lean: Crear -> Medir -> Aprender",
            subtitle = "La velocidad de aprendizaje define el éxito",
            icon = Icons.Default.RocketLaunch,
            iconTint = Color(0xFFD97706),
            points = listOf(
                "Formula hipótesis claras: 'Creo que las familias de mi barrio pagarán \$20 semanales por una caja de verduras orgánicas frescas'.",
                "Diseña el experimento más barato posible para refutar o confirmar la hipótesis en menos de 7 días.",
                "Pivota sin remordimientos si los datos muestran que el mercado prefiere otro enfoque."
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun GuideMethodCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    points: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = iconTint.copy(alpha = 0.12f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            points.forEach { point ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircleOutline,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = point,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
