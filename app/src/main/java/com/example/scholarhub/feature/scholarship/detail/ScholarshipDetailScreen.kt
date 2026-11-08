package com.example.scholarhub.feature.scholarship.detail

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scholarhub.R
import com.example.scholarhub.data.scholarship.ScholarshipJsonParser
import com.example.scholarhub.domain.scholarship.Scholarship
import com.example.scholarhub.domain.scholarship.ScholarshipBenefit
import com.example.scholarhub.domain.scholarship.ScholarshipRegistration
import com.example.scholarhub.domain.scholarship.ScholarshipSection
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.launch

private val Navy = Color(0xFF001645)
private val Blue = Color(0xFF006398)
private val PaleBlue = Color(0xFFD9EEFB)
private val Background = Color(0xFFF1F1F1)
private val Outline = Color(0xFFF1F5F9)
private val Heading = Color(0xFF0F172A)
private val Body = Color(0xFF475569)
private val Muted = Color(0xFF64748B)
private val CardShape = RoundedCornerShape(16.dp)
private val SmallShape = RoundedCornerShape(12.dp)
private val Indonesian = Locale.forLanguageTag("id-ID")
private val DateFormat = DateTimeFormatter.ofPattern("d MMMM yyyy", Indonesian)
@OptIn(ExperimentalTextApi::class)
private val Nunito = FontFamily(
    listOf(FontWeight.Normal, FontWeight.Medium, FontWeight.SemiBold, FontWeight.Bold, FontWeight.ExtraBold)
        .map { weight ->
            Font(R.font.nunito_sans, weight = weight, variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)))
        },
)

private enum class DetailTab(val label: String) {
    Overview("Ringkasan"), Requirements("Persyaratan"), Guide("Panduan"),
}

/** Pass a relevance percentage only when opening this destination from a ScholarMatch result. */
@Preview
@Composable
fun ScholarshipDetailScreen(
    model: ScholarshipDetailViewModel,
    onBack: () -> Unit,
    scholarMatchRelevancePercent: Int? = null,
) {
    val state by model.uiState.collectAsState()
    ScholarshipDetailContent(state, onBack, model::load, scholarMatchRelevancePercent)
}

