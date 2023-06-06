package com.deucapstone2023.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deucapstone2023.design.theme.DeuCapStone2023Theme
import com.deucapstone2023.design.theme.deepGray
import com.deucapstone2023.design.theme.lightGray
import com.deucapstone2023.design.theme.red
import com.deucapstone2023.design.utils.tu

@Composable
fun LargeTextBox(
    inputMessage: String,
    hintMessage: String? = null,
    errorMessage: String? = null,
    onInputStringChanged: (String) -> Unit,
    fontSize: Int = 16,
    fontWeight: FontWeight = FontWeight.Bold,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    headerIcon: @Composable (() -> Unit)? = null,
    tailIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val borderColor = when {
        !enabled -> lightGray
        !errorMessage.isNullOrBlank() -> red
        else -> deepGray
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(4.dp),
                    color = borderColor
                )
                .padding(contentPadding)
        ) {
            BasicTextField(
                value = inputMessage,
                onValueChange = {value -> onInputStringChanged(value)},
                modifier = Modifier,
                enabled = enabled,
                readOnly = readOnly,
                maxLines = maxLines,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                decorationBox = { innerTextFiled ->
                    if(inputMessage.isBlank() && !hintMessage.isNullOrBlank())
                        Text(
                            text = hintMessage,
                            color = lightGray,
                            fontSize = fontSize.tu,
                            fontWeight = fontWeight
                        )
                    else
                        innerTextFiled()
                }
            )
        }
        if(!errorMessage.isNullOrBlank())
            Text(
                text = errorMessage,
                color = red,
                fontSize = 8.tu,
                fontWeight = FontWeight.Light
            )
    }

}

@Composable
@Preview(widthDp = 300, showBackground = true)
private fun PreviewLargeTextView() =
    DeuCapStone2023Theme {
        LargeTextBox(
            inputMessage = "이렇게 입력됩니다.",
            onInputStringChanged = {}
        )
    }

@Composable
@Preview(widthDp = 300, showBackground = true)
private fun PreviewLargeTextViewWithError() =
    DeuCapStone2023Theme {
        LargeTextBox(
            inputMessage = "에러가 발생했습니다.",
            errorMessage = "이러한 에러가 발생했습니다.",
            onInputStringChanged = {}
        )
    }

@Composable
@Preview(widthDp = 300, showBackground = true)
private fun PreviewLargeTextViewWithDisabled() =
    DeuCapStone2023Theme {
        LargeTextBox(
            inputMessage = "",
            hintMessage = "힌트는 이렇게 보입니다.",
            onInputStringChanged = {}
        )
    }