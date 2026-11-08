package com.example.scholarhub.feature.mentor

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scholarhub.R
import com.example.scholarhub.feature.auth.components.AuthHeader
import com.example.scholarhub.ui.theme.NunitoFontFamily

private val BackgroundColor = Color(0xFFF1F1F1)
private val Navy = Color(0xFF001645)
private val NavyDark = Color(0xFF0F2A66)
private val Blue = Color(0xFF006398)
private val MutedText = Color(0xFF444650)
private val StatCircleBg = Color(0xFFE0E9EF)
private val LogoutRed = Color(0xFFBA1A1A)

enum class MentorNavTab {
    Schedule,
    Home,
    Profile,
}

@Composable
fun ProfileMentorScreen(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {},
) {
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            AuthHeader(
                badgeText = "MENTOR",
                onBack = onBack,
            )
        },
        bottomBar = {
            MentorBottomBar(
                currentTab = MentorNavTab.Profile,
                onTabSelect = { tab ->
                    if (tab == MentorNavTab.Schedule) {
                        onNavigateToSchedule()
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            // 1. Top Profile Card
            TopProfileCard()

            // 2. Highlight Section: Dompet & Saldo
            WalletHighlightCard()

            // 3. Statistik Ringkas Mentor
            MentorStatsRow()

            // 4. Riwayat Penarikan Terakhir
            WithdrawalHistorySection()

            // 5. Pengaturan Layanan Mentor
            MentorSettingsSection()

            // 6. Tombol Logout
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFECF5FB))
                    .clickable(onClick = onLogout),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_mentor_logout),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(LogoutRed),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Keluar dari Akun Mentor",
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = LogoutRed,
                        ),
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TopProfileCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(14.dp), spotColor = Color(0x14000000))
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(16.dp),
    ) {
        // Decorative top right accent
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = 40.dp, y = (-40).dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Color(0x33CCE5FF)),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Mentor Photo
            Image(
                painter = painterResource(R.drawable.mentor_avatar),
                contentDescription = "Avatar Mentor",
                modifier = Modifier
                    .size(width = 72.dp, height = 70.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, Blue.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop,
            )

            // Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Samantha Dira, S.Pd",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = Navy,
                    ),
                )
                Text(
                    text = "Awardee LPDP University of Oxford • Top Rated Mentor",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = MutedText,
                    ),
                )
            }
        }
    }
}

@Composable
private fun WalletHighlightCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(14.dp), spotColor = Color(0x26001645))
            .clip(RoundedCornerShape(14.dp))
            .background(Navy)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Label Saldo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_mentor_wallet),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(Color(0xFFDBE1FF)),
            )
            Text(
                text = "SALDO PENGHASILAN TERSEDIA",
                style = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.5.sp,
                    color = Color(0xFFDBE1FF),
                    letterSpacing = 0.5.sp,
                ),
            )
        }

        // Saldo Nominal
        Text(
            text = "Rp 450.000",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 38.sp,
                color = Color.White,
            ),
        )

        // Bank Details Inset
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF0F2A66))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_mentor_bank),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        colorFilter = ColorFilter.tint(Navy),
                    )
                }
                Column {
                    Text(
                        text = "BCA • 8271****90",
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp,
                            color = Color.White,
                        ),
                    )
                    Text(
                        text = "a.n. Sarah Aurelia",
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.5.sp,
                            color = Color(0xFFB3C5FF),
                        ),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(0x1AFFFFFF))
                    .clickable { }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Ubah Rekening",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp,
                        color = Color.White,
                    ),
                )
            }
        }

        // CTA Tarik Fee
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .shadow(elevation = 3.dp, shape = RoundedCornerShape(100.dp))
                .clip(RoundedCornerShape(100.dp))
                .background(Blue)
                .clickable { },
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_mentor_money_out),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(Color.White),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Tarik Fee Sekarang",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White,
                    ),
                )
            }
        }
    }
}

@Composable
private fun MentorStatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        StatCard(
            iconRes = R.drawable.ic_stat_cap,
            value = "36",
            label = "Sesi Selesai",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            iconRes = R.drawable.ic_stat_shield,
            value = "98%",
            label = "Kehadiran Tepat",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            iconRes = R.drawable.ic_stat_fee,
            value = "Rp 1.4jt",
            label = "Total Fee\nMentoring",
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatCard(
    @DrawableRes iconRes: Int,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp), spotColor = Color(0x14000000))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(StatCircleBg),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(Navy),
            )
        }
        Text(
            text = value,
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = Navy,
            ),
        )
        Text(
            text = label,
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                lineHeight = 13.sp,
                color = MutedText,
                textAlign = TextAlign.Center,
            ),
        )
    }
}

