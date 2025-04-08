package com.uwange.myownrecipe.util

import android.R
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.uwange.myownrecipe.data.FoodArgumentData

//TODO  SPinner 아이콘으로 변경하고, edittext에 spinner 선택 값 적용하도록 변경해야함
class EditableSpinner(
    private val imageView: ImageView,
    private val editText: EditText
) {
    private var adapter: ArrayAdapter<FoodArgumentData>? = null
    private var modifiedList: MutableList<FoodArgumentData>? = null
    private var selectedItem: FoodArgumentData? = null

    fun setup(foodCategoryList: List<FoodArgumentData>) {
        modifiedList = mutableListOf<FoodArgumentData>()
        modifiedList?.add(FoodArgumentData(0, "직접입력"))
        modifiedList?.addAll(foodCategoryList)

        adapter = object: ArrayAdapter<FoodArgumentData>(
            imageView.context,
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
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val item = getItem(position)

                if(view is TextView && item != null){
                    view.text = item.name // name만 표시
                }

                return view
            }
        }

        adapter?.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        val listPopupWindow = ListPopupWindow(imageView.context).apply {
            setAdapter(adapter)
            anchorView = editText
            setOnItemClickListener { _, _, position, _ ->
                val selectedFoodData = adapter?.getItem(position)
                this@EditableSpinner.selectedItem = selectedFoodData
                //선택한 아이템에 따라 동작이 다름
                editText.setText(selectedFoodData?.name)
                if (selectedFoodData?.foodId == 0) {
                    editText.setText("")
                    editText.isFocusableInTouchMode = true
                    editText.isClickable = true
                    editText.isEnabled = true
                    editText.isFocusable = true
                    editText.requestFocus()
                    editText.setHint("직접입력")
                } else {
                    editText.setText(selectedFoodData?.name)
                    editText.isFocusableInTouchMode = false
                    editText.isClickable = false
                    editText.isEnabled = false
                    editText.isFocusable = false
                    editText.clearFocus()
                    editText.hint = null
                }

                dismiss()

            }
        }

        imageView.setOnClickListener {
            listPopupWindow.show()
        }
        editText.setOnClickListener {
            if (selectedItem?.foodId != 0)
                listPopupWindow.show()
        }
    }

    //초기 선택 되어 있는 item
    fun setInitValue(foodId: Int?) {
        val item = modifiedList?.find { it.foodId == foodId } ?: modifiedList?.first() ?: return
        val position = adapter?.getPosition(item) ?: return
        selectedItem = item

        //선택한 아이템에 따라 동작이 다름
        if (position == 0) {
            editText.setText("")
            editText.isFocusableInTouchMode = true
            editText.isClickable = true
            editText.isEnabled = true
            editText.isFocusable = true
            editText.requestFocus()
            editText.setHint("직접입력")
        } else {
            editText.setText(item.name)
            editText.isFocusableInTouchMode = false
            editText.isClickable = false
            editText.isEnabled = false
            editText.isFocusable = false
            editText.clearFocus()
            editText.hint = null
        }
    }
}