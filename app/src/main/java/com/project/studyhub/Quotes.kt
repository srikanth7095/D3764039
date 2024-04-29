package com.project.studyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.studyhub.quotes.QuoteApiClient
import com.project.studyhub.quotes.QuoteResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Quotes : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyQuotes()
        }
    }
}

@Preview
@Composable
fun MyQuotes() {
    var quotes by remember { mutableStateOf<List<QuoteResponse>>(emptyList()) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Image on the left corner
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterVertically)
                ) {

                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "Latest Quotes",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterVertically)
                ) {

                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn {
                items(quotes) { quote ->
                    QuoteItem(quote = quote)
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = {
                scope.launch {
                    val response = withContext(Dispatchers.IO) {
                        QuoteApiClient.quoteApiService.getQuote()
                    }
                    quotes = response
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)) {
                Text("Get Quotes")
            }
        }
    }

}
@Composable
fun QuoteItem(quote: QuoteResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White, //Card background color
            //contentColor = Color.White  //Card content color,e.g.text
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ){
        Column (modifier = Modifier.padding(16.dp)){
            Text(text = quote.quote, modifier = Modifier.padding(16.dp))
            Text(
                text = "- ${quote.author}",
                modifier = Modifier.padding(end = 16.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }

}