@Composable
private fun WithdrawalHistorySection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_mentor_history),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(Navy),
                )
                Text(
                    text = "Riwayat Penarikan Terakhir",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.5.sp,
                        color = Navy,
                    ),
                )
            }
            Text(
                text = "Lihat Semua →",
                style = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Blue,
                ),
            )
        }

        HistoryCard(
            date = "15 Okt 2026 • BCA Transfer",
            amount = "Rp 200.000",
        )
        HistoryCard(
            date = "01 Okt 2026 • BCA Transfer",
            amount = "Rp 750.000",
        )
    }
}

@Composable
private fun HistoryCard(date: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp), spotColor = Color(0x14000000))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD9EEFB)),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_mentor_check),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(Blue),
                )
            }
            Column {
                Text(
                    text = "Penarikan Berhasil",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Navy,
                    ),
                )
                Text(
                    text = date,
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = MutedText,
                    ),
                )
            }
        }

        Text(
            text = amount,
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Navy,
            ),
        )
    }
}

@Composable
private fun MentorSettingsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Pengaturan Layanan Mentor",
            style = TextStyle(
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.5.sp,
                color = Navy,
            ),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(14.dp), spotColor = Color(0x14000000))
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(vertical = 6.dp),
        ) {
            SettingRow(
                iconRes = R.drawable.ic_sett_price,
                title = "Tarif Sesi Bimbingan",
                subtitle = "Atur tarif per sesi (60 menit) online",
                trailingText = "Rp 50k/jam",
            )
            SettingRow(
                iconRes = R.drawable.ic_sett_vault,
                title = "Brankas Materi & Template Mentoring",
                subtitle = "Panduan CV, Essay, dan Mock Interview",
            )
            SettingRow(
                iconRes = R.drawable.ic_sett_sec,
                title = "Keamanan & Rekening Pencairan",
                subtitle = "PIN penarikan, 2FA, dan rekening bank aktif",
            )
            SettingRow(
                iconRes = R.drawable.ic_sett_cs,
                title = "Bantuan Mentor & CS VIP",
                subtitle = "Respon prioritas penanganan kendala sesi",
            )
        }
    }
}

@Composable
private fun SettingRow(
    @DrawableRes iconRes: Int,
    title: String,
    subtitle: String,
    trailingText: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE8F1F8)),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(Navy),
                )
            }
            Column {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.5.sp,
                        color = Navy,
                    ),
                )
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.5.sp,
                        color = MutedText,
                    ),
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (trailingText != null) {
                Text(
                    text = trailingText,
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Blue,
                    ),
                )
            }
            Image(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                colorFilter = ColorFilter.tint(Color(0xFF757681)),
            )
        }
    }
}

@Composable
fun MentorBottomBar(
    currentTab: MentorNavTab,
    onTabSelect: (MentorNavTab) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp)
            .background(Color.White)
            .height(72.dp)
            .padding(horizontal = 32.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Tab 1: Jadwal Sesi
            Column(
                modifier = Modifier
                    .clickable { onTabSelect(MentorNavTab.Schedule) }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_nav_calendar),
                    contentDescription = "Jadwal Sesi",
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(
                        if (currentTab == MentorNavTab.Schedule) Navy else Color(0xFF757681),
                    ),
                )
                Text(
                    text = "Jadwal Sesi",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = if (currentTab == MentorNavTab.Schedule) Navy else Color(0xFF757681),
                    ),
                )
            }

            // Tab 2: Home
            Column(
                modifier = Modifier
                    .clickable { onTabSelect(MentorNavTab.Home) }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF757681)),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_nav_home),
                        contentDescription = "Home",
                        modifier = Modifier.size(18.dp),
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                }
                Text(
                    text = "Home",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = Color(0xFF757681),
                    ),
                )
            }

            // Tab 3: Profil
            Column(
                modifier = Modifier
                    .clickable { onTabSelect(MentorNavTab.Profile) }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_nav_profile),
                    contentDescription = "Profil",
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(
                        if (currentTab == MentorNavTab.Profile) Navy else Color(0xFF757681),
                    ),
                )
                Text(
                    text = "Profil",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = if (currentTab == MentorNavTab.Profile) Navy else Color(0xFF757681),
                    ),
                )
            }
        }
    }
}

@Preview(widthDp = 402, heightDp = 1482)
@Composable
private fun ProfileMentorPreview() {
    ProfileMentorScreen()
}
