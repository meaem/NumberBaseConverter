package converter

fun main() {

    while (true) {
        println("Do you want to convert /from decimal or /to decimal? (To quit type /exit):")
        val command = readln()
        when (command) {
            "/from" -> {
                print("Enter number in decimal system:")
                val num = readln().toInt()
                print("Enter target base:")
                val radix = readln().toInt()
                print("Conversion result:")
                println(num.toString(radix))
            }
            "/to" -> {
                print("Enter source number:")
                val num = readln()
                print("Enter source base:")
                val radix = readln().toInt()
                print("Conversion to decimal result:")
                println(num.uppercase().toInt(radix))
            }
            "/exit" -> break
        }
        println()
    }
}