package com.uwange.myownrecipe.util

import android.R
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import com.uwange.myownrecipe.data.FoodArgumentData

/**
 * ImageView, EditText Click 시 -> EditText 아래애 Sinner DropDown List 표시
 * if (position == 0) EditText Enable  else EditText Disable
 *
 * @return selectedItem : FoodArgumentData
 *  position 0  ->  FoodArgumentData(-1, "직접입력")
 *  position ...->  FoodArgumentData(foodId, foodName)
 */
class EditableSpinner(
    private val imageView: ImageView,
    private val editText: EditText
) {
    private var adapter: ArrayAdapter<FoodArgumentData>? = null
    private var modifiedList: MutableList<FoodArgumentData>? = null
    private var selectedItem: FoodArgumentData? = null

    fun setup(foodCategoryList: List<FoodArgumentData>) {
        modifiedList = mutableListOf<FoodArgumentData>()
        modifiedList?.add(FoodArgumentData(-1, "직접입력"))
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
                if (selectedFoodData?.foodId == -1) {
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
            if (selectedItem?.foodId != -1)
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

    fun getSelectedItem(): FoodArgumentData? {
        return if (selectedItem != null && editText.text.isNotBlank())
            FoodArgumentData(selectedItem!!.foodId, editText.text.toString())
        else
            null
    }
}