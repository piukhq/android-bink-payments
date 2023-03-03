package com.bink.libraries

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bink.libraries.ui.theme.LibrariesTheme
import com.bink.payments.BinkPayments
import com.bink.payments.model.wallet.Configuration
import com.bink.payments.model.wallet.CredentialType

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BinkPayments.init(
            context = this,
            userToken = "eyJhbGciOiJIUzUxMiIsImtpZCI6ImFjY2Vzcy1zZWNyZXQtMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjEyNDc1MywiY2hhbm5lbCI6ImNvbS50cnVzdGVkLmJpbmsud2FsbGV0IiwiaXNfdGVzdGVyIjpmYWxzZSwiaXNfdHJ1c3RlZF9jaGFubmVsIjp0cnVlLCJpYXQiOjE2NzUzMzY2MjQsImV4cCI6MTY3NTMzODQyNH0.bx0eVbrfROHIiXtqURSyTfC6v7eS4jHcxuPAHeLN9gwWIOf2tp-esBwy2X28rbvW-iv5sFAcmHLGsl90iyRGKQ",
            spreedlyEnvironmentKey = "1Lf7DiKgkcx5Anw7QxWdDxaKtTa",
            configuration = Configuration(0, 0, CredentialType.ADD),
            isDebug = true)

        setContent {
            LibrariesTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            modifier = Modifier
                                .width(200.dp)
                                .height(100.dp),
                            onClick = {
                                BinkPayments.startCardEntry(this@MainActivity)
                            }) {
                            Text(text = "Add Payment Card")
                        }

                        Spacer(modifier = Modifier.size(10.dp))

                        Button(
                            modifier = Modifier
                                .width(200.dp)
                                .height(100.dp),
                            onClick = {
                                Toast.makeText(this@MainActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
                            }) {
                            Text(text = "Show Payment Cards")
                        }

                        Spacer(modifier = Modifier.size(10.dp))

                        Button(
                            modifier = Modifier
                                .width(200.dp)
                                .height(100.dp),
                            onClick = {
                                BinkPayments.setTrustedLoyaltyCard(this@MainActivity, "Z99783494A", "jbest@bink.com") { exception ->
                                    if (exception != null) {
                                        Toast.makeText(this@MainActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this@MainActivity, "Set trusted card", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }) {
                            Text(text = "Set Trusted Card")
                        }

                        Spacer(modifier = Modifier.size(10.dp))

                        Button(
                            modifier = Modifier
                                .width(200.dp)
                                .height(100.dp),
                            onClick = {
                                BinkPayments.replaceTrustedLoyaltyCard(this@MainActivity, 238, "Z99783494A", "jbest@bink.com") { exception ->
                                    if (exception != null) {
                                        Toast.makeText(this@MainActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this@MainActivity, "Replace trusted card", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }) {
                            Text(text = "Replace Trusted Card")
                        }

                        Spacer(modifier = Modifier.size(10.dp))

                        Button(
                            modifier = Modifier
                                .width(200.dp)
                                .height(100.dp),
                            onClick = {
                                Toast.makeText(this@MainActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
                            }) {
                            Text(text = "Show Loyalty Card")
                        }

                        Spacer(modifier = Modifier.size(10.dp))


                        Button(
                            modifier = Modifier
                                .width(200.dp)
                                .height(100.dp),
                            onClick = {
                                BinkPayments.getWallet(this@MainActivity) {
                                    BinkPayments.getPLLStatus(this@MainActivity) { pllState, exception ->
                                        if (exception != null) {
                                            Toast.makeText(this@MainActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this@MainActivity, "Linked cards ${pllState?.linked?.size}, Unlinked Cards ${pllState?.unlinked?.size}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }

                            }) {
                            Text(text = "Am I PLL Linked?")
                        }

                        Spacer(modifier = Modifier.size(10.dp))

                        Button(
                            modifier = Modifier
                                .width(200.dp)
                                .height(100.dp),
                            onClick = {
                                Toast.makeText(this@MainActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
                            }) {
                            Text(text = "Token Refresh")
                        }

                        Spacer(modifier = Modifier.size(10.dp))

                    }

                }
            }
        }
    }

}