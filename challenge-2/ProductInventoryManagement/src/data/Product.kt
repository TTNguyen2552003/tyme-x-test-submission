package data

/**
 * Represents a product with a name, price, and quantity.
 *
 * @property name The name of the product.
 * @property price The price of the product.
 * @property quantity The quantity of the product in stock.
 */
class Product(
    val name: String,
    val price: Double,
    val quantity: Int
)