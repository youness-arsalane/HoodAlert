package com.example.hoodalert.ui


import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hoodalert.HoodAlertApplication
import com.example.hoodalert.ui.viewmodel.RegisterViewModel
import com.example.hoodalert.ui.viewmodel.SignInViewModel
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
            SignInViewModel(
                hoodAlertApplication().container.usersRepository,
                hoodAlertApplication().container.userSessionsRepository
            )
        }

        initializer {
            RegisterViewModel(hoodAlertApplication().container.usersRepository)
        }

        initializer {
            CommunityEditViewModel(
                this.createSavedStateHandle(),
                hoodAlertApplication().container.communitiesRepository
            )
        }

        initializer {
            CommunityEntryViewModel(hoodAlertApplication().container.communitiesRepository)
        }

        initializer {
            CommunityDetailsViewModel(
                this.createSavedStateHandle(),
                hoodAlertApplication().container.communitiesRepository
            )
        }

        initializer {
            CommunityListViewModel(hoodAlertApplication().container.communitiesRepository)
        }

        initializer {
            IncidentEditViewModel(
                this.createSavedStateHandle(),
                hoodAlertApplication().container.incidentsRepository
            )
        }

        initializer {
            IncidentEntryViewModel(hoodAlertApplication().container.incidentsRepository)
        }

        initializer {
            IncidentDetailsViewModel(
                this.createSavedStateHandle(),
                hoodAlertApplication().container.incidentsRepository
            )
        }

        initializer {
            IncidentListViewModel(hoodAlertApplication().container.incidentsRepository)
        }
    }
}

fun CreationExtras.hoodAlertApplication(): HoodAlertApplication {
    return (this[AndroidViewModelFactory.APPLICATION_KEY] as HoodAlertApplication)
}
