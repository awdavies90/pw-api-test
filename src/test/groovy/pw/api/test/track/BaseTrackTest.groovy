package pw.api.test.track

import spock.lang.*
import pw.api.test.BaseApiTest

class BaseTrackTest extends BaseApiTest {
	
	def cleanupSpec() {
		trackHelper.deleteAllCustomTracks()
	}
	
	def verifyCustomTracks(Map expectedTrack, String token, actualTracks) {
	}
	
	def verifyCustomTrack(Map expectedTrack, String token, actualTrack) {

	}
}
