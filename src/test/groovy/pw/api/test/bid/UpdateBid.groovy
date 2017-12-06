package pw.api.test.bid

import spock.lang.*

@Unroll
class UpdateBid extends BaseBidTest {

	def "Update Bid"() {
		
		given:'A bid is to be updated'
			def saveParams = [
				postId:1,
				userId:5,
				amount:500,
				notes:'Some save notes'
			]
			def bidId = bidHelper.saveBid(saveParams).id
			def updateParams = [
				id:bidId,
				amount:450,
				notes:'Discounted price.'
			]
					
		when:"When the bid's amount and notes are updated"
			def response = bidHelper.updateBid(updateParams)
			def getResponse = bidHelper.getBid(bidId)
			def bidsForUserResponse = bidHelper.getBidsForUser(saveParams.userId)
			def bidsForUserPostsResponse = bidHelper.getBidsForUserPosts(1)
			def bidsForPostResponse = bidHelper.getBidsForPost(saveParams.postId)
		
		then:'The bid is correctly updated'
			with(response) {
				post.id == saveParams.postId
				user.id == saveParams.userId
				amount == updateParams.amount
				notes == updateParams.notes
				status == 'PENDING'
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserResponse[0]) {
				post.id == saveParams.postId
				user.id == saveParams.userId
				amount == updateParams.amount
				notes == updateParams.notes
				status == 'PENDING'
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserPostsResponse[0]) {
				post.id == saveParams.postId
				user.id == saveParams.userId
				amount == updateParams.amount
				notes == updateParams.notes
				status == 'PENDING'
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForPostResponse[0]) {
				post.id == saveParams.postId
				user.id == saveParams.userId
				amount == updateParams.amount
				notes == updateParams.notes
				status == 'PENDING'
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
	}
}
