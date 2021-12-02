package com.example.mednotes.api

import com.example.mednotes.R
import com.example.mednotes.data.Medicine
import com.example.mednotes.data.Moment
import com.example.mednotes.usecase.parseDate
import java.util.*

class MomentsApi {
    companion object {

        private fun breakfast(day: String): Moment {
            return Moment(
                title = "Ontbijt",
                at = parseDate("2019-01-$day 08:00")!!,
                withIcon = R.drawable.ic_breakfast,
                withMedicines = listOf(
                    Medicine(
                        name = "Paracetamol",
                        isTaken = Random().nextBoolean()
                    ),
                )
            )
        }

        private fun lunch(day: String): Moment {
            return Moment(
                title = "Lunch",
                at = parseDate("2019-01-$day 12:00")!!,
                withIcon = R.drawable.ic_lunch,
                withMedicines = listOf(
                    Medicine(
                        name = "Acebutol",
                        isTaken = Random().nextBoolean()
                    ),
                )
            )
        }

        private fun atWork(day: String): Moment {
            return Moment(
                title = "Op`t werk",
                at = parseDate("2019-01-$day 15:00")!!,
                withIcon = R.drawable.ic_work,
                withMedicines = listOf(
                    Medicine(
                        name = "Paracetamol",
                        isTaken = Random().nextBoolean()
                    ),
                )
            )
        }

        private fun bedTime(day: String): Moment {
            return Moment(
                title = "Voor het slapen",
                at = parseDate("2019-01-$day 22:00")!!,
                withIcon = R.drawable.ic_bedtime,
                withMedicines = listOf(
                    Medicine(
                        name = "Melatonin",
                        isTaken = Random().nextBoolean()
                    ),
                )
            )
        }

        val GetMoments = listOf(
            breakfast(day = "01"),
            lunch(day = "01"),

            breakfast(day = "02"),
            lunch(day = "02"),
            atWork(day = "02"),

            breakfast(day = "03"),
            lunch(day = "03"),

            breakfast(day = "04"),
            atWork(day = "04"),

            breakfast(day = "06"),
            lunch(day = "06"),
            atWork(day = "06"),

            bedTime(day = "07"),

            breakfast(day = "08"),
            lunch(day = "08"),

            breakfast(day = "09"),
            lunch(day = "09"),
            atWork(day = "09"),

            breakfast(day = "10"),
            lunch(day = "10"),

            breakfast(day = "11"),
            atWork(day = "11"),

            breakfast(day = "13"),
            lunch(day = "13"),
            atWork(day = "13"),

            bedTime(day = "14"),
        )
    }
}