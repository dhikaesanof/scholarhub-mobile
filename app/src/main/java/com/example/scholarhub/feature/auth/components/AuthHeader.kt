package com.example.scholarhub.feature.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scholarhub.R
import com.example.scholarhub.ui.theme.NunitoFontFamily

private val Navy = Color(0xFF001645)
private val BadgeBg = Color(0xFFCCE5FF)
private val BadgeText = Color(0xFF001D31)
private val HeaderBg = Color(0xFFF1F1F1)

@Composable
fun AuthHeader(
    badgeText: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                spotColor = Color(0x1F000000),
                ambientColor = Color(0x1F000000),
            )
            .background(HeaderBg)
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 16.dp),
    ) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.detail_back),
                    contentDescription = "Kembali",
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            // Circle logo container
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Navy, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_scholarhub_logo),
                    contentDescription = "ScholarHub Logo",
                    modifier = Modifier.size(width = 13.dp, height = 14.dp),
                )
            }

            Spacer(Modifier.width(8.dp))

            // App Name
            Text(
                text = "ScholarHub",
                style = TextStyle(
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = Navy,
                ),
            )

            Spacer(Modifier.width(8.dp))

            // Badge (LOGIN / REGISTER / MENTOR)
            Box(
                modifier = Modifier
                    .background(BadgeBg, CircleShape)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = badgeText.uppercase(),
                    style = TextStyle(
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = BadgeText,
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun AuthHeaderPreview() {
    AuthHeader(badgeText = "LOGIN", onBack = {})
}
