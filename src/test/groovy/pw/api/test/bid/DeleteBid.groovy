package pw.api.test.bid

import pw.api.test.utils.RandomUtils

class DeleteBid extends BaseBidTest {
	
	def "Delete Bid"() {
		
		given:'A bid is to be deleted'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'Test notes'
			]
			def reason = 'Made the bid by mistake.'
					
		when:'The bid is deleted'
			def bidId = bidHelper.saveBid(params, bandUserToken)?.id
			def deleteBidResponse = bidHelper.deleteBid(bidId, bandUserToken)
			
			def getResponse = bidHelper.getBid(bidId, bandUserToken)
			def bidsForUserResponse = bidHelper.getBidsForUser(bandUserToken)
			def bidsForUserPostsResponse = bidHelper.getBidsForUserPosts(individualUserToken)
			def bidsForPostResponse = bidHelper.getBidsForPost(params.postId, bandUserToken)
		
		then:'It no longer exists'
			deleteBidResponse.success == true
			getResponse.errors[0] == 'No bid was found with this ID.'
			bidsForUserResponse.find { it.id == bidId } == null
			bidsForUserPostsResponse.find { it.id == bidId } == null
			bidsForPostResponse.find { it.id == bidId } == null
	}
}
