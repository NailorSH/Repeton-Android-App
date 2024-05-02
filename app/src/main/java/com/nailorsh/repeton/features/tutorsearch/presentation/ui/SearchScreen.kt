package com.nailorsh.repeton.features.tutorsearch.presentation.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Tutor
import com.nailorsh.repeton.common.data.models.UserId
import com.nailorsh.repeton.common.data.sources.FakeTutorsSource
import com.nailorsh.repeton.core.ui.components.ErrorScreen
import com.nailorsh.repeton.core.ui.components.ExpandableText
import com.nailorsh.repeton.core.ui.components.IconWithText
import com.nailorsh.repeton.core.ui.components.LikeButton
import com.nailorsh.repeton.core.ui.components.LoadingScreen
import com.nailorsh.repeton.core.ui.components.SearchBarWithFilter
import com.nailorsh.repeton.core.ui.theme.AmbientColor
import com.nailorsh.repeton.core.ui.theme.BodyColor
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.core.ui.theme.ShowMoreTextButtonColor
import com.nailorsh.repeton.core.ui.theme.SpotColor
import com.nailorsh.repeton.core.ui.theme.StarColor
import com.nailorsh.repeton.core.ui.theme.TitleColor
import com.nailorsh.repeton.core.ui.theme.White
import com.nailorsh.repeton.core.ui.theme.WriteButtonBackgroundColor
import com.nailorsh.repeton.core.ui.theme.WriteButtonTextColor
import com.nailorsh.repeton.features.tutorsearch.presentation.viewmodel.SearchUiState

@Composable
fun SearchScreen(
    typingGetSearchResults: (String) -> Unit,
    getSearchResults: () -> Unit,
    searchUiState: SearchUiState,
    onTutorCardClicked: (UserId) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 15.dp)
        ) {
            SearchBarWithFilter(
                placeholder = R.string.search_placeholder,
                leadingIcon = R.drawable.ic_search_icon,
                filterIcon = R.drawable.ic_filter_icon,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        getSearchResults()
                    }
                ),
                value = query,
                onSearch = getSearchResults,
                onValueChanged = {
                    query = it
                    typingGetSearchResults(it)
                },
                modifier = Modifier.fillMaxWidth()
            )

            when (searchUiState) {
                is SearchUiState.None -> {}
                is SearchUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())

                is SearchUiState.Success -> TutorList(
                    tutors = searchUiState.tutors,
                    onTutorCardClicked = onTutorCardClicked,
                    modifier = Modifier.fillMaxWidth()
                )

                is SearchUiState.Error -> ErrorScreen(
                    getSearchResults,
                    modifier = modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun TutorList(
    tutors: List<Tutor>,
    onTutorCardClicked: (UserId) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(tutors) {
            TutorCard(
                modifier = Modifier.fillMaxWidth(),
                tutor = it,
                onWriteButtonClicked = {},
                onCardClicked = onTutorCardClicked
            )
        }
    }
}

@Composable
fun TutorCard(
    tutor: Tutor,
    onWriteButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    onCardClicked: (UserId) -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }

    val photoSrc = painterResource(R.drawable.man_photo)

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .shadow(elevation = 4.dp, spotColor = SpotColor, ambientColor = AmbientColor)
            .background(color = White, shape = RoundedCornerShape(size = 5.dp))
            .clickable { onCardClicked(tutor.id) }
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = photoSrc,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(81.dp)
                        .height(81.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${tutor.name}\n${tutor.middleName ?: ""}",
                            style = TextStyle(
                                fontSize = 22.sp,
                                lineHeight = 26.sp,
                                fontWeight = FontWeight(800),
                                color = TitleColor,
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        LikeButton(
                            modifier = Modifier
                                .padding(start = 9.dp, top = 5.dp)
                                .width(24.dp)
                                .height(22.dp),
                            isLiked = isLiked,
                            onButtonClicked = { isLiked = !isLiked }
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.Start),
                        verticalAlignment = Alignment.Top,
                    ) {
                        IconWithText(
                            icon = R.drawable.ic_star_icon,
                            iconTint = StarColor,
                            text = tutor.rating.toString()
                        )
                        IconWithText(
                            icon = R.drawable.ic_comment_icon,
                            text = "${tutor.reviewsNumber} отзывов"
                        )
                    }
                }
            }

            tutor.about?.let {
                ExpandableText(
                    modifier = modifier.fillMaxWidth(),
                    text = it,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(400),
                        color = BodyColor,
                    ),
                    textButtonStyle = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(500),
                        color = ShowMoreTextButtonColor,
                    )
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                tutor.subjects?.let {
                    InfoSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = R.string.subjects_section,
                        body = it.joinToString(separator = " • ")
                    )
                }

                tutor.education?.let {
                    InfoSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = R.string.education_section,
                        body = it
                    )
                }

                tutor.subjectsPrices?.let {
                    PriceSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = R.string.prices_section,
                        subjectsPrices = it
                    )
                }
            }
        }
        Button(
            onClick = { onWriteButtonClicked() },
            shape = RoundedCornerShape(size = 8.dp),
            contentPadding = PaddingValues(horizontal = 5.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WriteButtonBackgroundColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.message_button),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight(600),
                    color = WriteButtonTextColor
                ),
            )
        }
    }
}


@Composable
fun InfoSection(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    body: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Text(
            text = stringResource(title),
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(500),
                color = TitleColor,
            )
        )
        Text(
            text = body,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(400),
                color = BodyColor,
            )
        )
    }
}

@Composable
fun PriceSection(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    subjectsPrices: Map<String, String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Text(
            text = stringResource(title),
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(500),
                color = TitleColor,
            ),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            for (pair in subjectsPrices) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = pair.key,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(400),
                            color = BodyColor,
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp)
                    )
                    Text(
                        text = pair.value,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight(400),
                            color = BodyColor,
                        ),
                        textAlign = TextAlign.Right,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun TutorCardPreview() {
    RepetonTheme {
        TutorCard(
            tutor = FakeTutorsSource.getTutorsList()[0],
            onWriteButtonClicked = {},
            onCardClicked = {}
        )
    }
}


@Preview(
    showSystemUi = true
)
@Composable
fun SearchScreenPreview() {
    RepetonTheme {
        SearchScreen(
            typingGetSearchResults = { _ -> },
            getSearchResults = { },
            searchUiState = SearchUiState.None,
            onTutorCardClicked = {}
        )
    }
}