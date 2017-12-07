package api.test.helpers

import spock.lang.*

import javax.activity.InvalidActivityException

import pw.api.test.BaseApiTest

class EventHelper {
	
	BaseApiTest baseTest
	def EventHelper(BaseApiTest baseTest) {
		this.baseTest = baseTest
	}
	
	def getEventsForPost(postId) {
		baseTest.get("event/forPost/$postId")
	}
	
	def getEventsForUser(userId) {
		baseTest.get("event/forUser/$userId")
	}
}
