package pw.api.test.bid

import pw.api.test.BaseApiTest

class AcceptBidValidation extends BaseBidTest {
	
	def "1 - Accept Bid - Incorrect Bid Id"() {
		
		given:'A bid is to be accepted'
			def bidId = "999999999999"
					
		when:'A bid with an invalid id is accepted'
			def response = acceptBid(bidId)
		
		then:'An appropriate error response is received'
			response.errors[0] == "No bid was found with this ID."
	}
	
	def "2 - Accept Bid - Bid Has Already Been Accepted"() {
		
		given:'A Bid is already accepted'
			def params = [
				postId:1,
				userId:5,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The bid is accepted again'
			def createBidResponse = saveBid(params)
			def bidId = createBidResponse.id
			def validAcceptBidResponse = acceptBid(bidId)
			def invalidAcceptBidResponse = acceptBid(bidId)
		
		then:'An appropriate error response is received'
			invalidAcceptBidResponse.errors[0] == 'The bid has already been accepted.'
	}
	
	def "3 - Accept Bid - Bid Has Already Been Withdrawn"() {
		
		given:'A bid has already been withdrawn'
			def params = [
				postId:1,
				userId:5,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The bid is accepted'
			def createBidResponse = saveBid(params)
			def bidId = createBidResponse.id
			def withdrawBidResponse = withdrawBid(bidId)
			def invalidAcceptBidResponse = acceptBid(bidId)
		
		then:'An appropriate error response is received'
			invalidAcceptBidResponse.errors[0] == 'The bid has already been withdrawn.'
	}
	
	def "4 - Accept Bid - Another Bid Already Accepted"() {
		
		given:'Another bid for this post has been accepted'
			def params = [
				postId:1,
				userId:5,
				amount:500,
				notes:'These are some notes'
			]
			def params2 = [
				postId:1,
				userId:6,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The bid is accepted'
			def createValidBidResponse = saveBid(params)
			def validBidId = createValidBidResponse.id
			def createInvaildBidResponse = saveBid(params2)
			def invalidBidId = createInvaildBidResponse.id
			def validAcceptBidResponse = acceptBid(validBidId)
			def invalidAcceptBidResponse = acceptBid(invalidBidId)
		
		then:'An appropriate error response is received'
			invalidAcceptBidResponse.errors[0] == 'Another bid for this post has already been accepted.'
	}
}
