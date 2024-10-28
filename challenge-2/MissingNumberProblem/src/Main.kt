/**
 * Finds the missing number in a sequence from 1 to n where n is the length of the input array.
 *
 * @param array An array containing n distinct numbers from 1 to n + 1, with one number missing
 * @return The missing number from the sequence
 *
 * Example:
 *
 * Input: array = [3, 7, 1, 2, 6, 4]
 *
 * Output: 5 (missing number)
 */
fun missingNumber(array: IntArray): Int {
//    The sum of numbers from 1 to n is n * (n + 1) / 2
//    Here we want sum from 1 to n + 1, where n is array.size
    val n = array.size + 1
    val expectedSum = (n.toLong() * (n + 1) / 2)
    val actualSum = array.sumOf { it.toLong() }

    return (expectedSum - actualSum).toInt()
}

fun main() {
    val array: IntArray = intArrayOf(3, 7, 1, 2, 6, 4)
    print(missingNumber(array)) // 5
}