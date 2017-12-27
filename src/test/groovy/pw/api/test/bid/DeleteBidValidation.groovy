package pw.api.test.bid

class DeleteBidValidation extends BaseBidTest {
	
	def "1 - Delete Bid Validation - Incorrect Bid Id"() {
		
		given:'A bid is to be deleted'
			def bidId = "999999999999"
					
		when:'A bid with an invalid id is deleted'
			def response = bidHelper.deleteBid(bidId, bandUserToken)
		
		then:'An appropriate error response is received'
			response.errors[0] == "No bid was found with this ID."
	}
	
	def "2 - Delete Bid Validation - Bid Has Already Been Accepted"() {
		
		given:'A Bid has already been accepted'
			def params = [
				postId:1,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The bid is then deleted'
			def createBidResponse = bidHelper.saveBid(params, bandUserToken)
			def bidId = createBidResponse.id
			bidHelper.acceptBid(bidId, individualUserToken)
			def deleteBidResponse = bidHelper.deleteBid(bidId, bandUserToken)
		
		then:'An appropriate error response is received'
			deleteBidResponse.errors[0] == 'The bid has already been accepted.'
	}
	
	def "3 - Delete Bid Validation - Bid Has Already Been Withdrawn"() {
		
		given:'A bid has already been withdrawn'
			def params = [
				postId:1,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The bid is deleted'
			def createBidResponse = bidHelper.saveBid(params, bandUserToken)
			def bidId = createBidResponse.id
			bidHelper.withdrawBid(bidId, bandUserToken)
			def deleteBidResponse = bidHelper.deleteBid(bidId, bandUserToken)
		
		then:'An appropriate error response is received'
			deleteBidResponse.errors[0] == 'The bid has already been withdrawn.'
	}
	
	def "4 - Delete Bid Validation - Another Bid Already Accepted"() {
		
		given:'Another bid for this post has been accepted'
			def params = [
				postId:1,
				amount:500,
				notes:'These are some notes'
			]
			def params2 = [
				postId:1,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The bid is deleted'
			def bid1Id = bidHelper.saveBid(params, bandUserToken).id
			def bid2Id = bidHelper.saveBid(params2, bandUserToken2).id
			bidHelper.acceptBid(bid1Id, individualUserToken)
			def acceptBidResponse = bidHelper.deleteBid(bid2Id, bandUserToken2)
		
		then:'An appropriate error response is received'
			acceptBidResponse.errors[0] == 'Another bid for this post has already been accepted.'
	}
}
