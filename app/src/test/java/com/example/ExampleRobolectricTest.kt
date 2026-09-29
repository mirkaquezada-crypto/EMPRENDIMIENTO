package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.engine.SmartAnalysisEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Validador de Ideas", appName)
  }

  @Test
  fun `analyze organic idea produces organic pillar feedback`() {
    val organicIdea = "Venta y distribución de hortalizas y frutas orgánicas directamente desde productores locales con empaque compostable."
    val result = SmartAnalysisEngine.analyzeIdea(organicIdea, "🌱 Productos Orgánicos")

    assertNotNull(result)
    assertTrue("Should mention organic products", result.innovacion.contains("orgánicos", ignoreCase = true))
    assertTrue("Should mention distribution or demand", result.viabilidad.contains("distribución", ignoreCase = true) || result.viabilidad.contains("demanda", ignoreCase = true))
    assertTrue("Should contain actionable steps", result.getSteps().isNotEmpty())
    assertTrue("Score should be valid", result.viabilityScore in 50..100)
  }
}
