package pw.api.test.helpers

import spock.lang.*
import pw.api.test.BaseApiTest

class VenueHelper {
	
	List<Integer> venueIds = []
	List<Integer> customVenueIds = []
	BaseApiTest baseTest
	
	def VenueHelper(BaseApiTest baseTest) {
		this.baseTest = baseTest
	}
	
	def venueSearch(String searchText, String token) {
		baseTest.authToken = token
		def venues = baseTest.getCall("venue/search?address=${URLEncoder.encode(searchText, 'UTF-8')}")
		if (venues) {
			venues.each {
				venueIds << it.id
			}
		}
		venues
	}
	
	def saveVenue(Map params, String token) {
		baseTest.authToken = token
		def response = baseTest.post('venue/save', 'venue/SaveVenue', params)
		if (response) {
			customVenueIds << response.id
		}
		response
	}
	
	def getVenue(id, String token) {
		baseTest.authToken = token
		baseTest.getCall("venue/$id")
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
	
	def deleteVenue(id, String token) {
		baseTest.authToken = token
		baseTest.delete("venue/$id")
	}
	
	def deleteVenue(id) {
		deleteVenue(id, baseTest.adminUserToken)
	}
	
	def deleteAllCustomVenues() {
		customVenueIds.each { venueId ->
			deleteVenue(venueId)
		}
		customVenueIds = []
	}
}
