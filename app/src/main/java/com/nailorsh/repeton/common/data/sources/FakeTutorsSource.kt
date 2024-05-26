package com.nailorsh.repeton.common.data.sources

import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.user.Tutor

object FakeTutorsSource {
    private val tutors = listOf(
        Tutor(
            id = Id("0"),
            name = "Александр ",
            surname = "Киселёв",
            middleName = "Витальевич",
            photoSrc = "https://i.imgur.com/C25Otm8.jpeg",
            about = "Я являюсь техническим руководителем проекта «Учу на Профи.Ру» и активно " +
                    "использую современные технологии на своих занятиях.",
            subjects = listOf(
                FakeSubjectsSource.getSubject(1),
                FakeSubjectsSource.getSubject(2)
            ),
            education = "Окончил МФТИ, ФОПФ, два красных диплома, 2005 г.",
            subjectsPrices = null,
            averagePrice = 500,
            rating = 4.93,
            reviewsNumber = 100,
            taughtLessonNumber = 250,
            experienceYears = 6,
            languages = null,
        ),

        Tutor(
            id = Id("1"),
            name = "Александр",
            surname = "Коновалов",
            middleName = "Владимирович",
            about = "Преподаватель МГТУ имени Н.Э. Баумана и программист C и Refal",
            subjects = listOf(
                FakeSubjectsSource.getSubject(1),
                FakeSubjectsSource.getSubject(2)
            ),
            education = "",
            subjectsPrices = null,
            averagePrice = 1000,
            rating = 4.0,
            reviewsNumber = 3,
            taughtLessonNumber = 10,
            experienceYears = 1,
            languages = null,
        ),

        Tutor(
            id = Id("2"),
            name = "Данила",
            surname = "Посевин",
            middleName = "Павлович",
            about = "Я Данила Палыч: не заботал — пересдача! " +
                    "Люблю физику и математику, аналоговые приборы, тумблерочки и проводочки.",
            subjects = listOf(
                FakeSubjectsSource.getSubject(7),
                FakeSubjectsSource.getSubject(8)
            ),
            education = "2004 — Московский физико-технический институт\n" +
                    "Прикладные математика и физика",
            subjectsPrices = null,
            averagePrice = 1500,
            rating = 4.5,
            reviewsNumber = 10,
            taughtLessonNumber = 50,
            experienceYears = 2,
            languages = null,
        ),

        Tutor(
            id = Id("3"),
            name = "Иван",
            surname = "Иванов",
            middleName = "Иванович",
            about = null,
            photoSrc = null,
            subjects = listOf(
                FakeSubjectsSource.getSubject(1),
                FakeSubjectsSource.getSubject(2)
            ),
            education = "Новгородский государственный университет имени Ярослава Мудрого, " +
                    "оператор электронно-вычислительных и вычислительных машин второго разряда" +
                    "\n2014–2015 гг.",
            subjectsPrices = null,
            averagePrice = 800,
            rating = 3.9,
            reviewsNumber = 19,
            taughtLessonNumber = 100,
            experienceYears = 8,
            languages = null,
        ),
    )

    fun getTutorsList(): List<Tutor> = tutors

    fun getTutorById(id: Id): Tutor = tutors.firstOrNull { it.id.value == id.value }
        ?: throw NoSuchElementException("The tutor could not be found")
}