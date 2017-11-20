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
	
	def "Save Bid - Required Params"() {
		
		given:'There is a test'
			def params = [
				postId:postId,
				userId:userId,
				amount:amount,
				notes:notes
			]
			def url = 'bid/save'
					
		when:'The test is executed'
			def response = post(url, "bid/SaveBid", params);
		
		then:'The test passes'
			with(response) {
				errors[0] == 'The following params are required [postId, userId, amount, notes]'
			}
	        //"dateCreated": "2017-11-19T14:27:14Z",
	        //"dateUpdated": "2017-11-19T14:27:14Z",
		
		where:
			postId | userId | amount | notes 				  | description
			null   | 5		| 120	 | 'These are some notes' | 'No postId'
			1      | null	| 120	 | 'These are some notes' | 'No userId'
			1      | 5		| null	 | 'These are some notes' | 'No amount'
			1      | 5		| 500	 | null					  | 'No notes'
	}
}
