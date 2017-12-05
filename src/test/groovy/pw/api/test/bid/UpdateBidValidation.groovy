package pw.api.test.bid

import spock.lang.*

@Unroll
class UpdateBidValidation extends BaseBidTest {

	def "Save Bid Validation - Incorrect Bid ID"() {
		
		given:'There is a test'
			def params = [
				id: 999999999,
				amount:500,
				notes:'These are some notes'
			]
					
		when:'The test is executed'
			def response = bidHelper.updateBid(params)
		
		then:'The test passes'
			response.errors[0] == "No bid was found with this ID."
	}
	
	def "Save Bid Validation - Either Amount or Notes Must be Supplied"() {
		
		given:'There is a test'
			def saveParams = [
				postId:1,
				userId:5,
				amount:'500',
				notes:'Some save notes'
			]
			def bidId = bidHelper.saveBid(saveParams).id
			def updateParams = [
				id:bidId
			]
					
		when:'The test is executed'
			def response = bidHelper.updateBid(updateParams)
		
		then:'The test passes'
			if (!valid) {
				response.errors[0] == 'Either amount or notes must be supplied.'
			} else {
				response.amount == amount
				response.notes = notes
			}
			
		
		where:
			amount | notes 				    | valid | description
			null   | 'These are some notes' | true	| 'No amount'
			750	   | null					| true	| 'No notes'
			null   | null					| false	| 'No amount or notes'
	}
	
	def "Save Bid Validation - Invalid Characters in Notes"() {
		
		given:'There is a test'
			def saveParams = [
				postId:1,
				userId:5,
				amount:'500',
				notes:'Some save notes'
			]
			def bidId = bidHelper.saveBid(saveParams).id
			def updateParams = [
				id:bidId,
				amount:'500',
				notes:notes
			]
					
		when:'The test is executed'
			def response = bidHelper.updateBid(updateParams)
		
		then:'The test passes'
			response.errors[0] == 'Text cannot contain any of the following characters `^*_{}[]~|;<>'
			
		where:
			notes << invalidChars
	}
}
