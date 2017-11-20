package pw.api.test.bid

import spock.lang.*
import pw.api.test.BaseApiTest

class BaseBidTest extends BaseApiTest {
	
	def saveBid(Map params) {
		post('bid/save', "SaveBid", params)
	}
	
	def deleteBid(bidId) {
		delete("bid/delete/$bidId")
	}
}
