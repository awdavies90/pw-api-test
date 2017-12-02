package pw.api.test.bid

import spock.lang.*
import pw.api.test.BaseApiTest

class BaseBidTest extends BaseApiTest {
	
	@Shared static savedBids = []
	
	def cleanup() {
		savedBids.each { bidId ->
			deleteBid(bidId)
		}
		savedBids = []
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
	
	def getBidsForPost(postId) {
		get("bid/forPost/$postId")
	}
	
	def getBidsForUser(userId) {
		get("bid/forUser/$userId")
	}
	
	def getBidsForUserPosts(userId) {
		get("bid/forUserPosts/$userId")
	}
	
	def acceptBid(id) {
		println "Performing put to bid/accept/$id"
		put("bid/accept/$id")
	}
	
	def withdrawBid(id) {
		println "Performing put to bid/withdraw/$id"
		put("bid/withdraw/$id")
	}
	
	def deleteBid(bidId) {
		delete("bid/delete/$bidId")
	}
}
