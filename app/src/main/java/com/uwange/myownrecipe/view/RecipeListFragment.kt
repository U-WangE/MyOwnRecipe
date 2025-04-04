package com.uwange.myownrecipe.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.uwange.myownrecipe.adapter.RecipeItemAdapter
import com.uwange.myownrecipe.data.RecipeItem
import com.uwange.myownrecipe.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment() {
    private var _binding: FragmentRecipeListBinding? = null
    private val binding: FragmentRecipeListBinding get() = _binding!!

    private val args: RecipeListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)

        binding.tvFoodName.text = args.foodArgumentData.name

        setupRecipeRecyclerView()

        return binding.root
    }

    private fun setupRecipeRecyclerView() {
        val recipeList = listOf(
            RecipeItem(
                1,
                1,
                "https://i.pravatar.cc/300",
                "A vibrant close-up of a colorful salad with fresh greens, tomatoes, and a light vinaigrette.", // image description
                true,
                "Mediterranean Quinoa Salad", // food name
                4.5f,
                "A refreshing and healthy salad packed with quinoa, fresh vegetables, and a zesty lemon dressing. Perfect for a light lunch or a side dish." // supportText
            ),
            RecipeItem(
                2,
                1,
                "https://i.pravatar.cc/400",
                "A steaming bowl of ramen with rich broth, tender noodles, a soft-boiled egg, and sliced pork.", // image description
                false,
                "Tonkotsu Ramen", // food name
                3.8f,
                "A classic Japanese ramen with a creamy pork bone broth, thin noodles, and flavorful toppings. A comforting and satisfying meal." // supportText
            ),
            RecipeItem(
                3,
                1,
                "https://i.pravatar.cc/500",
                "A golden-brown pizza with melted cheese, pepperoni, and a crispy crust.", // image description
                false,
                "Pepperoni Pizza", // food name
                4.2f,
                "A timeless favorite! Our pepperoni pizza features a crispy crust, tangy tomato sauce, melted mozzarella, and savory pepperoni." // supportText
            ),
            RecipeItem(
                4,
                1,
                "https://i.pravatar.cc/600",
                "A juicy burger with melted cheese, lettuce, tomato, and a special sauce, served on a sesame seed bun.", // image description
                false,
                "Classic Cheeseburger", // food name
                4.0f,
                "Our classic cheeseburger is made with a juicy beef patty, melted cheddar cheese, fresh lettuce, ripe tomato, and our signature sauce." // supportText
            ),
            RecipeItem(
                5,
                1,
                "https://i.pravatar.cc/700",
                "A plate of crispy fried chicken with a golden-brown crust, served with a side of creamy mashed potatoes.", // image description
                false,
                "Crispy Fried Chicken", // food name
                3.5f,
                "Our crispy fried chicken is perfectly seasoned and fried to golden perfection. Served with creamy mashed potatoes for a comforting meal." // supportText
            ),
            RecipeItem(
                6,
                1,
                "https://i.pravatar.cc/800",
                "A decadent chocolate cake with rich frosting and chocolate shavings.", // image description
                false,
                "Double Chocolate Cake", // food name
                4.8f,
                "Indulge in our double chocolate cake, a moist and rich chocolate cake with decadent chocolate frosting and shavings. A chocolate lover's dream!" // supportText
            ),
            RecipeItem(
                7,
                1,
                "https://i.pravatar.cc/900",
                "A colorful plate of sushi rolls with fresh fish, vegetables, and rice.", // image description
                false,
                "Assorted Sushi Rolls", // food name
                3.2f,
                "Enjoy a variety of fresh and flavorful sushi rolls, including California rolls, spicy tuna rolls, and more. Perfect for sharing or enjoying solo." // supportText
            ),
            RecipeItem(
                8,
                1,
                "https://i.pravatar.cc/1000",
                "A plate of fluffy pancakes with fresh berries, whipped cream, and maple syrup.", // image description
                false,
                "Berry Pancakes", // food name
                4.7f,
                "Start your day with our fluffy pancakes topped with fresh mixed berries, whipped cream, and a drizzle of maple syrup. A delightful breakfast treat!" // supportText
            ),
            RecipeItem(
                9,
                1,
                "https://i.pravatar.cc/1100",
                "A close-up of a spicy Thai curry with coconut milk, vegetables, and tofu.", // image description
                false,
                "Thai Green Curry", // food name
                3.9f,
                "Experience the bold flavors of our Thai green curry, made with coconut milk, fresh vegetables, and tofu. A spicy and aromatic dish." // supportText
            ),
            RecipeItem(
                10,
                1,
                "https://i.pravatar.cc/1200",
                "A plate of spaghetti with rich tomato sauce, meatballs, and parmesan cheese.", // image description
                false,
                "Spaghetti and Meatballs", // food name
                4.3f,
                "A classic Italian dish! Our spaghetti and meatballs are made with a rich tomato sauce, juicy meatballs, and a sprinkle of parmesan cheese." // supportText
            )
        )
            .sortedWith(compareBy<RecipeItem> { !it.bookmark } // Sort bookmarks to the top
                .thenByDescending { it.score }) // Then sort by score in descending order

        binding.rvFoodList.adapter = RecipeItemAdapter(recipeList) { id, name ->
            Log.d("여기 id name", id.toString() + name)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}