package pw.api.test.helpers

import spock.lang.*
import pw.api.test.BaseApiTest
import pw.api.test.utils.RandomUtils

class PostHelper {
	
	List<Integer> postIds = []
	BaseApiTest baseTest
	
	def PostHelper(BaseApiTest baseTest) {
		this.baseTest = baseTest
	}
	
	def create(Map postValues, String token) {
		baseTest.authToken = token
		def response = baseTest.post("post/save", "post/Create", postValues)
		if (response) {
			postIds << response.id
		}
		response
	}
	
	def getRandomPostId(String token) {
		if (postIds.size() == 0) {
			createRandomPost(token)
		}
		def randomGenerator = new Random()
		def randomNum = randomGenerator.nextInt(postIds.size())
		postIds[randomNum]
	}
	
	def createRandomPost(String token) {
		def params = [
			name:"My Post - ${new Date()}",
			date:RandomUtils.GetRandomFutureRequestDate(),
			venueId:baseTest.venueHelper.getRandomVenueId(token)
		]
		create(params, token)
	}
}
