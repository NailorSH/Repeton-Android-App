package com.nailorsh.repeton.features.about.data

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.common.data.models.language.LanguageLevel
import com.nailorsh.repeton.common.firestore.FirestoreRepository
import com.nailorsh.repeton.features.about.data.model.AboutUpdatedData
import com.nailorsh.repeton.features.about.data.model.AboutUserData
import com.nailorsh.repeton.features.about.data.model.EducationItem
import com.nailorsh.repeton.features.about.data.model.LanguageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AboutRepositoryImpl @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : AboutRepository {
    override suspend fun getUserData() = withContext(Dispatchers.IO) {
        val userDto = firestoreRepository.getUserDto()
        AboutUserData(
            name = userDto.name,
            surname = userDto.surname,
            photoSrc = "https://i.imgur.com/C25Otm8.jpeg",
            isTutor = userDto.canBeTutor,
            languages = listOf(
                Language(
                    id = Id("0"),
                    name = "Английский",
                    level = LanguageLevel.OTHER
                ), Language(
                    id = Id("1"),
                    name = "Русский",
                    level = LanguageLevel.OTHER
                )
            ),
        )
    }

    override suspend fun updateAboutData(data: AboutUpdatedData) {
        TODO("Not yet implemented")
    }


    override suspend fun getLanguages(): List<LanguageItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getEducation(): List<EducationItem> {
        TODO("Not yet implemented")
    }

}