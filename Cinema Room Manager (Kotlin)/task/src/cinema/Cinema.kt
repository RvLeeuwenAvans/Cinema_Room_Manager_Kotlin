package cinema

/**
 * When given only one parameter, all seats are the same price.
 *
 * When given two ticket prices:
 *  - If the total number of seats is not more than 60, all seats are priced the according to the first parameter
 *  - In a larger room, the front and back half are priced according to the first and second parameter respectively
 */
class Cinema(
    private val ticketPrice: Int,
    private val secondaryPrice: Int = ticketPrice,
) {
    private var seatingMatrix: List<List<MutableMap<String, Any>>>

    init {
        try {
            println("Enter the number of rows:")
            val amountOfRows =
                readln().toInt().let {
                    if (it !in 1..9)
                        error("number of rows must be in range 1..9")
                    else it
                }
            println("Enter the number of seats in each row:")
            val seatsInRow = readln().toInt().let {
                if (it !in 1..9)
                    error("number of seats must be in range 1..9")
                else it
            }

            seatingMatrix = buildSeatingMatrix(seatsInRow, amountOfRows)
        } catch (e: NumberFormatException) {
            throw error("Input needs to be an integer")
        }
    }

    fun run() {
        var running = true

        while (running) {
            println(
                """
                1. Show the seats
                2. Buy a ticket
                3. Statistics
                0. Exit
                """.trimIndent()
            )

            try {
                when (readln().toInt()) {
                    1 -> displaySeatingArrangement()
                    2 -> buyTicket()
                    3 -> displayStatistics()
                    0 -> running = false
                }
            } catch (e: NumberFormatException) {
                println("Input needs to be an integer")
            }
            println()
        }

    }

    private fun displayStatistics() {
        // Maximum amount of tickets available is equal to the total number of seats.
        val maximumNumberOfTickets = seatingMatrix.size * seatingMatrix.first().size
        val ticketsBought = calculateTicketsBought()
        val percentageOfticketsBought = (ticketsBought.toDouble() / maximumNumberOfTickets) * 100

        println("Number of purchased tickets: $ticketsBought")
        println("Percentage: ${"%.2f".format(percentageOfticketsBought)}%")
        println("Current income: \$${calculateIncome()}")
        println("Total income: \$${calculateIncome(true)}")
    }

    private fun buyTicket() {
        try {
            println("Enter a row number:")
            val row = readln().toInt()
            println("Enter a seat number in that row:")
            val seat = readln().toInt()

            if (seatingMatrix[row - 1][seat - 1]["booked"] == false) {
                println("Ticket price: \$${seatingMatrix[row - 1][seat - 1]["price"]}")
                seatingMatrix[row - 1][seat - 1]["booked"] = true
            } else {
                println("That ticket has already been purchased!")
                // retry
                this.buyTicket()
            }
        } catch (e: NumberFormatException) {
            println("Input needs to be an integer")
        } catch (e: IndexOutOfBoundsException) {
            println("Wrong input")
        }
    }

    private fun displaySeatingArrangement() {
        val builder = StringBuilder()

        val seatsInRow = seatingMatrix.first().size
        val amountOfRows = seatingMatrix.size

        builder.appendLine("\n Cinema:")
        builder.append("   ")

        repeat(seatsInRow) {
            builder.append("${it + 1} ")
        }.let { builder.appendLine() }

        repeat(amountOfRows) { row ->
            builder.append(" ${row + 1}")
            repeat(seatsInRow) { seat ->
                builder.append(
                    if (seatingMatrix[row][seat]["booked"] as Boolean)
                        " B"
                    else
                        " S"
                )
            }.let { builder.appendLine() }
        }

        println(builder.toString())
    }

    private fun buildSeatingMatrix(seatsInRow: Int, amountOfRows: Int): List<List<MutableMap<String, Any>>> =
        buildList {
            repeat(amountOfRows) { currentRow ->


                val seatPrice = if ((amountOfRows * seatsInRow > 60)) {
                    if (currentRow + 1 in 1..amountOfRows / 2) ticketPrice
                    else secondaryPrice
                } else ticketPrice

                add(buildList {
                    repeat(seatsInRow) {
                        add(
                            mutableMapOf<String, Any>(
                                "price" to seatPrice,
                                "booked" to false
                            )
                        )
                    }
                })
            }
        }.toList()

    private fun calculateTicketsBought(): Int {
        var seatsBooked = 0
        seatingMatrix.forEach { rows ->
            rows.forEach { seat ->
                if (seat["booked"] == true) seatsBooked++
            }
        }
        return seatsBooked
    }

    private fun calculateIncome(calculateMaximum: Boolean = false): Int {
        var seatPriceTotal = 0

        seatingMatrix.forEach { rows ->
            rows.forEach { seat ->
                val currentSeatPrice = seat["price"].toString().toInt()

                if (calculateMaximum) {
                    seatPriceTotal += currentSeatPrice
                } else {
                    if (seat["booked"] == true) seatPriceTotal += currentSeatPrice
                }
            }
        }

        return seatPriceTotal
    }
}