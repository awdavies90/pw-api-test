package pw.api.test.bid

import spock.lang.*

class AcceptBid extends BaseBidTest {
	
	def "Accept Bid"() {
		
		given:'A bid is to be accepted'
			def params = [
				postId:1,
				userId:5,
				amount:120.26,
				notes:'Test notes'
			]
			def reason = 'This bid is by far the best.'
					
		when:'The bid is accepted'
			def bidId = bidHelper.saveBid(params)?.id
			def acceptBidResponse = bidHelper.acceptBid(bidId, reason)
			
			def getResponse = bidHelper.getBid(bidId)
			def bidsForUserResponse = bidHelper.getBidsForUser(params.userId)
			def bidsForUserPostsResponse = bidHelper.getBidsForUserPosts(1)
			def bidsForPostResponse = bidHelper.getBidsForPost(params.postId)
		
		then:"It's status is correctly saved"
			with(acceptBidResponse) {
				post?.id == params.postId
				user?.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'ACCEPTED'
				acceptReason == reason
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserResponse[0]) {
				post?.id == params.postId
				user?.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'ACCEPTED'
				acceptReason == reason
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserPostsResponse[0]) {
				post?.id == params.postId
				user?.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'ACCEPTED'
				acceptReason == reason
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForPostResponse[0]) {
				post?.id == params.postId
				user?.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'ACCEPTED'
				acceptReason == reason
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
	}
	
	def "Accept Bid - Check Created Event"() {
		
		given:'A bid is to be accepted'
			def params = [
				postId:1,
				userId:5,
				amount:120.26,
				notes:'Test notes'
			]
					
		when:'The accept request is submitted'
			def bidId = bidHelper.saveBid(params).id
			def acceptBidResponse = bidHelper.acceptBid(bidId, "It's an awesome bid.")
			
			def eventsForPostResponse = eventHelper.getEventsForPost(params.postId)
			//def eventsForUserResponse = eventHelper.getEventsForUser(params.userId)
		
		then:'An event is correctly created'
			with(eventsForPostResponse[0]) {
				post?.id == params.postId
				//Needs to be revisited as it's post.user.name
				//user.id == params.userId
				bid?.id == bidId
				amount == params.amount
				notes == params.notes
				isPublic == true
				//Needs to be revisited as it contains a username
				//title == ''
				type == 'BID_ACCEPTED'
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			//Needs to be revisited once events are set up to have multiple users
//			with(eventsForUserResponse[0]) {
//				post.id == params.postId
//				//Needs to be revisited as it's post.user.name
//				//user.id == params.userId
//				bid.id == bidId
//				amount == params.amount
//				notes == params.notes
//				isPublic == true
//				//Needs to be revisited as it contains a username
//				//title == ''
//				type == 'BID_ACCEPTED'
//				dateCreated > tenSecondsAgo
//				dateUpdated > tenSecondsAgo
//			}
	}
}
