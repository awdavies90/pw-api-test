package pw.api.test.bid

import spock.lang.*

@Unroll
class UpdateBidValidation extends BaseBidTest {

	def "Update Bid Validation - Incorrect Bid ID"() {
		
		given:'A bid with an ID which does not exist is to be updated'
			def params = [
				id: 999999999,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The bid is updated'
			def response = bidHelper.updateBid(params, bandUserToken)
		
		then:'An appropriate error message is returned'
			response.errors[0] == "No bid was found with this ID."
	}
	
	def "Update Bid Validation - Either Amount or Notes Must be Supplied"() {
		
		given:'A bid is to be updated'
			def saveParams = [
				postId:1,
				amount:'500',
				notes:'Some save notes'
			]
			def bidId = bidHelper.saveBid(saveParams, bandUserToken).id
			def updateParams = [
				id:bidId
			]
					
		when:'The bid is updated but niether a amount or notes value is provided'
			def response = bidHelper.updateBid(updateParams, bandUserToken)
		
		then:'An appropriate error message is returned'
			response.errors[0] == 'Either amount or notes must be supplied.'
	}
	
	def "Update Bid Validation - Invalid Characters in Notes"() {
		
		given:'A bid is to be updated'
			def saveParams = [
				postId:1,
				amount:'500',
				notes:'Some save notes'
			]
			def bidId = bidHelper.saveBid(saveParams, bandUserToken).id
			def updateParams = [
				id:bidId,
				amount:'500',
				notes:notes
			]
					
		when:'The bid is updated with an invalid special character in the notes'
			def response = bidHelper.updateBid(updateParams, bandUserToken)
		
		then:'An appropriate error message is returned'
			response.errors[0] == 'Text cannot contain any of the following characters `^*_{}[]~|;<>'
			
		where:
			notes << invalidChars
	}
}
