package pw.api.test.bid

import pw.api.test.utils.RandomUtils
import spock.lang.*

@Unroll
class SaveBid extends BaseBidTest {
	
	def "Save Bid"() {
		
		given:'A bid containing valid data is to be saved'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'Test notes'
			]
					
		when:'The bid is submitted'
			def saveResponse = bidHelper.saveBid(params, bandUserToken)
			def bidId = saveResponse.id
			def getResponse = bidHelper.getBid(bidId)
			def bidsForUserResponse = bidHelper.getBidsForUser(bandUserToken)
			def bidsForUserPostsResponse = bidHelper.getBidsForUserPosts(individualUserToken)
			def bidsForPostResponse = bidHelper.getBidsForPost(params.postId)
		
		then:'It is correctly saved'
			with(saveResponse) {
				post.id == params.postId
				//user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'PENDING', displayName:'Pending']
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserResponse[0]) {
				post.id == params.postId
				//user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'PENDING', displayName:'Pending']
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserPostsResponse[0]) {
				post.id == params.postId
				//user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'PENDING', displayName:'Pending']
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForPostResponse[0]) {
				post.id == params.postId
				//user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == [name:'PENDING', displayName:'Pending']
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
	}
	
	def "Save Bid - Check Created Event"() {
		
		given:'A bid containing valid data is to be saved'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'Test notes'
			]
					
		when:'The bid is submitted'
			def bidId = bidHelper.saveBid(params, bandUserToken).id
			def eventsForPostResponse = eventHelper.getEventsForPost(params.postId)
			def eventsForUserResponse = eventHelper.getEventsForUser(bandUserToken)
		
		then:'An event is correctly created'
			with(eventsForPostResponse[0]) {
				post.id == params.postId
				//user.id == params.userId
				bid.id == bidId
				amount == params.amount
				notes == params.notes
				isPublic == true
				//Needs to be revisited as it contains a username
				//title == ''
				type == [name:'BID_MADE', displayName:'Bid Made']
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(eventsForUserResponse[0]) {
				post.id == params.postId
				//user.id == params.userId
				bid.id == bidId
				amount == params.amount
				notes == params.notes
				isPublic == true
				//Needs to be revisited as it contains a username
				//title == ''
				type == [name:'BID_MADE', displayName:'Bid Made']
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
	}
	
	@Ignore //Needs to be revisited when posts & users can be easily created
	def "Save Bid - Valid Specical Characters in Notes"() {
		
		given:'A bid containing valid special characters is to be saved'
			def params = [
				postId:1,
				userId:5,
				amount:'120.26',
				notes:notes
			]
					
		when:'The bid is submitted'
			def response = bidHelper.saveBid(params)
			"2017-11-19T14:27:14Z"
		
		then:'It is correctly saved'
			response.errors[0] == 'The following params are required [postId, userId, amount, notes]'
		
		where:
			notes << validChars
	}
}
