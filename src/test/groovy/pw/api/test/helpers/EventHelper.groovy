package pw.api.test.helpers

import spock.lang.*

import javax.activity.InvalidActivityException

import pw.api.test.BaseApiTest

class EventHelper {
	
	BaseApiTest baseTest
	def EventHelper(BaseApiTest baseTest) {
		this.baseTest = baseTest
	}
	
	def getEventsForPost(postId) {
		baseTest.getCall("event/forPost/$postId")
	}
	
	def getEventsForUser(userId) {
		baseTest.getCall("event/forUser/$userId")
	}
}
