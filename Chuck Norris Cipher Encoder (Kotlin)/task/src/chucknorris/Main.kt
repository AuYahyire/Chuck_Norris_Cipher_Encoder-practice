
fun main() {
    // Repeat the menu until the user chooses to exit
    do {
        val action = menuOption()
    } while (action != "exit")
}

/** Function to display the menu and get user's choice*/
fun menuOption(): String {
    println("Please input operation (encode/decode/exit):")
    val action = readln().trim()
    when (action.lowercase()) {
        "encode" -> codify()
        "decode" -> decodify()
        "exit" -> println("Bye!")
        else -> println("There is no '${action}' operation")
    }
    // Return the user's choice up to main function.
    return action
}

/** Chain of functions to encode a string */
fun codify() {
    println("Input string:")
    val message = readln().trim()
    // Convert the input string to a list of characters, needed to encode.
    val characters = getCharacters(message)
    // Print the encoded string
    println("Encoded string:")
    //Print characters in an encoded form
    printCharacters(characters)
}

/** Chain of functions to decode an encoded string*/
fun decodify() {
    println("Input encoded string:")
    val message = readln().trim()
    // Check if the encoded string is valid
    val conditionOne = message.validEncodedString()
    // If the encoded string is valid, decode it to binary
    val binaryDecodedMessage = if (conditionOne) decodeToBinary(message) else "NOT VALID."
    // Check if the binary decoded message is valid
    val conditionTwo = if (binaryDecodedMessage != "NOT VALID.") binaryDecodedMessage.validBinaryString() else false

    // If both conditions are met, decode the binary to characters and print the result
    if (conditionOne && conditionTwo) {
        val binaryElements = divideBinaryBySeven(binaryDecodedMessage)
        println("Decoded string:")
        println(binaryToChar(binaryElements))
    } else {
        // If the encoded string is not valid, print an error message
        println("Encoded string is not valid.")
    }
}

/** Function to print characters in an encoded form*/
fun printCharacters(message: MutableList<Char>) {
    var binaryMessage = ""
    // Convert each character in the message to its binary representation in 7 bits.
    message.forEach {
        binaryMessage += Integer.toBinaryString(it.code).padStart(7, '0')
    }
    // Encode the binary message and print the result
    val encryptedMessage = encodeCharacters(binaryMessage)
    println(encryptedMessage)
}


/** Function to decode an encoded string to binary*/
fun decodeToBinary(message: String): String {
    // Split the message by spaces to get individual blocks
    val messageSplitted = message.splitToSequence(" ").toList()
    val binaryListMessage = mutableListOf<Char>()

    // Iterate through the blocks and decode them to binary
    for (i in messageSplitted.indices step 2) {
        when (messageSplitted[i]) {
            "0" -> repeat(messageSplitted[i + 1].length) { binaryListMessage.add('1') }
            "00" -> repeat(messageSplitted[i + 1].length) { binaryListMessage.add('0') }
        }
    }

    // Join the binary characters and return the resulting binary string
    val stringBinaryMessage = binaryListMessage.joinToString("")
    return stringBinaryMessage
}

/** Function to divide a binary string into chunks of seven characters*/
fun divideBinaryBySeven(binaryString: String): List<String> {
    return binaryString.chunked(7)
}

/** Function to convert a list of binary strings to characters*/
fun binaryToChar(binaryMessage: List<String>): String {
    val charDecodedList: MutableList<Char> = mutableListOf()
    for (i in binaryMessage) {
        // Convert each binary string to its corresponding character and add it to the list
        charDecodedList.add(i.toInt(2).toChar())
    }

    // Join the characters and return the resulting string
    return charDecodedList.joinToString("")
}

/** Function to convert a string to a list of characters*/
fun getCharacters(message: String): MutableList<Char> {
    val characters: MutableList<Char> = mutableListOf()
    message.forEach {
        characters.add(it)
    }
    return characters
}

/** Function to encode characters from a binary string */
fun encodeCharacters(binaryMessage: String): String {
    var encodedMessage = ""
    var i = 0
    // Iterate through the binary string and encode it into blocks of zeros and ones
    while (i < binaryMessage.length) {
        val currentChar = binaryMessage[i]
        var count = 1

        // Count consecutive 0s or 1s in the block
        while (i + 1 < binaryMessage.length && binaryMessage[i + 1] == currentChar) {
            count++
            i++
        }

        // Encode the block based on its content and length
        encodedMessage += when (currentChar) {
            '0' -> "00 "
            '1' -> "0 "
            else -> ""
        }

        encodedMessage += "0".repeat(count) + " "
        i++
    }
    // Remove trailing whitespace and return the encoded message
    return encodedMessage.trimEnd()
}

/** Extension function to check if an encoded string is valid*/
fun String.validEncodedString(): Boolean {
    var isValid = true

    // If the encoded message includes characters other than 0 or spaces, it's invalid
    if (!all { it == '0' || it == ' ' }) {
        isValid = false
    }

    // Split blocks by whitespaces.
    val blocks = this.split(' ')

    // The number of blocks is odd, it's invalid
    if (blocks.size % 2 != 0) {
        isValid = false
    }

    // The first block of each sequence is not 0 or 00, it's invalid
    for (i in blocks.indices step 2) {
        if (blocks[i] != "0" && blocks[i] != "00") {
            isValid = false
        }
    }

    return isValid
}

/** Extension function to check if a binary string is valid (length is a multiple of 7)*/
fun String.validBinaryString(): Boolean {
    // Check if the string length is a multiple of 7
    return length % 7 == 0
}
