package pw.api.test.bid

import spock.lang.*

class UpdateBidPermissions extends BaseBidTest {
	
	@Unroll
	def "1 - Update Bid Permissions - Only the User Who Created the Bid Can Update it"() {
		
		given:'A Bid is to be updated'
			def saveParams = [
				postId:1,
				amount:500,
				notes:'These are some notes'
			]
			def updateParams = [
				amount:510,
				notes:'Price has gone up a bit'
			]
					
		when:"The bid is updated by a user who didn't create it"
			def createBidResponse = bidHelper.saveBid(saveParams, bandUserToken)
			def bidId = createBidResponse.id
			updateParams.id = bidId
			def updateBidResponse = bidHelper.updateBid(updateParams, token)
		
		then:'An appropriate error response is received'
			updateBidResponse?.errors[0] == 'You do not have permission to perform this operation.'
		
		where:'The following user tokens are used'
			token				| descroption
			bandUserToken2		| "Another band user who didn't make the bid"
			individualUserToken | "Individual user who didn't make the bid"
	}
}
