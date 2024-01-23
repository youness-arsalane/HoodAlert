package com.example.hoodalert.ui


import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hoodalert.HoodAlertApplication
import com.example.hoodalert.ui.viewmodel.auth.LoginViewModel
import com.example.hoodalert.ui.viewmodel.auth.RegisterViewModel
import com.example.hoodalert.ui.viewmodel.communities.CommunityDetailsViewModel
import com.example.hoodalert.ui.viewmodel.communities.CommunityFormViewModel
import com.example.hoodalert.ui.viewmodel.communities.CommunityListViewModel
import com.example.hoodalert.ui.viewmodel.incidents.IncidentDetailsViewModel
import com.example.hoodalert.ui.viewmodel.incidents.IncidentFormViewModel
import com.example.hoodalert.ui.viewmodel.incidents.IncidentListViewModel
import com.example.inventory.ui.map.MapViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(hoodAlertApplication().container)
        }

        initializer {
            RegisterViewModel(hoodAlertApplication().container)
        }

        initializer {
            CommunityFormViewModel(
                this.createSavedStateHandle(),
                hoodAlertApplication().container
            )
        }

        initializer {
            CommunityDetailsViewModel(
                this.createSavedStateHandle(),
                hoodAlertApplication().container
            )
        }

        initializer {
            CommunityListViewModel(hoodAlertApplication().container)
        }

        initializer {
            IncidentFormViewModel(
                this.createSavedStateHandle(),
                hoodAlertApplication().container
            )
        }

        initializer {
            IncidentDetailsViewModel(
                this.createSavedStateHandle(),
                hoodAlertApplication().container
            )
        }

        initializer {
            IncidentListViewModel(hoodAlertApplication().container)
        }

        initializer {
            MapViewModel(hoodAlertApplication().container)
        }
    }
}

fun CreationExtras.hoodAlertApplication(): HoodAlertApplication {
    return (this[AndroidViewModelFactory.APPLICATION_KEY] as HoodAlertApplication)
}
