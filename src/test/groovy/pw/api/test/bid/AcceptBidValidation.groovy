package pw.api.test.bid

import pw.api.test.utils.RandomUtils

class AcceptBidValidation extends BaseBidTest {
	
	def "1 - Accept Bid - Incorrect Bid Id"() {
		
		given:'A bid is to be accepted'
			def bidId = "999999999999"
					
		when:'A bid with an invalid id is accepted'
			def response = bidHelper.acceptBid(bidId, individualUserToken)
		
		then:'An appropriate error response is received'
			response.errors[0] == "No bid was found with this ID."
	}
	
	def "2 - Accept Bid - Bid Has Already Been Accepted"() {
		
		given:'A Bid is already accepted'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				//userId:5,
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:'The bid is accepted again'
			def createBidResponse = bidHelper.saveBid(params, bandUserToken)
			def bidId = createBidResponse.id
			def validAcceptBidResponse = bidHelper.acceptBid(bidId, individualUserToken)
			def invalidAcceptBidResponse = bidHelper.acceptBid(bidId, individualUserToken)
		
		then:'An appropriate error response is received'
			invalidAcceptBidResponse.errors[0] == 'The bid has already been accepted.'
	}
	
	def "3 - Accept Bid - Bid Has Already Been Withdrawn"() {
		
		given:'A bid has already been withdrawn'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				//userId:5,
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:'The bid is accepted'
			def createBidResponse = bidHelper.saveBid(params, bandUserToken)
			def bidId = createBidResponse.id
			def withdrawBidResponse = bidHelper.withdrawBid(bidId, bandUserToken)
			def invalidAcceptBidResponse = bidHelper.acceptBid(bidId, individualUserToken)
		
		then:'An appropriate error response is received'
			invalidAcceptBidResponse.errors[0] == 'The bid has already been withdrawn.'
	}
	
	def "4 - Accept Bid - Another Bid Already Accepted"() {
		
		given:'Another bid for this post has been accepted'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				//userId:5,
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
			def params2 = [
				postId:postHelper.getRandomPostId(individualUserToken),
				//userId:6,
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:'The bid is accepted'
			def createValidBidResponse = bidHelper.saveBid(params, bandUserToken)
			def validBidId = createValidBidResponse.id
			def createInvaildBidResponse = bidHelper.saveBid(params2, bandUserToken2)
			def invalidBidId = createInvaildBidResponse.id
			def validAcceptBidResponse = bidHelper.acceptBid(validBidId, individualUserToken)
			def invalidAcceptBidResponse = bidHelper.acceptBid(invalidBidId, individualUserToken)
		
		then:'An appropriate error response is received'
			invalidAcceptBidResponse.errors[0] == 'Another bid for this post has already been accepted.'
	}
}
