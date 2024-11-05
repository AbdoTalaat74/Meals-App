package com.example.mealzapp.meals.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealzapp.R
import com.example.mealzapp.composables.CategoryCard
import com.example.mealzapp.composables.CountryCard
import com.example.mealzapp.composables.IngredientCard
import com.example.mealzapp.composables.MealCard
import com.example.mealzapp.meals.data.local.Meal
import com.example.mealzapp.meals.presentation.mealsScreen.MealsState
import com.example.mealzapp.ui.theme.PurpleGrey80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: CategoryState,
    randomMealsState: RandomMealsState,
    ingredientsState: MealsState,
    countriesState: MealsState,
    onCountryClick: (countryName: String) -> Unit,
    onIngredientClick: (ingredientName: String) -> Unit,
    onCategoryClick: (categoryName: String) -> Unit,
    onMealClick: (Meal) -> Unit,
    onRefresh: () -> Unit,
    onSearchClick:()->Unit

    ) {
    val isRefreshing = randomMealsState.refreshState
    Scaffold(modifier = Modifier.fillMaxSize(), topBar =
    {
        TopAppBar(
            title = { Text(text = "Meals App") },
            actions = {
                IconButton(onClick ={onSearchClick()} ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp)
        )
    }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
        ) {
            if (state.isLoading || randomMealsState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PurpleGrey80)
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                item {

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.random_meals),
                            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
                            )

                        Card(
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = Modifier
                                .width(40.dp)
                                .height(48.dp)
                                .padding(bottom = 8.dp)

                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.refresh_icon),
                                    contentDescription = "refresh",
                                    Modifier
                                        .alpha(if (isRefreshing) 0f else 1f)
                                        .padding(8.dp)
                                        .size(30.dp)// Hidden when refreshing
                                        .clickable {
                                            if (!isRefreshing) onRefresh() // Call refresh
                                        }
                                )


                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                    modifier = Modifier
                                        .alpha(if (isRefreshing) 1f else 0f)
                                        .padding(8.dp)
                                        .size(28.dp)
                                )
                            }
                        }


                    }
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        items(randomMealsState.meals) { meal ->
                            MealCard(
                                meal,
                                onClick = { onMealClick(it) },
                                isInMainScreen = true
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = stringResource(R.string.categories),
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),

                        )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        items(state.categories) { category ->
                            CategoryCard(
                                category = category,
                                onClick = { onCategoryClick(category.strCategory) }

                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = stringResource(R.string.ingredients),
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),

                        )

                    Spacer(modifier = Modifier.height(8.dp))


                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(ingredientsState.meals.size) { index ->
                            IngredientCard(ingredientsState.meals[index]) {
                                it.strIngredient?.let { it1 -> onIngredientClick(it1) }
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = stringResource(R.string.countries),
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(countriesState.meals.size) { index ->
                            if (!countriesState.meals[index].strArea.equals("Unknown")){
                                CountryCard(
                                    meal = countriesState.meals[index]
                                ) {
                                    countriesState.meals[index].strArea?.let { it1 -> onCountryClick(it1) }

                                }
                            }
                        }
                    }

                }
            }


        }
    }


}


