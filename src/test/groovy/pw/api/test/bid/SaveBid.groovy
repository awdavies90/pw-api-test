package pw.api.test.bid

import spock.lang.*
import pw.api.test.BaseApiTest

@Unroll
class SaveBid extends BaseBidTest {
	
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
	
	def "Save Bid - Required Params Not Supplied"() {
		
		given:'There is a test'
			def params = [
				postId:postId,
				userId:userId,
				amount:amount,
				notes:notes
			]
					
		when:'The test is executed'
			def response = saveBid(params)
		
		then:'The test passes'
			response.errors[0] == 'The following params are required [postId, userId, amount, notes]'
	        //"dateCreated": "2017-11-19T14:27:14Z",
	        //"dateUpdated": "2017-11-19T14:27:14Z",
		
		where:
			postId | userId | amount | notes 				  | description
			null   | 5		| 120	 | 'These are some notes' | 'No postId'
			1      | null	| 120	 | 'These are some notes' | 'No userId'
			1      | 5		| null	 | 'These are some notes' | 'No amount'
			1      | 5		| 500	 | null					  | 'No notes'
	}
	
	def "Save Bid - Incorrect Post and User IDs"() {
		
		given:'There is a test'
			def params = [
				postId:postId,
				userId:userId,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def response = saveBid(params)
		
		then:'The test passes'
			response.errors[0] == "No $message was found with this ID."
		
		where:
			postId | userId | message | description
			999999 | 5		| 'post'  | "postId doesn't exist"
			1      | 999999	| 'user'  | "userId doesn't exist"
	}
	
	def "Save Bid - Bid Can Only be Made By Band"() {
		
		given:'There is a test'
			def params = [
				postId:1,
				userId:1,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def response = saveBid(params)
		
		then:'The test passes'
			response.errors[0] == 'A bid can only be made by a user of type Band'
	}
	
	def "Save Bid - User has Already Made a Bid for Post"() {
		
		given:'There is a test'
			def params = [
				postId:1,
				userId:5,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def validResponse = saveBid(params)
			def response = saveBid(params)
		
		then:'The test passes'
			response.errors[0] == 'A bid already exists for this user and post'
	}
}
