package pw.api.test.bid

import spock.lang.*
import pw.api.test.BaseApiTest

@Unroll
class SaveBid extends BaseBidTest {
	
	def "Save Bid"() {
		
		given:'A bid containing valid data is to be saved'
			def params = [
				postId:1,
				userId:5,
				amount:120.26,
				notes:'Test notes'
			]
					
		when:'The bid is submitted'
			def saveResponse = saveBid(params)
			def bidId = saveResponse.id
			def getResponse = getBid(bidId)
			def bidsForUserResponse = getBidsForUser(params.userId)
			def bidsForUserPostsResponse = getBidsForUserPosts(1)
			def bidsForPostResponse = getBidsForPost(params.postId)
		
		then:'It is correctly saved'
			with(saveResponse) {
				post.id == params.postId
				user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'PENDING'
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserResponse[0]) {
				post.id == params.postId
				user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'PENDING'
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForUserPostsResponse[0]) {
				post.id == params.postId
				user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'PENDING'
				acceptReason == null
				withdrawReason == null
				dateCreated > tenSecondsAgo
				dateUpdated > tenSecondsAgo
			}
			with(bidsForPostResponse[0]) {
				post.id == params.postId
				user.id == params.userId
				amount == params.amount
				notes == params.notes
				status == 'PENDING'
				acceptReason == null
				withdrawReason == null
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
			def response = saveBid(params)
			"2017-11-19T14:27:14Z"
		
		then:'It is correctly saved'
			response.errors[0] == 'The following params are required [postId, userId, amount, notes]'
		
		where:
			notes << validChars
	}
}
