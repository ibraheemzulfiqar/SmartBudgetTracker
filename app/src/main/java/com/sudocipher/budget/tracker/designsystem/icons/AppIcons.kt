package com.sudocipher.budget.tracker.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import com.sudocipher.budget.tracker.designsystem.components.AppPainter
import com.sudocipher.budget.tracker.designsystem.components.rememberAppPainter

object AppIcons {

    val ArrowBack: AppPainter
        @Composable get() = rememberAppPainter(Icons.AutoMirrored.Filled.ArrowBack)

    val ArrowForward: AppPainter
        @Composable get() = rememberAppPainter(Icons.AutoMirrored.Filled.ArrowForward)

    val Check: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.Check)

    val Add: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.Add)

    val AddCircle: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.AddCircleOutline)

    val Statistics: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.BarChart)

    val PieChart: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.PieChart)

    val Income: AppPainter
        @Composable get() = rememberAppPainter(Icons.AutoMirrored.Filled.TrendingUp)

    val Expense: AppPainter
        @Composable get() = rememberAppPainter(Icons.AutoMirrored.Filled.TrendingDown)

    val Dashboard: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.AccountBalance)

    val Savings: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.Savings)

    val Edit: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.Edit)

    val Delete: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.Delete)

    val CalendarMonth: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.CalendarMonth)

    val Cash: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.Payments)

    val Wallet: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.AccountBalanceWallet)

    val Bank: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.AccountBalance)

    // Presets
    val Vehicle: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.DirectionsCar)

    val Home: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.Home)

    val Vacation: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.Flight)

    val Education: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.School)

    val Gadget: AppPainter
        @Composable get() = rememberAppPainter(Icons.Default.Laptop)

    val Others: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.Dashboard)

    val Food: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.Fastfood)

    val Bar: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.LocalBar)

    val Grocery: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.LocalGroceryStore)

    val Restaurant: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.Restaurant)

    val ShoppingCart: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.ShoppingCart)

    val Gift: AppPainter
        @Composable get() = rememberAppPainter(Icons.Outlined.CardGiftcard)


}
