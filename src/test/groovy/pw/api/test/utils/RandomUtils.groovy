package pw.api.test.utils

import java.math.MathContext

class RandomUtils {
	
	static Random random = new Random()
	
	static BigDecimal getRandomDecimal(int minValue, int maxValue) {
		BigDecimal randomDecimal = 0
		while (!(randomDecimal >= minValue && randomDecimal <= maxValue)) {
			randomDecimal = maxValue.toBigDecimal() * random.nextDouble()
		}
		randomDecimal.setScale(2, BigDecimal.ROUND_UP)
	}
	
	static String getRandomFutureRequestDate() {
		Date date = new Date().plus(30)
		date.format("yyyy-MM-dd'T'HH:mm:ss'Z'")
	}
	
	static getRandomValueFrom(List listOfValues) {
		def randomIndex = random.nextInt(listOfValues.size())
		listOfValues[randomIndex]
	}
	
	static String getRandomText() {
		getRandomText(50)
	}
	
	static String getRandomText(int numberOfCharacters) {
		def words = ['Test', 'Text', 'Description', 'Notes', 'Post', 'Venue', 'Playlist', 'Bid', 'User']
		def text = ''
		while (text.size() < numberOfCharacters) {
			text += "${getRandomValueFrom(words)} "
		}
		text[0..(numberOfCharacters - 1)]
	}
}
