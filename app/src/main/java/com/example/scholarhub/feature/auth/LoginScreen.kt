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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scholarhub.R
import com.example.scholarhub.feature.auth.components.AuthHeader
import com.example.scholarhub.feature.auth.components.AuthInputField
import com.example.scholarhub.ui.theme.NunitoFontFamily

private val BackgroundColor = Color(0xFFF1F1F1)
private val NavyDark = Color(0xFF0F2A66)
private val Navy = Color(0xFF001645)
private val Blue = Color(0xFF006398)
private val LabelColor = Color(0xFF141D21)
private val MutedText = Color(0xFF444650)
private val DividerColor = Color(0xFFE0E9EF)
private val SwitcherBg = Color(0xFFE0E9EF)

@Composable
fun LoginScreen(
    onLoginSuccess: (email: String, isMentor: Boolean) -> Unit = { _, _ -> },
    onNavigateToRegister: (role: String) -> Unit = {},
) {
    var selectedRole by rememberSaveable { mutableStateOf(AuthRole.Mahasiswa) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val isMentor = selectedRole == AuthRole.Mentor

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            AuthHeader(badgeText = if (isMentor) "MENTOR" else "LOGIN")
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Mascot illustration standing directly on background
            Image(
                painter = painterResource(R.drawable.auth_mascot),
                contentDescription = "ScholarHub Mascot",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .padding(vertical = 4.dp),
                contentScale = ContentScale.Fit,
            )

            Spacer(Modifier.height(8.dp))

            // Main Login Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color(0x140F2A66),
                        ambientColor = Color(0x140F2A66),
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Role Switcher inside Login Card for easy testing
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(100.dp))
                        .background(SwitcherBg)
                        .padding(4.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        // Mahasiswa Tab
                        val isMahasiswaActive = selectedRole == AuthRole.Mahasiswa
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .then(
                                    if (isMahasiswaActive) {
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
                                text = "Akun Mahasiswa",
                                style = TextStyle(
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = if (isMahasiswaActive) Color.White else MutedText,
                                ),
                            )
                        }

                        // Mentor Tab
                        val isMentorActive = selectedRole == AuthRole.Mentor
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .then(
                                    if (isMentorActive) {
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
                                text = "Akun Mentor",
                                style = TextStyle(
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = if (isMentorActive) Color.White else MutedText,
                                ),
                            )
                        }
                    }
                }

                // Email field
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = if (isMentor) "Email Mentor" else "Email",
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp,
                            color = LabelColor,
                        ),
                    )
                    AuthInputField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = if (isMentor) "mentor@scholarhub.com" else "Masukkan email Anda",
                        leadingIcon = R.drawable.ic_auth_email,
                        keyboardType = KeyboardType.Email,
                        cornerRadius = 12.dp,
                    )
                }

                // Password field
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Kata Sandi",
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp,
                            color = LabelColor,
                        ),
                    )
                    AuthInputField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Masukkan kata sandi akun",
                        leadingIcon = R.drawable.ic_auth_lock,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        cornerRadius = 12.dp,
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

                // CTA Button: Masuk ke Akun
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp), spotColor = Color(0x330F2A66))
                        .clip(RoundedCornerShape(12.dp))
                        .background(NavyDark)
                        .clickable { onLoginSuccess(email, isMentor || email.contains("mentor", ignoreCase = true)) },
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = if (isMentor) "Masuk ke Akun Mentor" else "Masuk ke Akun",
                            style = TextStyle(
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
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

                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(DividerColor),
                )
            }

            Spacer(Modifier.height(16.dp))

            // Footer / Registration Switcher
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Belum punya akun ScholarHub? ",
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.5.sp,
                            color = MutedText,
                        ),
                    )
                    Text(
                        text = "Daftar Sekarang:",
                        style = TextStyle(
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp,
                            color = Navy,
                        ),
                    )
                }

                // Two button options: Daftar Mahasiswa & Gabung Mentor
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    // Daftar Mahasiswa Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .shadow(elevation = 1.dp, shape = RoundedCornerShape(12.dp), spotColor = Color(0x1A000000))
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable { onNavigateToRegister("mahasiswa") }
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_auth_student),
                                contentDescription = null,
                                modifier = Modifier.size(width = 15.dp, height = 11.dp),
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Daftar Mahasiswa",
                                style = TextStyle(
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp,
                                    color = Navy,
                                ),
                            )
                        }
                    }

                    // Gabung Mentor Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .shadow(elevation = 1.dp, shape = RoundedCornerShape(12.dp), spotColor = Color(0x1A000000))
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .clickable { onNavigateToRegister("mentor") }
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_auth_mentor),
                                contentDescription = null,
                                modifier = Modifier.size(width = 15.dp, height = 14.dp),
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Gabung Mentor",
                                style = TextStyle(
                                    fontFamily = NunitoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp,
                                    color = Navy,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 402, heightDp = 862)
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}
