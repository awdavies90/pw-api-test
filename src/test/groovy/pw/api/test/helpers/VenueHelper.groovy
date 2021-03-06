package pw.api.test.helpers

import spock.lang.*
import pw.api.test.BaseApiTest

class VenueHelper {
	
	List<Integer> venueIds = []
	List<Integer> customVenueIds = []
	static venueWithPostcode
	BaseApiTest baseTest
	
	def VenueHelper(BaseApiTest baseTest) {
		this.baseTest = baseTest
	}
	
	def venueSearch(String searchText, String token) {
		baseTest.authToken = token
		def venues = baseTest.getCall("venue/search?address=${URLEncoder.encode(searchText, 'UTF-8')}")
		if (venues) {
			venues.each {
				if (it?.id && !it.name?.contains('&')) {
					venueIds << it.id
				}
			}
		}
		venues
	}
	
	def saveVenue(Map params, String token) {
		baseTest.authToken = token
		def response = baseTest.post('venue/save', 'venue/SaveVenue', params)
		if (response?.id) {
			customVenueIds << response.id
		}
		response
	}
	
	def updateVenue(Map params, String token) {
		baseTest.authToken = token
		baseTest.post("venue/update/${params.id}", 'venue/SaveVenue', params)
	}
	
	def getVenue(id, String token) {
		baseTest.authToken = token
		baseTest.getCall("venue/$id")
	}
	
	int getRandomVenueId(String token) {
		getRandomVenueIdOrCustomVenueId(venueIds) {
			baseTest.whileWithLimit(5, { venueIds.size() == 0 }) {
				def randomPostcode = getRandomPostcode()
				venueSearch(randomPostcode, token)
			}
		}
	}
	
	int getRandomCustomVenueId(String token) {
		getRandomVenueIdOrCustomVenueId(customVenueIds) {
			def randomPostcode = getRandomPostcode()
			def params = [
				name:"Custom Venue Name ${new Date()}",
				address:"Custom Venue address ${new Date()}",
				postcode:randomPostcode
			]
			saveVenue(params, token)
		}
	}
	
	private int getRandomVenueIdOrCustomVenueId(List<Integer> listOfIds, Closure action) {
		while (listOfIds.size() == 0) {
			action()
		}
		Random randomGenerator = new Random()
		int randomNum = randomGenerator.nextInt(listOfIds.size())
		listOfIds[randomNum]
	}
	
	def getRandomPostcode() {
		def response = baseTest.getExternal('http://api.postcodes.io/random/postcodes')
		response.result.postcode
	}
	
	def deleteVenue(id) {
		deleteVenue(id, baseTest.adminUserToken)
	}
	
	def deleteVenue(id, String token) {
		baseTest.authToken = token
		def response = baseTest.delete("venue/$id")
		if (response.success) {
			venueIds.removeIf { it == id }
			customVenueIds.removeIf { it == id }
		}
		response
	}
	
	def deleteAllCustomVenues() {
		List<Integer> venueIds = customVenueIds.collect { it }
		venueIds.each { venueId ->
			deleteVenue(venueId)
		}
		customVenueIds = []
	}
	
	def getRandomVenueWithPostcode() {
		def existingVenueId = getRandomVenueId(baseTest.individualUserToken)
		def existingVenue = venueWithPostcode
		if (!existingVenue) {
			existingVenue = getVenue(existingVenueId, baseTest.individualUserToken)
		}
		if (!existingVenue.postcode) {
			def randomPostcode = getRandomPostcode()
			def params = [
				id:existingVenueId,
				postcode:randomPostcode
			]
			existingVenue = updateVenue(params, baseTest.adminUserToken)
		}
		venueWithPostcode = existingVenue
		existingVenue
	}
}
