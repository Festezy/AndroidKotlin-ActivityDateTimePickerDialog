package com.ariqandrean.myapplication12

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.Month
import java.time.Year
import java.util.*

class MainActivity : AppCompatActivity() {
    var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addRecord()

        // Untuk Menyembunyikan keyboard Ketika pertama kali dipilih
        etDate.inputType = InputType.TYPE_NULL
        etTime.inputType = InputType.TYPE_NULL

        //Aksi yang akan dijalankan ketika tanggal telah dipilih
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayofmonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DATE, dayofmonth)
//            updateDateInView()
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            etDate.setText(sdf.format(calendar.time))
        }
        // using override
//        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
//            override fun onDateSet(view: DatePicker?, year:Int, month:Int, dayofmonth: Int ){
//                calendar.set(Calendar.YEAR, year)
//                calendar.set(Calendar.MONTH, month)
//                calendar.set(Calendar.DATE, dayofmonth)
//                updateDateInView()
//            }
//        }
        // aksi yang akan dijalankan ketika timepicker di pilih
        val timeSetListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                calendar.set(Calendar.HOUR, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

//                updateTimeInView()
                val myFormat = "HH:mm"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                etTime.setText(sdf.format(calendar.time))
            }
        }

        etDate!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View){
                closeKeyboard()
                DatePickerDialog(this@MainActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up)
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        })
        etTime.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                closeKeyboard()
                TimePickerDialog(this@MainActivity,
                timeSetListener,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE), true).show()
            }

        })
    }

    private fun addRecord(){
        val date = etDate.text.toString()
        val time = etTime.text.toString()
        val dateTime = "${date}(${time})"
        val description = etDescription.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (!date.isEmpty() && !time.isEmpty() && !description.isEmpty()){
            val status = databaseHandler.addActivity(MyActivityModel(0, dateTime, description))
            if (status > -1){
                Toast.makeText(applicationContext, "Record Saved", Toast.LENGTH_SHORT).show()
                etDate.text.clear()
                etTime.text.clear()
                etDescription.text.clear()
                closeKeyboard()
            }
        } else {
            Toast.makeText(applicationContext, "Datetime or Description cannot be blank", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getItemList(): ArrayList<MyActivityModel> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val activityList: ArrayList<MyActivityModel> = databaseHandler.viewActivity()

        return activityList
    }

    //Method to show data to recyclerView
    private fun setupListOfDataIntoRecyclerView(){
        if(getItemList().size > 0){
            rvItemList.visibility = View.VISIBLE
            tvNoRecordAvailable.visibility = View.GONE

            rvItemList.layoutManager = LinearLayoutManager(this)
            val itemAdapter = ItemAdapter(this, getItemList())
            rvItemList.adapter = itemAdapter
        } else {
            rvItemList.visibility = View.GONE
            tvNoRecordAvailable.visibility = View.VISIBLE
        }
    }

    private fun closeKeyboard() {

    }

//    private fun updateDateInView() {
//        val myFormat = "dd/MM/yyyy"
//        val sdf = SimpleDateFormat(myFormat, Locale.US)
//        etDate.setText(sdf.format(calendar.time))
//    }

    private fun updateTimeInView() {

    }
}