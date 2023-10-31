package com.example.hoodalert.ui


import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hoodalert.HoodAlertApplication
import com.example.hoodalert.ui.viewmodel.LoginViewModel
import com.example.hoodalert.ui.viewmodel.RegisterViewModel
import com.example.hoodalert.ui.viewmodel.communities.CommunityDetailsViewModel
import com.example.hoodalert.ui.viewmodel.communities.CommunityEditViewModel
import com.example.hoodalert.ui.viewmodel.communities.CommunityEntryViewModel
import com.example.hoodalert.ui.viewmodel.communities.CommunityListViewModel
import com.example.hoodalert.ui.viewmodel.incidents.IncidentDetailsViewModel
import com.example.hoodalert.ui.viewmodel.incidents.IncidentEditViewModel
import com.example.hoodalert.ui.viewmodel.incidents.IncidentEntryViewModel
import com.example.hoodalert.ui.viewmodel.incidents.IncidentListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(hoodAlertApplication().container)
        }

        initializer {
            RegisterViewModel(hoodAlertApplication().container)
        }

        initializer {
            CommunityEditViewModel(
                this.createSavedStateHandle(),
                hoodAlertApplication().container
            )
        }

        initializer {
            CommunityEntryViewModel(hoodAlertApplication().container)
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
            IncidentEditViewModel(
                this.createSavedStateHandle(),
                hoodAlertApplication().container
            )
        }

        initializer {
            IncidentEntryViewModel(
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
    }
}

fun CreationExtras.hoodAlertApplication(): HoodAlertApplication {
    return (this[AndroidViewModelFactory.APPLICATION_KEY] as HoodAlertApplication)
}
