package com.uwnage.myownrecipe

import android.content.Context
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uwnage.myownrecipe.adapter.FoodItemAdapter
import com.uwnage.myownrecipe.data.FoodItem
import com.uwnage.myownrecipe.databinding.ItemFoodCardBinding

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class FoodItemAdapterTest {

    private lateinit var context: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodItemAdapter
    private lateinit var foodList: List<FoodItem>


    private fun dummyData(): List<FoodItem> {
        return listOf(
            FoodItem(
                1,
                "https://i.pravatar.cc/300",
                "A vibrant close-up of a colorful salad with fresh greens, tomatoes, and a light vinaigrette.", // image description
                "Mediterranean Quinoa Salad", // food name
                4.5f,
                "A refreshing and healthy salad packed with quinoa, fresh vegetables, and a zesty lemon dressing. Perfect for a light lunch or a side dish." // supportText
            ),
            FoodItem(
                2,
                "https://i.pravatar.cc/400",
                "A steaming bowl of ramen with rich broth, tender noodles, a soft-boiled egg, and sliced pork.", // image description
                "Tonkotsu Ramen", // food name
                3.8f,
                "A classic Japanese ramen with a creamy pork bone broth, thin noodles, and flavorful toppings. A comforting and satisfying meal." // supportText
            ),
            FoodItem(
                3,
                "https://i.pravatar.cc/500",
                "A golden-brown pizza with melted cheese, pepperoni, and a crispy crust.", // image description
                "Pepperoni Pizza", // food name
                4.2f,
                "A timeless favorite! Our pepperoni pizza features a crispy crust, tangy tomato sauce, melted mozzarella, and savory pepperoni." // supportText
            ),
            FoodItem(
                4,
                "https://i.pravatar.cc/600",
                "A juicy burger with melted cheese, lettuce, tomato, and a special sauce, served on a sesame seed bun.", // image description
                "Classic Cheeseburger", // food name
                4.0f,
                "Our classic cheeseburger is made with a juicy beef patty, melted cheddar cheese, fresh lettuce, ripe tomato, and our signature sauce." // supportText
            ),
            FoodItem(
                5,
                "https://i.pravatar.cc/700",
                "A plate of crispy fried chicken with a golden-brown crust, served with a side of creamy mashed potatoes.", // image description
                "Crispy Fried Chicken", // food name
                3.5f,
                "Our crispy fried chicken is perfectly seasoned and fried to golden perfection. Served with creamy mashed potatoes for a comforting meal." // supportText
            ),
            FoodItem(
                6,
                "https://i.pravatar.cc/800",
                "A decadent chocolate cake with rich frosting and chocolate shavings.", // image description
                "Double Chocolate Cake", // food name
                4.8f,
                "Indulge in our double chocolate cake, a moist and rich chocolate cake with decadent chocolate frosting and shavings. A chocolate lover's dream!" // supportText
            ),
            FoodItem(
                7,
                "https://i.pravatar.cc/900",
                "A colorful plate of sushi rolls with fresh fish, vegetables, and rice.", // image description
                "Assorted Sushi Rolls", // food name
                3.2f,
                "Enjoy a variety of fresh and flavorful sushi rolls, including California rolls, spicy tuna rolls, and more. Perfect for sharing or enjoying solo." // supportText
            ),
            FoodItem(
                8,
                "https://i.pravatar.cc/1000",
                "A plate of fluffy pancakes with fresh berries, whipped cream, and maple syrup.", // image description
                "Berry Pancakes", // food name
                4.7f,
                "Start your day with our fluffy pancakes topped with fresh mixed berries, whipped cream, and a drizzle of maple syrup. A delightful breakfast treat!" // supportText
            ),
            FoodItem(
                9,
                "https://i.pravatar.cc/1100",
                "A close-up of a spicy Thai curry with coconut milk, vegetables, and tofu.", // image description
                "Thai Green Curry", // food name
                3.9f,
                "Experience the bold flavors of our Thai green curry, made with coconut milk, fresh vegetables, and tofu. A spicy and aromatic dish." // supportText
            ),
            FoodItem(
                10,
                "https://i.pravatar.cc/1200",
                "A plate of spaghetti with rich tomato sauce, meatballs, and parmesan cheese.", // image description
                "Spaghetti and Meatballs", // food name
                4.3f,
                "A classic Italian dish! Our spaghetti and meatballs are made with a rich tomato sauce, juicy meatballs, and a sprinkle of parmesan cheese." // supportText
            )
        )
    }

    @Before
    fun setUp() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        context = ContextThemeWrapper(appContext, R.style.Base_Theme_MyOwnRecipe)
        recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context)

        foodList = dummyData()

        adapter = FoodItemAdapter(foodList) {
            Log.i("FoodItemAdapterTest", "Clicked on food item with ID: $it")
        }

        recyclerView.adapter = adapter
    }

    @Test
    fun testItemCount() {
        assertEquals(foodList.size, adapter.itemCount)
    }
}