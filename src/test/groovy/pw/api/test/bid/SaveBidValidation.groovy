package pw.api.test.bid

import spock.lang.*

@Unroll
class SaveBidValidation extends BaseBidTest {

	def "Save Bid Validation - Required Params Not Supplied"() {
		
		given:'There is a test'
			def params = [
				postId:postId,
				userId:userId,
				amount:amount,
				notes:notes
			]
					
		when:'The test is executed'
			def response = bidHelper.saveBid(params)
		
		then:'The test passes'
			response.errors[0] == 'The following params are required [postId, userId, amount, notes]'
		
		where:
			postId | userId | amount | notes 				  | description
			null   | 5		| 120	 | 'These are some notes' | 'No postId'
			1      | null	| 120	 | 'These are some notes' | 'No userId'
			1      | 5		| null	 | 'These are some notes' | 'No amount'
			1      | 5		| 500	 | null					  | 'No notes'
	}
	
	def "Save Bid Validation - Incorrect Post and User IDs"() {
		
		given:'There is a test'
			def params = [
				postId:postId,
				userId:userId,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def response = bidHelper.saveBid(params)
		
		then:'The test passes'
			response.errors[0] == "No $message was found with this ID."
		
		where:
			postId | userId | message | description
			999999 | 5		| 'post'  | "postId doesn't exist"
			1      | 999999	| 'user'  | "userId doesn't exist"
	}
	
	def "Save Bid Validation - Bid Can Only be Made By Band"() {
		
		given:'There is a test'
			def params = [
				postId:1,
				userId:1,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def response = bidHelper.saveBid(params)
		
		then:'The test passes'
			response.errors[0] == 'A bid can only be made by a user of type Band.'
	}
	
	def "Save Bid Validation - User has Already Made a Bid for Post"() {
		
		given:'There is a test'
			def params = [
				postId:1,
				userId:5,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def validResponse = bidHelper.saveBid(params)
			def response = bidHelper.saveBid(params)
		
		then:'The test passes'
			response.errors[0] == 'A bid already exists for this user and post.'
	}
	
	def "Save Bid Validation - Another Bid Already Accepted"() {
		
		given:'There is a test'
			def params = [
				postId:1,
				userId:5,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def validResponse = bidHelper.saveBid(params)
			bidHelper.acceptBid(validResponse?.id)
			def response = bidHelper.saveBid(postId:1, userId:6, amount:500, notes:'These are some notes')
		
		then:'The test passes'
			response.errors[0] == 'A bid for this post has already been accepted.'
	}
	
	def "Save Bid Validation - Invalid Characters in Notes"() {
		
		given:'There is a test'
			def params = [
				postId:1,
				userId:1,
				amount:500,
				notes:notes
			]
					
		when:'The test is executed'
			def response = bidHelper.saveBid(params)
		
		then:'The test passes'
			response.errors[0] == 'Text cannot contain any of the following characters `^*_{}[]~|;<>'
			
		where:
			notes << invalidChars
	}
}
