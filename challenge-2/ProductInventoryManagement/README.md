# Question 2.1: Product Inventory Management
This repository contains solution for the Android internship technical assessment. The problem is implemented in Kotlin.

### Problem
You are given a **Product** class with the following fields:
- **name:** A String representing the product's name
- **price:** A Double representing the product's price
- **quantity:** Ant Int representing the number of items in stock

**You have an inventory of products as follows**
```
    Product List:
    
    Laptop: price 999.99, quantity 5
    Smartphone: price 499.99, quantity 10
    Tablet: price 299.99, quantity 0
    Smartwatch: price 199.99, quantity 3
```

Write a program to perform the following tasks:
- **Calculate the total inventory**
    - Return the total of all products in stock
    - Example output: 10,599.82
- **Find the most expensive product**
    - Return the name of the product with the highest price
    - Example output: **"Laptop"**
- **Check if the product named "Headphones" is in stock**
    - Return **true** or **false** depending on whether the product with the name "Headphones" exist in the inventory.
- **Sort the products in descending/ascending order with options like by price, quantity**

## Project Structure

```
root folder
|____src
|    |____data
|    |    |____Inventory.kt
|    |    |____Product.kt
|    |____Main.kt
|____...
```

## Setup

### Prerequisites
- IDE (IntelliJ is recommended)
- JDK (Version 21 is recommended)
- Kotlin (version 2.0 is recommended)

### Download project
- Clone project
```
    git clone https://github.com/TTNguyen2552003/tyme-x-test-submission.git
```
- Change the branch
```
    git checkout question-2_1
```
- Navigate to project folder
```
    cd tyme-x-test-submission/challenge-2/ProductInventoryManagement
```

## Author
- [@TTNguyen2552003](https://www.github.com/TTNguyen2552003)