package com.example.hoodalert.ui


import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hoodalert.HoodAlertApplication
import com.example.hoodalert.ui.viewmodel.incidents.IncidentListViewModel
import com.example.hoodalert.ui.viewmodel.incidents.IncidentDetailsViewModel
import com.example.hoodalert.ui.viewmodel.incidents.EditViewModel
import com.example.hoodalert.ui.viewmodel.incidents.IncidentEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            EditViewModel(
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
