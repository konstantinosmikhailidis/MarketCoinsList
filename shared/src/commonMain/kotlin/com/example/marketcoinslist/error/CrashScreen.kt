package com.example.marketcoinslist.error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import marketcoinslist.shared.generated.resources.Res
import marketcoinslist.shared.generated.resources.crash_text
import marketcoinslist.shared.generated.resources.got_to_main_window
import marketcoinslist.shared.generated.resources.ic_error
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CrashScreen(clickCallback: () -> Unit) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                painter = painterResource(Res.drawable.ic_error),
                contentDescription = stringResource(Res.string.crash_text)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.crash_text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                style = typography.headlineMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 360.dp),
                colors = ButtonDefaults.buttonColors(Color.Blue),
                onClick = clickCallback,
                shape = RoundedCornerShape(20)
            ) {
                Text(
                    text = stringResource(Res.string.got_to_main_window),
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    style = typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}