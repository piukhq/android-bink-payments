package com.bink.libraries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.bink.libraries.ui.theme.LibrariesTheme
import com.bink.payments.BinkPayments
import com.bink.payments.screens.BinkPaymentsOptions


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BinkPayments.init(
            userToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6ImFjY2Vzcy1zZWNyZXQtMiJ9.eyJzdWIiOjM4MjgzLCJjaGFubmVsIjoiY29tLmJpbmsud2FsbGV0IiwiaWF0IjoxNjQ2NjQ3NDY0LCJleHAiOjE2NzgxODM0NjR9.yiF0v2Ufzj4eMTCyaR-q6NiX2KUUqSTe59OkUY5mM_Rdj1SWLv5rOw8h2ixgKJ_7JpZD-14qSu37-25UTKwSbQ",
            spreedlyEnvironmentKey = "1Lf7DiKgkcx5Anw7QxWdDxaKtTa",
            isDebug = true
        )

        setContent {
            LibrariesTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
                    Button(onClick = {
                        BinkPayments.startCardEntry(
                            this,
                            BinkPaymentsOptions()
                        )
                    }) {
                        Text(text = "Launch Bink Payments")
                    }
                }
            }
        }
    }

}