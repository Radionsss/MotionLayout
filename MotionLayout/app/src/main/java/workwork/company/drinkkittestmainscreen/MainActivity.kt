package workwork.company.drinkkittestmainscreen

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RawRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.rememberMotionLayoutState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import kotlinx.coroutines.launch
import workwork.company.drinkkittestmainscreen.ui.theme.DrinkKitTestMainScreenTheme

data class Item(val text: String, val videoResId: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val list = listOf(R.raw.coffe_big, R.raw.coffe_big, R.raw.coffe_big)
            DrinkKitTestMainScreenTheme {
                RecipeDetail(list)
            }
        }
    }
}

@OptIn(ExperimentalMotionApi::class, ExperimentalFoundationApi::class)
@Composable
fun RecipeDetail(list: List<Int>) {
    val motionState = rememberMotionLayoutState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.motion_scene)
            .readBytes()
            .decodeToString()
    }

    val pagerState = rememberPagerState(
        initialPageOffsetFraction = 0f
    ) {
        return@rememberPagerState list.size
    }

        val videoUri = rawResourceUri(context, list[0])

        val player = remember {
            ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri(videoUri)
                setMediaItem(mediaItem)
                repeatMode = ExoPlayer.REPEAT_MODE_ONE
                prepare()
            }
        }

        LaunchedEffect(motionState) {
            snapshotFlow { motionState.currentProgress }.collect { progress ->
                player.playWhenReady = true
            }
        }
        DisposableEffect(player) {
            onDispose {
                player.release()
            }
        }
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.layoutId("HorizontalPager")  .fillMaxHeight()
    ) { page ->
        MotionLayout(
            motionScene = MotionScene(content = motionScene),
            motionLayoutState = motionState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {})
                }
        ) {
            AndroidView(
                modifier = Modifier.layoutId("headerImage"),
                factory = {
                    PlayerView(it).apply {
                        this.player = player
                        useController = false
                        resizeMode =
                            AspectRatioFrameLayout.RESIZE_MODE_FILL
                    }
                }
            )
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .background(
//                        White,
//                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
//                    )
//                    .layoutId("contentBg")
//            )

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            White,
                            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                        )
                        .layoutId("contentBg")
                )
            Text(
                color = Color.White,
                text = "Fresh Strawberry Cake", fontSize = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold, modifier = Modifier

                    .layoutId("title_price")
                    .fillMaxWidth()
                    .padding(10.dp)
            )
            Text(
                text = list[page].toString(), fontSize = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold, modifier = Modifier
                    .layoutId("title")
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            Divider(
                Modifier
                    .layoutId("titleDivider")
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp)
            )

            Text(
                text = "by John Kanell", fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Gray, fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .layoutId("subTitle")
                    .fillMaxWidth()
                    .padding(6.dp)
            )
            Text(
                modifier = Modifier
                    .layoutId("date")
                    .fillMaxWidth()
                    .padding(6.dp),
                text = "September, 2022", fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Gray
            )
            Row(
                modifier = Modifier
                    .layoutId("actions")
                    .background(Color.DarkGray),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = "texttttttt",
                    modifier = Modifier
                        .fillMaxHeight()
                        .layoutId("text")
                        .padding(horizontal = 16.dp),
                    fontSize = 12.sp,
                )
            }
        }
    }
}
fun rawResourceUri(context: Context, @RawRes rawResId: Int): Uri {
    return RawResourceDataSource.buildRawResourceUri(rawResId)
}
//@Composable
//fun VideoWithScrollableList(videoResId: Int, itemCount: Int) {
//    val context = LocalContext.current
//
//    // Получаем URI для видео из ресурсов
//    val videoUri = rawResourceUri(context, videoResId)
//
//    // Создаем ExoPlayer
//    val player = ExoPlayer.Builder(context).build()
//
//    // Задаем источник для видео
//    val mediaItem = MediaItem.fromUri(videoUri)
//    player.setMediaItem(mediaItem)
//    player.repeatMode = ExoPlayer.REPEAT_MODE_ONE // Циклическое воспроизведение
//    player.prepare()
//    player.playWhenReady = true // Начинаем воспроизведение
//
//    // Используем DisposableEffect для освобождения ресурсов
//    DisposableEffect(player) {
//        onDispose {
//            player.release() // Освобождаем ресурсы при уничтожении
//        }
//    }
//
//    // Размещаем видео и список
//    Box(modifier = Modifier.fillMaxSize()) {
//        // Полноэкранное видео
//        AndroidView(
//            modifier = Modifier.fillMaxSize(),
//            factory = {
//                PlayerView(it).apply {
//                    this.player = player
//                    useController = false // Убираем элементы управления
//                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL // Заполнение экрана
//                }
//            }
//        )
//
//        // Список поверх видео
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Transparent) // Прозрачный фон
//        ) {
//            // Добавляем остальные элементы списка
//            items((1..itemCount).toList()) { index ->
//                // Горизонтальный список для каждого элемента вертикального списка
//                HorizontalItemRow(index = index)
//            }
//        }
//    }
//}
//// Composable для горизонтального списка
//@Composable
//fun HorizontalItemRow(index: Int) {
//    LazyRow(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.Transparent)
//            .padding(vertical = 8.dp) // Отступ между горизонтальными строками
//    ) {
//        items((1..2).toList()) { itemIndex ->
//        // Элементы в горизонтальном списке
//            Box(
//                modifier = Modifier
//                    .width(150.dp) // Ширина каждого элемента
//                    .height(100.dp) // Высота каждого элемента
//                    .padding(8.dp)
//                    .background(Color.Black)
//                    .padding(8.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                // Здесь можно разместить содержимое элемента
//                Text(text = "Item $index-$itemIndex")
//            }
//        }
//    }
//}
//
//// Функция для получения URI ресурса
