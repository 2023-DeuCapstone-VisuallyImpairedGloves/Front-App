package com.example.deucapstone2023.ui.screen.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deucapstone2023.ui.component.DefaultLayout
import com.example.deucapstone2023.ui.component.LargeButton
import com.example.deucapstone2023.ui.component.LargeTextBox
import com.example.deucapstone2023.ui.component.VerticalSpacer
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.example.deucapstone2023.ui.theme.deepGray
import com.example.deucapstone2023.utils.tu

@Composable
fun Login(
    loginUiState: LoginUiState,
    onIdChange: (String) -> Unit,
    onPwChange: (String) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    DefaultLayout(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            VerticalSpacer(height = 130.dp)
            Text(
                text = "로그인하기",
                fontSize = 22.tu,
                fontWeight = FontWeight.ExtraBold
            )
            VerticalSpacer(height = 16.dp)
            LargeTextBox(
                inputMessage = loginUiState.id,
                hintMessage = "아이디",
                onInputStringChanged = onIdChange
            )
            VerticalSpacer(height = 16.dp)
            LargeTextBox(
                inputMessage = loginUiState.pw,
                hintMessage = "패스워드",
                onInputStringChanged = onPwChange,
                visualTransformation = { text ->
                    TransformedText(
                        text = AnnotatedString(text = "*".repeat(text.text.length)),
                        offsetMapping = OffsetMapping.Identity
                    )
                }
            )
            VerticalSpacer(height = 16.dp)
            Text(
                text = "계정이 없으신가요?",
                fontSize = 10.tu,
                fontWeight = FontWeight.Bold,
                color = deepGray,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
                    .clickable { onNavigateToSignUp() }
            )


            Spacer(modifier = Modifier.weight(1f))
            LargeButton(
                content = "로그인 하기",
                modifier = Modifier.fillMaxWidth()
            )
            VerticalSpacer(height = 16.dp)
        }
    }
}

@Composable
@Preview
private fun PreviewLoginScreen() =
    DeuCapStone2023Theme {
        Login(
            loginUiState = LoginUiState(
                id = "jinho1234",
                pw = "jino1122"
            ),
            onIdChange = {},
            onPwChange = {},
            onNavigateToSignUp = {}
        )
    }