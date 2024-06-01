package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.qrcode.QrCodeContent

@Composable
fun FeedContent(
    modifier: Modifier = Modifier,
    component: FeedComponent
) {
    QrCodeContent()
}