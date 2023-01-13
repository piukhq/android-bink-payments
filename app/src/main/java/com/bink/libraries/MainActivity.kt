package com.bink.libraries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bink.libraries.ui.theme.LibrariesTheme
import com.bink.payments.BinkPayments

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        BinkPayments.init(
            userToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6ImFjY2Vzcy1zZWNyZXQtMiJ9.eyJzdWIiOjM4MjgzLCJjaGFubmVsIjoiY29tLmJpbmsud2FsbGV0IiwiaWF0IjoxNjUxMTUyOTU5LCJleHAiOjE2ODI2ODg5NTl9.mvcKT3eALLCOENFIWl39Zo6t5Jux8RVuMH0-nawnjNPjv5tGALlpM6-gNcPtdXEB6_ZL_uJAmaJZNT4h1V-yYw",
            spreedlyEnvironmentKey = "1Lf7DiKgkcx5Anw7QxWdDxaKtTa",
            isDebug = true)


        setContent {
            LibrariesTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            modifier = Modifier
                                .width(200.dp)
                                .height(100.dp),
                            onClick = {
                                BinkPayments.startCardEntry(this@MainActivity)
                            }) {
                            Text(text = "Launch Bink Payments")
                        }

                        Spacer(modifier = Modifier.size(10.dp))

                        Button(
                            modifier = Modifier
                                .width(200.dp)
                                .height(100.dp),
                            onClick = {
                                BinkPayments.getPLLStatus(this@MainActivity)
                            }) {
                            Text(text = "Get PLL Status")
                        }
                    }

                }
            }
        }
    }
}