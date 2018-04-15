package pw.api.test.venue

import spock.lang.*
import pw.api.test.BaseApiTest

class BaseVenueTest extends BaseApiTest {
	
	def cleanupSpec() {
		venueHelper.deleteAllCustomVenues()
	}
	
	def verifyCustomVenues(Map expectedVenue, String token, actualVenues) {
		actualVenues.removeAll { it.responseCode }
		assert actualVenues.size() == 1
		verifyCustomVenue(expectedVenue, token, actualVenues[0])
	}
	
	def verifyCustomVenue(Map expectedVenue, String token, actualVenue) {
		assert actualVenue.id != null
		assert actualVenue.address == expectedVenue.address
		assert actualVenue.dateCreated != null && actualVenue.dateCreated != ''
		assert actualVenue.dateUpdated != null && actualVenue.dateUpdated != ''
		assert actualVenue.googleMapsPlaceId == null
		assert actualVenue.isCustomAddress == true
		assert actualVenue.location == null
		assert actualVenue.name == expectedVenue.name
		assert actualVenue.postcode == expectedVenue.postcode.toUpperCase()
		assert actualVenue.user?.id == userHelper.getUserIdByToken(token)
		true
	}
}
