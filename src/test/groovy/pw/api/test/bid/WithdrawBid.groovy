package pw.api.test.bid

import pw.api.test.utils.RandomUtils

class WithdrawBid extends BaseBidTest {
	
	def "Withdraw Bid"() {
		
		given:'A bid is to be accepted'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'Test notes'
			]
			def reason = 'Can no longer do the gig.'
					
		when:'The bid is accepted'
			def bidId = bidHelper.saveBid(params, bandUserToken)?.id
			def acceptBidResponse = bidHelper.withdrawBid(bidId, reason, bandUserToken)
			
			def getResponse = bidHelper.getBid(bidId, bandUserToken)
			def bidsForUserResponse = bidHelper.getBidsForUser(bandUserToken)
			def bidsForUserPostsResponse = bidHelper.getBidsForUserPosts(individualUserToken)
			def bidsForPostResponse = bidHelper.getBidsForPost(params.postId, bandUserToken)
		
		then:"It's status is correctly saved"
			with(acceptBidResponse) {
				post.id == params.postId
				//user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'WITHDRAWN', displayName:'Withdrawn']
				acceptReason == null
				withdrawReason == reason
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserResponse[0]) {
				post.id == params.postId
				//user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'WITHDRAWN', displayName:'Withdrawn']
				acceptReason == null
				withdrawReason == reason
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserPostsResponse[0]) {
				post.id == params.postId
				//user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'WITHDRAWN', displayName:'Withdrawn']
				acceptReason == null
				withdrawReason == reason
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForPostResponse[0]) {
				post.id == params.postId
				//user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'WITHDRAWN', displayName:'Withdrawn']
				acceptReason == null
				withdrawReason == reason
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
	}
}
