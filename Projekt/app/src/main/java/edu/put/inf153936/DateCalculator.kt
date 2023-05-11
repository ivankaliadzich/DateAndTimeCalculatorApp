package edu.put.inf153936

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import java.util.Calendar
import java.util.Locale

class DateCalculator : AppCompatActivity() {

    private var YearFirst = 0
    private var MonthFirst = 0
    private var DayFirst = 0
    private var YearSecond = 0
    private var MonthSecond =0
    private var DaySecond = 0

    private var DaysBetweenDates: EditText?=null
    private var WorkingDaysBetweenDates: TextView?=null
    private var DatePickerSecond: DatePicker?=null
    private var inputMethodManager: InputMethodManager? = null
    private var calendar: Calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Locale.setDefault(Locale.UK)
        setContentView(R.layout.activity_date_calculator)
        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        WorkingDaysBetweenDates = findViewById(R.id.workingDaysBetweenDates)
        val DatePickerFirst = findViewById<DatePicker>(R.id.datePickerFirst)
        DatePickerSecond = findViewById(R.id.datePickerSecond)

        val AddButton = findViewById<Button>(R.id.add)
        val DeductButton = findViewById<Button>(R.id.deduct)
        AddButton.setOnClickListener{addDays(DaysBetweenDates?.editableText.toString())}
        DeductButton.setOnClickListener{deductDays(DaysBetweenDates?.editableText.toString())}

        DaysBetweenDates = findViewById(R.id.betweenDates)
        DaysBetweenDates?.setOnEditorActionListener{v, actionID, _ ->
            if(actionID == EditorInfo.IME_ACTION_DONE){
                clear(v)
            }
            false
        }

        YearFirst = DatePickerFirst.year
        MonthFirst = DatePickerFirst.month + 1
        DayFirst = DatePickerFirst.dayOfMonth
        YearSecond = DatePickerSecond!!.year
        MonthSecond = DatePickerSecond!!.month+1
        DaySecond = DatePickerSecond!!.dayOfMonth

        countDays(YearFirst, MonthFirst, DayFirst, YearSecond, MonthSecond, DaySecond)

        DatePickerFirst.setOnDateChangedListener{_, year, month, day ->
            YearFirst = year
            MonthFirst = month + 1
            DayFirst = day
            countDays(YearFirst, MonthFirst, DayFirst, YearSecond, MonthSecond, DaySecond)
        }

