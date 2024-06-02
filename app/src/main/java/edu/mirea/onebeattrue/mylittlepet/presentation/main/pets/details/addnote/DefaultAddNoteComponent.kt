package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addnote

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultAddNoteComponent @AssistedInject constructor(
    private val storeFactory: AddNoteStoreFactory,
    @Assisted("pet") private val pet: Pet,
    @Assisted("onAddNoteClosed") private val onAddNoteClosed: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : AddNoteComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create(pet = pet) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    AddNoteStore.Label.CloseAddNote -> onAddNoteClosed()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddNoteStore.State>
        get() = store.stateFlow

    override fun iconChanged(icon: NoteIcon) {
        store.accept(AddNoteStore.Intent.ChangeIcon(icon))
    }

    override fun noteTextChanged(text: String) {
        store.accept(AddNoteStore.Intent.ChangeText(text))
    }

    override fun addNote() {
        store.accept(AddNoteStore.Intent.AddNote)
    }

    override fun onBackClicked() {
        store.accept(AddNoteStore.Intent.ClickBack)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("pet") pet: Pet,
            @Assisted("onAddNoteClosed") onAddNoteClosed: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultAddNoteComponent
    }
}