@Composable
private fun ScholarshipDetailContent(
    state: ScholarshipDetailState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    relevancePercent: Int?,
) {
    val uriHandler = LocalUriHandler.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val openLink: (String) -> Unit = { url ->
        try {
            uriHandler.openUri(url)
        } catch (_: IllegalArgumentException) {
            scope.launch { snackbar.showSnackbar("Tautan tidak dapat dibuka.") }
        }
    }
    val backdrop = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) rememberGraphicsLayer() else null
    Scaffold(
        containerColor = Background,
        topBar = { DetailHeader(onBack, backdrop) },
        bottomBar = { DetailFooter() },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        val direction = LocalLayoutDirection.current
        Box(
            Modifier.fillMaxSize()
                .then(
                    if (backdrop == null) Modifier else Modifier.drawWithContent {
                        backdrop.record {
                            drawRect(Background)
                            this@drawWithContent.drawContent()
                        }
                        drawLayer(backdrop)
                    },
                )
                .padding(
                    start = padding.calculateStartPadding(direction),
                    bottom = padding.calculateBottomPadding(),
                    end = padding.calculateEndPadding(direction),
                ),
        ) {
            when (state) {
                is ScholarshipDetailState.Ready -> {
                    val scholarship = state.scholarship
                    var tab by rememberSaveable(scholarship.id) { mutableStateOf(DetailTab.Overview) }
                    Column(
                        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                            .padding(
                                start = 22.dp, end = 22.dp,
                                top = padding.calculateTopPadding() + 16.dp, bottom = 16.dp,
                            ),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Hero(scholarship, relevancePercent)
                        DetailTabs(tab) { tab = it }
                        when (tab) {
                            DetailTab.Overview -> Overview(scholarship, openLink)
                            DetailTab.Requirements -> Sections(scholarship.requirements)
                            DetailTab.Guide -> {
                                Sections(scholarship.guide.sections)
                                scholarship.guide.officialGuideUrl?.let { url ->
                                    Portal("PANDUAN RESMI", url) { openLink(url) }
                                }
                            }
                        }
                    }
                }
                else -> Box(
                    Modifier.fillMaxSize().padding(top = padding.calculateTopPadding()),
                    contentAlignment = Alignment.Center,
                ) {
                    when (state) {
                        ScholarshipDetailState.Loading -> CircularProgressIndicator(color = Blue)
                        ScholarshipDetailState.NotFound -> DetailText("Beasiswa tidak ditemukan.")
                        ScholarshipDetailState.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            DetailText("Detail beasiswa gagal dimuat.")
                            TextButton(onClick = onRetry) { DetailText("Coba lagi", color = Blue) }
                        }
                        is ScholarshipDetailState.Ready -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailHeader(onBack: () -> Unit, backdrop: GraphicsLayer?) {
    Box(Modifier.fillMaxWidth().shadow(4.dp)) {
        if (backdrop != null) {
            Box(
                Modifier.matchParentSize().blur(12.dp).drawBehind {
                    drawLayer(backdrop)
                },
            )
        }
        Box(
            Modifier.fillMaxWidth().background(
                Background.copy(alpha = if (backdrop == null) 0.95f else 0.15f),
            ).statusBarsPadding().height(64.dp).padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Asset(R.drawable.detail_back, 16, 16, "Kembali")
            }
            DetailText("Detail Beasiswa", 19, FontWeight.Bold, Navy)
            Box(Modifier.align(Alignment.CenterEnd).size(40.dp), contentAlignment = Alignment.Center) {
                Asset(R.drawable.detail_bookmark, 20, 20)
            }
        }
    }
}

@Composable
private fun DetailFooter() {
    Row(
        Modifier.fillMaxWidth().shadow(4.dp).background(Color(0xFFFBF9F8))
            .navigationBarsPadding().height(80.dp).padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FooterItem("Premium", R.drawable.detail_premium, 16, 21)
        FooterItem("Mentor", R.drawable.detail_mentor, 20, 18)
        FooterItem("Home", R.drawable.detail_home, 15, 17)
        FooterItem("Beasiswa", R.drawable.detail_scholarship, 22, 18)
        FooterItem("Profil", R.drawable.detail_profile, 16, 16)
    }
}

@Composable
private fun FooterItem(label: String, @DrawableRes icon: Int, width: Int, height: Int) {
    val muted = Color(0xFF757681)
    Column(Modifier.width(64.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.height(28.dp), contentAlignment = Alignment.Center) {
            if (label == "Home") {
                Box(
                    Modifier.offset(y = (-6).dp).requiredSize(40.dp).background(muted, CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center,
                ) { Asset(icon, width, height) }
            } else {
                Asset(icon, width, height)
            }
        }
        DetailText(label, 10, FontWeight.ExtraBold, if (label == "Beasiswa") Navy else muted)
    }
}

@Composable
private fun Hero(scholarship: Scholarship, relevancePercent: Int?) {
    val registration = scholarship.registration
    val status = registrationStatus(registration)
    Column(
        Modifier.fillMaxWidth().shadow(2.dp, CardShape).clip(CardShape)
            .background(Brush.horizontalGradient(listOf(Color.White, Color(0xFFEAF6FC), Color.White)))
            .border(1.dp, Outline, CardShape).padding(21.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(80.dp).background(Navy, SmallShape).border(1.dp, Blue, SmallShape))
            relevancePercent?.let { Badge("$it% Relevan", Color.White, Blue) }
        }
        Column(Modifier.padding(top = 4.dp)) {
            DetailText(scholarship.name, 19, FontWeight.Bold, Color(0xFF0A2540))
            DetailText(scholarship.provider, color = Muted)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Badge(status.label, status.foreground, status.background)
            val deadline = registration.closesOn?.format(DateFormat)
            if (deadline != null) {
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically) {
                    Asset(R.drawable.detail_calendar, 16, 16)
                    val time = registration.closesAtLocal?.let { " • $it" }.orEmpty()
                    DetailText("Tutup $deadline$time", 11, color = Blue)
                }
            }
        }
    }
}

private enum class RegistrationStatus(val label: String, val foreground: Color, val background: Color) {
    Open("Dibuka", Color(0xFF17663A), Color(0xFFD8F3E1)),
    Closed("Ditutup", Color(0xFFBA1A1A), Color(0xFFFFB0B1)),
    Upcoming("Belum dibuka", Navy, PaleBlue),
    Unknown("Jadwal belum tersedia", Body, Outline),
}

private fun registrationStatus(registration: ScholarshipRegistration): RegistrationStatus {
    val now = ZonedDateTime.now(registration.timeZone)
    val today = now.toLocalDate()
    val close = registration.closesOn
    val closeTime = registration.closesAtLocal
    return when {
        registration.statusAtSnapshot == "archived" -> RegistrationStatus.Closed
        close != null && today > close -> RegistrationStatus.Closed
        today == close && closeTime != null &&
            now.toLocalTime().truncatedTo(ChronoUnit.MINUTES) > closeTime -> RegistrationStatus.Closed
        registration.opensOn?.let { today < it } == true -> RegistrationStatus.Upcoming
        registration.opensOn != null || close != null -> RegistrationStatus.Open
        else -> RegistrationStatus.Unknown
    }
}

@Composable
private fun Badge(label: String, foreground: Color, background: Color) {
    Box(Modifier.background(background, SmallShape).padding(horizontal = 8.dp, vertical = 4.dp)) {
        DetailText(label, 11, color = foreground)
    }
}

@Composable
private fun DetailTabs(selected: DetailTab, onSelect: (DetailTab) -> Unit) {
    Row(Modifier.fillMaxWidth().background(Color.White, SmallShape).selectableGroup()) {
        for (tab in DetailTab.entries) {
            val active = tab == selected
            Box(
                Modifier.weight(1f).clip(SmallShape).background(if (active) Navy else Color.White)
                    .selectable(active, role = Role.Tab, onClick = { onSelect(tab) })
                    .heightIn(min = 36.dp).padding(horizontal = 4.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                DetailText(tab.label, 13, FontWeight.Medium, if (active) PaleBlue else Navy)
            }
        }
    }
}

@Composable
private fun Overview(scholarship: Scholarship, openLink: (String) -> Unit) {
    val summary = scholarship.summary
    scholarship.registration.applicationUrl?.let { url ->
        Portal("PORTAL RESMI PENDAFTARAN", url) { openLink(url) }
    }
    if (summary.about.isNotBlank()) {
        DetailCard("Tentang Program", verticalPadding = 21) { DetailText(summary.about) }
    }
    if (summary.benefits.isNotEmpty()) {
        DetailCard("Manfaat") {
            for (benefit in summary.benefits) BenefitCard(benefit)
        }
    }
    val coverage = summary.coverage.filter(String::isNotBlank)
    if (coverage.isNotEmpty()) {
        DetailCard("Cakupan Program") { DetailLines(coverage) }
    }
}

@Composable
private fun Portal(label: String, url: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(CardShape).background(Blue).clickable(role = Role.Button, onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            DetailText(label, 13, FontWeight.Bold, Color.White)
            DetailText(url.substringAfter("://").trimEnd('/'), 11, color = PaleBlue, maxLines = 2)
        }
        Row(
            Modifier.background(Color.White, SmallShape).padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DetailText("Buka Tautan", 11, FontWeight.Bold, Blue)
            Asset(R.drawable.detail_external, 11, 11)
        }
    }
}

@Composable
private fun BenefitCard(benefit: ScholarshipBenefit) {
    Column(
        Modifier.fillMaxWidth().background(PaleBlue, SmallShape).border(1.dp, Outline, SmallShape)
            .padding(13.dp),
    ) {
        DetailText(benefit.title, 16, FontWeight.Bold, Navy)
        val amount = benefit.amountIdr?.let {
            val prefix = if (benefit.amountKind == "maximum") "Hingga Rp" else "Rp"
            prefix + NumberFormat.getNumberInstance(Indonesian).format(it)
        } ?: "Nominal belum tersedia"
        DetailText(listOfNotNull(amount, benefit.unitOrCoverage).joinToString(" / "), color = Navy)
        benefit.duration?.let { DetailText(it, color = Navy) }
        benefit.description?.let { DetailText(it, color = Navy) }
    }
}

@Composable
private fun Sections(sections: List<ScholarshipSection>) {
    for (section in sections.filter { it.body.isNotBlank() || it.items.isNotEmpty() }) {
        DetailCard(section.title) {
            when (section.key) {
                "educationAcademics" -> DetailLines(section.body.lines())
                "documents" -> {
                    if (section.body.isNotBlank()) DetailText(section.body)
                    for (document in section.items) {
                        Row(
                            Modifier.fillMaxWidth().background(PaleBlue, SmallShape)
                                .border(1.dp, Outline, SmallShape).padding(13.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Asset(R.drawable.detail_document, 24, 24)
                            DetailText(document, color = Navy)
                        }
                    }
                }
                else -> DetailText(section.body)
            }
        }
    }
}

@Composable
private fun DetailLines(lines: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        for (line in lines.filter(String::isNotBlank)) {
            val fields = line.split(": ", limit = 2)
            if (fields.size == 2) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DetailText(fields[0], 12, FontWeight.Bold, Navy, modifier = Modifier.weight(0.4f))
                    DetailText(fields[1], 12, FontWeight.Normal, Body, modifier = Modifier.weight(0.6f))
                }
            } else {
                DetailText(line)
            }
        }
    }
}

