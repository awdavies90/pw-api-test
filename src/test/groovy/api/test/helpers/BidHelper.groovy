package api.test.helpers

import spock.lang.*

import javax.lang.model.element.NestingKind

import pw.api.test.BaseApiTest

class BidHelper {
	
	BaseApiTest baseTest
	def BidHelper(BaseApiTest baseTest) {
		this.baseTest = baseTest
	}
	
	@Shared static savedBids = []
	
	def saveBid(Map params, String token) {
		baseTest.authToken = token
		def response = baseTest.post('bid/save', "bid/SaveBid", params)
		if (response?.id) {
			savedBids << response.id
		}
		response
	}
	
	def updateBid(Map params, String token) {
		baseTest.authToken = token
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
	
	def getBid(id, String token) {
		baseTest.authToken = token
		baseTest.get("bid/$id")
	}
	
	def getBidsForPost(postId) {
		baseTest.get("bid/forPost/$postId")
	}
	
	def getBidsForPost(postId, String token) {
		baseTest.authToken = token
		baseTest.get("bid/forPost/$postId")
	}
	
	def getBidsForUser(token) {
//		baseTest.get("bid/forUser/$userId")
		baseTest.authToken = token
		baseTest.get('bid/forUser')
	}
	
	def getBidsForUserPosts(token) {
//		baseTest.get("bid/forUserPosts/$userId")
		baseTest.authToken = token
		baseTest.get('bid/forUserPosts')
	}
	
	def acceptBid(id, String token) {
		baseTest.authToken = token
		baseTest.put("bid/accept/$id")
	}
	
	def acceptBid(id, String acceptReason, String token) {
		baseTest.authToken = token
		def requestContent = "{\"reason\":\"$acceptReason\"}"
		baseTest.put("bid/accept/$id", requestContent)
	}
	
//	def withdrawBid(id) {
//		baseTest.put("bid/withdraw/$id")
//	}
	
	def withdrawBid(id, String token) {
		baseTest.authToken = token
		baseTest.put("bid/withdraw/$id")
	}
	
	def withdrawBid(id, String withdrawReason, String token) {
		baseTest.authToken = token
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
		baseTest.authToken = baseTest.bandUserToken
		baseTest.delete("bid/delete/$bidId")
	}
}
