package pw.api.test.bid

import pw.api.test.utils.RandomUtils
import spock.lang.*

class AcceptBid extends BaseBidTest {
	
	def "Accept Bid"() {
		
		given:'A bid is to be accepted'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				//userId:5,
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'Test notes'
			]
			def reason = 'This bid is by far the best.'
					
		when:'The bid is accepted'
			def bidId = bidHelper.saveBid(params, bandUserToken)?.id
			def acceptBidResponse = bidHelper.acceptBid(bidId, reason, individualUserToken)
			
			def getResponse = bidHelper.getBid(bidId)
			def bidsForUserResponse = bidHelper.getBidsForUser(bandUserToken)
			def bidsForUserPostsResponse = bidHelper.getBidsForUserPosts(individualUserToken)
			def bidsForPostResponse = bidHelper.getBidsForPost(params.postId)
		
		then:"It's status is correctly saved"
			with(acceptBidResponse) {
				post?.id == params.postId
				//user?.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'ACCEPTED', displayName:'Accepted']
				acceptReason == reason
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserResponse[0]) {
				post?.id == params.postId
				//user?.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'ACCEPTED', displayName:'Accepted']
				acceptReason == reason
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserPostsResponse[0]) {
				post?.id == params.postId
				//user?.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'ACCEPTED', displayName:'Accepted']
				acceptReason == reason
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForPostResponse[0]) {
				post?.id == params.postId
				//user?.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'ACCEPTED', displayName:'Accepted']
				acceptReason == reason
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
	}
	
	def "Accept Bid - Check Created Event"() {
		
		given:'A bid is to be accepted'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				//userId:5,
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'Test notes'
			]
					
		when:'The accept request is submitted'
			def bidId = bidHelper.saveBid(params, bandUserToken).id
			def acceptBidResponse = bidHelper.acceptBid(bidId, "It's an awesome bid.", individualUserToken)
			
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
				type == [name:'BID_ACCEPTED', displayName:'Bid Accepted']
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
