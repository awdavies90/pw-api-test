package pw.api.test.bid

import pw.api.test.utils.RandomUtils

class WithdrawBidValidation extends BaseBidTest {
	
	def "1 - Withdraw Bid Validation - Incorrect Bid Id"() {
		
		given:'A bid is to be withdrawn'
			def bidId = "999999999999"
					
		when:'A bid with an invalid id is withdrawn'
			def response = bidHelper.withdrawBid(bidId, bandUserToken)
		
		then:'An appropriate error response is received'
			response.errors[0] == "No bid was found with this ID."
	}
	
	def "2 - Withdraw Bid Validation - Bid Has Already Been Accepted"() {
		
		given:'A Bid is already accepted'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:'The bid is then withdrawn'
			def createBidResponse = bidHelper.saveBid(params, bandUserToken)
			def bidId = createBidResponse.id
			bidHelper.acceptBid(bidId, individualUserToken)
			def withdrawBidResponse = bidHelper.withdrawBid(bidId, bandUserToken)
		
		then:'An appropriate error response is received'
			withdrawBidResponse.errors[0] == 'The bid has already been accepted.'
	}
	
	def "3 - Withdraw Bid Validation - Bid Has Already Been Withdrawn"() {
		
		given:'A bid has already been withdrawn'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:'The bid is accepted'
			def createBidResponse = bidHelper.saveBid(params, bandUserToken)
			def bidId = createBidResponse.id
			bidHelper.withdrawBid(bidId, bandUserToken)
			def withdrawBidResponse = bidHelper.withdrawBid(bidId, bandUserToken)
		
		then:'An appropriate error response is received'
			withdrawBidResponse.errors[0] == 'The bid has already been withdrawn.'
	}
	
	def "4 - Withdraw Bid Validation - Another Bid Already Accepted"() {
		
		given:'Another bid for this post has been accepted'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
			def params2 = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:'The bid is accepted'
			def bid1Id = bidHelper.saveBid(params, bandUserToken).id
			def bid2Id = bidHelper.saveBid(params2, bandUserToken2).id
			bidHelper.acceptBid(bid1Id, individualUserToken)
			def acceptBidResponse = bidHelper.withdrawBid(bid2Id, bandUserToken2)
		
		then:'An appropriate error response is received'
			acceptBidResponse.errors[0] == 'Another bid for this post has already been accepted.'
	}
}
