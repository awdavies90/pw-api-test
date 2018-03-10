package pw.api.test.bid

import pw.api.test.utils.RandomUtils

class UpdateBid extends BaseBidTest {

	def "Update Bid"() {
		
		given:'A bid is to be updated'
			def saveParams = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'Some save notes'
			]
			def bidId = bidHelper.saveBid(saveParams, bandUserToken).id
			def updateParams = [
				id:bidId,
				amount:450,
				notes:'Discounted price.'
			]
					
		when:"When the bid's amount and notes are updated"
			def response = bidHelper.updateBid(updateParams, bandUserToken)
			def getResponse = bidHelper.getBid(bidId)
			def bidsForUserResponse = bidHelper.getBidsForUser(bandUserToken)
			def bidsForUserPostsResponse = bidHelper.getBidsForUserPosts(individualUserToken)
			def bidsForPostResponse = bidHelper.getBidsForPost(saveParams.postId, bandUserToken)
		
		then:'The bid is correctly updated'
			with(response) {
				post.id == saveParams.postId
				//user.id == saveParams.userId
				amount == updateParams.amount
				notes == updateParams.notes
				status == [name:'PENDING', displayName:'Pending']
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserResponse[0]) {
				post.id == saveParams.postId
				//user.id == saveParams.userId
				amount == updateParams.amount
				notes == updateParams.notes
				status == [name:'PENDING', displayName:'Pending']
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserPostsResponse[0]) {
				post.id == saveParams.postId
				//user.id == saveParams.userId
				amount == updateParams.amount
				notes == updateParams.notes
				status == [name:'PENDING', displayName:'Pending']
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForPostResponse[0]) {
				post.id == saveParams.postId
				//user.id == saveParams.userId
				amount == updateParams.amount
				notes == updateParams.notes
				status == [name:'PENDING', displayName:'Pending']
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
	}
}