        DatePickerSecond!!.setOnDateChangedListener{_, year, month ,day ->
            YearSecond = year
            MonthSecond = month + 1
            DaySecond = day
            countDays(YearFirst, MonthFirst, DayFirst, YearSecond, MonthSecond, DaySecond)
        }
    }


    private fun countDays( YearFirst: Int, MonthFirst: Int, DayFirst: Int, YearSecond: Int, MonthSecond: Int, DaySecond: Int) : Int{
        var countAllDays = 0
        var countWeekends = 0
        var countHolidays = 0
        var countEasterDays = 0
        var Day_Week: Int
        var EasterDayFirst: Pair<Int, Int>
        var EasterDaySecond: Pair<Int, Int>
        var LENGTH: Int
        var FLAG = false
        var year1 = YearFirst
        var month1 = MonthFirst
        var day1 = DayFirst
        var year2 = YearSecond
        var month2 = MonthSecond
        var day2 = DaySecond
        var CHECK = true

        if (year1 > year2)
            CHECK = false
        else if (year1 == year2){
            if (month1 > month2)
                CHECK = false
            else if (month1 == month2) {
                if (day1 > day2)
                    CHECK = false
            }
        }
        if (!CHECK) {
            year1 = year2.also { year2 = year1 }
            month1 = month2.also { month2 = month1 }
            day1 = day2.also { day2 = day1 }
        }

        calendar.set(year1, month1 - 1, day1)
        Day_Week = calendar.get(Calendar.DAY_OF_WEEK)

        while (!FLAG) {
            EasterDayFirst = getFirstEasterDate(year1)
            EasterDaySecond = getSecondEasterDate(EasterDayFirst)

            while (!FLAG) {
                LENGTH = MonthLength(month1, year1)

                do {
                    if (year1 == year2 && month1 == month2 && day1 == day2) {
                        FLAG = true
                        break
                    }

                    if (Day_Week == 1 || Day_Week == 7)
                        countWeekends++
                    else {
                        if ((month1 == 1 && day1 == 1) || (month1 == 1 && day1 == 6)  || (month1 == 5 && day1 == 1) ||
                            (month1 == 5 && day1 == 3) || (month1 == 8 && day1 == 15) || (month1 == 11 && day1 == 1) ||
                            (month1 == 11 && day1 == 11) || (month1 == 12 && day1 == 25) || (month1 == 12 && day1 == 26 ))
                            countHolidays++

                        if (month1 in 3..5 && Day_Week == 2)
                            if (EasterDayFirst.second == month1 && EasterDayFirst.first == day1) {
                                countEasterDays++
                            }

                        if (month1 in 4..7 && Day_Week == 5)
                            if (EasterDaySecond.second == month1 && EasterDaySecond.first == day1) {
                                countEasterDays++
                            }
                    }

                    if (Day_Week++ >= 7)
                        Day_Week = 1

                    countAllDays++
                } while (day1++ < LENGTH)

                day1 = 1
                if (month1++ >= 12)
                    break

            }
            month1 = 1
            year1++
        }
        DaysBetweenDates?.setText(countAllDays.toString())
        WorkingDaysBetweenDates?.text = "Working days between dates: ${countAllDays - countWeekends - countHolidays - countEasterDays}"
        return countAllDays
    }


    private fun MonthLength(MONTH: Int, YEAR: Int): Int{
        var ans :Int
        if (MONTH == 1 || MONTH == 3 || MONTH == 5 || MONTH == 7 || MONTH == 8 || MONTH == 10 || MONTH == 12  ){
            ans = 31
        } else if (MONTH == 4 || MONTH == 6 || MONTH == 9  || MONTH == 11  ){
            ans = 30
        } else {
            if (YEAR % 400 == 0){
                ans = 29
            } else if (YEAR % 100 ==0 ){
                ans = 28
            } else if (YEAR % 4 == 0){
                ans = 29
            } else {
                ans = 28
            }
        }
    return ans
    }


    private fun getFirstEasterDate(year: Int): Pair<Int, Int> {
        val a: Int = year % 19
        val b: Int = year / 100
        val c: Int = year % 100
        val d: Int = b / 4
        val e: Int = b % 4
        val f: Int = (b + 8) / 25
        val g: Int = (b - f + 1) / 3
        val h: Int = (19 * a + b - d - g + 15) % 30
        val i: Int = c / 4
        val k: Int = c % 4
        val l: Int = (32 + 2 * e + 2 * i - h - k) % 7
        val m: Int = (a + 11 * h + 22 * l) / 451
        val p: Int = (h + l - 7 * m + 114)
        var day = p % 31 + 1
        var month = p / 31

        if ((month == 3 && day == 31) || (month == 4 && day == 30)) {
            month++
            day = 1
        } else
            day++

        return Pair(day, month)
    }


    private fun getSecondEasterDate(firstEasterDate: Pair<Int, Int>): Pair<Int, Int> {
        var days = 59 + firstEasterDate.first
        var month = firstEasterDate.second

        while (days > 0)
            days -= MonthLength(month++, 2000)

        return Pair(days + MonthLength(--month, 200), month)
    }


    private fun addDays(count: String) {
        var countDays: Int = count.toInt()
        var currentYear = YearFirst
        var currentMonth = MonthFirst
        var currentDay = DayFirst
        var lengthMonth: Int
        var exitFlag = false
        clear(DaysBetweenDates!!)

        while (!exitFlag) {
            while (!exitFlag) {
                lengthMonth = MonthLength(currentMonth, currentYear)

               do {
                    if (countDays-- == 0)
                    {
                        DatePickerSecond!!.updateDate(currentYear, currentMonth - 1, currentDay)
                        exitFlag = true
                        break
                    }

                } while (currentDay++ < lengthMonth)
                currentDay = 1

                if (currentMonth++ >= 12)
                    break
            }

            currentMonth = 1
            currentYear++
        }
    }


    private fun deductDays(count: String) {
        var countDays: Int = count.toInt()
        var currentYear = YearFirst
        var currentMonth = MonthFirst
        var currentDay = DayFirst
        var exitFlag = false
        clear(DaysBetweenDates!!)

        while (!exitFlag) {
            while (!exitFlag) {
                do {
                    if (countDays-- == 0) {
                        DatePickerSecond!!.updateDate(currentYear, currentMonth - 1, currentDay)
                        exitFlag = true
                        break
                    }
                } while (currentDay-- > 1)

                if (currentMonth-- <= 1)
                    break

                currentDay = MonthLength(currentMonth, currentYear)
            }

            currentMonth = 12
            currentDay = MonthLength(currentMonth, --currentYear)
        }
    }


    private fun clear(view: View) {
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}