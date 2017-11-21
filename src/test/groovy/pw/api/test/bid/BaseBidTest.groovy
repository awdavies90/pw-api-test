package pw.api.test.bid

import spock.lang.*
import pw.api.test.BaseApiTest

class BaseBidTest extends BaseApiTest {
	
	@Shared static savedBids = []
	
	def cleanupSpec() {
		savedBids.each { bidId ->
			deleteBid(bidId)
		}
	}
	
	def saveBid(Map params) {
		def response = post('bid/save', "bid/SaveBid", params)
		if (response?.id) {
			savedBids << response.id
		}
		response
	}
	
	def deleteBid(bidId) {
		delete("bid/delete/$bidId")
	}
}
