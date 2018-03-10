package pw.api.test.bid

import pw.api.test.utils.RandomUtils

class AcceptBidPermissions extends BaseBidTest {
	
	def "1 - Accept Bid Permissions - Only the Owner of the Post Can Accept a Bid"() {
		
		given:'A Bid is to be accepted'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:'The bid is accepted by a user who is not the owner of the post'
			def createBidResponse = bidHelper.saveBid(params, bandUserToken)
			def bidId = createBidResponse.id
			def acceptBidResponse = bidHelper.acceptBid(bidId, individualUserToken2)
		
		then:'An appropriate error response is received'
			acceptBidResponse.errors[0] == 'You do not have permission to perform this operation.'
	}
}
