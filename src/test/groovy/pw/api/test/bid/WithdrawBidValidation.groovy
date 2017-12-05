package pw.api.test.bid

class WithdrawBidValidation extends BaseBidTest {
	
	def "1 - Withdraw Bid Validation - Incorrect Bid Id"() {
		
		given:'A bid is to be withdrawn'
			def bidId = "999999999999"
					
		when:'A bid with an invalid id is withdrawn'
			def response = bidHelper.withdrawBid(bidId)
		
		then:'An appropriate error response is received'
			response.errors[0] == "No bid was found with this ID."
	}
	
	def "2 - Withdraw Bid Validation - Bid Has Already Been Accepted"() {
		
		given:'A Bid is already accepted'
			def params = [
				postId:1,
				userId:5,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The bid is accepted again'
			def createBidResponse = bidHelper.saveBid(params)
			def bidId = createBidResponse.id
			bidHelper.acceptBid(bidId)
			def withdrawBidResponse = bidHelper.withdrawBid(bidId)
		
		then:'An appropriate error response is received'
			withdrawBidResponse.errors[0] == 'The bid has already been accepted.'
	}
	
	def "3 - Withdraw Bid Validation - Bid Has Already Been Withdrawn"() {
		
		given:'A bid has already been withdrawn'
			def params = [
				postId:1,
				userId:5,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The bid is accepted'
			def createBidResponse = bidHelper.saveBid(params)
			def bidId = createBidResponse.id
			bidHelper.withdrawBid(bidId)
			def withdrawBidResponse = bidHelper.acceptBid(bidId)
		
		then:'An appropriate error response is received'
			withdrawBidResponse.errors[0] == 'The bid has already been withdrawn.'
	}
	
	def "4 - Withdraw Bid Validation - Another Bid Already Accepted"() {
		
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
			def bid1Id = bidHelper.saveBid(params).id
			def bid2Id = bidHelper.saveBid(params2).id
			bidHelper.acceptBid(bid1Id)
			def acceptBidResponse = bidHelper.acceptBid(bid2Id)
		
		then:'An appropriate error response is received'
			acceptBidResponse.errors[0] == 'Another bid for this post has already been accepted.'
	}
}
