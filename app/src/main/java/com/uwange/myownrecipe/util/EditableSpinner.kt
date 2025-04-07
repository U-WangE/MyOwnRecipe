package com.uwange.myownrecipe.util

import android.R
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.uwange.myownrecipe.data.FoodArgumentData

//TODO  SPinner 아이콘으로 변경하고, edittext에 spinner 선택 값 적용하도록 변경해야함
class EditableSpinner(
    private val spinner: Spinner,
    private val editText: EditText
) {
    private var adapter: ArrayAdapter<FoodArgumentData>? = null
    private var modifiedList: MutableList<FoodArgumentData>? = null

    fun setup(foodCategoryList: List<FoodArgumentData>) {
        modifiedList = mutableListOf<FoodArgumentData>()
        modifiedList?.add(FoodArgumentData(0, "직접입력"))
        modifiedList?.addAll(foodCategoryList)

        adapter = object: ArrayAdapter<FoodArgumentData>(
            spinner.context,
            R.layout.simple_spinner_item,
            modifiedList!!
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val item = getItem(position)

                if (view is TextView && item != null)
                    view.text = item.name
                return view
            }
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getView(position, convertView, parent)
                val item = getItem(position)

                if(view is TextView && item != null){
                    view.text = item.name // name만 표시
                }

                return view
            }
        }

        adapter?.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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

    fun setInitValue(foodId: Int?) {
        val item = modifiedList?.find { it.foodId == foodId } ?: modifiedList?.first() ?: return
        val position = adapter?.getPosition(item) ?: return
        spinner.setSelection(position)

        if (item.foodId == 0) {
            editText.visibility = VISIBLE
        } else {
            editText.visibility = GONE
        }
    }
}