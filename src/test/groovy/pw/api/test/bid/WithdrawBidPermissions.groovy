package pw.api.test.bid

import pw.api.test.utils.RandomUtils
import spock.lang.*

class WithdrawBidPermissions extends BaseBidTest {
	
	@Unroll
	def "1 - Withdraw Bid Permissions - Only the User Who Created the Bid Can Withdraw it"() {
		
		given:'A Bid is to be withdrawn'
			def params = [
				postId:postHelper.getRandomPostId(individualUserToken),
				amount:RandomUtils.getRandomDecimal(100, 1000),
				notes:'These are some notes'
			]
					
		when:"The bid is then withdrawn by a user who didn't create it"
			def createBidResponse = bidHelper.saveBid(params, bandUserToken)
			def bidId = createBidResponse.id
			def withdrawBidResponse = bidHelper.withdrawBid(bidId, token)
		
		then:'An appropriate error response is received'
			withdrawBidResponse?.errors[0] == 'You do not have permission to perform this operation.'
		
		where:'The following user tokens are used'
			token				| descroption
			bandUserToken2		| "Another band user who didn't make the bid"
			individualUserToken | "Individual user who didn't make the bid"
	}
}
