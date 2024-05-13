package com.nailorsh.repeton.features.schedule.data

import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.common.data.sources.FakeLessonSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class FakeScheduleRepository @Inject constructor() : ScheduleRepository {
    override suspend fun getLessons(): List<Lesson> = withContext(Dispatchers.IO) {
        FakeLessonSource.loadLessons()
    }

    override suspend fun getLessons(day: LocalDate): List<Lesson> = withContext(Dispatchers.IO) {
        FakeLessonSource.loadLessons().filter {
            it.startTime.year == day.year && it.startTime.dayOfYear == day.dayOfYear
        }
    }
}