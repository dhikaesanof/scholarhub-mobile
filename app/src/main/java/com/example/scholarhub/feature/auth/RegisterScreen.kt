package com.example.scholarhub.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scholarhub.R
import com.example.scholarhub.feature.auth.components.AuthHeader
import com.example.scholarhub.feature.auth.components.AuthInputField
import com.example.scholarhub.ui.theme.NunitoFontFamily

enum class AuthRole(val label: String) {
    Mahasiswa("Mahasiswa"),
    Mentor("Mentor"),
}

private val BackgroundColor = Color(0xFFF1F1F1)
private val NavyDark = Color(0xFF0F2A66)
private val Navy = Color(0xFF001645)
private val Blue = Color(0xFF006398)
private val MutedText = Color(0xFF444650)
private val SwitcherBg = Color(0xFFE0E9EF)

@Composable
fun RegisterScreen(
    initialRole: AuthRole = AuthRole.Mahasiswa,
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
) {
    var selectedRole by rememberSaveable { mutableStateOf(initialRole) }
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var university by rememberSaveable { mutableStateOf("") }
    var scholarshipAwardee by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = { AuthHeader(badgeText = "REGISTER") },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // Header Intro Text
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = "Mulai Perjalananmu!",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp,
                        color = Navy,
                        textAlign = TextAlign.Center,
                    ),
                )
                Text(
                    text = "Daftar akun ScholarHub untuk eksplorasi beasiswa\nimpian dan terhubung dengan mentor terbaik.",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = MutedText,
                        textAlign = TextAlign.Center,
                    ),
                )
            }

            // Sleek Role Switcher
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .background(SwitcherBg)
                    .padding(6.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    // Mahasiswa Button
                    val isMahasiswa = selectedRole == AuthRole.Mahasiswa
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .then(
                                if (isMahasiswa) {
                                    Modifier
                                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(100.dp))
                                        .clip(RoundedCornerShape(100.dp))
                                        .background(Navy)
                                } else {
                                    Modifier
                                        .clip(RoundedCornerShape(100.dp))
                                        .clickable { selectedRole = AuthRole.Mahasiswa }
                                },
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Mahasiswa",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.5.sp,
                                color = if (isMahasiswa) Color.White else MutedText,
                            ),
                        )
                    }

                    // Mentor Button
                    val isMentor = selectedRole == AuthRole.Mentor
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .then(
                                if (isMentor) {
                                    Modifier
                                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(100.dp))
                                        .clip(RoundedCornerShape(100.dp))
                                        .background(Navy)
                                } else {
                                    Modifier
                                        .clip(RoundedCornerShape(100.dp))
                                        .clickable { selectedRole = AuthRole.Mentor }
                                },
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Mentor",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.5.sp,
                                color = if (isMentor) Color.White else MutedText,
                            ),
                        )
                    }
                }
            }

            // Main Registration Form Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp), spotColor = Color(0x1F000000))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Field 1: Nama Lengkap
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Nama Lengkap",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Navy,
                            ),
                        )
                        Text(
                            text = "Wajib",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.5.sp,
                                color = Blue,
                            ),
                        )
                    }
                    AuthInputField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = "Contoh: Sarah Azhari",
                        leadingIcon = R.drawable.ic_auth_user,
                        cornerRadius = 8.dp,
                    )
                }

                // Field 2: Email
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Email",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Navy,
                            ),
                        )
                        Text(
                            text = "Wajib",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.5.sp,
                                color = Blue,
                            ),
                        )
                    }
                    AuthInputField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "example@gmail.com",
                        leadingIcon = R.drawable.ic_auth_email,
                        keyboardType = KeyboardType.Email,
                        cornerRadius = 8.dp,
                    )
                }

                // Field 3: Universitas / Institusi Asal
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Universitas / Institusi Asal",
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Navy,
                        ),
                    )
                    AuthInputField(
                        value = university,
                        onValueChange = { university = it },
                        placeholder = "Pilih perguruan tinggi asal",
                        leadingIcon = R.drawable.ic_auth_school,
                        cornerRadius = 8.dp,
                        trailingIcon = {
                            Image(
                                painter = painterResource(R.drawable.ic_chevron_down),
                                contentDescription = null,
                                modifier = Modifier.size(width = 10.dp, height = 7.dp),
                                colorFilter = ColorFilter.tint(Color(0xFF757681)),
                            )
                        },
                    )
                }

                // Field 4: Awardee Beasiswa (Tampil jika role Mentor)
                if (selectedRole == AuthRole.Mentor) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Awardee Beasiswa",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Navy,
                            ),
                        )
                        AuthInputField(
                            value = scholarshipAwardee,
                            onValueChange = { scholarshipAwardee = it },
                            placeholder = "Pilih awardee Beasiswa anda",
                            leadingIcon = R.drawable.ic_auth_mentor,
                            cornerRadius = 8.dp,
                            trailingIcon = {
                                Image(
                                    painter = painterResource(R.drawable.ic_chevron_down),
                                    contentDescription = null,
                                    modifier = Modifier.size(width = 10.dp, height = 7.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFF757681)),
                                )
                            },
                        )
                    }
                }

                // Field 5: Kata Sandi
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Kata Sandi",
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Navy,
                        ),
                    )
                    AuthInputField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Masukkan kata sandi",
                        leadingIcon = R.drawable.ic_auth_lock,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        cornerRadius = 8.dp,
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.size(24.dp),
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_auth_eye),
                                    contentDescription = if (passwordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi",
                                    modifier = Modifier.size(18.dp),
                                    colorFilter = ColorFilter.tint(
                                        if (passwordVisible) Blue else Color(0xFF757681),
                                    ),
                                )
                            }
                        },
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Primary Action CTA Button (Capsule / Pill)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(100.dp), spotColor = Color(0x330F2A66))
                        .clip(RoundedCornerShape(100.dp))
                        .background(NavyDark)
                        .clickable { onRegisterSuccess() },
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Daftar Akun Sekarang",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.5.sp,
                                color = Color.White,
                            ),
                        )
                        Spacer(Modifier.width(8.dp))
                        Image(
                            painter = painterResource(R.drawable.ic_arrow_forward),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
            }

            // Footer Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Sudah punya akun ScholarHub? ",
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = MutedText,
                    ),
                )
                Text(
                    text = "Masuk ke Akun",
                    modifier = Modifier.clickable { onNavigateToLogin() },
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Blue,
                    ),
                )
            }
        }
    }
}

@Preview(name = "Register Mahasiswa", showBackground = true, widthDp = 402, heightDp = 862)
@Composable
private fun RegisterMahasiswaPreview() {
    RegisterScreen(initialRole = AuthRole.Mahasiswa)
}

@Preview(name = "Register Mentor", showBackground = true, widthDp = 402, heightDp = 970)
@Composable
private fun RegisterMentorPreview() {
    RegisterScreen(initialRole = AuthRole.Mentor)
}
