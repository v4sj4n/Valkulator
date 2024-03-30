package com.example.valkulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.valkulator.ui.theme.ValkulatorTheme
import org.mozilla.javascript.Context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ValkulatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorApp()

                }
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var expression by remember {
        mutableStateOf("0")
    }
    var result by remember {
        mutableStateOf("")
    }

    fun addElementToExpression(el: String) {
        when {
            el.equals("AC", true) -> {
                expression = ""
                result = ""
            }

            el == "C" -> expression = expression.dropLast(1)
            el == "=" -> {
                val res = calculateResult(expression)
                if (res != "err") {
                    expression = res
                    result = res
                } else {
                    expression = "0"
                    result = "0"
                }
            }

            expression == "0" && el !in listOf("+", "-", "/", "*") -> expression = el
            el == ")" -> {
                if (expression.count { it == '(' } > expression.count { it == ')' }) {
                    expression += el
                }
            }

            else -> {
                val lastChar = expression.lastOrNull()
                when {
                    el in listOf(
                        "+", "-", "/", "*"
                    ) && lastChar?.let { it.isDigit() || it in listOf(')') } == false -> {
                        expression =
                            expression.dropLastWhile { it in listOf('+', '-', '*', '/') } + el
                    }

                    else -> expression += el
                }
            }
        }
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Valkulator",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = MaterialTheme.typography.headlineLarge,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),

                ) {

                Text(
                    text = expression,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.9f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.inversePrimary,

                    )
                Text(
                    text = result,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.displayLarge,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton("AC", { addElementToExpression("AC") })
                CalculatorButton("(", { addElementToExpression("(") })
                CalculatorButton(")", { addElementToExpression(")") })
                CalculatorButton("/", { addElementToExpression("/") })
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton("7", { addElementToExpression("7") })
                CalculatorButton("8", { addElementToExpression("8") })
                CalculatorButton("9", { addElementToExpression("9") })
                CalculatorButton("*", { addElementToExpression("*") })
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton("4", { addElementToExpression("4") })
                CalculatorButton("5", { addElementToExpression("5") })
                CalculatorButton("6", { addElementToExpression("6") })
                CalculatorButton("-", { addElementToExpression("-") })
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton("1", { addElementToExpression("1") })
                CalculatorButton("2", { addElementToExpression("2") })
                CalculatorButton("3", { addElementToExpression("3") })
                CalculatorButton("+", { addElementToExpression("+") })
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton("C", { addElementToExpression("C") })
                CalculatorButton("0", { addElementToExpression("0") })
                CalculatorButton(".", { addElementToExpression(".") })
                CalculatorButton("=", { addElementToExpression("=") })
            }
        }

    }
}

@Composable
fun CalculatorButton(btnStr: String, clickHandler: () -> Unit) {

    when {
        btnStr in listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".") -> {
            Button(
                onClick = clickHandler, modifier = Modifier
                    .width(80.dp)
                    .height(60.dp)
            ) {
                ButtonText(txt = btnStr)
            }
        }

        btnStr in listOf("*", "/", "+", "-", "(", ")", "()") -> {
            FilledTonalButton(
                onClick = clickHandler, modifier = Modifier
                    .width(80.dp)
                    .height(60.dp)

            ) {
                ButtonText(txt = btnStr)
            }
        }

        btnStr.equals("ac", true) || btnStr.equals("c", true) -> {
            ElevatedButton(
                onClick = clickHandler, modifier = Modifier
                    .width(80.dp)
                    .height(60.dp)

            ) {
                ButtonText(txt = btnStr)
            }
        }

        else -> {
            OutlinedButton(
                onClick = clickHandler, modifier = Modifier
                    .width(80.dp)
                    .height(60.dp)
            ) {
                ButtonText(txt = btnStr)
            }
        }
    }

}

@Composable
fun ButtonText(txt: String) {
    Text(
        text = txt,
        modifier = Modifier.padding(2.dp),
        fontSize = if (txt.length < 2) 26.sp else 20.sp

    )
}

fun calculateResult(exp: String): String {
    return try {
        val context = Context.enter()
        context.setOptimizationLevel(-1)
        val scriptable = context.initStandardObjects()
        var finalResult = context.evaluateString(scriptable, exp, "Javascript", 1, null).toString()
        if (finalResult.endsWith(".0")) {
            finalResult = finalResult.replace(".0", "")
        }
        finalResult
    } catch (err: Exception) {
        "Err"
    }
}

@Preview
@Composable
private fun CalculatorAppPreview() {
    CalculatorApp()
}
