package api.test.helpers

import spock.lang.*

import javax.activity.InvalidActivityException

import pw.api.test.BaseApiTest

class BidHelper {
	
	BaseApiTest baseTest
	def BidHelper(BaseApiTest baseTest) {
		this.baseTest = baseTest
	}
	
	@Shared static savedBids = []
	
	def saveBid(Map params) {
		def response = baseTest.post('bid/save', "bid/SaveBid", params)
		if (response?.id) {
			savedBids << response.id
		}
		response
	}
	
	def updateBid(Map params) {
		if (params.id) {
			println "Performing put to bid/update/$params.id"
			baseTest.put("bid/update/$params.id", "bid/UpdateBid", params)
		} else {
			throw new Exception("No id was provided.")
		}
	}
	
	def getBid(id) {
		baseTest.get("bid/$id")
	}
	
	def getBidsForPost(postId) {
		baseTest.get("bid/forPost/$postId")
	}
	
	def getBidsForUser(userId) {
		baseTest.get("bid/forUser/$userId")
	}
	
	def getBidsForUserPosts(userId) {
		baseTest.get("bid/forUserPosts/$userId")
	}
	
	def acceptBid(id) {
		baseTest.put("bid/accept/$id")
	}
	
	def acceptBid(id, String acceptReason) {
		def requestContent = "{\"reason\":\"$acceptReason\"}"
		baseTest.put("bid/accept/$id", requestContent)
	}
	
	def withdrawBid(id) {
		baseTest.put("bid/withdraw/$id")
	}
	
	def withdrawBid(id, String withdrawReason) {
		def requestContent = "{\"reason\":\"$withdrawReason\"}"
		baseTest.put("bid/withdraw/$id", requestContent)
	}
	
	def deleteAllBids() {
		savedBids.each { bidId ->
			deleteBid(bidId)
		}
		savedBids = []
	}
	
	def deleteBid(bidId) {
		baseTest.delete("bid/delete/$bidId")
	}
}
