package workwork.company.drinkkittestmainscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Pagers() {
    val animals = listOf(
        R.drawable.cake,
        R.drawable.cake,
        R.drawable.ic_launcher_background,
    )
    val pagerState = rememberPagerState(){
        return@rememberPagerState animals.size
    }
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            Image(
                painter = painterResource(id = animals[index]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .offset(y = -(16).dp)
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(100))
                .padding(8.dp)
                .align(Alignment.BottomCenter)
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage - 1
                        )
                    }
                },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Go back"
                )
            }
            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1
                        )
                    }
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Go forward"
                )
            }
        }

    }
}