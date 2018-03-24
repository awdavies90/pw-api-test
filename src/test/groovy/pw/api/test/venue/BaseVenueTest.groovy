package pw.api.test.venue

import spock.lang.*
import pw.api.test.BaseApiTest

class BaseVenueTest extends BaseApiTest {
	
	def cleanupSpec() {
		venueHelper.deleteAllCustomVenues()
	}
}
