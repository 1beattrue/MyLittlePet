package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addnote

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.EditPetUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addnote.AddNoteStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addnote.AddNoteStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addnote.AddNoteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AddNoteStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent
        data object AddNote : Intent
        data class ChangeText(val text: String) : Intent
        data class ChangeIcon(val icon: NoteIcon) : Intent
    }

    data class State(
        val text: String,
        val selectedIcon: NoteIcon,
        val isIncorrect: Boolean
    )

    sealed interface Label {
        data object CloseAddNote : Label
    }
}

class AddNoteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val editPetUseCase: EditPetUseCase
) {

    fun create(
        pet: Pet,
        noteList: List<Note>
    ): AddNoteStore =
        object : AddNoteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddNoteStore",
            initialState = State(
                text = "",
                selectedIcon = NoteIcon.FoodItem,
                isIncorrect = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(pet, noteList) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class OnTextChanged(val text: String) : Msg
        data class OnIconChanged(val icon: NoteIcon) : Msg
        data object EmptyNote : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val pet: Pet,
        private val noteList: List<Note>,
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddNote -> {
                    scope.launch {
                        val noteText = getState().text
                        if (noteText.isBlank()) {
                            dispatch(Msg.EmptyNote)
                        } else {
                            val iconResId = getState().selectedIcon.iconResId
                            val newNote = Note(
                                id = generateNoteId(noteList),
                                text = noteText,
                                iconResId = iconResId
                            )

                            val oldNoteList = noteList
                            val newNoteList = oldNoteList
                                .toMutableList()
                                .apply { add(newNote) }
                                .toList()
                            editPetUseCase(pet = pet.copy(noteList = newNoteList))
                            publish(Label.CloseAddNote)
                        }
                    }
                }

                is Intent.ChangeIcon -> {
                    dispatch(Msg.OnIconChanged(intent.icon))
                }

                is Intent.ChangeText -> {
                    val text = formattedText(intent.text)
                    dispatch(Msg.OnTextChanged(text))
                }

                Intent.ClickBack -> {
                    publish(Label.CloseAddNote)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.EmptyNote -> copy(isIncorrect = true)
                is Msg.OnIconChanged -> copy(selectedIcon = msg.icon)
                is Msg.OnTextChanged -> copy(isIncorrect = false, text = msg.text)
            }
    }

    private fun formattedText(text: String): String {
        if (text.length > 500) return text.substring(0..<500)
        return text
    }

    private fun generateNoteId(list: List<Note>): Int {
        if (list.isEmpty()) return 0
        var maxId = list[0].id
        list.forEach {
            if (it.id > maxId) maxId = it.id
        }
        return maxId + 1
    }
}
