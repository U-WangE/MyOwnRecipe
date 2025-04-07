package com.uwange.myownrecipe.util

import android.R
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.uwange.myownrecipe.data.FoodArgumentData

class EditableSpinner(
    private val spinner: Spinner,
    private val editText: EditText
) {
    fun setup(foodCategoryList: List<FoodArgumentData>) {
        val modifiedList = mutableListOf<FoodArgumentData>()
        modifiedList.add(FoodArgumentData(0, "직접입력"))
        modifiedList.addAll(foodCategoryList)
        val adapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, modifiedList)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (position == 0) {
                    editText.setText("")
                    editText.visibility = VISIBLE
                } else {
                    editText.setText("")
                    editText.visibility = GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                editText.setText("")
                editText.visibility = GONE
            }
        }
    }

    fun setInitValue(foodId: Int) {
        val adapter = spinner.adapter as ArrayAdapter<FoodArgumentData>
        val position = adapter.getPosition(adapter.getItem(foodId))
        spinner.setSelection(position)
    }
}