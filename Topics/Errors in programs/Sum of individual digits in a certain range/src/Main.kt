import java.util.*

//Start of Kotlin Program
fun main(args: Array<String>) {

    //Create a scan instance for readling line from standard Input
    val scan = Scanner(System.`in`)

    // Reading an integer as input
    var userInput = scan.nextLine().trim()

    if (userInput.toIntOrNull() != null && userInput.toInt() in 0..9999) {
        val integerList = userInput.map {
            digit: Char ->
            digit.digitToInt()
        }

        if (integerList.sum() > 0) return println(integerList.sum())

    }

    println("Invalid Input")
}
// End of Kotlin Program