@Composable
private fun DetailCard(title: String, verticalPadding: Int = 17, content: @Composable () -> Unit) {
    Column(
        Modifier.fillMaxWidth().shadow(2.dp, CardShape).background(Color.White, CardShape)
            .border(1.dp, Outline, CardShape).padding(horizontal = 21.dp, vertical = verticalPadding.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        DetailText(title, 19, FontWeight.Bold, Heading)
        content()
    }
}

@Composable
private fun Asset(@DrawableRes resource: Int, width: Int, height: Int, description: String? = null) {
    Image(painterResource(resource), description, Modifier.size(width.dp, height.dp))
}

@Composable
private fun DetailText(
    text: String,
    size: Int = 13,
    weight: FontWeight = FontWeight.SemiBold,
    color: Color = Body,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
) {
    Text(
        text, modifier = modifier, color = color, maxLines = maxLines, overflow = TextOverflow.Ellipsis,
        style = TextStyle(fontFamily = Nunito, fontWeight = weight, fontSize = size.sp,
            lineHeight = (size * 1.5f).sp),
    )
}

@Preview(name = "Detail Hana Bank", widthDp = 402, heightDp = 874, showSystemUi = true)
@Composable
private fun ScholarshipDetailPreview() {
    val assets = LocalContext.current.assets
    val state = remember(assets) {
        val json = assets.open("scholarships.json").bufferedReader().use { it.readText() }
        val scholarship = ScholarshipJsonParser.parse(json).scholarships.first { it.id == "SH28" }
        ScholarshipDetailState.Ready(scholarship)
    }
    ScholarshipDetailContent(state, onBack = {}, onRetry = {}, relevancePercent = null)
}
