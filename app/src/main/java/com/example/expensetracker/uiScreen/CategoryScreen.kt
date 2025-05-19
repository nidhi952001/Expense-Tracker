package com.example.expensetracker.uiScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.entity.Category
import com.example.expensetracker.ui.theme.AppColors.inverseOnSurface
import com.example.expensetracker.ui.theme.AppColors.surface
import com.example.expensetracker.utils.InputUIState.CategoryInputState
import com.example.expensetracker.viewModel.CategoryViewModel

@Composable
fun addCategory(categoryViewModel: CategoryViewModel = hiltViewModel()){
   // val category = Category(1,"Food", Icons.Default.ArrowBack.name.length, CategoryType.EXPENSE)
   // categoryViewModel.addCategory(category)
}

@Composable
fun ShowCategoryList(
    listOfExpCategory: List<Category>,
    onSelectCategory: (Int) -> Unit,
    selectedCategory: CategoryInputState
) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(color = inverseOnSurface).padding(top = 20.dp).background(color = surface)) {
        items(listOfExpCategory){
            showCategory(
                category =it,
                selectedCategory = selectedCategory,
                onSelectCategory = {onSelectCategory(it)})
        }
    }

}
@Composable
fun showCategory(
    category: Category,
    onSelectCategory: (Int) -> Unit,
    selectedCategory: CategoryInputState
) {
    val borderStroke =
        if(selectedCategory.selectedCategoryId!=category.categoryId)
        BorderStroke(1.dp,Color.Black)
    else
        BorderStroke(0.dp,Color.Unspecified)
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = {onSelectCategory(category.categoryId)})
            .padding(top = 10.dp).padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween ,
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth() ,verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Image(
                    painter = painterResource(category.categoryIcon),
                    contentDescription = stringResource(category.categoryName),
                    modifier = Modifier.size(35.dp)
                        .clip(CircleShape)
                        .background(color = category.categoryColor).padding(3.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(color = Color.White)

                )
                Text(
                    text = stringResource(category.categoryName),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Box(
            modifier = Modifier
                .size(30.dp)
                .padding(5.dp)
                .clip(CircleShape)
                .background(color = if(selectedCategory.selectedCategoryId == category.categoryId) {
                    Color.Blue.copy(alpha = 0.6F)
                }else{
                    Color.Unspecified
                })
                .border(borderStroke, CircleShape)
        ){
            if(selectedCategory.selectedCategoryId==category.categoryId) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.padding(3.dp)
                )
            }
        }

    }

}
