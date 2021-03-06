package apps.jizzu.simpletodo.activity.base

import android.animation.Animator
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import apps.jizzu.simpletodo.R
import apps.jizzu.simpletodo.activity.MainActivity
import apps.jizzu.simpletodo.fragment.DatePickerFragment
import apps.jizzu.simpletodo.fragment.TimePickerFragment
import apps.jizzu.simpletodo.utils.Utils
import kotlinx.android.synthetic.main.activity_add_task.*
import kotterknife.bindView
import java.util.*


abstract class BaseTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    lateinit var mCalendar: Calendar
    val mReminderSwitch: SwitchCompat by bindView(R.id.reminderSwitch)
    val mReminderLayout: RelativeLayout by bindView(R.id.reminderContainer)
    val mTitleEditText: EditText by bindView(R.id.taskTitle)
    val mDateEditText: EditText by bindView(R.id.taskDate)
    val mTimeEditText: EditText by bindView(R.id.taskTime)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCalendar = Calendar.getInstance()
        MainActivity.mActivityIsShown = true
        screenResolutionCheck()
        showKeyboard()
    }

    override fun onResume() {
        super.onResume()
        setOnClickListeners()
    }

    fun initToolbar(titleText: String) {
        title = ""
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = titleText

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        }
    }

    private fun screenResolutionCheck() {
        val displayMetrics = DisplayMetrics()
        (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        Log.d(ContentValues.TAG, "width = $width, height = $height")

        if (width <= 480 || height <= 800) {
            tvSetReminder.setText(R.string.set_reminder_short)
            taskDateLayout.layoutParams.width = 150
            taskTimeLayout.layoutParams.width = 150
        }
    }

    private fun setOnClickListeners() {
        mReminderSwitch.setOnTouchListener { _, event -> event.actionMasked == MotionEvent.ACTION_MOVE }
        mReminderSwitch.setOnClickListener {
            if (mReminderSwitch.isChecked) {
                hideKeyboard(mTitleEditText)
                mReminderLayout.animate().alpha(1.0f).setDuration(500).setListener(
                        object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {
                                mReminderLayout.visibility = View.VISIBLE
                            }

                            override fun onAnimationEnd(animation: Animator) {}

                            override fun onAnimationCancel(animation: Animator) {}

                            override fun onAnimationRepeat(animation: Animator) {}
                        }
                )
            } else {
                mReminderLayout.animate().alpha(0.0f).setDuration(500).setListener(
                        object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {

                            }

                            override fun onAnimationEnd(animation: Animator) {
                                mReminderLayout.visibility = View.INVISIBLE
                            }

                            override fun onAnimationCancel(animation: Animator) {

                            }

                            override fun onAnimationRepeat(animation: Animator) {

                            }
                        }
                )
                mDateEditText.text = null
                mTimeEditText.text = null
            }
        }

        mDateEditText.setOnClickListener {
            mDateEditText.text = null
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(fragmentManager, "DatePickerFragment")
        }

        mTimeEditText.setOnClickListener {
            mTimeEditText.text = null
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(fragmentManager, "TimePickerFragment")
        }

        container.setOnClickListener { hideKeyboard(mTitleEditText) }
    }

    private fun showKeyboard() {
        val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideKeyboard(editText: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard(mTitleEditText)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            if (item.itemId == android.R.id.home) {
                hideKeyboard(mTitleEditText)
                onBackPressed()
                true
            } else false

    override fun onDateSet(datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        mCalendar.set(Calendar.YEAR, year)
        mCalendar.set(Calendar.MONTH, monthOfYear)
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        mDateEditText.setText(Utils.getDate(mCalendar.timeInMillis))
    }

    override fun onTimeSet(timePicker: TimePicker, hourOfDay: Int, minute: Int) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        mCalendar.set(Calendar.MINUTE, minute)
        mCalendar.set(Calendar.SECOND, 0)
        mTimeEditText.setText(Utils.getTime(mCalendar.timeInMillis))
    }
}