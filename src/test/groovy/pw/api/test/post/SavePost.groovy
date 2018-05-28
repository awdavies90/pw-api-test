package pw.api.test.post

import pw.api.test.BaseApiTest
import pw.api.test.helpers.VenueHelper
import pw.api.test.utils.RandomUtils
import spock.lang.*

class SavePost extends BasePostTest {
	
	@Unroll
	@IgnoreRest
	def "Save Post"() {
		
		given:'A valid post is to be saved'
			def params = [
				name:"My Post $num",
				date:RandomUtils.getRandomFutureRequestDate(),
				venueId:venueHelper.getRandomVenueId(individualUserToken),
				fromPrice: fromPrice ? RandomUtils.getRandomDecimal(100, 500) : null,
				toPrice: toPrice ? RandomUtils.getRandomDecimal(501, 1000) : null,
				description: postDescription ? RandomUtils.getRandomText() : null,
				venueDescription: venueDescription ? RandomUtils.getRandomText() : null
			]
					
		when:'The post is saved'
			def saveResponse = postHelper.create(params, individualUserToken)
			def getPostResponse = postHelper.getPost(saveResponse?.id, individualUserToken)
		
		then:'The post is correctly saved'
			verifyPost(saveResponse, params)
			verifyPost(getPostResponse, params)
		
		where:'The following inputs are used'
			num | fromPrice | toPrice | postDescription | venueDescription | description
			1	| false		| false	  | false			| false			   | 'No optional fields provided'
			2	| true		| false	  | false			| false			   | 'From price provided'
			3	| false		| true	  | false			| false			   | 'To price provided'
			4	| false		| false	  | true			| false			   | 'Post description provided'
			5	| false		| false	  | false			| true			   | 'Venue description provided'
			6	| true		| true	  | true			| true			   | 'All optional fields provided'
	}
	
	def verifyPost(post, expectedValues) {
		with(post) {
			assert id != null
			assert date == expectedValues.date
			assert dateCreated > tenSecondsAgo
			assert dateUpdated > tenSecondsAgo
			assert description == expectedValues.description?.trim()
			assert fromPrice == expectedValues.fromPrice
			assert isPublic == false
			assert name == expectedValues.name
			assert playlist == null
			assert status == [name:'CREATED', displayName:'Created']
			assert toPrice == expectedValues.toPrice
			assert withdrawReason == null
			assert venue.id == expectedValues.venueId
			assert venueDescription == expectedValues.venueDescription?.trim()
		}
		assert post.user.id == userHelper.getUserIdByToken(individualUserToken)
		true
	}
	
	def "Save Venue - Same Custom Venue for Two Users"() {
		
		given:'A custom venue is to be saved'
			def params = [
				name:'Same Name Venue',
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'XX1 2XX'
			]
					
		when:'The same venue is saved for two different users'
			def saveResponse1 = venueHelper.saveVenue(params, individualUserToken)
			def getVenueResponse1 = venueHelper.getVenue(saveResponse1?.id, individualUserToken)
			def saveResponse2 = venueHelper.saveVenue(params, individualUserToken2)
			def getVenueResponse2 = venueHelper.getVenue(saveResponse2?.id, individualUserToken2)
		
		then:'Both venues are correctly saved'
			verifyCustomVenue(params, individualUserToken, saveResponse1)
			verifyCustomVenue(params, individualUserToken, getVenueResponse1)
			verifyCustomVenue(params, individualUserToken2, saveResponse2)
			verifyCustomVenue(params, individualUserToken2, getVenueResponse2)
	}
}
