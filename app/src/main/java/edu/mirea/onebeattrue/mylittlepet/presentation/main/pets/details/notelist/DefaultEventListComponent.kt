package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultNoteListComponent @AssistedInject constructor(
    private val storeFactory: EventListStoreFactory,

    @Assisted("pet") private val pet: Pet,
    @Assisted("onAddNote") private val onAddNote: (List<Note>) -> Unit,
    @Assisted("onClickBack") private val onClickBack: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : NoteListComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create(pet) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is NoteListStore.Label.OnAddNoteClick -> {
                        onAddNote()
                    }

                    NoteListStore.Label.OnClickBack -> {
                        onClickBack()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<NoteListStore.State>
        get() = store.stateFlow

    override fun onAddNote() {
        store.accept(NoteListStore.Intent.AddNote)
    }

    override fun onDeleteNote(note: Note) {
        store.accept(NoteListStore.Intent.DeleteNote(note))
    }


    override fun onBackClicked() {
        store.accept(NoteListStore.Intent.OnClickBack)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("pet") pet: Pet,
            @Assisted("onAddNote") onAddNote: (List<Note>) -> Unit,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultNoteListComponent
    }
}