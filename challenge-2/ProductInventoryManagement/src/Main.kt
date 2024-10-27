import data.Inventory
import data.InventorySortingOption
import data.Product

fun main() {
    val inventory = Inventory(
        productsList = listOf(
            Product(name = "Laptop", price = 999.99, quantity = 5),
            Product(name = "Smartphone", price = 499.99, quantity = 10),
            Product(name = "Tablet", price = 299.99, quantity = 0),
            Product(name = "Smartwatch", price = 199.99, quantity = 3)
        )
    )

//    Calculate and display the total inventory value.
    val totalInventoryValue: Double = inventory.totalValue()
    val resultFormatted: String = Inventory.VALUE_FORMATTER.format(totalInventoryValue)
    println("Total inventory value: $resultFormatted")

//    Display the most expensive product.
    println("The most expensive product: ${inventory.theMostExpensiveProduct()}")

//    Check if a product named "Headphones" is in stock.
    println("Is \"Headphones\" in stock: ${inventory.isInStock(productName = "Headphones")}")

    println()

//    Sort the products in descending order by price and display the sorted list.
    println("Products sorted by price (descending):")
    inventory.sort(
        isDescending = true,
        option = InventorySortingOption.PRICE
    )
    inventory.showProducts()

    println()

//    Sort the products in ascending order by price and display the sorted list.
    println("Products sorted by price (ascending):")
    inventory.sort(
        isDescending = false,
        option = InventorySortingOption.PRICE
    )
    inventory.showProducts()

    println()

//    Sort the products in descending order by quantity and display the sorted list.
    println("Products sorted by quantity (descending):")
    inventory.sort(
        isDescending = true,
        option = InventorySortingOption.QUANTITY
    )
    inventory.showProducts()

    println()

//    Sort the products in ascending order by quantity and display the sorted list.
    println("Products sorted by quantity (ascending):")
    inventory.sort(
        isDescending = false,
        option = InventorySortingOption.QUANTITY
    )
    inventory.showProducts()
}