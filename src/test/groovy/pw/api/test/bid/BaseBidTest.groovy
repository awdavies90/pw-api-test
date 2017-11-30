package pw.api.test.bid

import spock.lang.*
import pw.api.test.BaseApiTest

class BaseBidTest extends BaseApiTest {
	
	@Shared static savedBids = []
	
	def cleanup() {
		savedBids.each { bidId ->
			deleteBid(bidId)
			//assert getBid(bidId).text == '{"errors":["No bid found with this id"]}'
		}
	}
	
	def saveBid(Map params) {
		def response = post('bid/save', "bid/SaveBid", params)
		if (response?.id) {
			savedBids << response.id
		}
		response
	}
	
	def getBid(id) {
		get("bid/$id")
	}
	
	def acceptBid(id) {
		get("bid/accept/$id")
	}
	
//	def withdrawBid(id) {
//		post("bid/withdraw/$id")
//	}
	
	def deleteBid(bidId) {
		delete("bid/delete/$bidId")
	}
}
