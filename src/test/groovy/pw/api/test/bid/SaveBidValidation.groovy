package pw.api.test.bid

import pw.api.test.utils.RandomUtils
import spock.lang.*

@Unroll
class SaveBidValidation extends BaseBidTest {

	def "Save Bid Validation - Required Params Not Supplied"() {
		
		given:'There is a test'
			def params = [
				postId:postId,
				amount:amount,
				notes:notes
			]
					
		when:'The test is executed'
			def response = bidHelper.saveBid(params, bandUserToken)
		
		then:'The test passes'
			response.errors[0] == 'The following params are required [postId, amount, notes].'
		
		where:
			postId											| amount 								  | notes 				  | description
			null											| RandomUtils.getRandomDecimal(100, 1000) | 'These are some notes'| 'No postId'
			postHelper.getRandomPostId(individualUserToken) | null									  | 'These are some notes'| 'No amount'
			postHelper.getRandomPostId(individualUserToken) | RandomUtils.getRandomDecimal(100, 1000) | null				  | 'No notes'
	}
	
	def "Save Bid Validation - Incorrect Post ID"() {
		
		given:'There is a test'
			def params = [
				postId:999999,
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def response = bidHelper.saveBid(params, bandUserToken)
		
		then:'The test passes'
			response.errors[0] == 'No post was found with this ID.'
	}
	
	def "Save Bid Validation - Bid Can Only be Made By Band"() {
		
		given:'There is a test'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def response = bidHelper.saveBid(params, individualUserToken)
		
		then:'The test passes'
			response.errors[0] == 'A bid can only be made by a user of type Band.'
	}
	
	def "Save Bid Validation - User has Already Made a Bid for Post"() {
		
		given:'There is a test'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def validResponse = bidHelper.saveBid(params, bandUserToken)
			def response = bidHelper.saveBid(params, bandUserToken)
		
		then:'The test passes'
			response.errors[0] == 'A bid already exists for this user and post.'
	}
	
	def "Save Bid Validation - Another Bid Already Accepted"() {
		
		given:'There is a test'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def validResponse = bidHelper.saveBid(params, bandUserToken)
			bidHelper.acceptBid(validResponse?.id, individualUserToken)
			def response = bidHelper.saveBid(params, bandUserToken2)
		
		then:'The test passes'
			response.errors[0] == 'A bid for this post has already been accepted.'
	}
	
	def "Save Bid Validation - Invalid Characters in Notes"() {
		
		given:'There is a test'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:notes
			]
					
		when:'The test is executed'
			def response = bidHelper.saveBid(params, bandUserToken)
		
		then:'The test passes'
			response.errors[0] == 'Text cannot contain any of the following characters `^*_{}[]~|;<>'
			
		where:
			notes << invalidChars
	}
}
