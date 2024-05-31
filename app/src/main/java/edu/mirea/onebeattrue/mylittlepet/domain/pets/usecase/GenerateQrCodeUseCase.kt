package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import android.graphics.Bitmap
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.PetRepository
import javax.inject.Inject

class GenerateQrCodeUseCase @Inject constructor(private val repository: PetRepository) {
    suspend operator fun invoke(pet: Pet): Bitmap {
        return repository.generateQrCode(pet)
    }
}