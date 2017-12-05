package pw.api.test.bid

class WithdrawBid extends BaseBidTest {
	
	def "Withdraw Bid"() {
		
		given:'A bid is to be accepted'
			def params = [
				postId:1,
				userId:5,
				amount:120.26,
				notes:'Test notes'
			]
			def reason = 'Can no longer do the gig.'
					
		when:'The bid is accepted'
			def bidId = bidHelper.saveBid(params)?.id
			def acceptBidResponse = bidHelper.withdrawBid(bidId, reason)
			
			def getResponse = bidHelper.getBid(bidId)
			def bidsForUserResponse = bidHelper.getBidsForUser(params.userId)
			def bidsForUserPostsResponse = bidHelper.getBidsForUserPosts(1)
			def bidsForPostResponse = bidHelper.getBidsForPost(params.postId)
		
		then:"It's status is correctly saved"
			with(acceptBidResponse) {
				post.id == params.postId
				user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'WITHDRAWN'
				acceptReason == null
				withdrawReason == reason
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserResponse[0]) {
				post.id == params.postId
				user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'WITHDRAWN'
				acceptReason == null
				withdrawReason == reason
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserPostsResponse[0]) {
				post.id == params.postId
				user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'WITHDRAWN'
				acceptReason == null
				withdrawReason == reason
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForPostResponse[0]) {
				post.id == params.postId
				user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'WITHDRAWN'
				acceptReason == null
				withdrawReason == reason
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
	}
}
