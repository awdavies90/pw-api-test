package pw.api.test.bid

import spock.lang.*
import pw.api.test.BaseApiTest

class SaveBid extends BaseApiTest {
	
	def cleanup() {
		
	}
	
	@Ignore
	def "Test One"() {
		
		given:'There is a test'
			def url = 'bid/forPost?id=1'
					
		when:'The test is executed'
			def response = get(url)
		
		then:'The test passes'
			response.post.id.every {
				it == 1
			}
	}
	
	def "Test Two"() {
		
		given:'There is a test'
			def params = [
				postId:1,
				userId:8,
				amount:413.10,
				notes:'retj retjr tkjhret'
			]
			def url = 'bid/save'
					
		when:'The test is executed'
			def response = post(url, "SaveBid", params);
		
		then:'The test passes'
			with(response) {
				acceptReason == null
				amount == params.amount
				notes == params.notes
				post.id == params.postId
				status == 'PENDING'
				user.id == params.userId
				withdrawReason == null
			}
	        //"dateCreated": "2017-11-19T14:27:14Z",
	        //"dateUpdated": "2017-11-19T14:27:14Z",
	}
}
