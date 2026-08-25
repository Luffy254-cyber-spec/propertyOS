package com.him.landlordtenant.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    header: @Composable () -> Unit,
    items: List<DrawerNavItem>,
    onItemClick: (DrawerNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier = modifier) {
        header()
        HorizontalDivider()
        Spacer(modifier = Modifier.height(12.dp))
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label) },
                selected = item.selected,
                onClick = { onItemClick(item) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

data class DrawerNavItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val selected: Boolean,
    val route: String
)
