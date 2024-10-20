package com.example.mealzapp.meals.presentation.mealsScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealzapp.meals.domain.GetMealsByAreaUseCase
import com.example.mealzapp.meals.domain.GetMealsByCategoryUseCase
import com.example.mealzapp.meals.domain.GetMealsByIngredientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMealsByCategoryUseCase: GetMealsByCategoryUseCase,
    private val getMealsByAreaUseCase: GetMealsByAreaUseCase,
    private val getMealsByIngredientUseCase: GetMealsByIngredientUseCase
) : ViewModel() {


    private var filterBy: String
    private var _mealsState by mutableStateOf(
        MealsState(
            meals = emptyList(),
            isLoading = false,
        )
    )
    val mealsState: State<MealsState>
        get() = derivedStateOf { _mealsState }

    private var currentPage = 0
    private var endReached = false
    private val pageSize = 8


    init {

        val filterKeyArgument: String? = savedStateHandle.get<String>("filter_key")
        val searchTypeArgument: String? = savedStateHandle.get<String>("search_type")

        filterBy = filterKeyArgument ?: ""
        Log.e("MealsViewModelArgument", filterBy)
        when (searchTypeArgument) {
            "category" -> getMealsByCategoryName(filterBy)
            "area" -> getMealsByAreaName(filterBy)
            "ingredient" -> getMealsByIngredient(filterBy)
        }


    }


    fun getMealsByCategoryName(categoryName: String) {

        if (_mealsState.isLoading || endReached) return
        _mealsState.isLoading = true

        viewModelScope.launch(Dispatchers.IO) {

            val newMeals = getMealsByCategoryUseCase.getMealsByCategory(
                categoryName,
                currentPage * pageSize,
                pageSize
            )
            if (newMeals.isNotEmpty()) {
                _mealsState = _mealsState.copy(
                    meals = mealsState.value.meals + newMeals,
                    isLoading = false
                )
                currentPage++
            } else {
                endReached = true
            }

        }
    }

    private fun getMealsByAreaName(areaName: String) {

        if (_mealsState.isLoading || endReached) return
        _mealsState.isLoading = true

        viewModelScope.launch(Dispatchers.IO) {

            val newMeals = getMealsByAreaUseCase.getMealsByArea(
                areaName,
                currentPage * pageSize,
                pageSize
            )
            if (newMeals.isNotEmpty()) {
                _mealsState = _mealsState.copy(
                    meals = mealsState.value.meals + newMeals,
                    isLoading = false
                )
                currentPage++
            } else {
                endReached = true
            }
        }

    }

    private fun getMealsByIngredient(ingredient: String) {
        if (_mealsState.isLoading || endReached) return
        _mealsState.isLoading = true

        viewModelScope.launch(Dispatchers.IO) {

            val newMeals = getMealsByIngredientUseCase.getMealsByIngredient(
                ingredient,
                currentPage * pageSize,
                pageSize
            )
            if (newMeals.isNotEmpty()) {
                _mealsState = _mealsState.copy(
                    meals = mealsState.value.meals + newMeals,
                    isLoading = false
                )
                currentPage++
            } else {
                endReached = true
            }
        }

    }
}


