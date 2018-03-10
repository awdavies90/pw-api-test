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
	
	static String GetRandomFutureRequestDate() {
		Date date = new Date().plus(30)
		date.format("yyyy-MM-dd'T'HH:mm:ss'Z'")
	}
}
