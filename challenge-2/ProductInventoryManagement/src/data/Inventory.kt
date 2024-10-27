package data

import java.text.DecimalFormat

/**
 * Enum representing the options for sorting the inventory.
 */
enum class InventorySortingOption {
    PRICE,
    QUANTITY
}

/**
 * Manages a list of products and provides methods for inventory operations like
 * calculating total value, finding the most expensive product, checking stock availability,
 * and sorting products.
 *
 * @property productsList The list of products in the inventory.
 * @see Product
 *
 */
class Inventory(var productsList: List<Product>) {
    /**
     * Calculates the total value of all products in the inventory.
     *
     * @return The total value of all products (price * quantity for each product).
     */
    fun totalValue(): Double {
        return this.productsList.sumOf { product: Product ->
            product.price * product.quantity
        }
    }

    /**
     * Finds the most expensive product in the inventory.
     *
     * @return The name of the product with the highest price, or "No products available" if the inventory is empty.
     */
    fun theMostExpensiveProduct(): String {
        return this.productsList.maxByOrNull { product: Product ->
            product.price
        }?.name ?: ""
    }

    /**
     * Checks if a product with the specified name is in stock.
     *
     * @param productName The name of the product to check.
     * @return `true` if the product is in stock, `false` otherwise.
     */
    fun isInStock(productName: String): Boolean {
        return this.productsList.any { product: Product -> product.name == productName }
    }

    /**
     * Sorts the products in the inventory based on the specified option (price or quantity)
     * and the sorting order (ascending or descending).
     *
     * @param isDescending If `true`, sorts in descending order; otherwise, sorts in ascending order.
     * @param option The sorting option, either by `PRICE` or `QUANTITY`.
     */
    fun sort(
        isDescending: Boolean,
        option: InventorySortingOption
    ) {
        when (option) {
            InventorySortingOption.PRICE -> {
                if (isDescending) {
                    this.productsList = this.productsList.sortedByDescending { product: Product ->
                        product.price
                    }
                } else {
                    this.productsList = this.productsList.sortedBy { product: Product ->
                        product.price
                    }
                }
            }

            InventorySortingOption.QUANTITY -> {
                if (isDescending) {
                    this.productsList = this.productsList.sortedByDescending { product: Product ->
                        product.quantity
                    }
                } else {
                    this.productsList = this.productsList.sortedBy { product: Product ->
                        product.quantity
                    }
                }
            }
        }
    }

    /**
     * Displays the list of products in the inventory with their name, price, and quantity.
     */
    fun showProducts() {
        this.productsList.forEach { product: Product ->
            println("${product.name}: price: ${product.price}, quantity: ${product.quantity}")
        }
    }

    companion object {
        /**
         * Formatter for displaying any value of price in a formatted string.
         */
        val VALUE_FORMATTER = DecimalFormat("#,###.##")
    }
}