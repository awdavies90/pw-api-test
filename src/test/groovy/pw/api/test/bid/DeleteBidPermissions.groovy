package pw.api.test.bid

import spock.lang.*

class DeleteBidPermissions extends BaseBidTest {
	
	@Unroll
	def "1 - Delete Bid Permissions - Only the User Who Created the Bid Can Delete it"() {
		
		given:'A Bid is to be deleted'
			def params = [
				postId:1,
				amount:500,
				notes:'These are some notes'
			]
					
		when:"The bid is then deleted by a user who didn't create it"
			def createBidResponse = bidHelper.saveBid(params, bandUserToken)
			def bidId = createBidResponse.id
			def deleteBidResponse = bidHelper.deleteBid(bidId, token)
		
		then:'An appropriate error response is received'
			deleteBidResponse?.errors[0] == 'You do not have permission to perform this operation.'
		
		where:'The following user tokens are used'
			token				| descroption
			bandUserToken2		| "Another band user who didn't make the bid"
			individualUserToken | "Individual user who didn't make the bid"
	}
}
