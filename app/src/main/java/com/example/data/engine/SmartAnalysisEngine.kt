package com.example.data.engine

import com.example.data.model.IdeaEvaluation
import java.util.Locale

object SmartAnalysisEngine {

    fun analyzeIdea(rawIdea: String, category: String): IdeaEvaluation {
        val lower = rawIdea.lowercase(Locale.ROOT)

        // Detect keywords to tailor analysis
        val isOrganic = lower.contains("orgánic") || lower.contains("organic") ||
                lower.contains("ecológic") || lower.contains("bio") ||
                lower.contains("natural") || lower.contains("huerto") ||
                lower.contains("vegano") || lower.contains("sostenible") ||
                category.contains("Orgánico", ignoreCase = true)

        val isTech = lower.contains("app") || lower.contains("software") ||
                lower.contains("plataforma") || lower.contains("ia") ||
                lower.contains("inteligencia artificial") || lower.contains("web") ||
                lower.contains("algoritmo") || category.contains("Tecnología", ignoreCase = true)

        val isFood = lower.contains("comida") || lower.contains("café") ||
                lower.contains("restaurante") || lower.contains("panadería") ||
                lower.contains("cocina") || lower.contains("gastronomía") ||
                category.contains("Gastronomía", ignoreCase = true)

        val isService = lower.contains("servicio") || lower.contains("asesoría") ||
                lower.contains("consultoría") || lower.contains("reparación") ||
                lower.contains("limpieza") || category.contains("Servicios", ignoreCase = true)

        val isEducation = lower.contains("curso") || lower.contains("educación") ||
                lower.contains("aprender") || lower.contains("enseñanza") ||
                lower.contains("taller") || category.contains("Educación", ignoreCase = true)

        // Generate dynamic title
        val words = rawIdea.trim().split("\\s+".toRegex())
        val generatedTitle = when {
            words.size in 1..4 -> rawIdea.replaceFirstChar { it.uppercase() }
            isOrganic -> "Proyecto Eco & Orgánico: " + words.take(3).joinToString(" ")
            isTech -> "Solución Digital: " + words.take(3).joinToString(" ")
            isFood -> "Concepto Gastronómico: " + words.take(3).joinToString(" ")
            isEducation -> "Iniciativa Educativa: " + words.take(3).joinToString(" ")
            else -> words.take(4).joinToString(" ").replaceFirstChar { it.uppercase() }
        }

        // 1. Innovación
        val innovacion = when {
            isOrganic -> "Tu idea destaca por su claro enfoque en productos orgánicos y sostenibles, lo cual es una fuerte tendencia de consumo consciente global y regional. Ofrece una alternativa saludable frente a la producción industrial convencional."
            isTech -> "Gran componente de modernización tecnológica. Automatiza fricciones tradicionales mediante herramientas digitales escalables, permitiendo acceso rápido y reducción de costos operativos."
            isFood -> "Atracción sensorial y de experiencia gastronómica diferenciada. Responde a la búsqueda de ingredientes frescos, autenticidad culinaria y conveniencia de consumo."
            isEducation -> "Alto impacto de valor formativo. Aprovecha metodologías prácticas y aprendizaje personalizado para resolver brechas de conocimiento actuales en el mercado."
            else -> "Propuesta con alto potencial de valor percibido. Atiende una necesidad latente en el mercado con una solución orientada a simplicidad, conveniencia y confianza para el usuario."
        }

        // 2. Viabilidad
        val viabilidad = when {
            isOrganic -> "Considera analizar minuciosamente la demanda local, los costos logísticos de refrigeración/cadena de frío y los canales de distribución directa (b2c, ferias y suscripciones) para optimizar márgenes."
            isTech -> "Viabilidad técnica elevada si se comienza con un Producto Mínimo Viable (MVP) sin sobrediseñar funciones. Prioriza arquitectura modular en la nube y pruebas tempranas de adopción."
            isFood -> "Requiere validar permisos sanitarios locales, estandarización de recetas, mermas de inventario y márgenes brutos de al menos 65% para asegurar sostenibilidad financiera."
            isEducation -> "Baja inversión inicial en infraestructura física. La clave es el costo de adquisición de estudiantes (CAC) y el porcentaje de retención/completitud de los programas."
            else -> "Técnicamente realizable con inversión escalonada. Se sugiere medir la disposición de pago real antes de comprometer capital significativo en inventario o contratos fijos."
        }

        // 3. Competencia
        val competencia = when {
            isOrganic -> "Investiga si existen otros productores o cooperativas similares en tu zona geográfica. Diferénciate a través de certificaciones de origen, empaques compostables, frescura garantizada y trato cercano al cliente."
            isTech -> "Existen herramientas globales, pero tu ventaja competitiva radicará en la hiperpersonalización, mejor soporte en tu idioma/región y velocidad de resolución del problema central."
            isFood -> "Competencia alta y dinámica. Tu diferenciador debe apoyarse en la experiencia del cliente, recetas exclusivas, puntualidad en la entrega y programa de lealtad."
            isEducation -> "Abundante contenido genérico en internet. El diferenciador clave será el acompañamiento tutorizado, proyectos prácticos aplicados a casos reales y comunidad activa."
            else -> "Mapea los 3 competidores más cercanos (directos e indirectos). Apaláncate en nichos desatendidos donde los grandes proveedores no ofrecen atención personalizada."
        }

        // 4. Público Objetivo
        val publicoObjetivo = when {
            isOrganic -> "Mercado local, familias jóvenes y adultos de 25-50 años preocupados por la salud, nutrición limpia, bienestar y el cuidado del medio ambiente con nivel socioeconómico medio-alto."
            isTech -> "Early adopters digitales, profesionales y pequeños negocios que buscan optimizar su tiempo diario y eliminar tareas repetitivas."
            isFood -> "Consumidores urbanos que valoran el sabor artesanal, la calidad de ingredientes y buscan opciones prácticas para su día a día o momentos de celebración."
            isEducation -> "Estudiantes, autodidactas y profesionales que buscan reconversión laboral o mejorar sus habilidades prácticas con metas profesionales concretas."
            else -> "Consumidores y profesionales que enfrentan la molestia actual que tu idea resuelve y que están dispuestos a pagar por una alternativa más confiable y ágil."
        }

        // 5. Próximos Pasos Accionables
        val proximosPasos = when {
            isOrganic -> listOf(
                "Realiza una encuesta rápida en tu comunidad y grupos vecinales para conocer hábitos de compra.",
                "Busca alianzas con agricultores, cooperativas y comercios locales para asegurar suministro constante.",
                "Lanza una preventa piloto de cajas degustación o pedidos semanales con 15 clientes iniciales.",
                "Diseña empaques ecológicos que comuniquen la historia detrás de cada producto y sus beneficios.",
                "Monitorea costos de merma y ajusta precios para garantizar un margen operativo saludable."
            ).joinToString("\n")
            isTech -> listOf(
                "Entrevista a 10 potenciales usuarios para validar si el problema es lo suficientemente doloroso.",
                "Diseña un prototipo navegable en Figma o wireframe antes de programar una sola línea de código.",
                "Crea una landing page de prelanzamiento para capturar correos de interesados tempranos.",
                "Construye la versión mínima con la funcionalidad principal (MVP) en un plazo no mayor a 30 días.",
                "Establece métricas clave: tasa de activación, retención semanal y feedback directo de usuarios."
            ).joinToString("\n")
            isFood -> listOf(
                "Organiza una cata a ciegas o degustación piloto con familiares y amigos exigentes para pulir recetas.",
                "Calcula el costo unitario por porción (costo de materia prima x factor de ganancia).",
                "Revisa la normativa sanitaria y permisos de manipulación de alimentos en tu municipalidad.",
                "Crea un perfil visual atractivo en redes sociales enfocado en el apetito y la elaboración limpia.",
                "Inicia con pedidos por encargo o cupos limitados para controlar la demanda y la calidad."
            ).joinToString("\n")
            else -> listOf(
                "Aplica 'The Mom Test': habla con 10 personas de tu público meta sin venderles, solo entendiendo su problema.",
                "Define tu propuesta de valor única en una sola frase contundente.",
                "Calcula la inversión mínima inicial para operar durante los primeros 3 meses.",
                "Consigue tus primeros 3 clientes dispuestos a pagar un adelanto o precio de lanzamiento.",
                "Registra métricas semanales y ajusta el servicio con base en las sugerencias recibidas."
            ).joinToString("\n")
        }

        // SWOT Matrix
        val fortalezas = when {
            isOrganic -> "Producto alineado con salud, bienestar y sostenibilidad; alta lealtad de clientes si la calidad es consistente; potencial de venta recurrente por suscripción."
            isTech -> "Escalabilidad exponencial con costos marginales bajos; capacidad de iterar y desplegar mejoras continuamente; alcance geográfico amplio."
            isFood -> "Generación inmediata de flujo de caja; alta conexión emocional con el consumidor; recomendación boca a boca natural."
            else -> "Flexibilidad para adaptarse a los primeros clientes; atención personalizada y cercanía que las grandes marcas no pueden ofrecer."
        }

        val oportunidades = when {
            isOrganic -> "Crecimiento del mercado verde; posibles alianzas con cafeterías saludables, gimnasios y ferias ecológicas locales."
            isTech -> "Integración con nuevas APIs e inteligencia artificial; expansión hacia nuevos segmentos B2B o microempresas."
            isFood -> "Eventos corporativos, servicios de catering, venta de productos complementarios empaquetados."
            else -> "Digitalización de procesos tradicionales; expansión a zonas vecinas desatendidas."
        }

        val debilidades = when {
            isOrganic -> "Caducidad corta de productos frescos; dependencia de temporadas de cosecha; costos de insumos certificados más elevados."
            isTech -> "Costo de adquisición de clientes inicial; necesidad de soporte técnico continuo; vulnerabilidad si no se fideliza al usuario."
            isFood -> "Altas exigencias de tiempo y presencia física; sensibilidad a la variación de costos de ingredientes básicos."
            else -> "Recursos iniciales limitados; marca aún desconocida que requiere construir reputación desde cero."
        }

        val amenazas = when {
            isOrganic -> "Supermercados incorporando líneas orgánicas a precios masivos; fluctuaciones climáticas que afecten cosechas."
            isTech -> "Aparición de competidores con mayor financiamiento; cambios en políticas de plataformas o APIs de terceros."
            isFood -> "Nuevas regulaciones sanitarias; competencia agresiva de precios en delivery."
            else -> "Inestabilidad macroeconómica o inflación que contraiga el gasto prescindible de los clientes."
        }

        // Calculate Viability Score
        val lengthBonus = (rawIdea.length / 15).coerceIn(0, 10)
        val baseScore = 75 + lengthBonus
        val viabilityScore = baseScore.coerceIn(68, 94)

        val scoreLabel = when {
            viabilityScore >= 85 -> "Excelente Viabilidad"
            viabilityScore >= 75 -> "Alta Viabilidad"
            viabilityScore >= 65 -> "Viabilidad Moderada"
            else -> "Requiere Refinamiento"
        }

        val monetization = when {
            isOrganic -> "Ventas directas al consumidor (B2C), modelos de suscripción semanal de cajas frescas y venta mayorista a restaurantes locales."
            isTech -> "Modelo Freemium con suscripción mensual (SaaS), comisión por transacción o tarifas por volumen de uso."
            isFood -> "Venta por unidad/menú, combos familiares, catering para reuniones y programas de lealtad prepagados."
            else -> "Cobro por proyecto/servicio, membresías mensuales recurrentes y paquetes escalonados."
        }

        return IdeaEvaluation(
            title = generatedTitle,
            rawIdea = rawIdea,
            category = category,
            innovacion = innovacion,
            viabilidad = viabilidad,
            competencia = competencia,
            publicoObjetivo = publicoObjetivo,
            proximosPasosList = proximosPasos,
            swotFortalezas = fortalezas,
            swotOportunidades = oportunidades,
            swotDebilidades = debilidades,
            swotAmenazas = amenazas,
            viabilityScore = viabilityScore,
            scoreLabel = scoreLabel,
            targetNiche = publicoObjetivo.take(60),
            monetizationStrategy = monetization,
            isRealAi = false
        )
    }
}
