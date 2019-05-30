package msc.fooxer.datereminderwidget

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        c.add(Calendar.DATE,1)
        val dpd = DatePickerDialog(activity, this, year, month, day)
        dpd.datePicker.minDate = c.time.time

        return dpd
    }
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        newDate = Date(year-1900, month, day)
        val difference: Long = newDate.time - myDate.time
        daysDiff = (difference / (1000*60*60*24))
    }

}
