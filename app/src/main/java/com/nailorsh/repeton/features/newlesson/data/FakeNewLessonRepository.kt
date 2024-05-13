package com.nailorsh.repeton.features.newlesson.data


import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.sources.FakeLessonSource
import com.nailorsh.repeton.common.data.sources.FakeSubjectsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FakeNewLessonRepository @Inject constructor() : NewLessonRepository {
    override suspend fun getSubjects(): List<Subject> = withContext(Dispatchers.IO) {
        FakeSubjectsSource.getSubjects()
    }

    override suspend fun saveNewLesson(lesson: Lesson) = withContext(Dispatchers.IO) {
        FakeLessonSource.addLesson(lesson)
    }

    override suspend fun getSubject(subjectName: String) = withContext(Dispatchers.IO) {
        FakeSubjectsSource.getSubjectByName(subjectName)
    }

}