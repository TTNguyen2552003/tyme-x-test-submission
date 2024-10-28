import org.junit.jupiter.api.Assertions.assertEquals

/**
 * Test class for the missingNumber function.
 * @see missingNumber
 * Contains test cases to verify the function correctly identifies the missing number
 * in a sequence of integers.
 */
class UnitTesting {
    @org.junit.jupiter.api.Test
    fun testCase1() {
        val array: IntArray = intArrayOf(3, 7, 1, 2, 6, 4)
        val expected = 5
        val actual = missingNumber(array)
        assertEquals(expected, actual)
    }

    @org.junit.jupiter.api.Test
    fun testCase2() {
        val array: IntArray = intArrayOf(4, 1, 3)
        val expected = 2
        val actual = missingNumber(array)
        assertEquals(expected, actual)
    }

    @org.junit.jupiter.api.Test
    fun testCase3() {
        val array: IntArray = intArrayOf(8, 2, 5, 7, 3, 4, 6, 1)
        val expected = 9
        val actual = missingNumber(array)
        assertEquals(expected, actual)
    }

    @org.junit.jupiter.api.Test
    fun testCase4() {
        val array: IntArray = intArrayOf(1, 2)
        val expected = 3
        val actual = missingNumber(array)
        assertEquals(expected, actual)
    }

    @org.junit.jupiter.api.Test
    fun testCase5() {
        val array: IntArray = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 13)
        val expected = 10
        val actual = missingNumber(array)
        assertEquals(expected, actual)
    }
}