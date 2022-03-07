package converter

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max

fun main() {

    val xx = "38012.0555311".toBigDecimal().setScale(4, RoundingMode.DOWN)
//    println("***${xx.setScale(5)}")
    println("FLOOR:${xx.setScale(5, RoundingMode.FLOOR)}")
//    println("HALF_EVEN:${xx.setScale(5,RoundingMode.HALF_EVEN)}")
//    println("HALF_DOWN:${xx.setScale(5,RoundingMode.HALF_DOWN)}")
//    println("DOWN:${xx.setScale(5,RoundingMode.DOWN)}")
//    println("CEILING:${xx.setScale(5,RoundingMode.CEILING)}")
//    println("HALF_UP:${xx.setScale(5,RoundingMode.HALF_UP)}")
//    println("UP:${xx.setScale(5,RoundingMode.UP)}")
    while (true) {
        print("Enter two numbers in format: {source base} {target base} (To quit type /exit) > ")
        val tokens = readln().split(" ")

        if (tokens.size == 1 && tokens[0] == "/exit") break
        val src_base = tokens[0].toInt()
        val target_base = tokens[1].toInt()
        while (true) {
            print("Enter number in base $src_base to convert to base $target_base (To go back type /back) > ")
            val input = readln()
            if (input == "/back") break
//            val numStr = input
            if ('.' !in input) {
                println("Conversion result: ${convertBigIntegerToTargetBase(input, src_base, target_base)}")

            } else {

                println("Conversion result: ${convertBigDecimalToTargetBase(input, src_base, target_base)}")

            }
        }

        println()
    }
}

fun convertBigIntegerToTargetBase(input: String, src_base: Int, target_base: Int): String {
    val num = if (src_base != 10) input.toBigInteger(src_base) else input.toBigInteger()
    return num.toString(target_base)
}

fun convertBigDecimalToDecimalBase(input: String, src_base: Int, scale: Int?, roundingMode: RoundingMode?): BigDecimal {

    val tokens = input.split(".")
    val scale = max(tokens[1].length , scale!!)
    val num1 = tokens[0].toBigInteger(src_base) //if (src_base != 10) else tokens[0].toBigInteger()
//    var num2 = ("0."+tokens[1]).toBigDecimal()// if (src_base != 10) ("0."+tokens[1]).toBigInteger(src_base) else ("0."+tokens[1]).toBigInteger()


    var numIndecimal = num1.toBigDecimal()//
//        while (num2 != BigDecimal.ONE) {

    tokens[1].forEachIndexed { idx, c ->
        val x = c.digitToInt(src_base).toBigDecimal()
        val z = BigDecimal.ONE.setScale(scale) / src_base.toBigDecimal().pow(idx.inc())
//            println("z:$z")
        val y = x.multiply(z)
        numIndecimal += y
    }
//    return if (scale != null)
//        input.toBigDecimal(MathContext(scale, roundingMode))
//    else {
//        input.toBigDecimal()
//    }
    return numIndecimal.setScale(scale,roundingMode)
}

fun convertBigDecimalToTargetBase(inp: String, src_base: Int, target_base: Int): String {
    var numIndecimal: BigDecimal
    var input = inp
    val out: String
    if (src_base != 10) {
        numIndecimal = convertBigDecimalToDecimalBase(input, src_base, 10, RoundingMode.UP)
        val n2 = numIndecimal.setScale(5,RoundingMode.UP)
        out = decimalBaseToTargetBaseString(n2, target_base,5,RoundingMode.UP)

    } else {
        numIndecimal = input.toBigDecimal().setScale(10, RoundingMode.DOWN)

        out = numIndecimal.setScale(5,RoundingMode.DOWN).toString()

    }



    return out
}

fun decimalBaseToTargetBaseString(input: BigDecimal, target_base: Int, scale: Int?, roundingMode: RoundingMode?): String {
    val tokens = input.toString().split(".")
    val wholePartStr = tokens[0].toBigInteger(10).toString(target_base)
    var fractionPart = "0.${tokens[1]}".toBigDecimal().setScale(scale!!*2,roundingMode!!)
    val quotionts = mutableListOf<Int>()
    var counter = 0
    while (counter < 10 && fractionPart.compareTo(BigDecimal.ZERO) != 0) {
        fractionPart = fractionPart.multiply(target_base.toBigDecimal())
        val q = fractionPart.toBigInteger()
        quotionts.add(q.toInt())
        fractionPart -= q.toBigDecimal()
        counter++
    }
    if (quotionts.size < scale!!) quotionts.addAll(List(scale!! - quotionts.size){0})
    val fractionPartStr = quotionts.take(scale!!).joinToString("") { it.toString(target_base) }
    return "$wholePartStr.$fractionPartStr"
}
