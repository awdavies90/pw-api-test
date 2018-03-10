package pw.api.test.helpers

import spock.lang.*
import pw.api.test.BaseApiTest

class VenueHelper {
	
	List<Integer> venueIds = []
	BaseApiTest baseTest
	
	def VenueHelper(BaseApiTest baseTest) {
		this.baseTest = baseTest
	}
	
	def venueSearch(String searchText, String token) {
		baseTest.authToken = token
		def venues = baseTest.get("venue/search?address=${URLEncoder.encode(searchText, 'UTF-8')}")
		if (venues) {
			venues.each {
				venueIds << it.id
			}
		}
		venues
	}
	
	int getRandomVenueId(String token) {
		while (venueIds.size() == 0) {
			def randomPostcode = getRandomPostcode()
			venueSearch(randomPostcode, token)
		}
		Random randomGenerator = new Random()
		int randomNum = randomGenerator.nextInt(venueIds.size())
		venueIds[randomNum]
	}
	
	def getRandomPostcode() {
		def response = baseTest.getExternal('http://api.postcodes.io/random/postcodes')
		response.result.postcode
	}
}
